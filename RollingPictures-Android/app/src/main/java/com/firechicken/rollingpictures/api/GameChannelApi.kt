package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GameChannelApi {

    //게임 방 생성
    @POST("api/gamechannel")
    fun makeGameChannel(@Body body: MakeGameChannelReqDTO): Call<SingleResult<GameChannelResDTO>>

}