package com.example.anime.domain.repository

import com.example.anime.data.remote.dto.image_upload.UploadImageDto
import com.example.anime.data.utils.StateWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ImageUploadRepository {

    suspend fun uploadPicture(
        image: MultipartBody.Part
    ): Flow<StateWrapper<UploadImageDto>>

}