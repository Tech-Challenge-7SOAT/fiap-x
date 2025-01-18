package com.fiapx.video.extensions

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.videoio.VideoCapture
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.file.Files
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeBytes

fun S3Client.download(videoUrl: String): ByteArray {
    val key = "uploads/${videoUrl.split("/").last()}"

    val request = GetObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(key)
        .build()

    return this.getObject(request).readAllBytes()
}

fun S3Client.upload(videoData: MultipartFile, videoExtension: String): URL {
    val key = "uploads/${UUID.randomUUID()}-video.$videoExtension"
    val tempFile = Files.createTempFile("upload", videoData.originalFilename).also { videoData.transferTo(it) }

    val request = PutObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(key)
        .contentType(videoData.contentType ?: "application/octet-stream")
        .build()

    this.putObject(request, tempFile)
    return this.utilities().getUrl { it.bucket("fiapx-videos").key(key) }
}

fun S3Client.upload(videoData: ByteArray, videoExtension: String, contentType: String): URL {
    val key = "uploads/${UUID.randomUUID()}-video.$videoExtension"
    val tempFile = createTempFile("upload", "video.$videoExtension").also { it.writeBytes(videoData) }

    val request = PutObjectRequest.builder()
        .bucket("fiapx-videos")
        .key(key)
        .contentType(contentType)
        .build()

    this.putObject(request, tempFile)

    return this.utilities().getUrl { it.bucket("fiapx-videos").key(key) }
}

fun S3Client.framesFrom(videoUrl: String): ByteArray {
    val videoData = this.download(videoUrl)
    val videoExtension = videoUrl.split(".").last()
    val tempFile = createTempFile(UUID.randomUUID().toString(), ".${videoExtension}").also { it.writeBytes(videoData) }

    val grabber = FFmpegFrameGrabber(tempFile.absolutePathString())
    val converter = Java2DFrameConverter()

    try {
        grabber.start()

        val zipOutputStream = ByteArrayOutputStream()
        ZipOutputStream(zipOutputStream).use { zip ->
            var frameNumber = 0
            while (true) {
                val frame = grabber.grab() ?: break
                val bufferedImage = converter.convert(frame)
                bufferedImage?.let {
                    val frameByte = ByteArrayOutputStream()
                    ImageIO.write(bufferedImage, "png", frameByte)

                    val entry = ZipEntry("frame_${grabber.frameNumber}.png")
                    zip.putNextEntry(entry)
                    zip.write(frameByte.toByteArray())
                    zip.closeEntry()

                    frameNumber++
                }
            }

            println("Extracted $frameNumber frames from video $videoUrl")
        }

        return zipOutputStream.toByteArray()
    } finally {
        grabber.stop()
        grabber.release()
        tempFile.deleteIfExists()
    }
}
