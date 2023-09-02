package com.example.anime.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCallWithFlow(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T):
        Flow<StateWrapper<T>> {
    return withContext(dispatcher) {
        flow {
            try {
                emit(StateWrapper.Success(apiCall.invoke()))
            } catch (throwable: Throwable) {
                emit(
                    when (throwable) {
                        is IOException -> StateWrapper.Failure(
                            isNetworkError = true,
                            errorCode = null,
                            errorMessage = "Couldn't connect to server, check your internet connection",
                            errorBody = null
                        )
                        is HttpException -> {
                            if (throwable.code() == 405) {
                                StateWrapper.Failure(
                                    isNetworkError = false,
                                    errorCode = throwable.code(),
                                    errorMessage = "Failed. Please try again later",
                                    errorBody = throwable.response()?.errorBody()
                                )
                            } else {
                                StateWrapper.Failure(
                                    isNetworkError = false,
                                    errorCode = throwable.code(),
                                    errorMessage = "Unexpected error occurred",
                                    errorBody = throwable.response()?.errorBody()
                                )
                            }
                        }
                        else -> {
                            StateWrapper.Failure(
                                isNetworkError = false,
                                errorCode = null,
                                errorMessage = throwable.localizedMessage
                                    ?: "Unexpected error occurred",
                                errorBody = null
                            )
                        }
                    }
                )
            }
        }.onStart {
            emit(StateWrapper.Loading)
        }
//            .onCompletion {
//            emit(StateWrapper.Complete)
//        }
    }
}