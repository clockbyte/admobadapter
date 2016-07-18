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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.AdmobAdapterCalculator;
import com.clockbyte.admobadapter.AdmobAdapterWrapperInterface;
import com.clockbyte.admobadapter.AdmobFetcherBase;
import com.clockbyte.admobadapter.R;
import com.clockbyte.admobadapter.RecyclerViewAdapterBase;
import com.clockbyte.admobadapter.ViewWrapper;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 */
public class AdmobExpressRecyclerAdapterWrapper<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>>
        implements AdmobFetcherBase.AdmobListener, AdmobAdapterWrapperInterface {

    private final String TAG = AdmobExpressRecyclerAdapterWrapper.class.getCanonicalName();

    private RecyclerViewAdapterBase<T, V> mAdapter;

    public RecyclerViewAdapterBase<T, V> getAdapter() {
        return mAdapter;
    }

    public void setAdapter(RecyclerViewAdapterBase<T, V> adapter) {
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

        });
    }

    AdmobFetcherExpress adFetcher;
    Context mContext;
    private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator(this);
    /*
    * Gets an object which incapsulates transformation of the source and ad blocks indices
    */
    public AdmobAdapterCalculator getAdapterCalculator(){return AdapterCalculator;}
    /*
* Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
* by inheritance of AdmobAdapterCalculator class
*/
    public void setAdapterCalculator(AdmobAdapterCalculator adapterCalculatordmob){AdapterCalculator = adapterCalculatordmob;}


    private static final int VIEW_TYPE_AD_EXPRESS = 1;

    private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
    private final static int DEFAULT_LIMIT_OF_ADS = 3;
    private static final AdSize DEFAULT_AD_SIZE = new AdSize(AdSize.FULL_WIDTH, 150);
    private static final String DEFAULT_AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";

    /*
    * Gets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public int getNoOfDataBetweenAds() {
        return AdapterCalculator.getNoOfDataBetweenAds();
    }

    /*
    * Sets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public void setNoOfDataBetweenAds(int mNoOfDataBetweenAds) {
        AdapterCalculator.setNoOfDataBetweenAds(mNoOfDataBetweenAds);
    }

    public int getFirstAdIndex() {
        return AdapterCalculator.getFirstAdIndex();
    }

    /*
    * Sets the first ad block index (zero-based) in the adapter, by default it equals to 0
    */
    public void setFirstAdIndex(int firstAdIndex) {
        AdapterCalculator.setFirstAdIndex(firstAdIndex);
    }

    /*
    * Gets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public int getLimitOfAds() {
        return AdapterCalculator.getLimitOfAds();
    }

    /*
    * Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public void setLimitOfAds(int mLimitOfAds) {
        AdapterCalculator.setLimitOfAds(mLimitOfAds);
    }

    private String mAdsUnitId;

    /*
    * Gets the res layout id for published express ads
    */
    public String getAdsUnitId() {
        return mAdsUnitId;
    }

    /*
    * Sets the res layout id for published express ads
    */
    public void setAdsUnitId(String mAdsUnitId) {
        this.mAdsUnitId = mAdsUnitId;
    }

    /*
    *Add a test device ID.
    */
    public void addTestDeviceId(String testDeviceId) {
        adFetcher.addTestDeviceId(testDeviceId);
    }

    /*
    *Sets a test device ID. Normally you don't have to set it
    */
    @Deprecated
    public void setTestDeviceId(String testDeviceId) {
        adFetcher.addTestDeviceId(testDeviceId);
    }

    private AdSize mAdSize;

    /*
    * Gets ad size
    */
    public AdSize getAdSize() {
        return mAdSize;
    }

    /*
    * Sets ad size
    */
    public void setAdSize(AdSize mAdSize) {
        this.mAdSize = mAdSize;
    }

    public AdmobExpressRecyclerAdapterWrapper(Context context) {
        setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
        setLimitOfAds(DEFAULT_LIMIT_OF_ADS);
        setAdsUnitId(DEFAULT_AD_UNIT_ID);
        setAdSize(DEFAULT_AD_SIZE);
        mContext = context;

        adFetcher = new AdmobFetcherExpress(mContext);
        adFetcher.addListener(this);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<V> viewHolder, int position) {
        if (viewHolder == null)
            return;

        if (viewHolder.getItemViewType() != VIEW_TYPE_AD_EXPRESS) {
            int origPos = AdapterCalculator.getOriginalContentPosition(position);
            mAdapter.onBindViewHolder(viewHolder, origPos);
        }
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_AD_EXPRESS:
                NativeExpressAdView item = getExpressAdView(parent);
                adFetcher.setupAd(item);
                adFetcher.fetchAd(item);
                return new ViewWrapper<V>((V) item);
            default:
                return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    private NativeExpressAdView getExpressAdView(ViewGroup parent) {
        NativeExpressAdView adView = new NativeExpressAdView(mContext);
        adView.setAdSize(getAdSize());
        adView.setAdUnitId(getAdsUnitId());
        adView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return adView;
    }

    /**
     * <p>Gets the count of all data, including interspersed ads.</p>
     * <p/>
     * <p>If data size is 10 and an ad is to be showed after every 5 items starting at the index 0, this method
     * will return 12.</p>
     *
     * @return the total number of items this adapter can show, including ads.
     * @see AdmobExpressRecyclerAdapterWrapper#setNoOfDataBetweenAds(int)
     * @see AdmobExpressRecyclerAdapterWrapper#getNoOfDataBetweenAds()
     */
    @Override
    public int getItemCount() {

        if (mAdapter != null) {
            /*
            No of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset.
             */
            int noOfAds = AdapterCalculator.getAdsCountToPublish();
            return mAdapter.getItemCount() > 0 ? mAdapter.getItemCount() + noOfAds : 0;
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (AdapterCalculator.canShowAdAtPosition(position)) {
            return VIEW_TYPE_AD_EXPRESS;
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position);
            return mAdapter.getItemViewType(origPos);
        }
    }

    /**
     * Destroys all currently fetched ads
     */
    public void destroyAds() {
        adFetcher.destroyAllAds();
    }

    /**
     * Clears all currently displaying ads to update them
     */
    public void requestUpdateAd() {
        adFetcher.updateAds();
    }

    @Override
    public void onAdCountChanged() {

        notifyDataSetChanged();
    }

    @Override
    public int getAdapterCount() {
        return mAdapter.getItemCount();
    }
}
