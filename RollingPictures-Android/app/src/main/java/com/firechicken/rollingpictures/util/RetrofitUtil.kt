package com.firechicken.rollingpictures.util

import com.firechicken.rollingpictures.api.ChannelApi
import com.firechicken.rollingpictures.api.UserApi
import com.firechicken.rollingpictures.config.ApplicationClass

class RetrofitUtil {
    companion object {
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val channelService = ApplicationClass.retrofit.create(ChannelApi::class.java)
    }
}