package com.firechicken.rollingpictures.util

interface RetrofitCallback<T> {

    fun onError(t: Throwable)

    fun onSuccess(code: Int, responseData: T)

    fun onFailure(code: Int)
}
