package com.fiapx.video.adapter.driver.controller

import com.fiapx.video.core.application.useCase.ProcessVideo
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(private val processVideo: ProcessVideo) {

    @GetMapping("/")
    fun index() = emptyList<String>()

    @GetMapping("/{id}")
    fun find(@PathVariable id: String) = emptyList<String>()

    @PostMapping("/")
    fun store(@RequestParam("videos") videos: List<MultipartFile>) = videos.forEach { _ -> processVideo.execute() }

    @DeleteMapping("/{id}")
    fun destroy(@PathVariable id: String) = emptyList<String>()
}
