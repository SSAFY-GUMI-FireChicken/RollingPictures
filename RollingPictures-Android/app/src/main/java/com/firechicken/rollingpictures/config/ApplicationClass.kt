package com.firechicken.rollingpictures.config

import android.app.Application
import android.util.Log
import com.firechicken.rollingpictures.dto.LoginUserResDTO
import com.firechicken.rollingpictures.dto.UserIdResDTO
import com.firechicken.rollingpictures.util.PreferenceUtil

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApplicationClass_싸피"
class ApplicationClass : Application() {
    companion object{
        const val SERVER_URL = "http://192.168.35.194:8185/"
        lateinit var retrofit: Retrofit
        lateinit var prefs: PreferenceUtil
        lateinit var loginUserResDTO: LoginUserResDTO
        lateinit var userIdResDTO: UserIdResDTO
    }


    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d(TAG, "onCreate: ")
    }

}