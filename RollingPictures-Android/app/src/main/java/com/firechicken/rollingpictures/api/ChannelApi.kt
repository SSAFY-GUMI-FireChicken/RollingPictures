package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ChannelApi {

    //방 생성
    @POST("api/channel")
    fun makeChannel(@Body body: MakeChannelReqDTO): Call<ChannelResDTO>

    //방 입장
    @POST("api/channel/user")
    fun inChannel(@Body body: InOutChannelReqDTO): Call<ChannelResDTO>
}