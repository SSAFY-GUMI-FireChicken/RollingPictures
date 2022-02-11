package com.firechicken.rollingpictures.dto

import com.google.gson.annotations.SerializedName


data class SingleResult<T>(
    var data: T,
    var msg: String?,
    var output :Int,
)