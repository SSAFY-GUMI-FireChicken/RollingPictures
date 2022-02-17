package com.firechicken.rollingpictures.dto

data class ChannelListResDTO (
    var totalCnt: Long,
    var channels: MutableList<ChannelResDTO>
)