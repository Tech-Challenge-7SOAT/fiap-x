package com.fiapx.video.http.controllers

import com.fiapx.video.services.VideoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(private val service: VideoService) {

    @GetMapping("/")
    fun index() = service.findAll()

    @GetMapping("/{id}")
    fun find(@PathVariable id: Long) = service.findById(id)

    @PostMapping("/")
    fun store(@RequestParam("videos") videos: List<MultipartFile>) = service.store(videos)
}
