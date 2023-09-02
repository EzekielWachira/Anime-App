package com.example.anime.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.anime.domain.model.Anime

@Dao
interface AnimeDao {

    @Upsert
    suspend fun upsertAnimes(animes: List<Anime>)

    @Query("SELECT * FROM `animations`")
    fun getAnimes(): PagingSource<Int, Anime>

    @Query("DELETE FROM `animations`")
    suspend fun clearAnimes()

}