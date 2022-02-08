package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firechicken.rollingpictures.databinding.ActivityMainBinding
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.dialog.PermissionsDialogFragment
import com.firechicken.rollingpictures.dialog.UserEditDialog
import com.firechicken.rollingpictures.dto.ChannelResDTO
import com.firechicken.rollingpictures.dto.InOutChannelReqDTO
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback

private const val TAG = "MainActivity_싸피"

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainBinding.apply {
            nickNameEditText.apply {
                setText(prefs.getNickName())
                setOnClickListener {
                    val dialog = UserEditDialog(this@MainActivity)
                    dialog.showDialog()
                    dialog.setOnClickListener(object : UserEditDialog.OnDialogClickListener {
                        override fun onDialogOkClick(name: String) {
                            setText(name)
                        }
                    })
                }
            }

            createButton.setOnClickListener {
                val intent = Intent(this@MainActivity, CreateRoomActivity::class.java)
                startActivity(intent)
            }

            entranceButton.setOnClickListener {
                activityMainBinding.roomCodeEditText.text.apply{
                    if(toString()==""){
                        Toast.makeText(this@MainActivity, "방 코드를 입력하세요.", Toast.LENGTH_SHORT).show()
                    }else{
                        inChannel(toString(), prefs.getId()!!)
                    }
                }
            }

        }

        if (!arePermissionGranted()) {
            val permissionsFragment: DialogFragment = PermissionsDialogFragment(this@MainActivity)
            permissionsFragment.show(supportFragmentManager, "Permissions Fragment")
        }
    }

    private fun arePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_DENIED
    }

    // 권한을 요청함
    fun askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                MY_PERMISSIONS_REQUEST
            )
        }
    }

    private fun inChannel(roomcode: String, userId: Long) {
        val req = InOutChannelReqDTO(roomcode, userId)
        Log.d(TAG, "inChannel: ")
        ChannelService().inChannel(req, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                Log.d(TAG, "responseData.data.id: ${responseData.data}")
                if(responseData.data != null){
                    if (responseData.data.id > 0) {
                        ApplicationClass.channelResDTO = responseData
                        val intent = Intent(this@MainActivity, GameWaitingActivity::class.java)
                        intent.putExtra("code", roomcode)
                        startActivity(intent)
                    } else {
                        Log.d(TAG, "onSuccess: null")
                        Toast.makeText(
                            this@MainActivity,
                            "문제가 발생하였습니다. 다시 시도해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        this@MainActivity,
                        "존재하지 않는 방 코드입니다. 다시 입력해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
                Toast.makeText(this@MainActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
                Toast.makeText(this@MainActivity, "문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
