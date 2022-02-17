package com.firechicken.rollingpictures.service

import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SectionService {

    fun makeSection(req: SectionReqDTO, callback: RetrofitCallback<ListResult<SectionResDTO>>) {
        RetrofitUtil.sectionService.makeSection(req)
            .enqueue(object : Callback<ListResult<SectionResDTO>> {
                override fun onResponse(call: Call<ListResult<SectionResDTO>>, response: Response<ListResult<SectionResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<ListResult<SectionResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
        })
    }

    fun getAllSection(gameChannelId: Long, callback: RetrofitCallback<ListResult<SectionAllRetrieveResDTO>>) {
        RetrofitUtil.sectionService.getAllSection(gameChannelId)
            .enqueue(object : Callback<ListResult<SectionAllRetrieveResDTO>> {
                override fun onResponse(call: Call<ListResult<SectionAllRetrieveResDTO>>, response: Response<ListResult<SectionAllRetrieveResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<ListResult<SectionAllRetrieveResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
            })
    }
}
