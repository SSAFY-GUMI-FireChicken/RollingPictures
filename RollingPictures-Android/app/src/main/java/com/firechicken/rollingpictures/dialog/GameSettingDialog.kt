package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatButton
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.dto.MakeChannelReqDTO

private const val TAG = "GameSettingDialog_싸피"

class GameSettingDialog(context: Context) : Dialog(context) {

    interface OnDialogClickListener {
        fun onDialogOkClick(changedChannel: MakeChannelReqDTO)
    }

    val dialog = Dialog(context)
    private lateinit var dialogListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        dialogListener = listener
    }

    fun showDialog() {
        dialog.apply {
            setContentView(R.layout.dialog_game_setting)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()
        }


        val roomTitleEditText = dialog.findViewById<EditText>(R.id.roomTitleEditText)
        val publicButton = dialog.findViewById<AppCompatButton>(R.id.publicButton)
        val privateButton = dialog.findViewById<AppCompatButton>(R.id.privateButton)
        val cancelButton = dialog.findViewById<AppCompatButton>(R.id.cancelButton)
        val okButton = dialog.findViewById<AppCompatButton>(R.id.okButton)
        val btnSubCount = dialog.findViewById<ImageButton>(R.id.btnSubCount)
        val btnAddCount = dialog.findViewById<ImageButton>(R.id.btnAddCount)
        val countTextView = dialog.findViewById<TextView>(R.id.countTextView)

        roomTitleEditText.setText(channelResDTO.data.title)

        var isPublic = channelResDTO.data.isPublic
        if(isPublic == "Y"){
            privateButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_round))
            publicButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_tool_detail))
        }else{
            publicButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_round))
            privateButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_tool_detail))
        }
        publicButton.setOnClickListener {
            isPublic = "Y"
            publicButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_round))
            privateButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_tool_detail))
        }

        privateButton.setOnClickListener {
            isPublic = "N"
            privateButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_round))
            publicButton.setBackgroundDrawable(getDrawable(dialog.context,R.drawable.bg_tool_detail))
        }

        countTextView.text = channelResDTO.data.maxPeopleCnt.toString()

        btnSubCount.setOnClickListener {
            val maximumCnt = countTextView.text.toString().toInt()
            if (maximumCnt == 2) {
                Toast.makeText(dialog.context, R.string.minimum_count, Toast.LENGTH_SHORT)
                    .show()
            } else if(maximumCnt == channelResDTO.data.curPeopleCnt) {
                Toast.makeText(dialog.context, R.string.cur_count, Toast.LENGTH_SHORT)
                    .show()
            } else {
                countTextView.text = Integer.toString((maximumCnt - 1))
            }
        }

        btnAddCount.setOnClickListener {
            val maximumCnt = countTextView.text.toString().toInt()
            if (maximumCnt == 6) {
                Toast.makeText(dialog.context, R.string.maximum_count, Toast.LENGTH_SHORT)
                    .show()
            } else {
                countTextView.text = Integer.toString((maximumCnt + 1))
            }
        }

        okButton.setOnClickListener {
            dialogListener.onDialogOkClick(MakeChannelReqDTO(
                channelResDTO.data.id,
                countTextView.text.toString(),
                isPublic,
                countTextView.text.toString().toInt()))
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

    }
}