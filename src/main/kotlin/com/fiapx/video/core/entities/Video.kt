package com.fiapx.video.core.entities

import com.fiapx.video.core.valueObjects.Status
import jakarta.persistence.*

@Entity
@Table(name = "videos")
data class Video(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long,

    @Column(name = "video_url", nullable = false)
    val videoUrl: String,

    @Column(name = "frames_url", nullable = true)
    val framesUrl: String? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status
)
