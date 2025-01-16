package com.fiapx.video.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiapx.video.entities.VideoMessage
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class QueueConsumer(private val objectMapper: ObjectMapper) {

    @SqsListener("videos")
    fun consume(@Payload message: VideoMessage) {
        println("Received message: ${message.id} - ${message.videoUrl}")
    }
}
