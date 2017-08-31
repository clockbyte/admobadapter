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

package com.clockbyte.admobadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by FILM on 30.08.2017.
 */

public class AdapterWrapperObserver extends RecyclerView.AdapterDataObserver {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterWrapper;
    private AdmobAdapterCalculator adapterCalculator;
    private AdmobFetcherBase fetcher;

    public AdapterWrapperObserver(@NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapterWrapper,
                                  @NonNull AdmobAdapterCalculator admobAdapterCalculator,
                                  @NonNull AdmobFetcherBase admobFetcher) {
        this.adapterWrapper = adapterWrapper;
        this.adapterCalculator = admobAdapterCalculator;
        this.fetcher = admobFetcher;
    }

    @Override
    public void onChanged() {
        adapterWrapper.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        this.onItemRangeChanged(positionStart, itemCount, null);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        int fetchedAdsCount = fetcher.getFetchedAdsCount();
        //getting the position in a final presentation
        int wrapperIndexFirst = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart, fetchedAdsCount);
        int wrapperIndexLast = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart + itemCount - 1, fetchedAdsCount);
        if (itemCount == 1)
            adapterWrapper.notifyItemRangeChanged(wrapperIndexFirst, 1, payload);
        else
            adapterWrapper.notifyItemRangeChanged(wrapperIndexFirst, wrapperIndexLast - wrapperIndexFirst + 1, payload);

    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        int fetchedAdsCount = fetcher.getFetchedAdsCount();
        //getting the position in a final presentation
        int wrapperIndexFirst = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart, fetchedAdsCount);
        int wrapperIndexLast = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart + itemCount - 1, fetchedAdsCount);
        if (itemCount == 1)
            adapterWrapper.notifyItemRangeInserted(wrapperIndexFirst, 1);
        else
            adapterWrapper.notifyItemRangeInserted(wrapperIndexFirst, wrapperIndexLast - wrapperIndexFirst + 1);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        int fetchedAdsCount = fetcher.getFetchedAdsCount();
        //getting the position in a final presentation
        int fromWrapperIndexFirst = adapterCalculator.translateSourceIndexToWrapperPosition(fromPosition, fetchedAdsCount);
        int fromWrapperIndexLast = adapterCalculator.translateSourceIndexToWrapperPosition(fromPosition + itemCount - 1, fetchedAdsCount);
        int toWrapperIndexFirst = adapterCalculator.translateSourceIndexToWrapperPosition(toPosition, fetchedAdsCount);
        int toWrapperIndexLast = adapterCalculator.translateSourceIndexToWrapperPosition(toPosition + itemCount - 1, fetchedAdsCount);
        int wrapperItemCount = fromWrapperIndexLast - fromWrapperIndexFirst + 1;
        if (itemCount == 1)
            adapterWrapper.notifyItemMoved(fromWrapperIndexFirst, 1);
        else for (int i = 0; i < wrapperItemCount; itemCount++)
                adapterWrapper.notifyItemMoved(fromWrapperIndexFirst + i, toWrapperIndexFirst + i);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        int fetchedAdsCount = fetcher.getFetchedAdsCount();
        //getting the position in a final presentation
        int wrapperIndexFirst = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart, fetchedAdsCount);
        int wrapperIndexLast = adapterCalculator.translateSourceIndexToWrapperPosition(positionStart + itemCount - 1, fetchedAdsCount);
        if (itemCount == 1)
            adapterWrapper.notifyItemRangeRemoved(wrapperIndexFirst, 1);
        else
            adapterWrapper.notifyItemRangeRemoved(wrapperIndexFirst, wrapperIndexLast - wrapperIndexFirst + 1);
    }
}