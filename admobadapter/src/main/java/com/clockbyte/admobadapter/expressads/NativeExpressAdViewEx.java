package com.clockbyte.admobadapter.expressads;

import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by FILM on 28.10.2016.
 */

public class NativeExpressAdViewEx {

    private final NativeExpressAdView mAdView;
    private boolean isFailedToLoad = false;

    public NativeExpressAdViewEx(NativeExpressAdView mAdView){
        this.mAdView = mAdView;
    }

    public NativeExpressAdView get() {
        return mAdView;
    }

    public boolean isFailedToLoad() {
        return isFailedToLoad;
    }
    public void setFailedToLoad(boolean failedToLoad) {
        isFailedToLoad = failedToLoad;
    }
}
