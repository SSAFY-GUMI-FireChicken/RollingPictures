package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }

            entranceButton.setOnClickListener {
                activityMainBinding.roomCodeEditText.text.apply{
                    if(toString()==""){
                        toast("방 코드를 입력하세요.")
                    }else{
                        inChannel(toString(), prefs.getId()!!)
                    }
                }
            }

            enterPublicButton.setOnClickListener{
                val intent = Intent(this@MainActivity, PublicRoomListActivity::class.java)
                startActivity(intent)
            }
        }

        if (!arePermissionGranted()) {
            val permissionsFragment: DialogFragment = PermissionsDialogFragment(this@MainActivity)
            permissionsFragment.show(supportFragmentManager, "Permissions Fragment")
        }
    }

    // 권한이 받아졌음을 boolean으로 return
    private fun arePermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED)
    }

    // 메인 액티비티에서 권한을 허가 받지 못했으면, 권한을 요청함
    fun askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST
            )
        }
    }

    private fun inChannel(roomcode: String, userId: Long) {
        val req = InOutChannelReqDTO(roomcode, userId)
        ChannelService().inChannel(req, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                if(responseData.data != null){
                    if (responseData.data.id > 0) {
                        ApplicationClass.channelResDTO = responseData
                        val intent = Intent(this@MainActivity, GameActivity::class.java)
                        intent.putExtra("code", roomcode)
                        startActivity(intent)
                    } else {
                        toast("inChannel에서 문제가 발생하였습니다. 다시 시도해주세요.")
                    }
                }else{
                    if(responseData.msg == "방 정보가 없습니다."){
                        toast("존재하지 않는 방 코드입니다. 다시 입력해주세요.")
                    }else{
                        toast("해당 방은 정원초과 입니다.")
                    }

                }
            }

            override fun onFailure(code: Int) {
                toast("inChannel에서 실패문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                toast("inChannel에서 에러문제가 발생하였습니다. 다시 시도해주세요.")
            }
        })
    }

    private fun toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }
}

