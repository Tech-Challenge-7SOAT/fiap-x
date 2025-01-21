package com.fiapx.video.extensions

import com.fiapx.video.entities.VideoMessage
import io.awspring.cloud.sqs.operations.SqsTemplate

fun SqsTemplate.dispatch(queue: String, message: VideoMessage) = send(queue, message)
