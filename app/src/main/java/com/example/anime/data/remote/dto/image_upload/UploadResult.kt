package com.example.anime.data.remote.dto.image_upload

data class UploadResult(
    val anilist: Int,
//    val episode: Int?,
    val filename: String,
    val from: Double,
    val image: String,
    val similarity: Double,
    val to: Double,
    val video: String
)