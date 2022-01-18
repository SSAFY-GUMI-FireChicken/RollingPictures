package com.firechicken.rollingpictures_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val entranceButton = findViewById<ImageButton>(R.id.entranceButton)
        entranceButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameWaitingActivity::class.java)
            startActivity(intent)
        }
    }
}