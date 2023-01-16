package com.myshopproject.domain.utils

import okhttp3.ResponseBody

sealed class Resource<out R>(val data: R? = null) {
    object Loading : Resource<Nothing>()
    class Success<R>(data: R) : Resource<R>(data)
    data class Error(
//        val isNetworkError: Boolean,
        val message: String?,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()
}


sealed class NetworkResult<T : Any> {
    class Success<T: Any>(val data: T) : NetworkResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T: Any>(val t: Throwable) : NetworkResult<T>()
}