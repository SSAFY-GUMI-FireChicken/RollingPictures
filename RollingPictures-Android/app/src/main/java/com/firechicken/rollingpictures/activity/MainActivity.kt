package com.firechicken.rollingpictures.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firechicken.rollingpictures.databinding.ActivityMainBinding
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.dialog.PermissionsDialogFragment

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.createButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }

        activityMainBinding.entranceButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameWaitingActivity::class.java)
            startActivity(intent)
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
}