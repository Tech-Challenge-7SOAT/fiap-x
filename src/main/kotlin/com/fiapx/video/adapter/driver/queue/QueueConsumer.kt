package com.fiapx.video.adapter.driver.queue

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class QueueConsumer {

    @RabbitListener(queues = ["process.video"])
    fun consume(message: String) {
        println("Received message: $message")
    }
}
