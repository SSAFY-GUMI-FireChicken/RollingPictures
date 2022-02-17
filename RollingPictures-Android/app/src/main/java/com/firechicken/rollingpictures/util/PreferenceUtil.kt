package com.firechicken.rollingpictures.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("preference", Context.MODE_PRIVATE)

    fun getUid(): String? {
        return prefs.getString("uid", "")
    }

    fun setUid(uid: String) {
        val editor = prefs.edit()
        editor.putString("uid", uid)
        editor.apply()
    }

    fun getNickName(): String? {
        return prefs.getString("nickname", "")
    }

    fun setNickName(nickname: String) {
        val editor = prefs.edit()
        editor.putString("nickname", nickname)
        editor.apply()
    }

    fun getId(): Long? {
        return prefs.getLong("id", -1)
    }

    fun setId(id: Long) {
        val editor = prefs.edit()
        editor.putLong("id", id)
        editor.apply()
    }

    fun getEnteredChannel(): String{
        return prefs.getString("in_channel", "none")!!
    }

    fun setEnteredChannel(code: String){
        val editor = prefs.edit()
        editor.putString("in_channel", code)
        editor.apply()
    }
}
