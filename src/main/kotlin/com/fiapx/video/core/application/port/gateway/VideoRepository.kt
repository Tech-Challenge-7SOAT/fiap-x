package com.fiapx.video.core.application.port.gateway

import com.fiapx.video.core.entities.Video
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<Video, Long> {
}
