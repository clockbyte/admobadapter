/*
 *  Copyright 2015 Yahoo Inc. All rights reserved.
 * Copyright 2015 Clockbyte LLC. All rights reserved.
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

package com.clockbyte.admobadapter.expressads;


import android.text.TextUtils;

import com.google.android.gms.ads.AdSize;

public class NativeExpressAd {
    private String adUnitId;
    private AdSize adSize;

    public NativeExpressAd(){
        this.adUnitId = "ca-app-pub-3940256099942544/1072772517";
        this.adSize = new AdSize(AdSize.FULL_WIDTH, 150);
    }

    public NativeExpressAd(String adUnitId){
        this.adUnitId = adUnitId;
        this.adSize = new AdSize(AdSize.FULL_WIDTH, 150);
    }

    public NativeExpressAd(String adUnitId, AdSize adSize){
        this.adUnitId = adUnitId;
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

    public void setAdUnitId(AdSize adSize){
        this.adSize = adSize;
    }

    public boolean isValid(){
        return !TextUtils.isEmpty(this.adUnitId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NativeExpressAd) {
            NativeExpressAd other = (NativeExpressAd) o;
            return (this.adUnitId == other.adUnitId && this.adSize.getHeight() == other.adSize.getHeight() && this.adSize.getWidth() == other.adSize.getWidth());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + adUnitId.hashCode();
        hash = hash * 31 + adSize.hashCode();
        if(adListener != null){
            hash = hash * 31 + adListener.hashCode();
        }
        return hash;
    }

}
