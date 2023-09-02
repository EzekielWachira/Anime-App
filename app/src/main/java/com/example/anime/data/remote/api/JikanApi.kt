package com.example.anime.data.remote.api

import com.example.anime.data.remote.dto.image_upload.UploadImageDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface JikanApi {

    @Multipart
    @POST("search")
    suspend fun uploadPicture(
        @Part image: MultipartBody.Part
    ): UploadImageDto

}