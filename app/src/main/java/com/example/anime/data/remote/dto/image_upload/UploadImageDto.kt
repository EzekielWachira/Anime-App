package com.example.anime.data.remote.dto.image_upload

data class UploadImageDto(
    val error: String,
    val frameCount: Int,
    val result: List<Result>
)