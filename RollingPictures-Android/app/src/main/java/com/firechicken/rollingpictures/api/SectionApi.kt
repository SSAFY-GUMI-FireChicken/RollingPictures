package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SectionApi {

    //섹션 생성
    @POST("api/section")
    fun makeSection(@Body body: SectionReqDTO): Call<ListResult<SectionResDTO>>

}