package com.fiapx.video.repositories

import com.fiapx.video.entities.Status
import com.fiapx.video.entities.Video
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface VideoRepository : CrudRepository<Video, Long> {

    @Query("update Video v set v.status = :status, v.framesUrl = :framesUrl where v.id = :id")
    @Modifying
    fun updateById(id: Long, status: Status, framesUrl: String): Unit
}
