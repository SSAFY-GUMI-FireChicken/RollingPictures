package com.firechicken.rollingpictures_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//참여자 리스트는 데이터가 아직 없어 리사이클러뷰는 아직 생성X
class GameWaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_waiting)
    }
}