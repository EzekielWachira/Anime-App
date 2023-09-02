package com.example.anime.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.anime.data.local.dao.AnimeDao
import com.example.anime.domain.model.Anime
import com.example.anime.domain.model.AnimeJpgImage
import com.example.anime.domain.model.AnimeJpgImageDataConverter

@Database(
    entities = [
        Anime::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    AnimeJpgImageDataConverter::class
)
abstract class AnimeDatabase: RoomDatabase() {

    abstract val animeDao: AnimeDao

}