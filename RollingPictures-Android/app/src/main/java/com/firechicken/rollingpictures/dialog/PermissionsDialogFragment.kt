package com.firechicken.rollingpictures.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.activity.MainActivity

class PermissionsDialogFragment(private val activity: Activity) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        builder.setTitle(R.string.permissions_dialog_title)
        builder.setMessage(R.string.no_permissions_granted)
            .setPositiveButton(R.string.accept_permissions_dialog) {
                    dialog, id -> if(activity is GameActivity){
                                        activity.askForPermissions()
                                    } else if (activity is MainActivity) {
                                        activity.askForPermissions()
                                    }
            }
            .setNegativeButton(R.string.cancel_dialog) { dialog, id ->
                Log.i(
                    TAG,
                    "User cancelled Permissions Dialog"
                )
            }
        return builder.create()
    }

    companion object {
        private const val TAG = "PermissionsDialog"
    }
}