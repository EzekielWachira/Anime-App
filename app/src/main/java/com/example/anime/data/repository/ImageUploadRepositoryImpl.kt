package com.example.anime.data.repository

import com.example.anime.data.remote.api.JikanApi
import com.example.anime.data.remote.di.JikanAPI
import com.example.anime.data.remote.dto.image_upload.UploadImageDto
import com.example.anime.data.utils.StateWrapper
import com.example.anime.data.utils.safeApiCallWithFlow
import com.example.anime.domain.repository.ImageUploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.Dispatcher
import okhttp3.MultipartBody
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    @JikanAPI private val api: JikanApi
) : ImageUploadRepository {

    override suspend fun uploadPicture(image: MultipartBody.Part): Flow<StateWrapper<UploadImageDto>> {
        return safeApiCallWithFlow(Dispatchers.IO) {
            api.uploadPicture(image)
        }
    }
}