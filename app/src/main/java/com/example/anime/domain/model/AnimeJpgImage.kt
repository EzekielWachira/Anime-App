package com.example.anime.domain.model

import androidx.room.TypeConverter
import com.google.gson.Gson

data class AnimeJpgImage(
    val imageUrl: String?,
    val largeImageUrl: String?,
    val smallImageUrl: String?
)

class AnimeJpgImageDataConverter {

    @TypeConverter
    fun toAnimeJpgImageString(animeJpgImage: AnimeJpgImage): String {
        return Gson().toJson(animeJpgImage)
    }

    @TypeConverter
    fun fromAnimeJpgImageString(str: String): AnimeJpgImage {
        return Gson().fromJson(str, AnimeJpgImage::class.java)
    }

}
