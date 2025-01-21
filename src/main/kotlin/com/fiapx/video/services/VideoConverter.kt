package com.fiapx.video.services

import com.fiapx.video.extensions.download
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import java.io.ByteArrayOutputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeBytes

@Service
class VideoConverter {

    private val frameConverter = Java2DFrameConverter()

    fun from(video: ByteArray, identifier: String, extension: String): ByteArray {
        val temporaryFile = kotlin.io.path.createTempFile(
            prefix = identifier,
            suffix = ".$extension"
        ).also { it.writeBytes(video) }

        val grabber = FFmpegFrameGrabber(temporaryFile.absolutePathString())

        try {
            grabber.start()
            val outputStream = ByteArrayOutputStream()
            ZipOutputStream(outputStream).use { zip ->
                while (true) {
                    val currentFrame = grabber.grab() ?: break
                    frameConverter.convert(currentFrame)?.let {
                        val frameByte = ByteArrayOutputStream()
                        ImageIO.write(it, "png", frameByte)

                        val entry = ZipEntry("frame_${grabber.frameNumber}.png")
                        zip.putNextEntry(entry)
                        zip.write(frameByte.toByteArray())
                        zip.closeEntry()
                    }
                }
            }

            return outputStream.toByteArray()
        } finally {
            grabber.stop()
            grabber.release()
            temporaryFile.deleteIfExists()
        }
    }
}
