package com.fiapx.video.entities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class VideoMessage @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("videoUrl") val videoUrl: String
)
