package com.example.anime.data.utils

import okhttp3.ResponseBody

sealed class StateWrapper<out T> {
    data class Success<out T>(val data: T) : StateWrapper<T>()
    data class Failure<T>(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorMessage: String,
        val errorBody: ResponseBody?
    ) : StateWrapper<T>()

    object Loading : StateWrapper<Nothing>()

    object Empty : StateWrapper<Nothing>()
//    object Complete : StateWrapper<Nothing>()
}
