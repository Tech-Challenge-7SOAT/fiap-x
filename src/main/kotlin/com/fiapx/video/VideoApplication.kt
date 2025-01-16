package com.fiapx.video

import com.fiapx.video.pubsub.QueueProducer
import com.fiapx.video.entities.VideoMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VideoApplication : CommandLineRunner {

    @Autowired
    private lateinit var queueProducer: QueueProducer

    override fun run(vararg args: String?) {
        queueProducer.dispatch(VideoMessage(3L, "Teste"))
    }
}

fun main(args: Array<String>) {
	runApplication<VideoApplication>(*args)
}
