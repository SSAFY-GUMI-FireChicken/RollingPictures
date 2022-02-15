package com.firechicken.rollingpictures.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundNum
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.RoundService
import com.firechicken.rollingpictures.util.RetrofitCallback
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_game_drawing.*
import kotlinx.android.synthetic.main.fragment_game_writing.*
import kotlinx.android.synthetic.main.fragment_game_writing.completeButton


private const val TAG = "GameWritingFragment_싸피"
class GameWritingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as GameActivity).apply {
            timeProgressBar.visibility = View.VISIBLE
            roundTextView.setText("Round ${roundNum}")
        }

        return inflater.inflate(R.layout.fragment_game_writing, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(roundNum==1){
            guideRoundTextView.setText(getString(R.string.guide_round1))
            pictureImageView.visibility = View.GONE
        }else{
            guideRoundTextView.setText(getString(R.string.guide_round2))
            pictureImageView.visibility = View.VISIBLE
        }

        roundView(gameChannelResDTO.data.id, ApplicationClass.loginUserResDTO.data.id, roundNum)

        completeButton.setOnClickListener {
            completeButton.text = "SUBMITED"
            completeButton.isEnabled = false
            Log.d(TAG, "onViewCreated: ${gameChannelResDTO.data.id}, ${prefs.getId()!!}, ${writingEditText.text.toString()}, ${roundNum}")
            val req = RoundReqDTO(gameChannelResDTO.data.id, prefs.getId()!!, writingEditText.text.toString(), roundNum)
            roundRegister(req)
        }
    }

    private fun roundRegister(req: RoundReqDTO) {
        RoundService().roundRegister(req, null, object : RetrofitCallback<SingleResult<RoundResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<RoundResDTO>) {
                if (responseData.output==1) {

                } else {
                    Toast.makeText(
                        context,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    //그림 조회
    private fun roundView(gameChannelId: Long, id: Long, roundNumber: Int) {
        RoundService().roundView(gameChannelId, id, roundNumber, object :
            RetrofitCallback<SingleResult<RoundResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<RoundResDTO>) {
                if (responseData.output == 1) {
                    Glide.with(context!!).load(responseData.data.imgSrc).into(pictureImageView)

                    Log.d(TAG, "onSuccess: 그림 조회")
                } else {
                    Toast.makeText(
                        context,
                        "문제가 발생하였습니다. 다시 시도해주세요.1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.2", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.3", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}