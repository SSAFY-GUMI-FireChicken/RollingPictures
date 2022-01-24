package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.MainActivity

class GameExitDialog(context: Context)
{
    private val dialog = Dialog(context)


    fun showDialog()
    {
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
            val intent = Intent(dialog.context, MainActivity::class.java)
            ContextCompat.startActivity(dialog.context, intent, null)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }


    }


}