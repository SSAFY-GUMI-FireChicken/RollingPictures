package com.firechicken.rollingpictures.preference

import android.content.Context
import android.content.SharedPreferences
import com.firechicken.rollingpictures.dto.User

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("preference", Context.MODE_PRIVATE)

    fun getUser(): User?{
        val uid = prefs.getString("uid", "")
        if (uid == ""){
            return User()
        }else{
            val nickname = prefs.getString("nickname", "")
            val mute = prefs.getBoolean("mute", false)
            val state = prefs.getBoolean("state", false)
            val token = prefs.getString("token", "")
            return User(uid, nickname, mute,state, token)
        }
    }

    fun setUser(user:User?){
        val editor = prefs.edit()
        editor.putString("uid", user?.uid)
        editor.putString("nickname", user?.nickName)
        editor.apply()
    }

}

