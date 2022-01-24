package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.firechicken.rollingpictures.databinding.ActivityLoginBinding
import com.firechicken.rollingpictures.dto.User
import com.firechicken.rollingpictures.preference.PreferenceUtil

class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var prefs: PreferenceUtil
        var user: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        prefs = PreferenceUtil(applicationContext)
        user = prefs.getUser()
        if (user?.uid == "") {
            user?.uid = Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        super.onCreate(savedInstanceState)

        val activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        if (user?.nickname != "") {
            activityLoginBinding.roomCodeEditText.setText(user?.nickname)
        }

        activityLoginBinding.loginButton.setOnClickListener {
            user?.apply {
                nickname = activityLoginBinding.roomCodeEditText.text.toString()
            }
            prefs.setUser(user)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        activityLoginBinding.infoImageButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, InfoActivity::class.java)
            startActivity(intent)
        }
    }
}