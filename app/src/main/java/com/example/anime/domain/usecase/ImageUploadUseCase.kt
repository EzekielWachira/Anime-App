package com.example.anime.domain.usecase

import com.example.anime.domain.repository.ImageUploadRepository
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ImageUploadUseCase @Inject constructor(
    private val imageUploadRepository: ImageUploadRepository
) {

    suspend operator fun invoke(
        image: MultipartBody.Part
    ) = imageUploadRepository.uploadPicture(image)

}