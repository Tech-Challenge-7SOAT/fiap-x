package com.fiapx.video.services

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.pubsub.QueueProducer
import com.fiapx.video.repositories.VideoRepository
import com.fiapx.video.storage.BucketStorage
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class VideoService(
    private val queue: QueueProducer,
    private val repository: VideoRepository,
    private val storage: BucketStorage
) {

    fun startProcessing(videos: List<MultipartFile>) = videos.forEach { video ->
        val uploadedFileUrl = storage.put(video)
        val savedVideo = repository.save(Video(videoUrl = uploadedFileUrl.path, status = Status.PENDING))
        queue.dispatch(VideoMessage(id = savedVideo.id!!, videoUrl = uploadedFileUrl.path))
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
}
