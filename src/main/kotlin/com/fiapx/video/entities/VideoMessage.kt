package com.fiapx.video.entities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class VideoMessage @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("url") val url: String,
    @JsonProperty("identifier") val identifier: String,
    @JsonProperty("extension") val extension: String
)
