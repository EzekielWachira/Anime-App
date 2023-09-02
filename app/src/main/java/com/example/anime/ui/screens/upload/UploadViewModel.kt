package com.example.anime.ui.screens.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anime.data.utils.StateWrapper
import com.example.anime.domain.usecase.ImageUploadUseCase
import com.example.anime.ui.screens.upload.state.ImageState
import com.example.anime.ui.screens.upload.state.UploadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val imageUploadUseCase: ImageUploadUseCase
): ViewModel() {

    private val _imageState = MutableStateFlow(ImageState())
    val imageState get() = _imageState.asStateFlow()

    private val _uploadState = MutableStateFlow(UploadState(StateWrapper.Empty))
    val uploadState get() = _uploadState.asStateFlow()

    fun onImageSelect(imageUri: Uri?) {
        _imageState.update {
            imageState.value.copy(
                uri = imageUri
            )
        }
    }

    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            imageUploadUseCase.invoke(image).collectLatest { state->
                _uploadState.update {
                    uploadState.value.copy(
                        state = state
                    )
                }
            }
        }
    }

}