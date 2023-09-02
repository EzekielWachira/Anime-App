package com.example.anime.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.anime.data.local.database.AnimeDatabase
import com.example.anime.data.mappers.AnimeMapper
import com.example.anime.data.remote.api.AnimeApi
import com.example.anime.domain.model.Anime
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeRemoteMediator(
    private val animeApi: AnimeApi,
    private val animeDatabase: AnimeDatabase
) : RemoteMediator<Int, Anime>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Anime>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastItem.malId / state.config.pageSize + 1
                    }
                }
            }

            val animeResponse = animeApi.getAnimes(
                page = loadKey.toInt(),
                pageCount = state.config.pageSize
            )

            animeDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    animeDatabase.animeDao.clearAnimes()
                }

                val animes = animeResponse.data.map { anime -> AnimeMapper.toDomain(anime) }
                animeDatabase.animeDao.upsertAnimes(animes)
            }

            MediatorResult.Success(
                endOfPaginationReached = !animeResponse.pagination.has_next_page
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }


}