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

package com.clockbyte.admobadapter;

import android.content.Context;
import android.os.Handler;

import com.google.android.gms.ads.AdRequest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AdmobFetcherBase {

    private final String TAG = AdmobFetcherBase.class.getCanonicalName();

    protected List<AdmobListener> mAdNativeListeners = new ArrayList<AdmobListener>();
    protected int mNoOfFetchedAds;
    protected int mFetchFailCount;
    protected WeakReference<Context> mContext = new WeakReference<Context>(null);
    protected AtomicBoolean lockFetch = new AtomicBoolean();

    protected ArrayList<String> testDeviceId = new ArrayList<String>();
    /*
    *Gets a test device ID. Normally you don't have to set it
     */
    public ArrayList<String> getTestDeviceIds() {
        return testDeviceId;
    }
    /*
    *Sets a test device ID. Normally you don't have to set it
     */
    public void addTestDeviceId(String testDeviceId) {
        this.testDeviceId.add(testDeviceId);
    }

    /**
     * Adds an {@link AdmobListener} that would be notified for any changes to the native ads
     * list.
     *
     * @param listener the listener to be notified
     */
    public synchronized void addListener(AdmobListener listener) {
        mAdNativeListeners.add(listener);
    }

    /**
     * Gets the number of ads that have been fetched so far.
     *
     * @return the number of ads that have been fetched
     */
    public synchronized int getFetchedAdsCount() {
        return mNoOfFetchedAds;
    }
    /**
     * Gets the number of ads that have been fetched and are currently being fetched
     *
     * @return the number of ads that have been fetched and are currently being fetched
     */
    public abstract int getFetchingAdsCount();

    /**
     * Fetches a new native ad.
     *
     * @param context the current context.
     * @see #destroyAllAds()
     */
    public synchronized void prefetchAds(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    /**
     * Destroys ads that have been fetched, that are still being fetched and removes all resource
     * references that this instance still has. This should only be called when the Activity that
     * is showing ads is closing, preferably from the {@link android.app.Activity#onDestroy()}.
     * </p>
     * The converse of this call is {@link #prefetchAds(Context)}.
     */
    public synchronized void destroyAllAds() {
        mFetchFailCount = 0;
        mNoOfFetchedAds = 0;
        notifyObserversOfAdSizeChange(-1);
        mContext.clear();
    }

    /**
     * Notifies all registered {@link AdmobListener} on a change of ad count.
     */
    protected void notifyObserversOfAdSizeChange(final int adIdx) {
        Context context = mContext.get();
        //context may be null if activity is destroyed
        if(context != null) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    for (AdmobListener listener : mAdNativeListeners)
                        if(adIdx < 0)
                            listener.onAdChanged();
                        else listener.onAdChanged(adIdx);
                }
            });
        }
    }

    /**
     * Setup and get an ads request
     */
    protected synchronized AdRequest getAdRequest() {
        AdRequest.Builder adBldr = new AdRequest.Builder();
        for (String id : getTestDeviceIds()) {
            adBldr.addTestDevice(id);
        }
        return adBldr.build();
    }

    /**
     * Listener that is notified when changes to the list of fetched native ads are made.
     */
    public interface AdmobListener {
        /**
         * Raised when the ad have changed. Adapters that implement this class
         * should notify their data views that the dataset has changed.
         * @param adIdx the index of ad block which state was changed
         */
        void onAdChanged(int adIdx);
        /**
         * Raised when the number of ads have changed. Adapters that implement this class
         * should notify their data views that the dataset has changed.
         */
        void onAdChanged();
    }
}
