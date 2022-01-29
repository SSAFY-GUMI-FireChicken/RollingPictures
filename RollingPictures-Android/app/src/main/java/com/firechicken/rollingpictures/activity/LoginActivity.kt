package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.userIdResDTO
import com.firechicken.rollingpictures.databinding.ActivityLoginBinding
import com.firechicken.rollingpictures.dto.LoginUserResDTO
import com.firechicken.rollingpictures.dto.SignUpReqDTO
import com.firechicken.rollingpictures.dto.UserIdResDTO
import com.firechicken.rollingpictures.service.UserService
import com.firechicken.rollingpictures.util.PreferenceUtil
import com.firechicken.rollingpictures.util.RetrofitCallback

private const val TAG = "LoginActivity_싸피"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        prefs = PreferenceUtil(applicationContext)

        super.onCreate(savedInstanceState)

        val activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        if (prefs.getNickName() != "") {
            activityLoginBinding.nickNameEditText.apply {
                setText(prefs.getNickName())
                setEnabled(false)
            }

        }

        activityLoginBinding.loginButton.setOnClickListener {

            val nickname = activityLoginBinding.nickNameEditText.text.toString()
            //닉네임 미입력
            if (nickname == "") {
                Toast.makeText(this@LoginActivity, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()

            } else {
                //회원가입 (미가입상태로 uid가 shared preferences에 없을 때)
                if (prefs.getUid() == "") {
                    val uid = Settings.Secure.getString(
                        applicationContext.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    prefs.setUid(uid)
                    prefs.setNickName(nickname)
                    signUp(nickname, "1", "none", uid)
                }
                //로그인
                login("1", "none", prefs.getUid())
            }


        }

        activityLoginBinding.infoImageButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(nickname: String, password: String, type: String, uid: String) {
        val user = SignUpReqDTO(uid, type, password, nickname)
        Log.d(TAG, "signUp: ${user}")
        UserService().signUp(user, object : RetrofitCallback<UserIdResDTO> {
            override fun onSuccess(code: Int, responseData: UserIdResDTO) {
                if (responseData != null) {
                    userIdResDTO = responseData
                    prefs.setId(userIdResDTO.id)
                    Log.d(TAG, "onSuccess: ${userIdResDTO.id}")
                    Toast.makeText(this@LoginActivity, "회원 가입 성공!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@LoginActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun login(password: String, type: String, uid: String?) {
        Log.d(TAG, "login: ")
        UserService().login(password, type, uid, object : RetrofitCallback<LoginUserResDTO> {
            override fun onSuccess(code: Int, responseData: LoginUserResDTO) {
                if (responseData != null) {
                    loginUserResDTO = responseData
                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@LoginActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}