package com.firechicken.rollingpictures.service

import android.util.Log
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserService_싸피"

class UserService {

    fun login(
        password: String,
        type: String,
        uid: String,
        callback: RetrofitCallback<SingleResult<LoginUserResDTO>>
    ) {
        Log.d(TAG, "password: ${password}")
        Log.d(TAG, "type: ${type}")
        Log.d(TAG, "uid: ${uid}")
        RetrofitUtil.userService.login(password, type, uid)
            .enqueue(object : Callback<SingleResult<LoginUserResDTO>> {
                override fun onResponse(
                    call: Call<SingleResult<LoginUserResDTO>>,
                    response: Response<SingleResult<LoginUserResDTO>>
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

                override fun onFailure(call: Call<SingleResult<LoginUserResDTO>>, t: Throwable) {
                    Log.d(TAG, "onResponse3: ")
                    callback.onError(t)
                }
            })
    }

    fun signUp(user: SignUpReqDTO, callback: RetrofitCallback<SingleResult<UserIdResDTO>>) {
        RetrofitUtil.userService.signUp(user).enqueue(object : Callback<SingleResult<UserIdResDTO>> {
            override fun onResponse(call: Call<SingleResult<UserIdResDTO>>, response: Response<SingleResult<UserIdResDTO>>) {
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

            override fun onFailure(call: Call<SingleResult<UserIdResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

    fun userInfoUpdate(user: UserInfoUpdateReqDTO, callback: RetrofitCallback<SingleResult<UserIdResDTO>>) {
        RetrofitUtil.userService.userInfoUpdate(user).enqueue(object : Callback<SingleResult<UserIdResDTO>> {
            override fun onResponse(call: Call<SingleResult<UserIdResDTO>>, response: Response<SingleResult<UserIdResDTO>>) {
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

            override fun onFailure(call: Call<SingleResult<UserIdResDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }

}