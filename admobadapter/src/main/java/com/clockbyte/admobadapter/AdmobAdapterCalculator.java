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

public class AdmobAdapterCalculator {

    protected int mNoOfDataBetweenAds;
    /*
    * Gets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public int getNoOfDataBetweenAds() {
        return mNoOfDataBetweenAds;
    }

    /*
    * Sets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public void setNoOfDataBetweenAds(int mNoOfDataBetweenAds) {
        this.mNoOfDataBetweenAds = mNoOfDataBetweenAds;
    }

    protected int firstAdIndex = 0;

    public int getFirstAdIndex() {
        return firstAdIndex;
    }

    /*
    * Sets the first ad block index (zero-based) in the adapter, by default it equals to 0
    */
    public void setFirstAdIndex(int firstAdIndex) {
        this.firstAdIndex = firstAdIndex;
    }

    protected int mLimitOfAds;

    /*
    * Gets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public int getLimitOfAds() {
        return mLimitOfAds;
    }

    /*
    * Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public void setLimitOfAds(int mLimitOfAds) {
        this.mLimitOfAds = mLimitOfAds;
    }

    /**
     * Gets the count of ads that could be published
     *
     * @param fetchedAdsCount the count of completely fetched ads that are ready to be published
     * @param sourceItemsCount the count of items in the source collection
     * @return the original position that the adapter position would have been without ads
     */
    public int getAdsCountToPublish(int fetchedAdsCount, int sourceItemsCount){
        if(fetchedAdsCount <= 0 || getNoOfDataBetweenAds() <= 0) return 0;
        int expected = 0;
        if(sourceItemsCount > 0 && sourceItemsCount >= getOffsetValue()+1)
            expected = (sourceItemsCount - getOffsetValue()) / getNoOfDataBetweenAds() + 1;
        expected = Math.max(0, expected);
        expected = Math.min(fetchedAdsCount, expected);
        return Math.min(expected, getLimitOfAds());
    }

    /**
     * Translates an adapter position to an actual position within the underlying dataset.
     *
     * @param position the adapter position
     * @param fetchedAdsCount the count of completely fetched ads that are ready to be published
     * @param sourceItemsCount the count of items in the source collection
     * @return the original position that the adapter position would have been without ads
     */
    public int getOriginalContentPosition(int position, int fetchedAdsCount, int sourceItemsCount) {
        int noOfAds = getAdsCountToPublish(fetchedAdsCount, sourceItemsCount);
        // No of spaces for ads in the dataset, according to ad placement rules
        int adSpacesCount = (getAdIndex(position) + 1);
        int originalPosition = position - Math.min(adSpacesCount, noOfAds);
        //Log.d("POSITION", position + " is originally " + originalPosition);
        return originalPosition;
    }

    /**
     * Translates an ad position to an actual position within the adapter wrapper.
     *
     * @param adPos the ad's position in the fetched list
     * @return the position of the adapter wrapper item
     */
    public int translateAdToWrapperPosition(int adPos) {
        int wrappedPosition = adPos*(getNoOfDataBetweenAds() + 1) + getOffsetValue();
        return wrappedPosition;
    }

    /**
     * Translates the source position to an actual position within the adapter wrapper.
     * @param fetchedAdsCount the count of completely fetched ads that are ready to be published
     * @param sourcePos the source index
     *
     * @return the position of the adapter wrapper item
     */
    public int translateSourceIndexToWrapperPosition(int sourcePos, int fetchedAdsCount) {
        int adSpacesCount = 0;
        if(sourcePos >= getOffsetValue() && getNoOfDataBetweenAds() > 0)
            adSpacesCount = (sourcePos - getOffsetValue())/getNoOfDataBetweenAds() + 1;
        adSpacesCount = Math.min(fetchedAdsCount, adSpacesCount);
        adSpacesCount = Math.max(0, adSpacesCount);
        adSpacesCount = Math.min(adSpacesCount, getLimitOfAds());
        int wrappedPosition = sourcePos + adSpacesCount;
        return wrappedPosition;
    }

    /**
     * Determines if an ad can be shown at the given position. Checks if the position is for
     * an ad, using the preconfigured ad positioning rules; and if a native ad object is
     * available to place in that position.
     *
     * @param position the adapter position
     * @param fetchedAdsCount the count of completely fetched ads that are ready to be published
     * @return <code>true</code> if ads can
     */
    public boolean canShowAdAtPosition(int position, int fetchedAdsCount) {

        // Is this a valid position for an ad?
        // Is an ad for this position available?
        return isAdPosition(position) && isAdAvailable(position, fetchedAdsCount);
    }

    /**
     * Gets the ad index for this adapter position within the list of currently fetched ads.
     *
     * @param position the adapter position
     * @return the index of the ad within the list of fetched ads
     */
    public int getAdIndex(int position) {
        int index = -1;
        if(position >= getOffsetValue())
            index = (position - getOffsetValue()) / (getNoOfDataBetweenAds()+1);
        //Log.d("POSITION", "index " + index + " for position " + position);
        return index;
    }

    /**
     * Checks if adapter position is an ad position.
     *
     * @param position the adapter position
     * @return {@code true} if an ad position, {@code false} otherwise
     */
    public boolean isAdPosition(int position) {
        int result = (position - getOffsetValue()) % (getNoOfDataBetweenAds() + 1);
        return result == 0;
    }

    public int getOffsetValue() {
        return getFirstAdIndex() > 0 ? getFirstAdIndex() : 0;
    }

    /**
     * Checks if an ad is available for this position.
     *
     * @param position the adapter position
     * @param fetchedAdsCount the count of completely fetched ads that are ready to be published
     * @return {@code true} if an ad is available, {@code false} otherwise
     */
    public boolean isAdAvailable(int position, int fetchedAdsCount) {
        if(fetchedAdsCount == 0) return false;
        int adIndex = getAdIndex(position);
        int firstAdPos = getOffsetValue();

        return position >= firstAdPos && adIndex >= 0 && adIndex < getLimitOfAds() && adIndex < fetchedAdsCount;
    }

    /**
     * Checks if we have to request the next ad block for this position.
     *
     * @param position the adapter position
     * @param fetchingAdsCount the count of fetched and currently fetching ads
     * @return {@code true} if an ad is not available to publish and we should fetch one, {@code false} otherwise
     */
    public boolean hasToFetchAd(int position, int fetchingAdsCount){
        int adIndex = getAdIndex(position);
        int firstAdPos = getOffsetValue();
        return  position >= firstAdPos && adIndex >= 0 && adIndex < getLimitOfAds() && adIndex >= fetchingAdsCount;
    }
}
