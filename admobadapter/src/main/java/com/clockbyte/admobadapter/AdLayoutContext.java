/*
 * Copyright (c) 2021 Clockbyte LLC. All rights reserved.
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

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdLayoutContext extends NativeAdLayoutContext{

    public static AdLayoutContext getDefault() {
        return new AdLayoutContext(R.layout.adlistview_item);
    }

    public AdLayoutContext(int mAdLayoutId){
        setAdLayoutId(mAdLayoutId);
    }

    @Override
    public void bind(NativeAdView nativeAdView, NativeAd nativeAd) throws ClassCastException{
        if (nativeAdView == null || nativeAd == null) return;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeAppInstallAdView's setHeadlineView method to register it.
        TextView tvHeader = nativeAdView.findViewById(R.id.tvHeader);
        tvHeader.setText(nativeAd.getHeadline());
        nativeAdView.setHeadlineView(tvHeader);

        TextView tvDescription = nativeAdView.findViewById(R.id.tvDescription);
        tvDescription.setText(nativeAd.getBody());
        nativeAdView.setBodyView(tvDescription);

        ImageView ivLogo = nativeAdView.findViewById(R.id.ivLogo);
        if(nativeAd.getIcon()!=null)
            ivLogo.setImageDrawable(nativeAd.getIcon().getDrawable());
        nativeAdView.setIconView(ivLogo);

        Button btnAction = nativeAdView.findViewById(R.id.btnAction);
        btnAction.setText(nativeAd.getCallToAction());
        nativeAdView.setCallToActionView(btnAction);

        TextView tvStore = nativeAdView.findViewById(R.id.tvStore);
        tvStore.setText(nativeAd.getStore());
        nativeAdView.setStoreView(tvStore);

        TextView tvPrice = nativeAdView.findViewById(R.id.tvPrice);
        tvPrice.setText(nativeAd.getPrice());
        nativeAdView.setPriceView(tvPrice);

        ImageView ivImage = nativeAdView.findViewById(R.id.ivImage);
        nativeAd.getImages();
        if (nativeAd.getImages().size() > 0) {
            ivImage.setImageDrawable(nativeAd.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        nativeAdView.setImageView(ivImage);

        // Call the NativeAppInstallAdView's setNativeAd method to register the NativeAd.
        nativeAdView.setNativeAd(nativeAd);
    }

}
