package com.firechicken.rollingpictures_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firechicken.rollingpictures_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.createButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }

        activityMainBinding.entranceButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameWaitingActivity::class.java)
            startActivity(intent)
        }
    }
}