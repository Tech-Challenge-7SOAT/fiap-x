package com.fiapx.video.core.requests

data class VideoUploadResponse(val uploadedVideos: List<UploadedVideoInfo>, val failedUploads: List<UploadedVideoInfo>)
