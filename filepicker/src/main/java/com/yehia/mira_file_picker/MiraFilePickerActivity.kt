package com.yehia.mira_file_picker

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions


class MiraFilePickerActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"

    private val perms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )
    private val cameraPermission = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )
    private val RC_READ_WRITE = 1
    private val RC_CAMERA = 2
    private var REQUEST_CODE = 0
    private var type: String? = null
    private var multiple = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        val intent: Intent = intent
        REQUEST_CODE = intent.getIntExtra("requestCode", 101)
        type = intent.getStringExtra("type")
        multiple = intent.getBooleanExtra("multiple", false)
        val camera = intent.getBooleanExtra("camera", false)
        if (camera) {
            checkCameraPermissions()
        } else {
            checkPermissions()
        }
    }

    private fun checkCameraPermissions() {
        if (EasyPermissions.hasPermissions(this, *cameraPermission)) {
            openCamera()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.camera_permission),
                RC_CAMERA, *cameraPermission
            )
        }
    }

    //    const val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"
    private fun checkPermissions() {
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            //permission not granted so far
            EasyPermissions.requestPermissions(
                this, getString(R.string.read_write_permissions),
                RC_READ_WRITE, *perms
            )

        } else {
            chooseFile()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun openCamera() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!checkStoragePermissionApi30(this)) {
//                return
//            }
//        }
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    private fun chooseFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!checkStoragePermissionApi30(this)) {
                return
            }
        }

        val chooserIntent = FileUtils.createGetContentIntent(type, multiple)
        startActivityForResult(chooserIntent, REQUEST_CODE)
    }

    @RequiresApi(30)
    fun checkStoragePermissionApi30(activity: AppCompatActivity): Boolean {
        val appOps = activity.getSystemService(AppOpsManager::class.java)
        val mode = appOps.unsafeCheckOpNoThrow(
            MANAGE_EXTERNAL_STORAGE_PERMISSION,
            activity.applicationInfo.uid,
            activity.packageName
        )
        if(mode !=AppOpsManager.MODE_ALLOWED){
            requestStoragePermissionApi30(this)
        }

        return mode == AppOpsManager.MODE_ALLOWED
    }

    @RequiresApi(30)
    fun requestStoragePermissionApi30(activity: AppCompatActivity) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)

        activity.startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String?>) {
        if (requestCode == RC_READ_WRITE) {
            chooseFile()
        } else if (requestCode == RC_CAMERA) {
            openCamera()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {}

}
