package com.example.anime.data.mappers

import com.example.anime.data.remote.dto.animes.Data
import com.example.anime.domain.model.Anime
import com.example.anime.domain.model.AnimeJpgImage

object AnimeMapper : DtoToDomain<Data, Anime> {
    override fun toDomain(origin: Data): Anime {
        return Anime(
            malId = origin.mal_id,
            url = origin.url,
            image = AnimeJpgImage(
                imageUrl = origin.images?.jpg?.image_url,
                largeImageUrl = origin.images?.jpg?.large_image_url,
                smallImageUrl = origin.images?.jpg?.small_image_url
            ),
            title = origin.title,
            titleEnglish = origin.title_english,
            type = origin.type,
            source = origin.source,
            status = origin.status,
            duration = origin.duration,
            rating = origin.rating,
            score = origin.score,
            rank = origin.rank,
            popularity = origin.popularity,
            members = origin.members,
            favorites = origin.favorites,
            year = origin.year,
            lastUpdatedAt = System.currentTimeMillis()
        )
    }
}