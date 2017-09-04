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

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.gms.ads.formats.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 */
public class AdmobAdapterWrapper extends BaseAdapter implements AdmobFetcherBase.AdmobListener {

    private final String TAG = AdmobAdapterWrapper.class.getCanonicalName();

    private BaseAdapter mAdapter;

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                notifyDataSetInvalidated();
            }
        });
    }

    private AdmobFetcher adFetcher;
    private Context mContext;
    private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator();
    /*
    * Gets an object which incapsulates transformation of the source and ad blocks indices
    */
    public AdmobAdapterCalculator getAdapterCalculator(){return AdapterCalculator;}
    /*
* Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
* by inheritance of AdmobAdapterCalculator class
*/
    public void setAdapterCalculator(AdmobAdapterCalculator adapterCalculatordmob){AdapterCalculator = adapterCalculatordmob;}


    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_AD_CONTENT = 0;
    private static final int VIEW_TYPE_AD_INSTALL = 1;

    private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
    private final static int DEFAULT_LIMIT_OF_ADS = 3;

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

    private int getViewTypeAdContent(){
        return mAdapter.getViewTypeCount() + VIEW_TYPE_AD_CONTENT;
    }

    private int getViewTypeAdInstall(){
        return mAdapter.getViewTypeCount() + VIEW_TYPE_AD_INSTALL;
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

    private NativeAdLayoutContext mContentAdsLayoutContext;

    /*
    * Gets the context (the res layout id and a strategy of inflating and binding) for published content ads {@link https://support.google.com/admob/answer/6240809}
    */
    public NativeAdLayoutContext getContentAdsLayoutContext() {
        return mContentAdsLayoutContext;
    }

    /*
    * Sets the context (the res layout id and a strategy of inflating and binding) for published content ads {@link https://support.google.com/admob/answer/6240809}
    */
    public void setContentAdsLayoutContext(NativeAdLayoutContext mContentAdsLayoutContext) {
        this.mContentAdsLayoutContext = mContentAdsLayoutContext;
    }

    private NativeAdLayoutContext mInstallAdsLayoutContext;

    /*
    * Gets the context (the res layout id and a strategy of inflating and binding) for published install app ads {@link https://support.google.com/admob/answer/6240809}
    */
    public NativeAdLayoutContext getInstallAdsLayoutContext() {
        return mInstallAdsLayoutContext;
    }

    /*
    * Sets the context (the res layout id and a strategy of inflating and binding) for published install app ads {@link https://support.google.com/admob/answer/6240809}
    */
    public void setInstallAdsLayoutContext(NativeAdLayoutContext mInstallAdsLayoutContext) {
        this.mInstallAdsLayoutContext = mInstallAdsLayoutContext;
    }

    /**
     * Use this constructor for test purposes. if you are going to release the live version
     * please use the appropriate constructor
     * @see #AdmobAdapterWrapper(Context, String)
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     */
    public AdmobAdapterWrapper(Context context, String[] testDevicesId) {
        this(context, testDevicesId, EnumSet.allOf(EAdType.class));
    }
    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobAdapterWrapper(Context context, String admobReleaseUnitId) {
        this(context, admobReleaseUnitId, EnumSet.allOf(EAdType.class));
    }

    /**
     * @param admobReleaseUnitIds sets a release unit IDs for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds) {
        this(context, admobReleaseUnitIds, EnumSet.allOf(EAdType.class));
    }

    /**
     * Use this constructor for test purposes. if you are going to release the live version
     * please use the appropriate constructor
     * @see #AdmobAdapterWrapper(Context, String)
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     * @param adTypesToShow sets the types of ads to show in the list.
     * By default all types are loaded by wrapper.
     * i.e. pass EnumSet.of(EAdType.ADVANCED_INSTALLAPP) to show only install app ads
     */
    public AdmobAdapterWrapper(Context context, String[] testDevicesId, EnumSet<EAdType> adTypesToShow) {
        init(context, null, testDevicesId, adTypesToShow);
    }
    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param adTypesToShow sets the types of ads to show in the list.
     * By default all types are loaded by wrapper.
     * i.e. pass EnumSet.of(EAdType.ADVANCED_INSTALLAPP) to show only install app ads
     */
    public AdmobAdapterWrapper(Context context, String admobReleaseUnitId, EnumSet<EAdType> adTypesToShow) {
        Collection<String> releaseUnitIds = admobReleaseUnitId==null
                ? null
                : Collections.singletonList(admobReleaseUnitId);
        init(context, releaseUnitIds, null, adTypesToShow);
    }

    /**
     * @param admobReleaseUnitIds sets a release unit ID for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param adTypesToShow sets the types of ads to show in the list.
     * By default all types are loaded by wrapper.
     * i.e. pass EnumSet.of(EAdType.ADVANCED_INSTALLAPP) to show only install app ads
     */
    public AdmobAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds, EnumSet<EAdType> adTypesToShow) {
        init(context, admobReleaseUnitIds, null, adTypesToShow);
    }

    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobAdapterWrapper(Context, String[]) or supply a
     * test ID here.
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param adTypesToShow sets the types of ads to show in the list.
     * By default all types are loaded by wrapper.
     * i.e. pass EnumSet.of(EAdType.ADVANCED_INSTALLAPP) to show only install app ads
     */
    public AdmobAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId, EnumSet<EAdType> adTypesToShow) {
        init(context, Collections.singletonList(admobReleaseUnitId), testDevicesId, adTypesToShow);
    }

    private void init(Context context, Collection<String> admobReleaseUnitIds, String[] testDevicesId, EnumSet<EAdType> adTypesToShow){
        setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
        setLimitOfAds(DEFAULT_LIMIT_OF_ADS);
        setContentAdsLayoutContext(ContentAdLayoutContext.getDefault());
        setInstallAdsLayoutContext(InstallAppAdLayoutContext.getDefault());
        mContext = context;

        adFetcher = new AdmobFetcher();
        if(testDevicesId!=null)
            for (String testId: testDevicesId)
                adFetcher.addTestDeviceId(testId);
        if(admobReleaseUnitIds!=null)
            adFetcher.setReleaseUnitIds(admobReleaseUnitIds);
        adFetcher.setAdTypeToFetch(adTypesToShow == null || adTypesToShow.isEmpty()
                ?  EnumSet.allOf(EAdType.class): adTypesToShow);
        adFetcher.addListener(this);
        // Start prefetching ads
        adFetcher.prefetchAds(context.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if(itemViewType == getViewTypeAdInstall()) {
            NativeAppInstallAdView lvi1;
            NativeAppInstallAd ad1 = (NativeAppInstallAd) getItem(position);
            if (convertView == null) {
                lvi1 = (NativeAppInstallAdView) getInstallAdsLayoutContext().inflateView(parent);
            } else {
                lvi1 = (NativeAppInstallAdView) convertView;
            }
            getInstallAdsLayoutContext().bind(lvi1, ad1);
            return lvi1;
        }
        else if(itemViewType == getViewTypeAdContent()) {
            NativeContentAdView lvi2;
            NativeContentAd ad2 = (NativeContentAd) getItem(position);
            if (convertView == null) {
                lvi2 = (NativeContentAdView) getContentAdsLayoutContext().inflateView(parent);
            } else {
                lvi2 = (NativeContentAdView) convertView;
            }
            getContentAdsLayoutContext().bind(lvi2, ad2);
            return lvi2;
        }
        else{
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getView(origPos, convertView, parent);
        }
    }

    /**
     * <p>Gets the count of all data, including interspersed ads.</p>
     * <p/>
     * <p>If data size is 10 and an ad is to be showed after every 5 items starting at the index 0, this method
     * will return 12.</p>
     *
     * @return the total number of items this adapter can show, including ads.
     * @see AdmobAdapterWrapper#setNoOfDataBetweenAds(int)
     * @see AdmobAdapterWrapper#getNoOfDataBetweenAds()
     */
    @Override
    public int getCount() {

        if (mAdapter != null) {
            /*
            No of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset.
             */
            int noOfAds = AdapterCalculator.getAdsCountToPublish(adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getCount() > 0 ? mAdapter.getCount() + noOfAds : 0;
        } else {
            return 0;
        }
    }

    /**
     * Gets the item in a given position in the dataset. If an ad is to be returned,
     * a {@link NativeAd} object is returned.
     *
     * @param position the adapter position
     * @return the object or ad contained in this adapter position
     */
    @Override
    public Object getItem(int position) {

        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            int adPos = AdapterCalculator.getAdIndex(position);
            return adFetcher.getAdForIndex(adPos);
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getItem(origPos);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT + getAdapter().getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            int adPos = AdapterCalculator.getAdIndex(position);
            NativeAd ad = adFetcher.getAdForIndex(adPos);
            return ad instanceof NativeAppInstallAd ? getViewTypeAdInstall() : getViewTypeAdContent();
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
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

    @Override
    public void onAdLoaded(int adIdx) {
        notifyDataSetChanged();
    }

    @Override
    public void onAdsCountChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onAdFailed(int adIdx, int errorCode, Object adPayload) {
        notifyDataSetChanged();
    }

}
