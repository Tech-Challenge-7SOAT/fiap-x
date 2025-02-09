package com.fiapx.video.pubsub

import com.fiapx.video.entities.VideoMessage
import com.fiapx.video.services.VideoService
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class QueueConsumer(private val service: VideoService) {

    @SqsListener("fiap-x-queue")
    fun consume(@Payload message: VideoMessage) {
        println("Received message: ${message.id} - ${message.url}")
        service.extractFrames(message)
    }
}
