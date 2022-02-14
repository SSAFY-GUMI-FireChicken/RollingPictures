package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface RoundApi {

//    //라운드 등록
//    @POST("api/round")
//    fun roundRegister(
//        @Query("gameChannelId") gameChannelId: Long,
//        @Query("id") id: Long,
//        @Query("keyword") keyword: String?,
//        @Query("roundNumber") roundNumber: Int,
//        @Part("multipartFile") multipartFile: MultipartBody.Part?
//    ): Call<SingleResult<RoundResDTO>>

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
//        @Query("keyword") keyword: String,
        @Query("roundNumber") roundNumber: Int
        ): Call<SingleResult<RoundResDTO>>

}