package com.firechicken.rollingpictures.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firechicken.rollingpictures.databinding.ActivityCreateRoomBinding

class CreateRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityCreateRoomBinding = ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(activityCreateRoomBinding.root)

        activityCreateRoomBinding.okButton.setOnClickListener {
            val intent = Intent(this@CreateRoomActivity, GameActivity::class.java)
            startActivity(intent)
        }

        activityCreateRoomBinding.cancelButton.setOnClickListener {
//            val intent = Intent(this@CreateRoomActivity, GameActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}