package com.firechicken.rollingpictures.service

import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoundService {

    //라운드 등록
    fun roundRegister(req: RoundReqDTO, multipartFile: MultipartBody.Part?, callback: RetrofitCallback<SingleResult<RoundResDTO>>) {
        RetrofitUtil.roundService.roundRegister(req, multipartFile)
            .enqueue(object : Callback<SingleResult<RoundResDTO>> {
                override fun onResponse(call: Call<SingleResult<RoundResDTO>>, response: Response<SingleResult<RoundResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<SingleResult<RoundResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
        })
    }

    //라운드 조회
    fun roundView(gameChannelId: Long, id: Long, roundNumber: Int, callback: RetrofitCallback<SingleResult<RoundResDTO>>) {
        RetrofitUtil.roundService.roundView(gameChannelId, id, roundNumber)
            .enqueue(object : Callback<SingleResult<RoundResDTO>> {
                override fun onResponse(call: Call<SingleResult<RoundResDTO>>, response: Response<SingleResult<RoundResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<SingleResult<RoundResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
            })
    }
}
