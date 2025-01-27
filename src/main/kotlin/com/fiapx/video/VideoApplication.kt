package com.fiapx.video

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "FiapX - video processor", version = "1.0.0"))
class VideoApplication

fun main(args: Array<String>) {
    runApplication<VideoApplication>(*args)
}
