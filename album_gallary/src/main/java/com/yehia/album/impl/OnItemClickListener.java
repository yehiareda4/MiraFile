/*
 * Copyright 2019 Yehia Reda.
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
package com.yehia.album.impl;

import android.view.View;

/**
 * <p>Listens on the item's click.</p>
 * Created by Yehia Reda on 2016/9/23.
 */
public interface OnItemClickListener {

    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    void onItemClick(View view, int position);
}