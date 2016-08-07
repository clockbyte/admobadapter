package com.clockbyte.admobadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdView;

/**
 * Created by FILM on 07.08.2016.
 */
public abstract class NativeAdLayoutContext {

    private int mAdLayoutId;

    /*
    * Gets the res layout id for ads
    */
    public int getAdLayoutId() {
        return mAdLayoutId;
    }

    /*
    * Sets the res layout id for ads
    */
    public void setAdLayoutId(int mAdLayoutId) {
        this.mAdLayoutId = mAdLayoutId;
    }

    public abstract void bind(NativeAdView nativeAdView, NativeAd nativeAd);

    public NativeAdView inflateView(ViewGroup root) throws IllegalArgumentException{
        if(root == null) throw new IllegalArgumentException("root should be not null");
        // Inflate a layout and add it to the parent ViewGroup.
        LayoutInflater inflater = (LayoutInflater) root.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return (NativeAdView) inflater
                .inflate(getAdLayoutId(), root, false);
    }
}

