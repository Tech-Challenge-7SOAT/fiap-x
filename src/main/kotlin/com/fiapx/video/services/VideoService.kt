package com.fiapx.video.services

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.pubsub.QueueProducer
import com.fiapx.video.repositories.VideoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class VideoService(
    private val queue: QueueProducer,
    private val repository: VideoRepository
) {

    fun startProcessing(videos: List<MultipartFile>) = videos.map {

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
