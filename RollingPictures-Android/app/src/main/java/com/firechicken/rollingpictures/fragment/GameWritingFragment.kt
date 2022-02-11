package com.firechicken.rollingpictures.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundNum
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.RoundService
import com.firechicken.rollingpictures.service.UserService
import com.firechicken.rollingpictures.util.RetrofitCallback
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_game_writing.*

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
        }else{
            guideRoundTextView.setText(getString(R.string.guide_round2))
        }

        pictureImageView.visibility = View.GONE

        completeButton.setOnClickListener {

            Log.d(TAG, "onViewCreated: ${gameChannelResDTO.data.id}, ${prefs.getId()!!}, ${writingEditText.text.toString()}, ${roundNum}")
            roundRegister(gameChannelResDTO.data.id, prefs.getId()!!, writingEditText.text.toString(), roundNum)

        }
    }

    private fun roundRegister(gameChannelId: Long, id: Long, keyword: String, roundNumber: Int) {
        val round = RoundReqDTO(gameChannelId, id, keyword, roundNumber)
        RoundService().roundRegister(round, object : RetrofitCallback<SingleResult<RoundResDTO>> {
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
}