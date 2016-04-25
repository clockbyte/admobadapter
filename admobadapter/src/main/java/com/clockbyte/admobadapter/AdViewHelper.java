/*
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

package com.clockbyte.admobadapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

public class AdViewHelper {

    public static void bindContentAdView(NativeContentAdView adView, NativeContentAd ad) {
        if (adView == null || ad == null) return;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
        TextView tvHeader = (TextView) adView.findViewById(R.id.tvHeader);
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);

        TextView tvDescription = (TextView) adView.findViewById(R.id.tvDescription);
        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        ImageView ivLogo = (ImageView) adView.findViewById(R.id.ivLogo);
        if(ad.getLogo()!=null)
            ivLogo.setImageDrawable(ad.getLogo().getDrawable());
        adView.setLogoView(ivLogo);

        Button btnAction = (Button) adView.findViewById(R.id.btnAction);
        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);

        TextView tvAdvertiser = (TextView) adView.findViewById(R.id.tvAdvertiser);
        tvAdvertiser.setText(ad.getAdvertiser());
        adView.setAdvertiserView(tvAdvertiser);

        ImageView ivImage = (ImageView) adView.findViewById(R.id.ivImage);
        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivImage.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setImageView(ivImage);

        // Call the NativeContentAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(ad);
    }

    public static void bindInstallAdView(NativeAppInstallAdView adView, NativeAppInstallAd ad) {
        if (adView == null || ad == null) return;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
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

        // Call the NativeContentAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(ad);
    }

}
