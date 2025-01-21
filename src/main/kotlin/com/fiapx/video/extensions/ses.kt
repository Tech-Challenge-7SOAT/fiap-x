package com.fiapx.video.extensions

import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.SendEmailResponse


fun SesClient.notify(
    to: String,
    subject: String,
    body: String
): SendEmailResponse = this.sendEmail {
    it.source("email@teste.com")
    it.destination { dest -> dest.toAddresses(to) }
    it.message { message ->
        message.subject { sub -> sub.data(subject) }
        message.body { bod -> bod.text { txt -> txt.data(body) } }
    }
}
