package com.firechicken.rollingpictures.dto

data class RoundReqDTO(
    var gameChannelId: Long,
    var id: Long,
    var keyword: String?,
    var roundNumber: Int
)
