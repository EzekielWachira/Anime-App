package com.example.anime.data.mappers

interface DtoToDomain<in T, out Y> {
    fun toDomain(origin: T): Y
}