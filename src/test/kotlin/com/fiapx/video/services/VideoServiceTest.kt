package com.fiapx.video.services

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.extensions.dispatch
import com.fiapx.video.extensions.download
import com.fiapx.video.repositories.VideoRepository
import io.awspring.cloud.sqs.operations.SqsTemplate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.context.annotation.Profile
import org.springframework.test.context.TestPropertySource
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.ses.SesClient
import strikt.api.expectDoesNotThrow
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty
import strikt.assertions.size
import java.util.*

@Profile("test")
@AutoConfigureDataJpa
class VideoServiceTest {

    private val storage: S3Client = mockk(relaxed = true)
    private val queue: SqsTemplate = mockk(relaxed = true)
    private val mail: SesClient = mockk(relaxed = true)
    private val converter: VideoConverter = mockk(relaxed = true)
    private val repository: VideoRepository = mockk()

    private val service: VideoService = VideoService(mail, storage, queue, converter, repository, "queue-name")

    @Test
    fun store() {
        every { repository.save(any()) } returns Video(1, "http://localhost", status = Status.PENDING)

        val videos = service.store(listOf(mockk(relaxed = true)))

        verifyOrder { queue.dispatch(any(), any()) }
        expectThat(videos) { isNotEmpty() }
    }

    @Test
    fun `should return empty list when no videos are saved`() {
        every { repository.findAll() } returns emptyList()

        expectThat(service.findAll()) {
            isEmpty()
        }
    }

    @Test
    fun `should return videos when saved`() {
        val video = Video(videoUrl = "http://localhost:8080/video.mp4", status = Status.PENDING)
        every { repository.findAll() } returns listOf(video)

        expectThat(service.findAll()) {
            size.isEqualTo(1)
            get { first().videoUrl }.isEqualTo("http://localhost:8080/video.mp4")
        }
    }

    @Test
    fun `should return video by ID`() {
        every { repository.findById(any()) } returns Optional.of(
            Video(
                1,
                videoUrl = "http://localhost:8080/video.mp4",
                status = Status.PENDING
            )
        )

        expectDoesNotThrow { service.findById(1) }
    }

    @Test
    fun `should throw when video does not exist`() {
        every { repository.findById(any()) } returns Optional.empty()

        assertThrows<NoSuchElementException> { service.findById(1) }
    }

    @Test
    fun `should mark processing as success`() {
        every { repository.updateById(any(), any()) } returns 1
        every { storage.download("123") } returns ByteArray(0)

        service.extractFrames(
            VideoMessage(
                id = 1,
                url = "http://localhost:8080/video.mp4",
                identifier = "123",
                extension = "mp4"
            )
        )

        verifyOrder {
            repository.updateById(1, Status.PROCESSING)
            repository.updateById(1, Status.COMPLETED, any())
        }
    }

    @Test
    fun `should mark processing as fail when something goes wrong`() {
        every { repository.updateById(any(), any()) } returns 0

        service.extractFrames(VideoMessage(1, "video.mp4", "http://localhost:8080/video.mp4", "mp4"))

        verifyOrder {
            repository.updateById(1, Status.PROCESSING)
            repository.updateById(1, Status.FAILED)
        }
    }
}
