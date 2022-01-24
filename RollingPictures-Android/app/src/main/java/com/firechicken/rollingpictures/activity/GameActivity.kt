package com.firechicken.rollingpictures.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import com.firechicken.rollingpictures.fragment.GameDrawingFragment
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val activityGameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(activityGameBinding.root)

        val progressGrow = AnimationUtils.loadAnimation(this, R.anim.grow)
        val timeProgressBar = findViewById<ProgressBar>(R.id.timeProgressBar)
        timeProgressBar.startAnimation(progressGrow)

//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameWritingFragment())
//        transaction.commit()

        val transaction =
            supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameDrawingFragment())
        transaction.commit()

//        val transaction = supportFragmentManager.beginTransaction().add(R.id.frameLayout, GameFinishFragment())
//        transaction.commit()

    }
}