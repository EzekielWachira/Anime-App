package com.example.anime.data.remote.dto.animes

data class AnimeDto(
    val `data`: List<Data>,
    val links: Links,
    val meta: Meta,
    val pagination: Pagination
)