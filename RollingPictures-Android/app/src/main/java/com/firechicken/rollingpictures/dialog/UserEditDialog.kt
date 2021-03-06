package com.firechicken.rollingpictures.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.dto.UserIdResDTO
import com.firechicken.rollingpictures.dto.UserInfoUpdateReqDTO
import com.firechicken.rollingpictures.service.UserService
import com.firechicken.rollingpictures.util.RetrofitCallback

class UserEditDialog(context: Context) : Dialog(context) {

    interface OnDialogClickListener {
        fun onDialogOkClick(nickname: String)
    }
    private val dialog = Dialog(context)
    private lateinit var dialogListener: OnDialogClickListener
    fun setOnClickListener(listener: OnDialogClickListener) {
        dialogListener = listener
    }

    fun showDialog() {
        dialog.apply {
            setContentView(R.layout.dialog_user_edit)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()
        }

        val editText = dialog.findViewById<EditText>(R.id.editText)
        val charNum = dialog.findViewById<TextView>(R.id.CharNumTextView)
        val okButton = dialog.findViewById<TextView>(R.id.dialog_ok_button)
        val cancelButton = dialog.findViewById<TextView>(R.id.dialog_cancel_button)

        editText.setText(prefs.getNickName())
        okButton.setOnClickListener {
            if (editText.text.toString() == "") {
                Toast.makeText(dialog.context, "변경할 닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                userInfoUpdate(editText.text.toString(), prefs.getUid())
                dialogListener.onDialogOkClick(editText.text.toString())
                dialog.dismiss()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                charNum.setText("${editText.text.length}/10")
            }
        })

    }

    private fun userInfoUpdate(nickname: String?, userId: String?) {

        val user = UserInfoUpdateReqDTO(nickname, userId)
        UserService().userInfoUpdate(user, object : RetrofitCallback<SingleResult<UserIdResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<UserIdResDTO>) {
                if (responseData.data.id > 0L) {
                    prefs.setNickName(nickname!!)
                    Toast.makeText(dialog.context, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(dialog.context, "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(dialog.context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(dialog.context, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
