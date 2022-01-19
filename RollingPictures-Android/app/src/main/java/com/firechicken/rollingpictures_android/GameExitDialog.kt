package com.firechicken.rollingpictures_android

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat

class GameExitDialog(context: Context)
{
    private val dialog = Dialog(context)


    fun showDialog()
    {
        dialog.apply {
            setContentView(R.layout.dialog_game_exit)
            window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()
        }

        val exitButton = dialog.findViewById<TextView>(R.id.dialog_exit_button)
        val cancelButton = dialog.findViewById<TextView>(R.id.dialog_cancel_button)

        exitButton.setOnClickListener {
            val intent = Intent(dialog.context, MainActivity::class.java)
            ContextCompat.startActivity(dialog.context, intent, null)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }


    }


}