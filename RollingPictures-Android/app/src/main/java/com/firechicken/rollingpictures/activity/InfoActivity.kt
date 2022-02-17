package com.firechicken.rollingpictures.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.firechicken.rollingpictures.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        findViewById<ImageButton>(R.id.closeImageButton).setOnClickListener {
            finish()
        }
    }
}
