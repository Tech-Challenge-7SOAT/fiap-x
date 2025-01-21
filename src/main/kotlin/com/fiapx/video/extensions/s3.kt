package com.fiapx.video.extensions

import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URL
import java.nio.file.Files
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeBytes

fun S3Client.download(identifier: String): ByteArray {
    val request = GetObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(identifier)
        .build()

    return this.getObject(request).readAllBytes()
}

fun S3Client.upload(videoData: MultipartFile, identifier: String): URL {
    val tempFile = Files.createTempFile("upload", identifier).also { videoData.transferTo(it) }

    val request = PutObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(identifier)
        .contentType(videoData.contentType ?: "application/octet-stream")
        .build()

    this.putObject(request, tempFile)

    return this.utilities()
        .getUrl { it.bucket("fiapx-videos").key(identifier) }
        .also { tempFile.deleteIfExists() }
}

fun S3Client.upload(videoData: ByteArray, extension: String, contentType: String, identifier: String? = null): URL {
    val tempFile = createTempFile("upload", "video.$extension").also { it.writeBytes(videoData) }

    val request = PutObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(identifier)
        .contentType(contentType)
        .build()

    this.putObject(request, tempFile)

    return this.utilities().getUrl { it.bucket("fiapx-videos").key(identifier) }
}
