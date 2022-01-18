package com.firechicken.rollingpictures_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}