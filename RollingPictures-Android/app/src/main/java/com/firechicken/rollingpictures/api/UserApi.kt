package com.firechicken.rollingpictures.api

import com.firechicken.rollingpictures.dto.*
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // 회원가입
    @POST("api/nickname")
    fun signUp(@Body body: SignUpReqDTO): Call<SingleResult<UserIdResDTO>>

    // 로그인.
    @GET("api/nickname")
    fun login(
        @Query("password") password: String,
        @Query("type") type: String,
        @Query("uid") uid: String?,
    ): Call<SingleResult<LoginUserResDTO>>

    //회원정보 수정
    @PUT("api/nickname")
    fun userInfoUpdate(@Body body: UserInfoUpdateReqDTO): Call<SingleResult<UserIdResDTO>>


}