package com.firechicken.rollingpictures.config

import android.app.Application
import android.util.Log
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.PreferenceUtil

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApplicationClass_싸피"
class ApplicationClass : Application() {
    companion object{
        const val SERVER_URL = "http://192.168.0.9:8185/"
        const val websocketURL = "ws://192.168.0.9:8185/rolling-pictures/websocket"
        lateinit var retrofit: Retrofit
        lateinit var prefs: PreferenceUtil
        lateinit var loginUserResDTO: SingleResult<LoginUserResDTO>
        lateinit var channelResDTO: SingleResult<ChannelResDTO>

        //방의 플레이어 목록
        val playerList: MutableList<UserInfoResDTO> =
            mutableListOf()
    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d(TAG, "onCreate: ")

        prefs = PreferenceUtil(applicationContext)
    }

}