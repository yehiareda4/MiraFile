/*
 * Copyright 2019 YehiaReda.
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
package com.yehia.album.api;

import android.content.Context;
import android.content.Intent;

import com.yehia.album.Album;
import com.yehia.album.app.camera.CameraActivity;

/**
 * <p>Camera wrapper.</p>
 * Created by Yehia Reda on 2017/4/18.
 */
public class ImageCameraWrapper extends BasicCameraWrapper<ImageCameraWrapper> {

    public ImageCameraWrapper(Context context) {
        super(context);
    }

    public void start() {
        CameraActivity.sResult = mResult;
        CameraActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, CameraActivity.class);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CAMERA_IMAGE);
        intent.putExtra(Album.KEY_INPUT_FILE_PATH, mFilePath);
        mContext.startActivity(intent);
    }
}