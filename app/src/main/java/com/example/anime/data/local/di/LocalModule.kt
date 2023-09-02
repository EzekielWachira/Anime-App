package com.example.anime.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.anime.data.local.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase =
        Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            "animes.db"
        ).build()

    @Provides
    @Singleton
    fun provideAnimeDao(database: AnimeDatabase) = database.animeDao

}