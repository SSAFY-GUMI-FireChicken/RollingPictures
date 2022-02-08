package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.MainActivity
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.dto.ChannelResDTO
import com.firechicken.rollingpictures.dto.InOutChannelReqDTO
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback


private const val TAG = "GameExitDialog_싸피"

class GameExitDialog(context: Context, var code: String) : Dialog(context){
    interface OnDialogClickListener {
        fun onDialogOkClick()
    }

    private val dialog = Dialog(context)
    private lateinit var dialogListener: OnDialogClickListener


    fun setOnClickListener(listener: OnDialogClickListener) {
        dialogListener = listener
    }

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

            dialogListener.onDialogOkClick()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

}