package com.example.anime.data.remote.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.anime.data.local.database.AnimeDatabase
import com.example.anime.data.remote.api.AnimeApi
import com.example.anime.data.remote.api.JikanApi
import com.example.anime.data.remote.mediator.AnimeRemoteMediator
import com.example.anime.data.repository.ImageUploadRepositoryImpl
import com.example.anime.data.utils.Constants.Network.ANIME_BASE_URL
import com.example.anime.data.utils.Constants.Network.TRACE_API_BASE_URL
import com.example.anime.domain.model.Anime
import com.example.anime.domain.repository.ImageUploadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import zerobranch.androidremotedebugger.logging.NetLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @AnimeAPI
    @Provides
    @Singleton
    fun provideAnimeApi(okHttpClient: OkHttpClient): AnimeApi {
        return Retrofit.Builder()
            .baseUrl(ANIME_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @JikanAPI
    @Provides
    @Singleton
    fun provideJikanApi(okHttpClient: OkHttpClient): JikanApi {
        return Retrofit.Builder()
            .baseUrl(TRACE_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        netLoggingInterceptor: NetLoggingInterceptor,
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(netLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    @Singleton
    fun provideNetLoggingInterceptor(): NetLoggingInterceptor =
        NetLoggingInterceptor()


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideAnimePager(database: AnimeDatabase,  @AnimeAPI api: AnimeApi): Pager<Int, Anime> {
        return Pager(
            config = PagingConfig(pageSize = 40),
            remoteMediator = AnimeRemoteMediator(
                animeDatabase = database,
                animeApi = api
            ),
            pagingSourceFactory =  {
                database.animeDao.getAnimes()
            }
        )
    }


    @Provides
    @Singleton
    fun provideImageUploadRepository(@JikanAPI api: JikanApi): ImageUploadRepository =
        ImageUploadRepositoryImpl(api)

}