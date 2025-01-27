package com.fiapx.video.http.controllers

import com.fiapx.video.services.VideoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "Video", description = "Video controller")
class VideoController(private val service: VideoService) {

    @GetMapping("/")
    @Operation(summary = "Get videos")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    ])
    fun index() = service.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get video by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "404", description = "Not Found"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    ])
    fun find(@PathVariable id: Long) = service.findById(id)

    @PostMapping("/")
    @Operation(summary = "Store videos")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    ])
    fun store(@RequestParam("videos") videos: List<MultipartFile>) = service.store(videos)
}
