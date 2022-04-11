/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
package com.yehia.mediascanner

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import java.util.*

/**
 *
 * MediaScanner.
 * Created by YanZhenjie on 17-3-27.
 */
class MediaScanner(context: Context) : MediaScannerConnection.MediaScannerConnectionClient {

    private val mMediaScanConn: MediaScannerConnection =
        MediaScannerConnection(context.applicationContext, this)
    private var mScannerListener: ScannerListener? = null
    private val mLinkedList = LinkedList<Array<String>>()
    private var mCurrentScanPaths: Array<String>? = null
    private var mScanCount = 0

    /**
     * Create scanner.
     *
     * @param context         context.
     * @param scannerListener [ScannerListener].
     */
    @Deprecated("use {@link #MediaScanner(Context)} instead.")
    constructor(context: Context, scannerListener: ScannerListener?) : this(context) {
        mScannerListener = scannerListener
    }

    /**
     * Scanner is running.
     *
     * @return true, other wise false.
     */
    val isRunning: Boolean
        get() = mMediaScanConn.isConnected

    /**
     * Scan file.
     *
     * @param filePath file absolute path.
     */
    fun scan(filePath: String) {
        scan(arrayOf(filePath))
    }

    /**
     * Scan file list.
     *
     * @param filePaths file absolute path list.
     */
    fun scan(filePaths: List<String>) {
        scan(filePaths.toTypedArray())
    }

    /**
     * Scan file array.
     *
     * @param filePaths file absolute path array.
     */
    fun scan(filePaths: Array<String>?) {
        if (filePaths != null && filePaths.size > 0) {
            mLinkedList.add(filePaths)
            executeOnce()
        }
    }

    /**
     * Execute scanner.
     */
    private fun executeOnce() {
        if (!isRunning && mLinkedList.size > 0) {
            mCurrentScanPaths = mLinkedList.removeAt(0)
            mMediaScanConn.connect()
        }
    }

    override fun onMediaScannerConnected() {
        for (filePath in mCurrentScanPaths!!) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            mMediaScanConn.scanFile(filePath, mimeType)
        }
    }

    override fun onScanCompleted(path: String, uri: Uri) {
        if (mScannerListener != null) mScannerListener!!.oneComplete(path, uri)
        mScanCount += 1
        if (mScanCount == mCurrentScanPaths!!.size) {
            mMediaScanConn.disconnect()
            if (mScannerListener != null) mScannerListener!!.allComplete(mCurrentScanPaths)
            mScanCount = 0
            mCurrentScanPaths = null
            executeOnce()
        }
    }

}