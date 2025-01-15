package com.fiapx.video.http.resources

data class VideoUploadResponse(val uploadedVideos: List<UploadedVideoInfo>, val failedUploads: List<UploadedVideoInfo>)
