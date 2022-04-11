/*
 * Copyright 2018 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yehia.album_media.mvp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.yehia.album_media.Album
import com.yehia.album_media.util.AlbumUtils

/**
 * Created by YanZhenjie on 2018/4/6.
 */
open class BaseActivity : AppCompatActivity(), Bye {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = Album.albumConfig!!.locale
        AlbumUtils.applyLanguageForContext(this, locale)
    }

    /**
     * Request permission.
     */
    protected fun requestPermission(permissions: Array<String?>, code: Int) {
        var permissions = permissions
        if (Build.VERSION.SDK_INT >= 23) {
            val deniedPermissions = getDeniedPermissions(this, *permissions)
            if (deniedPermissions.isEmpty()) {
                onPermissionGranted(code)
            } else {
                permissions = arrayOfNulls(deniedPermissions.size)
//                deniedPermissions.toArray<String>(permissions)
                ActivityCompat.requestPermissions(this, permissions, code)
            }
        } else {
            onPermissionGranted(code)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isGrantedResult(*grantResults)) onPermissionGranted(requestCode) else onPermissionDenied(
            requestCode
        )
    }

    protected open fun onPermissionGranted(code: Int) {}
    protected open fun onPermissionDenied(code: Int) {}
    override fun bye() {
        onBackPressed()
    }

    companion object {
        val PERMISSION_TAKE_PICTURE = arrayOf<String?>(
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val PERMISSION_TAKE_VIDEO = arrayOf<String?>(
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val PERMISSION_STORAGE = arrayOf<String?>(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

        private fun getDeniedPermissions(
            context: Context,
            vararg permissions: String?
        ): List<String?> {
            val deniedList: MutableList<String> = ArrayList(2)
            for (permission in permissions) {
                if (PermissionChecker.checkSelfPermission(
                        context,
                        permission!!
                    ) != PermissionChecker.PERMISSION_GRANTED
                ) {
                    deniedList.add(permission)
                }
            }
            return deniedList
        }

        private fun isGrantedResult(vararg grantResults: Int): Boolean {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) return false
            }
            return true
        }
    }
}