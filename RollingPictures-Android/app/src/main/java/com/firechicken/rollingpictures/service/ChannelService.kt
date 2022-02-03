package com.firechicken.rollingpictures.service

import android.util.Log
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserService_싸피"

class ChannelService {

    fun makeChannel(req: MakeChannelReqDTO, callback: RetrofitCallback<ChannelResDTO>) {
        RetrofitUtil.channelService.makeChannel(req).enqueue(object : Callback<ChannelResDTO> {
            override fun onResponse(call: Call<ChannelResDTO>, response: Response<ChannelResDTO>) {
                val res = response.body()
                Log.d(TAG, "onResponse: ${res}")
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 성공")
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    Log.d(TAG, "onResponse: 다른코드")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<ChannelResDTO>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

    fun inChannel(req: InOutChannelReqDTO, callback: RetrofitCallback<ChannelResDTO>) {
        RetrofitUtil.channelService.inChannel(req).enqueue(object : Callback<ChannelResDTO> {
            override fun onResponse(call: Call<ChannelResDTO>, response: Response<ChannelResDTO>) {
                val res = response.body()
                Log.d(TAG, "onResponse: ${res}")
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 성공")
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    Log.d(TAG, "onResponse: 다른코드")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<ChannelResDTO>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

}