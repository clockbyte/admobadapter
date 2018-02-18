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

package com.clockbyte.admobadapter.expressads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.clockbyte.admobadapter.AdPreset;
import com.clockbyte.admobadapter.AdViewHelper;
import com.clockbyte.admobadapter.AdapterWrapperObserver;
import com.clockbyte.admobadapter.AdmobAdapterCalculator;
import com.clockbyte.admobadapter.AdmobFetcherBase;
import com.clockbyte.admobadapter.NativeHolder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Collection;
import java.util.Collections;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 * @deprecated Use banners instead
 */
@Deprecated
public class AdmobExpressRecyclerAdapterWrapper
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AdmobFetcherBase.AdmobListener {

    private final String TAG = AdmobExpressRecyclerAdapterWrapper.class.getCanonicalName();

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return mAdapter;
    }

    private AdmobFetcherExpress adFetcher;
    private Context mContext;
    private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator();
    /**
    * Gets an object which incapsulates transformation of the source and ad blocks indices
    */
    public AdmobAdapterCalculator getAdapterCalculator(){return AdapterCalculator;}

    private AdViewWrappingStrategyBase AdViewWrappingStrategy = new AdViewWrappingStrategy();
    /**
    * Gets an object which incapsulates a wrapping logic for AdViews
    */
    public AdViewWrappingStrategyBase getAdViewWrappingStrategy(){return AdViewWrappingStrategy;}

    private static final int VIEW_TYPE_AD_EXPRESS = 0;

    private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
    private final static int DEFAULT_LIMIT_OF_ADS = 3;
    private final static int DEFAULT_VIEWTYPE_SOURCE_MAX = 0;

    /**
     * Gets the number of presets for ads that have been predefined by user (objects which contain adsize, unitIds etc).
     *
     * @return the number of ads that have been fetched
     */
    public int getAdPresetsCount(){
        return adFetcher!=null? this.adFetcher.getAdPresetsCount(): 0;
    }

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

    public int getViewTypeAdExpress(){
        return getViewTypeBiggestSource() + VIEW_TYPE_AD_EXPRESS + 1;
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

    private AdmobExpressRecyclerAdapterWrapper(){}

    public static Builder builder(@NonNull Context context) {
        return new AdmobExpressRecyclerAdapterWrapper().new Builder(context);
    }

    public class Builder{
        private Builder(Context context){
            mContext = context;

            viewTypeBiggestSource = DEFAULT_VIEWTYPE_SOURCE_MAX;

            AdapterCalculator.setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
            AdapterCalculator.setLimitOfAds(DEFAULT_LIMIT_OF_ADS);

            adFetcher = new AdmobFetcherExpress(mContext);
            adFetcher.addListener(AdmobExpressRecyclerAdapterWrapper.this);
        }

        /**
        * Sets the first ad block index (zero-based) in the adapter, by default it equals to 0
        */
        public Builder setFirstAdIndex(int firstAdIndex) {
            AdapterCalculator.setFirstAdIndex(firstAdIndex);
            return this;
        }

        /**
        * Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
        */
        public Builder setLimitOfAds(int mLimitOfAds) {
            AdapterCalculator.setLimitOfAds(mLimitOfAds);
            return this;
        }

        /**
        * Sets the number of your data items between ad blocks, by default it equals to 10.
        * You should set it according to the Admob's policies and rules which says not to
        * display more than one ad block at the visible part of the screen
        * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
        */
        public Builder setNoOfDataBetweenAds(int mNoOfDataBetweenAds) {
            AdapterCalculator.setNoOfDataBetweenAds(mNoOfDataBetweenAds);
            return this;
        }

        /**
         * Sets the biggest value among all the view types in the underlying source adapter, by default it equals to 0.
         */
        public Builder setViewTypeBiggestSource(int viewTypeBiggestSource) {
            AdmobExpressRecyclerAdapterWrapper.this.viewTypeBiggestSource = viewTypeBiggestSource;
            return this;
        }

        /**
         * Sets a devices ID to test ads interaction.
         * You could pass null but it's better to set ids for all your test devices
         * including emulators. for emulator just use the
         * @see {AdRequest.DEVICE_ID_EMULATOR}
         */
        public Builder setTestDeviceIds(@NonNull String[] testDevicesId){
            adFetcher.getTestDeviceIds().clear();
            for (String testId: testDevicesId)
                adFetcher.addTestDeviceId(testId);
            return this;
        }

        /**
         * Sets a collection of ad presets ( object which contains unit ID and AdSize for banner).
         * It works like cycling FIFO (first in = first out, cycling from end to start).
         * Each ad block will get one from the queue.
         * If the desired count of ad blocks is greater than this collection size
         * then it will go again to the first item and iterate as much as it required.
         * ID should be active, please check it in your Admob's account.
         * Be careful: don't pass release unit id without setting the testDevicesId if you still haven't deployed a Release.
         * Otherwise your Admob account could be banned
         */
        public Builder setAdPresets(@NonNull Collection<AdPreset> adPresets){
            adFetcher.setAdPresets(adPresets);
            return this;
        }

        /**
         * Sets a single unit ID for each ad block.
         * If you are testing ads please don't set it to the release id
         */
        public Builder setSingleAdUnitId(@NonNull String admobUnitId){
            AdPreset adPreset = adFetcher.getAdPresetSingleOr(new AdPreset());
            adPreset.setAdUnitId(admobUnitId);
            adFetcher.setAdPresets(Collections.singletonList(adPreset));
            return this;
        }

        /**
         * Sets a single ad size for each ad block. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
         */
        public Builder setSingleAdSize(@NonNull AdSize adSize){
            AdPreset adPreset = adFetcher.getAdPresetSingleOr(new AdPreset(null, null));
            adPreset.setAdSize(adSize);
            adFetcher.setAdPresets(Collections.singletonList(adPreset));
            return this;
        }

        /**
        * Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
        * by inheritance from {@link AdmobAdapterCalculator} class
        */
        public Builder setAdapterCalculator(@NonNull AdmobAdapterCalculator adapterCalculator){
            AdapterCalculator = adapterCalculator;
            return setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS).setLimitOfAds(DEFAULT_LIMIT_OF_ADS);
        }

        /**
         * Injects an object which incapsulates a wrapping logic for AdViews. You could inject your own implementation
         * by inheritance from {@link AdViewWrappingStrategyBase} class
         */
        public Builder setAdViewWrappingStrategy(@NonNull AdViewWrappingStrategyBase adViewWrappingStrategy){
            AdViewWrappingStrategy = adViewWrappingStrategy;
            return this;
        }

        /**
         * Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
         * by inheritance of {@link AdmobAdapterCalculator} class
         */
        public Builder setAdapterCalculator(@NonNull AdmobAdapterCalculator adapterCalculator, int noOfDataBetweenAds, int limitOfAds, int firstAdIndex){
            AdapterCalculator = adapterCalculator;
            return setNoOfDataBetweenAds(noOfDataBetweenAds).setLimitOfAds(limitOfAds).setFirstAdIndex(firstAdIndex);
        }

        /**
         * Sets underlying adapter with your data collection.
         * If you want to inject your implementation of {@link AdmobAdapterCalculator} please set it before this call
         */
        public Builder setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
            mAdapter = adapter;
            mAdapter.registerAdapterDataObserver(new AdapterWrapperObserver(AdmobExpressRecyclerAdapterWrapper.this, AdapterCalculator, adFetcher));
            notifyDataSetChanged();
            return this;
        }

        public AdmobExpressRecyclerAdapterWrapper build(){
            if(adFetcher.getAdPresetsCount() == 0)
                adFetcher.setAdPresets(null);
            prefetchAds(AdmobFetcherExpress.PREFETCHED_ADS_SIZE);
            return AdmobExpressRecyclerAdapterWrapper.this;
        }
    }

    /**
     * Creates N instances {@link NativeExpressAdView} from the next N taken instances {@link AdPreset}
     * Will start async prefetch of ad blocks to use its further
     * @return last created NativeExpressAdView
     */
    private NativeExpressAdView prefetchAds(int cntToPrefetch){
        NativeExpressAdView last = null;
        for (int i = 0; i < cntToPrefetch; i++) {
            final NativeExpressAdView item = AdViewHelper.getExpressAdView(mContext, adFetcher.takeNextAdPreset());
            adFetcher.setupAd(item);
            //50 ms throttling to prevent a high-load of server
            new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    adFetcher.fetchAd(item);
                }
            }, 50 * i);
            last = item;
        }
        return last;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder == null)
            return;
        if(viewHolder.getItemViewType() == getViewTypeAdExpress()) {
            NativeHolder nativeExpressHolder = (NativeHolder) viewHolder;
            ViewGroup wrapper = nativeExpressHolder.getAdViewWrapper();
            int adPos = AdapterCalculator.getAdIndex(position);
            NativeExpressAdView adView = adFetcher.getAdForIndex(adPos);
            if (adView == null)
                adView = prefetchAds(1);
            AdViewWrappingStrategy.recycleAdViewWrapper(wrapper, adView);
            //make sure the AdView for this position doesn't already have a parent of a different recycled NativeExpressHolder.
            if (adView.getParent() != null)
                ((ViewGroup) adView.getParent()).removeView(adView);
            AdViewWrappingStrategy.addAdViewToWrapper(wrapper, adView);

        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
            mAdapter.onBindViewHolder(viewHolder, origPos);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getViewTypeAdExpress()) {
            ViewGroup wrapper = AdViewWrappingStrategy.getAdViewWrapper(parent);
            return new NativeHolder(wrapper);
        }
        else{
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
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
            /*No of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset.*/
            int noOfAds = AdapterCalculator.getAdsCountToPublish(adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
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
        checkNeedFetchAd(position);
        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchingAdsCount())) {
            return getViewTypeAdExpress();
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
            return mAdapter.getItemViewType(origPos);
        }
    }

    private void checkNeedFetchAd(int position){
        if(AdapterCalculator.hasToFetchAd(position, adFetcher.getFetchingAdsCount()))
            prefetchAds(1);
    }

    /**
     * Clears all currently displaying ads and reinits the list
     */
    public void reinitialize() {
        adFetcher.destroyAllAds();
        prefetchAds(AdmobFetcherExpress.PREFETCHED_ADS_SIZE);
        notifyDataSetChanged();
    }

    /**
     * Free all resources, weak-refs
     */
    public void release(){
        adFetcher.release();
    }

    @Override
    public void onAdLoaded(int adIdx) {
        //raise ad's neighbour item changed.
        // cheap, quick and dirty solution to avoid ad's redraw and flickering.
        int pos = getAdapterCalculator().translateAdToWrapperPosition(adIdx);
        notifyItemChanged(pos==0? 1: Math.max(0, pos-1));
    }

    @Override
    public void onAdsCountChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onAdFailed(int adIdx, int errorCode, Object adPayload) {
        NativeExpressAdView adView = (NativeExpressAdView)adPayload;
        if (adView != null) {
            ViewParent parent = adView.getParent();
            if(parent == null || parent instanceof RecyclerView)
                adView.setVisibility(View.GONE);
            else {
                while (parent.getParent() != null && !(parent.getParent() instanceof RecyclerView))
                    parent = parent.getParent();
                ((View) parent).setVisibility(View.GONE);
            }
        }
        int pos = getAdapterCalculator().translateAdToWrapperPosition(Math.max(adIdx,0));
        notifyItemRangeChanged(pos, pos+15);
    }

}
