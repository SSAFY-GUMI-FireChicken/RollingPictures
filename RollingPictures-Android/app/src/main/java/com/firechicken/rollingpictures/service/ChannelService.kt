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

    fun makeChannel(req: MakeChannelReqDTO, callback: RetrofitCallback<SingleResult<ChannelResDTO>>) {
        RetrofitUtil.channelService.makeChannel(req).enqueue(object : Callback<SingleResult<ChannelResDTO>> {
            override fun onResponse(call: Call<SingleResult<ChannelResDTO>>, response: Response<SingleResult<ChannelResDTO>>) {
                val res = response.body()
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

            override fun onFailure(call: Call<SingleResult<ChannelResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

    fun inChannel(req: InOutChannelReqDTO, callback: RetrofitCallback<SingleResult<ChannelResDTO>>) {
        RetrofitUtil.channelService.inChannel(req).enqueue(object : Callback<SingleResult<ChannelResDTO>> {
            override fun onResponse(call: Call<SingleResult<ChannelResDTO>>, response: Response<SingleResult<ChannelResDTO>>) {
                val res = response.body()
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse1: 성공")
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    Log.d(TAG, "onResponse1: 다른코드")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<SingleResult<ChannelResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure1: ")
                callback.onError(t)
            }
        })
    }

    fun outChannel(req: InOutChannelReqDTO, callback: RetrofitCallback<SingleResult<Any>>) {
        RetrofitUtil.channelService.outChannel(req).enqueue(object : Callback<SingleResult<Any>> {
            override fun onResponse(call: Call<SingleResult<Any>>, response: Response<SingleResult<Any>>) {
                val res = response.body()
                Log.d(TAG, "onRes111: ${res}")
                Log.d(TAG, "onResponse111: ${response}")
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse111: 성공")
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    Log.d(TAG, "onResponse: 다른코드")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<SingleResult<Any>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

}