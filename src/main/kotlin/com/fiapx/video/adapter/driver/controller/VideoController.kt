package com.fiapx.video.adapter.driver.controller

import com.fiapx.video.core.requests.StoreVideoRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class VideoController {

    @GetMapping("/")
    fun index() = emptyList<String>()

    @GetMapping("/{id}")
    fun find(@PathVariable id: String) = emptyList<String>()

    @PostMapping("/")
    fun store(@RequestBody request: StoreVideoRequest) = emptyList<String>()

    @DeleteMapping("/{id}")
    fun destroy(@PathVariable id: String) = emptyList<String>()
}
