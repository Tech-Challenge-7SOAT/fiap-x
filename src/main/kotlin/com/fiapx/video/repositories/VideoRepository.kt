package com.fiapx.video.repositories

import com.fiapx.video.entities.Video
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<Video, Long> {
}
