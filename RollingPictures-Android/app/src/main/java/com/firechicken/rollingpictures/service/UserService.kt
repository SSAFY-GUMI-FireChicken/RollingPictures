package com.firechicken.rollingpictures.service

import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserService {

    fun login(password: String, type: String, uid: String, callback: RetrofitCallback<SingleResult<LoginUserResDTO>>) {
        RetrofitUtil.userService.login(password, type, uid)
            .enqueue(object : Callback<SingleResult<LoginUserResDTO>> {
                override fun onResponse(call: Call<SingleResult<LoginUserResDTO>>, response: Response<SingleResult<LoginUserResDTO>>) {
                    val res = response.body()
                    if (response.code() == 200) {
                        if (res != null) {
                            callback.onSuccess(response.code(), res)
                        }
                    } else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<SingleResult<LoginUserResDTO>>, t: Throwable) {
                    callback.onError(t)
                }
            })
    }

    fun signUp(user: SignUpReqDTO, callback: RetrofitCallback<SingleResult<UserIdResDTO>>) {
        RetrofitUtil.userService.signUp(user)
            .enqueue(object : Callback<SingleResult<UserIdResDTO>> {
            override fun onResponse(call: Call<SingleResult<UserIdResDTO>>, response: Response<SingleResult<UserIdResDTO>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<SingleResult<UserIdResDTO>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun userInfoUpdate(user: UserInfoUpdateReqDTO, callback: RetrofitCallback<SingleResult<UserIdResDTO>>) {
        RetrofitUtil.userService.userInfoUpdate(user).enqueue(object : Callback<SingleResult<UserIdResDTO>> {
            override fun onResponse(call: Call<SingleResult<UserIdResDTO>>, response: Response<SingleResult<UserIdResDTO>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<SingleResult<UserIdResDTO>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
}
