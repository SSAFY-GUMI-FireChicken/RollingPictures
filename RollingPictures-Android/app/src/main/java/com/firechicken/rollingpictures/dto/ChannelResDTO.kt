package com.firechicken.rollingpictures.dto

data class ChannelResDTO(

    var id: Long,
    var code: String,
    var title: String,
    var isPublic: String,
    var maxPeopleCnt: Int,
    var curPeopleCnt: Int,
    var users: MutableList<UserInfoResDTO>
)
