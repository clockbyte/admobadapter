package com.clockbyte.admobadapter.expressads;

import android.view.ViewGroup;

import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by FILM on 28.10.2016.
 */

public class NativeExpressAdViewHolder {

    private final NativeExpressAdView mAdView;
    private boolean isFailedToLoad = false;
    private ViewGroup adViewWrapper;

    public NativeExpressAdViewHolder(NativeExpressAdView mAdView){
        this.mAdView = mAdView;
    }

    public NativeExpressAdView getAdView() {
        return mAdView;
    }

    /**
     * if the ad was failed to fetch
     */
    public boolean isFailedToLoad() {
        return isFailedToLoad;
    }

    void setFailedToLoad(boolean failedToLoad) {
        isFailedToLoad = failedToLoad;
    }

    /**
     * This field is for internal use, could be null, especially for RecyclerView
     * @return some {@link ViewGroup} which wraps current ad view
     * @see NativeExpressAdViewHolder#getAdView()
     */
    public ViewGroup getAdViewWrapper() {
        return adViewWrapper;
    }

    /**
     * This field for internal use, it will be set automatically.
     * @param adViewWrapper some {@link ViewGroup} which wraps current ad view
     * @see NativeExpressAdViewHolder#getAdView()
     */
    void setAdViewWrapper(ViewGroup adViewWrapper) {
        this.adViewWrapper = adViewWrapper;
    }
}
