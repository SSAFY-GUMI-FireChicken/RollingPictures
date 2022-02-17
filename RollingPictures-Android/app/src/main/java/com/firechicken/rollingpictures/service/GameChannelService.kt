package com.firechicken.rollingpictures.service

import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameChannelService {

    fun makeGameChannel(req: MakeGameChannelReqDTO, callback: RetrofitCallback<SingleResult<GameChannelResDTO>>) {
        RetrofitUtil.gameChannelService.makeGameChannel(req)
            .enqueue(object : Callback<SingleResult<GameChannelResDTO>> {
                override fun onResponse(call: Call<SingleResult<GameChannelResDTO>>, response: Response<SingleResult<GameChannelResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<SingleResult<GameChannelResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
        })
    }
}
