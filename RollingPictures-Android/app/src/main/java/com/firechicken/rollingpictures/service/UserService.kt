package com.firechicken.rollingpictures.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.firechicken.rollingpictures.dto.LoginUserReqDTO
import com.firechicken.rollingpictures.dto.LoginUserResDTO
import com.firechicken.rollingpictures.dto.SignUpReqDTO
import com.firechicken.rollingpictures.dto.UserIdResDTO
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val TAG = "UserService_싸피"

class UserService {

    fun login(user:LoginUserReqDTO, callback: RetrofitCallback<LoginUserResDTO>)  {
        RetrofitUtil.userService.login(user).enqueue(object : Callback<LoginUserResDTO> {
            override fun onResponse(call: Call<LoginUserResDTO>, response: Response<LoginUserResDTO>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<LoginUserResDTO>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun signUp(user: SignUpReqDTO, callback: RetrofitCallback<UserIdResDTO>)  {
        RetrofitUtil.userService.signUp(user).enqueue(object : Callback<UserIdResDTO> {
            override fun onResponse(call: Call<UserIdResDTO>, response: Response<UserIdResDTO>) {
                val res = response.body()
                Log.d(TAG, "onResponse: prev")
                if(response.code() == 200){
                    Log.d(TAG, "onResponse: 성공")
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    Log.d(TAG, "onResponse: 다른코드")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<UserIdResDTO>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                callback.onError(t)
            }
        })
    }


}