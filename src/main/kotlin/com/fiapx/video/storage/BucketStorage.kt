package com.fiapx.video.storage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URL
import java.nio.file.Files
import java.util.UUID

@Service
class BucketStorage(private val client: S3Client) {

    fun put(video: MultipartFile): URL {
        val key = "uploads/${UUID.randomUUID()}-${video.originalFilename}"
        val tempFile = Files.createTempFile("upload", video.originalFilename).also { video.transferTo(it) }
        val request = PutObjectRequest.builder()
            .bucket("fiapx-videos")
            .key(key)
            .contentType(video.contentType ?: "application/octet-stream")
            .build()

        client.putObject(request, tempFile)
        return client.utilities().getUrl { it.bucket("fiapx-videos").key(key) }
    }
}
