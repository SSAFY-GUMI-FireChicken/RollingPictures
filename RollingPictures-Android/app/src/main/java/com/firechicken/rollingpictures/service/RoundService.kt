package com.firechicken.rollingpictures.service

import android.util.Log
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "RoundService_싸피"

class RoundService {

    //라운드 등록
    fun roundRegister(round: RoundReqDTO, callback: RetrofitCallback<SingleResult<RoundResDTO>>) {
        RetrofitUtil.roundService.roundRegister(round).enqueue(object : Callback<SingleResult<RoundResDTO>> {
            override fun onResponse(call: Call<SingleResult<RoundResDTO>>, response: Response<SingleResult<RoundResDTO>>) {
                val res = response.body()
                Log.d(TAG, "onResponse: ${res}")
                Log.d(TAG, "onResponse: prev")
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

            override fun onFailure(call: Call<SingleResult<RoundResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }


    //라운드 조회
    fun roundView(
        gameChannelId: Long,
        id: Long,
//        keyword: String,
        roundNumber: Int,
        callback: RetrofitCallback<SingleResult<RoundResDTO>>
    ) {
        RetrofitUtil.roundService.roundView(gameChannelId, id, roundNumber)
            .enqueue(object : Callback<SingleResult<RoundResDTO>> {
                override fun onResponse(
                    call: Call<SingleResult<RoundResDTO>>,
                    response: Response<SingleResult<RoundResDTO>>
                ) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            Log.d(TAG, "onResponse1: ${res}")
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        Log.d(TAG, "onResponse2: ")

                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<SingleResult<RoundResDTO>>, t: Throwable) {
                    Log.d(TAG, "onResponse3: ")
                    callback.onError(t)
                }
            })
    }
}