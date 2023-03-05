package com.myshopproject.domain.utils

import okhttp3.ResponseBody

sealed class Resource<out R>(val data: R? = null) {
    object Loading : Resource<Nothing>()
    class Success<R>(data: R) : Resource<R>(data)
    data class Error(val message: String?, val errorCode: Int?, val errorBody: ResponseBody?) : Resource<Nothing>()
}