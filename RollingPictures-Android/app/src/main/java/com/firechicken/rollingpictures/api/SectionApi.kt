package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SectionApi {

    //섹션 생성
    @POST("api/section")
    fun makeSection(@Body body: SectionReqDTO): Call<ListResult<SectionResDTO>>

    // 섹션조회
    @GET("api/section")
    fun getSection(
        @Query("gameChannelId") gameChannelId: Long,
        @Query("userId") userId: Long
    ): Call<ListResult<SectionRetrieveResDTO>>

}