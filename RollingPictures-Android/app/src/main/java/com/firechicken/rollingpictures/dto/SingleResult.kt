package com.firechicken.rollingpictures.dto

import com.google.gson.annotations.SerializedName


data class SingleResult<T>(
    val data: T,
    val msg: String?,
    val output :Int,
)