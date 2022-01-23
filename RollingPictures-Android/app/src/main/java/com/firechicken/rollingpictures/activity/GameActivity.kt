package com.firechicken.rollingpictures.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firechicken.rollingpictures.fragment.GameDrawingFragment
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityGameActivity = ActivityGameBinding.inflate(layoutInflater)
        setContentView(activityGameActivity.root)

//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameWritingFragment())
//        transaction.commit()

        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameDrawingFragment())
        transaction.commit()
//
//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameFinishFragment())
//        transaction.commit()

    }
}