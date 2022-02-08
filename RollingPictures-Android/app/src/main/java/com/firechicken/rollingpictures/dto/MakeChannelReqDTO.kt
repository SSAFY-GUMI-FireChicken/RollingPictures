package com.firechicken.rollingpictures.dto

data class MakeChannelReqDTO(
    var id: Long,
    var title: String,
    var isPublic: String,
    var maxPeopleCnt: Int
)
