/*
 * Copyright (c) 2017 Yahoo Inc. All rights reserved.
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 */
public class AdmobRecyclerAdapterWrapper
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AdmobFetcherBase.AdmobListener {

    private final String TAG = AdmobRecyclerAdapterWrapper.class.getCanonicalName();

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return mAdapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(new AdapterWrapperObserver(this, getAdapterCalculator(), adFetcher));
        notifyDataSetChanged();
    }

    private AdmobFetcher adFetcher;
    private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator();
    /**
    * Gets an object which incapsulates transformation of the source and ad blocks indices
    */
    public AdmobAdapterCalculator getAdapterCalculator(){return AdapterCalculator;}
    /**
    * Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
    * by inheritance of AdmobAdapterCalculator class
    */
    public void setAdapterCalculator(AdmobAdapterCalculator adapterCalculatordmob){AdapterCalculator = adapterCalculatordmob;}

    private static final int VIEW_TYPE_AD = 0;

    private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
    private final static int DEFAULT_LIMIT_OF_ADS = 3;
    private final static int DEFAULT_VIEWTYPE_SOURCE_MAX = 0;

    /**
     * Gets the number of ads that have been fetched so far.
     *
     * @return the number of ads that have been fetched
     */
    public int getFetchedAdsCount() {
        return adFetcher.getFetchedAdsCount();
    }

    /**
     * Gets the number of ads have been fetched so far + currently fetching ads
     *
     * @return the number of already fetched ads + currently fetching ads
     */
    public int getFetchingAdsCount(){
        return adFetcher.getFetchingAdsCount();
    }

    private int getViewTypeAd(){
        return getViewTypeBiggestSource() + VIEW_TYPE_AD + 1;
    }

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

    private int viewTypeBiggestSource;
    /*
   * Gets the biggest value among all the view types in the underlying source adapter, by default it equals to 0.
   */
    public int getViewTypeBiggestSource() {
        return viewTypeBiggestSource;
    }

    /*
    * Sets the biggest value among all the view types in the underlying source adapter, by default it equals to 0.
    */
    public void setViewTypeBiggestSource(int viewTypeBiggestSource) {
        this.viewTypeBiggestSource = viewTypeBiggestSource;
    }

    private NativeAdLayoutContext mAdsLayoutContext;

    /**
    * Gets the context (the res layout id and a strategy of inflating and binding) for published
    * app ads
    */
    public NativeAdLayoutContext getAdsLayoutContext() {
        return mAdsLayoutContext;
    }

    /**
    * Sets the context (the res layout id and a strategy of inflating and binding) for published
    * app ads
    */
    public void setAdsLayoutContext(NativeAdLayoutContext mAdsLayoutContext) {
        this.mAdsLayoutContext = mAdsLayoutContext;
    }

    /**
     * Use this constructor for test purposes. if you are going to release the live version
     * please use the appropriate constructor
     * @see #AdmobRecyclerAdapterWrapper(Context, String)
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     */
    public AdmobRecyclerAdapterWrapper(Context context, String[] testDevicesId) {
        init(context, null, testDevicesId);
    }

    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobRecyclerAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobRecyclerAdapterWrapper(Context context, String admobReleaseUnitId) {
        Collection<String> releaseUnitIds = admobReleaseUnitId==null
                ? null
                : Collections.singletonList(admobReleaseUnitId);
        init(context, releaseUnitIds, null);
    }

    /**
     * @param admobReleaseUnitIds sets a release unit ID for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobRecyclerAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobRecyclerAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds) {
        init(context, admobReleaseUnitIds, null);
    }

    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobRecyclerAdapterWrapper(Context, String[]) or supply a
     * test ID here.
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobRecyclerAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId) {
        init(context, Collections.singletonList(admobReleaseUnitId), testDevicesId);
    }

    private void init(Context context, Collection<String> admobReleaseUnitIds, String[] testDevicesId){
        setViewTypeBiggestSource(DEFAULT_VIEWTYPE_SOURCE_MAX);
        setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
        setLimitOfAds(DEFAULT_LIMIT_OF_ADS);
        setAdsLayoutContext(AdLayoutContext.getDefault());

        adFetcher = new AdmobFetcher();
        if(testDevicesId!=null)
            for (String testId: testDevicesId)
                adFetcher.addTestDeviceId(testId);
        if(admobReleaseUnitIds!=null)
            adFetcher.setReleaseUnitIds(admobReleaseUnitIds);
        adFetcher.addListener(this);
        // Start prefetching ads
        adFetcher.prefetchAds(context.getApplicationContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == getViewTypeAd()) {
            NativeAdView lvi1 = (NativeAdView) viewHolder.itemView;
            NativeAd ad1 = (NativeAd) getItem(position);
            getAdsLayoutContext().bind(lvi1, ad1);
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getItemCount());
            mAdapter.onBindViewHolder(viewHolder, origPos);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == getViewTypeAd()) {
            return new NativeHolder(onCreateItemView(parent, viewType));
        }
        else{
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    private NativeAdView onCreateItemView(ViewGroup parent, int viewType) {
        if (viewType == getViewTypeAd())
                return getAdsLayoutContext().inflateView(parent);
        else return null;
    }

    /**
     * <p>Gets the count of all data, including interspersed ads.</p>
     * <p/>
     * <p>If data size is 10 and an ad is to be showed after every 5 items starting at the index 0,
     * this method will return 12.</p>
     *
     * @return the total number of items this adapter can show, including ads.
     * @see AdmobRecyclerAdapterWrapper#setNoOfDataBetweenAds(int)
     * @see AdmobRecyclerAdapterWrapper#getNoOfDataBetweenAds()
     */
    @Override
    public int getItemCount() {

        if (mAdapter != null) {
            /*
            No of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset.
             */
            int noOfAds = AdapterCalculator.getAdsCountToPublish(adFetcher.getFetchedAdsCount(),
                    mAdapter.getItemCount());
            return mAdapter.getItemCount() > 0 ? mAdapter.getItemCount() + noOfAds : 0;
        } else {
            return 0;
        }
    }

    /**
     * Gets the item in a given position in the dataset. If an ad is to be returned,
     * a {@link NativeAd} object is returned.
     *
     *
     * @return the object or ad contained in this adapter position
     */
    public Object getItem(int position) {

        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            int adPos = AdapterCalculator.getAdIndex(position);
            return adFetcher.getAdForIndex(adPos);
        }
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            return getViewTypeAd();
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getItemCount());
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
        adFetcher.clearMapAds();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAdLoaded(int adIdx) {
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAdsCountChanged() {
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAdFailed(int adIdx, int errorCode, Object adPayload) {
        notifyDataSetChanged();
    }

}
