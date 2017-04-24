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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;

public class InstallAppAdLayoutContext extends NativeAdLayoutContext{

    public static InstallAppAdLayoutContext getDefault(){
        return new InstallAppAdLayoutContext(R.layout.adinstalllistview_item);
    }

    public InstallAppAdLayoutContext(int mAdLayoutId){
        setAdLayoutId(mAdLayoutId);
    }

    @Override
    public void bind(NativeAdView nativeAdView, NativeAd nativeAd) throws ClassCastException{
        if (nativeAdView == null || nativeAd == null) return;
        if(!(nativeAd instanceof NativeAppInstallAd) || !(nativeAdView instanceof NativeAppInstallAdView))
            throw new ClassCastException();

        NativeAppInstallAd ad = (NativeAppInstallAd) nativeAd;
        NativeAppInstallAdView adView = (NativeAppInstallAdView) nativeAdView;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeAppInstallAdView's setHeadlineView method to register it.
        TextView tvHeader = (TextView) adView.findViewById(R.id.tvHeader);
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);

        TextView tvDescription = (TextView) adView.findViewById(R.id.tvDescription);
        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        ImageView ivLogo = (ImageView) adView.findViewById(R.id.ivLogo);
        if(ad.getIcon()!=null)
            ivLogo.setImageDrawable(ad.getIcon().getDrawable());
        adView.setIconView(ivLogo);

        Button btnAction = (Button) adView.findViewById(R.id.btnAction);
        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);

        TextView tvStore = (TextView) adView.findViewById(R.id.tvStore);
        tvStore.setText(ad.getStore());
        adView.setStoreView(tvStore);

        TextView tvPrice = (TextView) adView.findViewById(R.id.tvPrice);
        tvPrice.setText(ad.getPrice());
        adView.setPriceView(tvPrice);

        ImageView ivImage = (ImageView) adView.findViewById(R.id.ivImage);
        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivImage.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setImageView(ivImage);

        // Call the NativeAppInstallAdView's setNativeAd method to register the
        // NativeAd.
        adView.setNativeAd(ad);
    }

}
