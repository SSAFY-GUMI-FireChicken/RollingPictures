package com.firechicken.rollingpictures.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.databinding.ActivityCreateRoomBinding

class CreateRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityCreateRoomBinding = ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(activityCreateRoomBinding.root)

        activityCreateRoomBinding.apply {
            publicButton.setOnClickListener {
                publicButton.setBackgroundDrawable(getDrawable(R.drawable.bg_round))
                privateButton.setBackgroundDrawable(getDrawable(R.drawable.bg_tool_detail))
            }

            privateButton.setOnClickListener {
                privateButton.setBackgroundDrawable(getDrawable(R.drawable.bg_round))
                publicButton.setBackgroundDrawable(getDrawable(R.drawable.bg_tool_detail))
            }

            btnSubCount.setOnClickListener {
                val maximumCnt = countTextView.text.toString().toInt()
                if (maximumCnt == 2) {
                    Toast.makeText(this@CreateRoomActivity, "정원은 2명 이상이어야 합니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    countTextView.setText(Integer.toString((maximumCnt - 1)))
                }
            }

            btnAddCount.setOnClickListener {
                val maximumCnt = countTextView.text.toString().toInt()
                if (maximumCnt == 6) {
                    Toast.makeText(this@CreateRoomActivity, "정원은 최대 6명입니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    countTextView.setText(Integer.toString((maximumCnt + 1)))
                }
            }

            okButton.setOnClickListener {
                val intent = Intent(this@CreateRoomActivity, GameWaitingActivity::class.java)
                startActivity(intent)
            }

            cancelButton.setOnClickListener {
                finish()
            }
        }
    }
}