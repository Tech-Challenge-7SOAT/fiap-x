package com.fiapx.video.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiapx.video.entities.VideoMessage
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class QueueProducer(
    private val sqsTemplate: SqsTemplate,
    private val objectMapper: ObjectMapper
) {

    @Value("\${sqs.queue-url}")
    private lateinit var queueUrl: String

    fun dispatch(message: VideoMessage) = sqsTemplate.send(queueUrl, message)
}
