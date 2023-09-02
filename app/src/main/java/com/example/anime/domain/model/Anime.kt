package com.example.anime.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animations")
data class Anime(
    @PrimaryKey(autoGenerate = false)
    val malId: Long,
    val url: String,
    val image: AnimeJpgImage,
    val title: String,
    val titleEnglish: String,
    val type: String,
    val source: String,
    val status: String,
    val duration: String,
    val rating: String,
    val score: Double,
    val rank: Long,
    val popularity: Int,
    val members: Long,
    val favorites: Long,
    val year: Int?
)
