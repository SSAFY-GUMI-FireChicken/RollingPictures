package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RoundApi {

    //라운드 등록
    @Multipart
    @POST("api/round")
    fun roundRegister(
        @Part("req") req: RoundReqDTO,
        @Part multipartFile: MultipartBody.Part?
    ): Call<SingleResult<RoundResDTO>>

    //라운드 조회
    @GET("api/round")
    fun roundView(
        @Query("gameChannelId") gameChannelId: Long,
        @Query("id") id: Long,
        @Query("roundNumber") roundNumber: Int
        ): Call<SingleResult<RoundResDTO>>
}
