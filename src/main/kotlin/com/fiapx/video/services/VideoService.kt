package com.fiapx.video.services

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.extensions.dispatch
import com.fiapx.video.extensions.download
import com.fiapx.video.extensions.notify
import com.fiapx.video.extensions.upload
import com.fiapx.video.repositories.VideoRepository
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.ses.SesClient
import java.util.*

@Service
class VideoService(
    private val mail: SesClient,
    private val storage: S3Client,
    private val queue: SqsTemplate,
    private val converter: VideoConverter,
    private val repository: VideoRepository,
    @Value("\${sqs.queue-url:random-queue-name}")
    private val queueName: String
) {

    @Value("\${sns.topic.arn}")
    private lateinit var topic: String

    fun store(videos: List<MultipartFile>): List<Video> = videos.map { video ->
        val uniqueKey = UUID.randomUUID()
        val videoExtension = video.originalFilename?.split(".")?.lastOrNull() ?: "mp4"

        val objectKey = "$uniqueKey.$videoExtension"
        val uploadedFile = storage.upload(video, objectKey)
        val uploadedFileURL = uploadedFile.toString()

        val savedVideo = repository.save(Video(videoUrl = uploadedFileURL, status = Status.PENDING))
        val message = VideoMessage(
            id = savedVideo.id!!,
            identifier = objectKey,
            url = uploadedFileURL,
            extension = video.originalFilename?.split(".")?.lastOrNull() ?: "mp4"
        )

        queue.dispatch(queueName, message)

        return@map savedVideo
    }

    fun findAll(): List<Video> = repository.findAll().toList()

    fun findById(videoId: Long): Video? = repository.findById(videoId).orElseThrow()

    fun extractFrames(video: VideoMessage) {
        kotlin.runCatching {
            repository.updateById(video.id, Status.PROCESSING)
            storage.download(video.identifier).also {
                val frames = converter.from(it, video.identifier, video.extension)
                storage.upload(frames, "zip", "application/zip", "${video.identifier}.zip")
                    .also { zipUrl -> repository.updateById(video.id, Status.COMPLETED, zipUrl.toString()) }
            }
        }.onFailure { err ->
            repository.updateById(video.id, Status.FAILED)
                .also {
                    mail.notify(
                        to = "user@example.com",
                        subject = "falha ao extrair imagem de videos",
                        body = "error: ${err.message}"
                    )
                }
        }
    }
}
