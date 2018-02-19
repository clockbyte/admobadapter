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

import android.content.Context;
import android.widget.AbsListView;

import com.clockbyte.admobadapter.bannerads.BannerAdPreset;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

public class AdViewHelper {

    public static NativeExpressAdView getExpressAdView(Context context, AdPreset adPreset) {
        NativeExpressAdView adView = new NativeExpressAdView(context);
        AdSize adSize = adPreset.getAdSize();
        adView.setAdSize(adSize);
        adView.setAdUnitId(adPreset.getAdUnitId());
        adView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                adSize.getHeightInPixels(context)));

        //set video options
        if(adPreset.getVideoOptions() != null)
        adView.setVideoOptions(adPreset.getVideoOptions());

        return adView;
    }

    public static AdView getBannerAdView(Context context, BannerAdPreset bannerAdPreset) {
        AdView adView = new AdView(context);
        AdSize adSize = bannerAdPreset.getAdSize();
        adView.setAdSize(adSize);
        adView.setAdUnitId(bannerAdPreset.getAdUnitId());
        adView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                adSize.getHeightInPixels(context)));

        //Banners do not have video options
        /*
        if(bannerAdPreset.getVideoOptions() != null)
        adView.setVideoOptions(bannerAdPreset.getVideoOptions());
        */

        return adView;
    }

}
