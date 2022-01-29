package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.LoginUserReqDTO
import com.firechicken.rollingpictures.dto.LoginUserResDTO
import com.firechicken.rollingpictures.dto.SignUpReqDTO
import com.firechicken.rollingpictures.dto.UserIdResDTO
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // 회원가입
    @POST("api/nickname")
    fun signUp(@Body body: SignUpReqDTO): Call<UserIdResDTO>

    // 로그인.
    @GET("api/nickname")
    fun login(@Body body: LoginUserReqDTO): Call<LoginUserResDTO>

}