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
import android.util.Log;
import android.view.View;

import com.clockbyte.admobadapter.AdPresetCyclingList;
import com.clockbyte.admobadapter.AdmobFetcherBase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.NativeExpressAdView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdmobFetcherExpress extends AdmobFetcherBase {

    private final String TAG = AdmobFetcherExpress.class.getCanonicalName();

    /**
     * Maximum number of ads to prefetch.
     */
    public static final int PREFETCHED_ADS_SIZE = 2;

    /**
     * Maximum number of times to try fetch an ad after failed attempts.
     */
    private static final int MAX_FETCH_ATTEMPT = 4;

    public AdmobFetcherExpress(Context context){
        super();
        mContext = new WeakReference<Context>(context);
    }

    private List<NativeExpressAdView> mPrefetchedAds = new ArrayList<NativeExpressAdView>();
    private AdPresetCyclingList mAdPresetCyclingList = new AdPresetCyclingList();

    /*
* Gets next ad preset for Admob banners from FIFO .
* It works like cycling FIFO (first in = first out, cycling from end to start).
* Each ad block will get one from the queue.
* If the desired count of ad blocks is greater than this collection size
* then it will go again to the first item and iterate as much as it required.
* ID should be active, please check it in your Admob's account.
 */
    public ExpressAdPreset takeNextAdPreset() {
        return this.mAdPresetCyclingList.get();
    }

    /*
   * Sets an unit IDs for admob banners.
* It works like cycling FIFO (first in = first out, cycling from end to start).
* Each ad block will get one from the queue.
* If the desired count of ad blocks is greater than this collection size
* then it will go again to the first item and iterate as much as it required.
* ID should be active, please check it in your Admob's account.
 */
    public void setAdPresets(Collection<ExpressAdPreset> adPresets) {
        Collection<ExpressAdPreset> mAdPresets = adPresets==null||adPresets.size() == 0
                ? Collections.singletonList(ExpressAdPreset.DEFAULT)
                :adPresets;
        mAdPresetCyclingList.clear();
        mAdPresetCyclingList.addAll(mAdPresets);
    }

    public int getAdPresetsCount(){
        return this.mAdPresetCyclingList.size();
    }

    /**
     * Gets native ad at a particular index in the fetched ads list.
     *
     * @param adPos the index of ad in the fetched ads list
     * @return the native ad in the list
     * @see #getFetchedAdsCount()
     */
    public synchronized NativeExpressAdView getAdForIndex(int adPos) {
        if(adPos >= 0 && mPrefetchedAds.size() > adPos)
            return mPrefetchedAds.get(adPos);
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
        if(mFetchFailCount > MAX_FETCH_ATTEMPT)
            return;

        if(!mPrefetchedAds.contains(adView))
            mPrefetchedAds.add(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Handle the failure by logging, altering the UI, etc.
                Log.i(TAG, "onAdFailedToLoad " + errorCode);
                mFetchFailCount++;
                //Since Fetch Ad is only called once without retries
                //hide ad row or rollback its count if still not added to list
                //best approach to work with custom adapters that cache their views
                if(adView.getParent()==null){
                    notifyObserversOfAdSizeChange(--mNoOfFetchedAds - 1);
                }else {
                    ((View) adView.getParent()).setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                onAdFetched(adView);
                notifyObserversOfAdSizeChange(mNoOfFetchedAds - 1);
            }
        });
        ++mNoOfFetchedAds;
    }

    /**
     * A handler for received native ads
     * @param adNative
     */
    private synchronized void onAdFetched(NativeExpressAdView adNative) {
        Log.i(TAG, "onAdFetched");
        mFetchFailCount = 0;
    }

    @Override
    public synchronized int getFetchingAdsCount() {
        return mPrefetchedAds.size();
    }

    @Override
    public synchronized void destroyAllAds() {
        super.destroyAllAds();
        for(NativeExpressAdView ad:mPrefetchedAds){
            ad.destroy();
        }
        mPrefetchedAds.clear();
    }

    public synchronized void updateFetchedAds() {
        final Context context = mContext.get();
        //use throttling to prevent high-load of server
        for(int i = 0;i<mPrefetchedAds.size();i++){
            final NativeExpressAdView finalItem = mPrefetchedAds.get(i);
            new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchAd(finalItem);
                }
            }, 20000*i);
        }
        Log.i(TAG, "onAdsUpdate");
    }
}
