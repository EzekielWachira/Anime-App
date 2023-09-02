package com.example.anime.ui.screens.upload.state

import com.example.anime.data.remote.dto.image_upload.UploadImageDto
import com.example.anime.data.utils.StateWrapper

data class UploadState(
    val state: StateWrapper<UploadImageDto>
)
