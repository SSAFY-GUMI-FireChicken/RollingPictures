package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.firechicken.rollingpictures.R

class GameExitDialog(context: Context) : Dialog(context){

    interface OnDialogClickListener {
        fun onDialogOkClick()
    }

    val dialog = Dialog(context)
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
