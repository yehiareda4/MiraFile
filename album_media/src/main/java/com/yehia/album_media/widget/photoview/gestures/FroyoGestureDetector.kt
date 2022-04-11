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
package com.yehia.album_media.widget.photoview.gestures

import android.annotation.TargetApi
import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector

@TargetApi(8)
class FroyoGestureDetector(context: Context?) : EclairGestureDetector(context) {
    protected val mDetector: ScaleGestureDetector
    override val isScaling: Boolean
        get() = mDetector.isInProgress

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            mDetector.onTouchEvent(ev)
            super.onTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            // Fix for support lib bug, happening when onDestroy is
            true
        }
    }

    init {
        val mScaleListener: ScaleGestureDetector.OnScaleGestureListener =
            object : ScaleGestureDetector.OnScaleGestureListener {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scaleFactor = detector.scaleFactor
                    if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) return false
                    mListener!!.onScale(
                        scaleFactor,
                        detector.focusX, detector.focusY
                    )
                    return true
                }

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    // NO-OP
                }
            }
        mDetector = ScaleGestureDetector(context, mScaleListener)
    }
}