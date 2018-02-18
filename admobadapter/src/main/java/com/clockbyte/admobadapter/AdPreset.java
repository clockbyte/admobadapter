/*
 * Copyright (c) 2017 Clockbyte LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clockbyte.admobadapter;


import android.text.TextUtils;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.VideoOptions;

import java.util.Locale;

public class AdPreset {
    private static final String UNIT_ID_DEFAULT_EXPRESS = "ca-app-pub-3940256099942544/1072772517";
    private static final AdSize SIZE_DEFAULT_EXPRESS = new AdSize(AdSize.FULL_WIDTH, 150);
    public static final AdPreset DEFAULT = new AdPreset(UNIT_ID_DEFAULT_EXPRESS, SIZE_DEFAULT_EXPRESS);

    private String adUnitId;
    private AdSize adSize;
    private VideoOptions videoOptions;

    public AdPreset(){
        this.adUnitId = UNIT_ID_DEFAULT_EXPRESS;
        this.adSize = SIZE_DEFAULT_EXPRESS;
    }

    public AdPreset(String adUnitId){
        this();
        if(!TextUtils.isEmpty(adUnitId))
            this.adUnitId = adUnitId;
    }

    public AdPreset(String adUnitId, AdSize adSize){
        this(adUnitId);
        if(adSize != null)
            this.adSize = adSize;
    }

    public String getAdUnitId(){
        return this.adUnitId;
    }
    public void setAdUnitId(String adUnitId){
        this.adUnitId = adUnitId;
    }

    public AdSize getAdSize(){
        return this.adSize;
    }
    public void setAdSize(AdSize adSize){
        this.adSize = adSize;
    }


    public VideoOptions getVideoOptions() {
        return videoOptions;
    }
    public void setVideoOptions(VideoOptions videoOptions) {
        this.videoOptions = videoOptions;
    }

    public boolean isValid(){
        return !TextUtils.isEmpty(this.adUnitId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AdPreset) {
            AdPreset other = (AdPreset) o;
            return (this.adUnitId.equals(other.adUnitId)
                    && this.adSize.getHeight() == other.adSize.getHeight()
                    && this.adSize.getWidth() == other.adSize.getWidth());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + adUnitId.hashCode();
        hash = hash * 31 + adSize.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s | %s", adUnitId, adSize);
    }
}
