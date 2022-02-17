package com.firechicken.rollingpictures.dto

data class SingleResult<T>(
    var data: T,
    var msg: String?,
    var output: Int
)
