package com.firechicken.rollingpictures.dto

data class ChannelResDTO(
    var code: String,
    var id: Long,
    var users: MutableList<UserInfoResDTO>
)
