package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameWaitingActivity
import com.firechicken.rollingpictures.activity.MainActivity
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.dto.ChannelResDTO
import com.firechicken.rollingpictures.dto.CommonResultResDTO
import com.firechicken.rollingpictures.dto.InOutChannelReqDTO
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback

private const val TAG = "GameExitDialog_싸피"

class GameExitDialog(context: Context, var code: String) {
    private val dialog = Dialog(context)


    fun showDialog() {
        dialog.apply {
            setContentView(R.layout.dialog_game_exit)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()
        }

        val exitButton = dialog.findViewById<TextView>(R.id.dialog_exit_button)
        val cancelButton = dialog.findViewById<TextView>(R.id.dialog_cancel_button)

        exitButton.setOnClickListener {
            Log.d(TAG, "code: $code")
            Log.d(TAG, "prefs.getUid(): ${prefs.getUid()}")
            outChannel(code,  prefs.getUid()!!)
//            val intent = Intent(dialog.context, MainActivity::class.java)
//            ContextCompat.startActivity(dialog.context, intent, null)
//            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }


    }
    private fun outChannel(code: String, uid: String) {
        val req = InOutChannelReqDTO(code, uid)
        Log.d(TAG, "outChannel: ")
        ChannelService().outChannel(req, object : RetrofitCallback<SingleResult<Any>> {
            override fun onSuccess(code: Int, responseData: SingleResult<Any>) {
                if (responseData.output == 1) {
                    ApplicationClass.channelResDTO = SingleResult (ChannelResDTO("",0, mutableListOf()),"",0)
                    val intent = Intent(dialog.context, MainActivity::class.java)
                    ContextCompat.startActivity(dialog.context, intent, null)
                    dialog.dismiss()
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        dialog.context,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(dialog.context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(dialog.context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}