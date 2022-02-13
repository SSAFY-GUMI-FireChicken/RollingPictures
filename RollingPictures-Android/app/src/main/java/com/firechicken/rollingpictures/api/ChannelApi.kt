package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ChannelApi {

    //방 생성
    @POST("api/channel")
    fun makeChannel(@Body body: MakeChannelReqDTO): Call<SingleResult<ChannelResDTO>>

    //방 입장
    @POST("api/channel/user")
    fun inChannel(@Body body: InOutChannelReqDTO): Call<SingleResult<ChannelResDTO>>

    //방 퇴장
//    @DELETE("api/channel/user")
    @HTTP(method = "DELETE", path = "api/channel/user", hasBody = true)
    fun outChannel(@Body body: InOutChannelReqDTO): Call<SingleResult<Any>>
//    fun outChannel(@Body body: InOutChannelReqDTO): Call<ChannelResDTO>
}

