package com.example.anime.data.remote.api

import com.example.anime.data.remote.dto.animes.AnimeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeApi {

    @GET("top/anime")
    suspend fun getAnimes(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int
    ): AnimeDto

}