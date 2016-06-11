/*
 * Copyright 2015 Yahoo Inc. All rights reserved.
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

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.clockbyte.admobadapter.AdmobFetcherBase;
import com.clockbyte.admobadapter.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AdmobFetcherExpress extends AdmobFetcherBase {

    private final String TAG = AdmobFetcherExpress.class.getCanonicalName();

    /**
     * Maximum number of times to try fetch an ad after failed attempts.
     */
    private static final int MAX_FETCH_ATTEMPT = 4;

    public AdmobFetcherExpress(Context context){
        super();
        mContext = new WeakReference<Context>(context);
    }

    private List<NativeExpressAdView> mPrefetchedAdList = new ArrayList<NativeExpressAdView>();

    /**
     * Gets native ad at a particular index in the fetched ads list.
     *
     * @param adPos the index of ad in the fetched ads list
     * @return the native ad in the list
     * @see #getFetchedAdsCount()
     */
    public NativeExpressAdView getAdForIndex(int adPos) {
        if(adPos>=0 && adPos < mPrefetchedAdList.size())
            return mPrefetchedAdList.get(adPos);
        return null;
    }

    /**
     * Fetches a new native ad.
     */
    protected synchronized void fetchAd(final NativeExpressAdView adView) {
        if(mFetchFailCount > MAX_FETCH_ATTEMPT)
            return;

        Context context = mContext.get();

        if (context != null) {
            Log.i(TAG, "Fetching Ad now");
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    adView.loadAd(getAdRequest()); //Fetching the ads item
                }
            });
        } else {
            mFetchFailCount++;
            Log.i(TAG, "Context is null, not fetching Ad");
        }
    }

    /**
     * Determines if the native ad can be used.
     *
     * @param adNative the native ad object
     * @return <code>true</code> if the ad object can be used, false otherwise
     */
    private boolean canUseThisAd(NativeExpressAdView adNative) {
        if (adNative == null || adNative.isLoading()) return false;
        return true;
    }

    /**
     * Subscribing to the native ads events
     * @param adView
     */
    protected synchronized void setupAd(final NativeExpressAdView adView) {
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Handle the failure by logging, altering the UI, etc.
                Log.i(TAG, "onAdFailedToLoad " + errorCode);
                mFetchFailCount++;
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                onAdFetched(adView);
            }
        });
    }

    /**
     * A handler for received native ads
     * @param adNative
     */
    private synchronized void onAdFetched(NativeExpressAdView adNative) {
        Log.i(TAG, "onAdFetched");
        if (canUseThisAd(adNative)) {
            mPrefetchedAdList.add(adNative);
            mNoOfFetchedAds++;
        }
        mFetchFailCount = 0;
        notifyObserversOfAdSizeChange();
    }

    @Override
    public synchronized void destroyAllAds() {
        super.destroyAllAds();
        for (NativeExpressAdView ad: mPrefetchedAdList) {
            ad.destroy();
        }
        mPrefetchedAdList.clear();
    }

    /**
     * update all the ads in Map to refresh
     * */
    public synchronized void updateAds() {
        for (NativeExpressAdView ad: mPrefetchedAdList) {
            fetchAd(ad);
        }
    }

}
