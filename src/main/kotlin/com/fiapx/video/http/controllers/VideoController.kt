package com.fiapx.video.http.controllers

import com.fiapx.video.services.VideoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(private val service: VideoService) {

    @GetMapping("/")
    fun index() = emptyList<String>()

    @GetMapping("/{id}")
    fun find(@PathVariable id: String) = emptyList<String>()

    @PostMapping("/")
    fun store(@RequestParam("videos") videos: List<MultipartFile>) = service.startProcessing(videos)

    @DeleteMapping("/{id}")
    fun destroy(@PathVariable id: String) = emptyList<String>()
}
