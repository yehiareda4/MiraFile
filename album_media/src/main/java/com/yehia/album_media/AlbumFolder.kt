/*
 * Copyright 2016 Yan Zhenjie.
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
package com.yehia.album_media

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 *
 * Album folder, contains selected status and pictures.
 * Created by Yan Zhenjie on 2016/10/14.
 */
class AlbumFolder : Parcelable {
    /**
     * Folder name.
     */
    var name: String? = null

    /**
     * Image list in folder.
     */
    var albumFiles: ArrayList<AlbumFile>? = ArrayList()
        private set

    /**
     * checked.
     */
    var isChecked = false

    constructor()

    fun addAlbumFile(albumFile: AlbumFile) {
        albumFiles!!.add(albumFile)
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        albumFiles = `in`.createTypedArrayList(AlbumFile.Companion.CREATOR)
        isChecked = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeTypedList(albumFiles)
        dest.writeByte((if (isChecked) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<AlbumFolder> = object : Parcelable.Creator<AlbumFolder> {
            override fun createFromParcel(`in`: Parcel): AlbumFolder{
                return AlbumFolder(`in`)
            }

            override fun newArray(size: Int): Array<AlbumFolder?> {
                return arrayOfNulls(size)
            }
        }
    }
}