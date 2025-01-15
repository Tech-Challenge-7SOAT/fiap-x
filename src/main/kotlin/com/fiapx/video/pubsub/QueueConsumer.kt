package com.fiapx.video.pubsub

import com.fiapx.video.entities.VideoMessage
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class QueueConsumer {

    @SqsListener("videos")
    fun consume(message: VideoMessage) {
        println("Received message: ${message.id}")
    }
}
