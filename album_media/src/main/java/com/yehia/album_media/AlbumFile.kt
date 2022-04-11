/*
 * Copyright 2017 Yan Zhenjie.
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
import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by YanZhenjie on 2017/8/15.
 */
class AlbumFile : Parcelable, Comparable<AlbumFile> {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(TYPE_IMAGE, TYPE_VIDEO)
    annotation class MediaType

    /**
     * File path.
     */
    var path: String? = null

    /**
     * Folder mName.
     */
    var bucketName: String? = null

    /**
     * File mime type.
     */
    var mimeType: String? = null

    /**
     * Add date.
     */
    var addDate: Long = 0

    /**
     * Latitude
     */
    var latitude = 0f

    /**
     * Longitude.
     */
    var longitude = 0f

    /**
     * Size.
     */
    var size: Long = 0

    /**
     * Duration.
     */
    var duration: Long = 0

    /**
     * Thumb path.
     */
    var thumbPath: String? = null

    /**
     * MediaType.
     */
    @get:MediaType
    var mediaType = 0

    /**
     * Checked.
     */
    var isChecked = false

    /**
     * Enabled.
     */
    var isDisable = false

    constructor()

    override fun compareTo(o: AlbumFile): Int {
        val time = o.addDate - addDate
        if (time > Int.MAX_VALUE) return Int.MAX_VALUE else if (time < -Int.MAX_VALUE) return -Int.MAX_VALUE
        return time.toInt()
    }

    override fun equals(obj: Any?): Boolean {
        if (obj != null && obj is AlbumFile) {
            val inPath = obj.path
            if (path != null && inPath != null) {
                return path == inPath
            }
        }
        return super.equals(obj)
    }

    override fun hashCode(): Int {
        return if (path != null) path.hashCode() else super.hashCode()
    }

    protected constructor(`in`: Parcel) {
        path = `in`.readString()
        bucketName = `in`.readString()
        mimeType = `in`.readString()
        addDate = `in`.readLong()
        latitude = `in`.readFloat()
        longitude = `in`.readFloat()
        size = `in`.readLong()
        duration = `in`.readLong()
        thumbPath = `in`.readString()
        mediaType = `in`.readInt()
        isChecked = `in`.readByte().toInt() != 0
        isDisable = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(path)
        dest.writeString(bucketName)
        dest.writeString(mimeType)
        dest.writeLong(addDate)
        dest.writeFloat(latitude)
        dest.writeFloat(longitude)
        dest.writeLong(size)
        dest.writeLong(duration)
        dest.writeString(thumbPath)
        dest.writeInt(mediaType)
        dest.writeByte((if (isChecked) 1 else 0).toByte())
        dest.writeByte((if (isDisable) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<AlbumFile> = object : Parcelable.Creator<AlbumFile> {
            override fun createFromParcel(`in`: Parcel): AlbumFile {
                return AlbumFile(`in`)
            }

            override fun newArray(size: Int): Array<AlbumFile?> {
                return arrayOfNulls(size)
            }
        }
    }
}