package com.firechicken.rollingpictures.dto

data class LoginUserReqDTO(
    var password: String,
    var token: String,
    var type:String,
    var uid:String
)
