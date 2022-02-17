package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.databinding.ActivityLoginBinding
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.service.UserService
import com.firechicken.rollingpictures.util.RetrofitCallback

private const val TAG = "LoginActivity_싸피"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        if(prefs.getEnteredChannel()!="none"){
            outChannel(prefs.getEnteredChannel(),prefs.getId()!!)
        }


        //비정상종료시 안전장치로 다시 실행시켰을 때 들어있었던 채널 퇴장하게 함.
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
                }else{
                    //로그인
                    prefs.getUid()?.let { it1 -> login("1", "none", it1) }
                }

            }


        }

        activityLoginBinding.infoImageButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(nickname: String, password: String, type: String, uid: String) {
        val user = SignUpReqDTO(nickname, password, type, uid)
        Log.d(TAG, "signUp: ${user}")
        UserService().signUp(user, object : RetrofitCallback<SingleResult<UserIdResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<UserIdResDTO>) {
                if (responseData.data.id > 0L) {
                    Log.d(TAG, "onSuccess: ${responseData}")
                    Toast.makeText(this@LoginActivity, "회원 가입 성공!", Toast.LENGTH_SHORT).show()
                    //바로 로그인
                    prefs.getUid()?.let { login("1", "none", it) }
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

    private fun login(password: String, type: String, uid: String) {
        Log.d(TAG, "login: ")
        UserService().login(password, type, uid, object : RetrofitCallback<SingleResult<LoginUserResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<LoginUserResDTO>) {
                if (responseData.data.id > 0L) {
                    loginUserResDTO = responseData
                    Log.d(TAG, "onSuccess: ${responseData}")
                    prefs.setId(loginUserResDTO.data.id)
                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "onSuccess: null")
                    Toast.makeText(
                        this@LoginActivity,
                        "문제가 발생하였습니다. 다시 시도해주세요.1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.2", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@LoginActivity, "문제가 발생하였습니다. 다시 시도해주세요.3", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun outChannel(code: String, userId: Long) {
        val req = InOutChannelReqDTO(code, userId)
        ChannelService().outChannel(req, object : RetrofitCallback<SingleResult<Any>> {
            override fun onSuccess(code: Int, responseData: SingleResult<Any>) {
                if (responseData.output == 1) {
                    ApplicationClass.channelResDTO = SingleResult (
                        ChannelResDTO(-1, "","", "", 0, 0, mutableListOf())
                        ,"",0)
                    ApplicationClass.playerList = mutableListOf()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "문제가 발생하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(applicationContext, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(applicationContext, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}