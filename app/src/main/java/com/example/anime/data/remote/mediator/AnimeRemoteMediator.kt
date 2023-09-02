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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeRemoteMediator(
    private val animeApi: AnimeApi,
    private val animeDatabase: AnimeDatabase
) : RemoteMediator<Int, Anime>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Anime>): MediatorResult {
        return withContext(Dispatchers.IO) {
            try {
                val loadKey = when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND -> return@withContext MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    LoadType.APPEND -> {
                        val lastItem = animeDatabase.animeDao.getLastAnime()
                        lastItem?.nextPage
                    }
                } ?: 1

                val animeResponse = animeApi.getAnimes(
                    page = loadKey,
                    pageCount = state.config.pageSize
                )

                animeDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        animeDatabase.animeDao.clearAnimes()
                    }

                    val animes = animeResponse.data.map { anime ->
                        AnimeMapper.toDomain(anime).apply {
                            nextPage = animeResponse.pagination.current_page + 1
                        }
                    }
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


}