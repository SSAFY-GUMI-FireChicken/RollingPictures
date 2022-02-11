package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RoundApi {

    //라운드 등록
    @POST("api/round")
    fun roundRegister(@Body body: RoundReqDTO): Call<SingleResult<RoundResDTO>>

    //라운드 조회
    @GET("api/round")
    fun roundView(
        @Query("gameChannelId") gameChannelId: Long,
        @Query("id") id: Long,
//        @Query("keyword") keyword: String,
        @Query("roundNumber") roundNumber: Int
        ): Call<SingleResult<RoundResDTO>>

}