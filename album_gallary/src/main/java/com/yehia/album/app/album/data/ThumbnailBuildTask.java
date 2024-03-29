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
package com.yehia.album.app.album.data;

import android.content.Context;
import android.os.AsyncTask;

import com.yehia.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by YehiaReda on 2019/5/2.
 */
public class ThumbnailBuildTask extends AsyncTask<Void, Void, ArrayList<AlbumFile>> {

    public interface Callback {
        /**
         * The task begins.
         */
        void onThumbnailStart();

        /**
         * Callback results.
         *
         * @param albumFiles result.
         */
        void onThumbnailCallback(ArrayList<AlbumFile> albumFiles);
    }

    private ArrayList<AlbumFile> mAlbumFiles;
    private Callback mCallback;

    private ThumbnailBuilder mThumbnailBuilder;

    public ThumbnailBuildTask(Context context, ArrayList<AlbumFile> albumFiles, Callback callback) {
        this.mAlbumFiles = albumFiles;
        this.mCallback = callback;
        this.mThumbnailBuilder = new ThumbnailBuilder(context);
    }

    @Override
    protected void onPreExecute() {
        mCallback.onThumbnailStart();
    }

    @Override
    protected ArrayList<AlbumFile> doInBackground(Void... params) {
        for (AlbumFile albumFile : mAlbumFiles) {
            int mediaType = albumFile.getMediaType();
            if (mediaType == AlbumFile.TYPE_IMAGE) {
                albumFile.setThumbPath(mThumbnailBuilder.createThumbnailForImage(albumFile.getPath()));
            } else if (mediaType == AlbumFile.TYPE_VIDEO) {
                albumFile.setThumbPath(mThumbnailBuilder.createThumbnailForVideo(albumFile.getPath(), (albumFile.getDuration() / 2)));
            }
        }
        return mAlbumFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<AlbumFile> albumFiles) {
        mCallback.onThumbnailCallback(albumFiles);
    }
}