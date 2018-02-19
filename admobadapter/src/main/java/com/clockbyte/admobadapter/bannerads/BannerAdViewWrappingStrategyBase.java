package com.clockbyte.admobadapter.bannerads;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;

/**
 * Created by L4grange on 14/02/2018
 */

public abstract class BannerAdViewWrappingStrategyBase {
    /**
     * Add the Banner {@param ad} to {@param wrapper}.
     * See the super's implementation for instance.
     */
    protected abstract void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad);

    /**
     * This method can be overridden to recycle (remove) {@param ad} from {@param wrapper} view before adding to wrap.
     * By default it will look for {@param ad} in the direct children of {@param wrapper} and remove the first occurrence.
     * See the super's implementation for instance.
     * The BannerHolder recycled by the RecyclerView may be a different
     * instance than the one used previously for this position. Clear the
     * wrapper of any subviews in case it has a different
     * AdView associated with it
     */
    protected abstract  void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad);

    /**
     * This method can be overridden to wrap the created ad view with a custom {@link ViewGroup}.<br/>
     * For example if you need to wrap the ad with your custom CardView
     * @return The wrapper {@link ViewGroup} for ad, by default {@link AdView} ad would be wrapped with a CardView which is returned by this method
     */
    @NonNull
    protected abstract ViewGroup getAdViewWrapper(ViewGroup parent);
}
