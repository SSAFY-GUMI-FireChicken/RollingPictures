package com.firechicken.rollingpictures.util

import com.firechicken.rollingpictures.api.*
import com.firechicken.rollingpictures.config.ApplicationClass

class RetrofitUtil {
    companion object {
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val channelService = ApplicationClass.retrofit.create(ChannelApi::class.java)
        val gameChannelService = ApplicationClass.retrofit.create(GameChannelApi::class.java)
        val sectionService = ApplicationClass.retrofit.create(SectionApi::class.java)
        val roundService = ApplicationClass.retrofit.create(RoundApi::class.java)

    }
}
