package com.firechicken.rollingpictures.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.databinding.ActivityCreateRoomBinding
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback

private const val TAG = "CreateRoomActivity_싸피"

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
                    Toast.makeText(this@CreateRoomActivity, R.string.minimum_count, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    countTextView.setText(Integer.toString((maximumCnt - 1)))
                }
            }

            btnAddCount.setOnClickListener {
                val maximumCnt = countTextView.text.toString().toInt()
                if (maximumCnt == 6) {
                    Toast.makeText(this@CreateRoomActivity, R.string.maximum_count, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    countTextView.setText(Integer.toString((maximumCnt + 1)))
                }
            }

            okButton.setOnClickListener {
                makeChannel(prefs.getId()!!)
            }

            cancelButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun makeChannel(userId: Long) {
        val req = MakeChannelReqDTO(userId, "Title", "N", 4)
        Log.d(TAG, "makeChannel: ")
        ChannelService().makeChannel(req, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                if (responseData.data.id > 0) {
                    channelResDTO = responseData
                    Log.d(TAG, "onSuccess: ${responseData}")
                    val intent = Intent(this@CreateRoomActivity, GameWaitingActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@CreateRoomActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@CreateRoomActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@CreateRoomActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}