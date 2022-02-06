package com.firechicken.rollingpictures.dto


data class ListResult<T>(
    val data: List<T>,
    val msg: String?,
    val output :Int,
)