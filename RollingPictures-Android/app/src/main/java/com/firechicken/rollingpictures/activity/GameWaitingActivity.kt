package com.firechicken.rollingpictures.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.databinding.ActivityGameWaitingBinding

//참여자 리스트는 데이터가 아직 없어 리사이클러뷰는 아직 생성X
class GameWaitingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityGameWaitingBinding = ActivityGameWaitingBinding.inflate(layoutInflater)
        setContentView(activityGameWaitingBinding.root)

        activityGameWaitingBinding.exitRoom.setOnClickListener {
            val dialog = GameExitDialog(this)
            dialog.showDialog()
        }
    }
}