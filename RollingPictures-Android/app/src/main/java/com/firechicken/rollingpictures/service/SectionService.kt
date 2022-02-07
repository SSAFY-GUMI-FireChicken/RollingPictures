package com.firechicken.rollingpictures.service

import android.util.Log
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SectionService_싸피"

class SectionService {

    fun makeSection(req: SectionReqDTO, callback: RetrofitCallback<ListResult<SectionResDTO>>) {
        RetrofitUtil.sectionService.makeSection(req).enqueue(object :
            Callback<ListResult<SectionResDTO>> {
            override fun onResponse(call: Call<ListResult<SectionResDTO>>, response: Response<ListResult<SectionResDTO>>) {
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

            override fun onFailure(call: Call<ListResult<SectionResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

}