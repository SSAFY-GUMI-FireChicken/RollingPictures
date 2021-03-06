package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ChannelApi {

    //방 목록 조회
    @GET("api/channel")
    fun searchChannelList(
        @Query("page") page: Int,
        @Query("batch") batch: Int
    ): Call<SingleResult<ChannelListResDTO>>

    //방 생성
    @POST("api/channel")
    fun makeChannel(@Body body: MakeChannelReqDTO): Call<SingleResult<ChannelResDTO>>

    //방 입장
    @POST("api/channel/user")
    fun inChannel(@Body body: InOutChannelReqDTO): Call<SingleResult<ChannelResDTO>>

    //방 퇴장
    @HTTP(method = "DELETE", path = "api/channel/user", hasBody = true)
    fun outChannel(@Body body: InOutChannelReqDTO): Call<SingleResult<Any>>

    //방 정보 수정
    @PUT("api/channel")
    fun updateChannel(@Body body: MakeChannelReqDTO): Call<SingleResult<ChannelResDTO>>
}
