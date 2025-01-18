package com.fiapx.video.services

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.extensions.framesFrom
import com.fiapx.video.extensions.upload
import com.fiapx.video.pubsub.QueueProducer
import com.fiapx.video.repositories.VideoRepository
import com.fiapx.video.storage.BucketStorage
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client

@Service
class VideoService(
    private val queue: QueueProducer,
    private val repository: VideoRepository,
    private val storage: BucketStorage,
    private val s3Client: S3Client
) {

    fun store(videos: List<MultipartFile>) = videos.forEach { video ->
        val uploadedFile = storage.put(video)
        val uploadedFileURL = uploadedFile.toString()
        val savedVideo = repository.save(Video(videoUrl = uploadedFileURL, status = Status.PENDING))

        queue.dispatch(
            VideoMessage(
                id = savedVideo.id!!,
                videoUrl = uploadedFileURL
            )
        )
    }

    fun findAll(): List<Video> {
        return emptyList()
    }

    fun findById(videoId: String): Video? {
        return null
    }

    fun delete(videoId: String): Unit {
        println("Deleting video $videoId")
    }

    fun extractFrames(video: VideoMessage): Unit {
        kotlin.runCatching {
            s3Client.framesFrom(video.videoUrl).also {
                val zipUrl = s3Client.upload(it, "zip", "application/zip")
                repository.updateById(video.id, Status.COMPLETED, zipUrl.path)
            }
        }.onFailure {
            println("--> Failed extracting frames from video ${video.id} - ${it.message} - ${it.stackTraceToString()}")
            repository.updateById(video.id, Status.FAILED, "")
        }
    }
}
