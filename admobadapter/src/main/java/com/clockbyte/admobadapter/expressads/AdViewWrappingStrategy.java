package com.clockbyte.admobadapter.expressads;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.R;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by fim on 31.08.2017.
 * @deprecated Use banners instead
 */
@Deprecated
public class AdViewWrappingStrategy extends AdViewWrappingStrategyBase {
    /**
     * Add the Native Express {@param ad} to {@param wrapper}.
     * See the super's implementation for instance.
     */
    @Override
    protected void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NonNull NativeExpressAdView ad) {
        wrapper.addView(ad);
    }

    /**
     * This method can be overriden to recycle (remove) {@param ad} from {@param wrapper} view before adding to wrap.
     * By default it will look for {@param ad} in the direct children of {@param wrapper} and remove the first occurence.
     * See the super's implementation for instance.
     * The NativeExpressHolder recycled by the RecyclerView may be a different
     * instance than the one used previously for this position. Clear the
     * wrapper of any subviews in case it has a different
     * AdView associated with it
     */
    @Override
    protected void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NonNull NativeExpressAdView ad) {
        for (int i = 0; i < wrapper.getChildCount(); i++) {
            View v = wrapper.getChildAt(i);
            if (v instanceof NativeExpressAdView) {
                wrapper.removeViewAt(i);
                break;
            }
        }
    }

    /**
     * This method can be overriden to wrap the created ad view with a custom {@link ViewGroup}.<br/>
     * For example if you need to wrap the ad with your custom CardView
     * @return The wrapper {@link ViewGroup} for ad, by default {@link NativeExpressAdView} ad would be wrapped with a CardView which is returned by this method
     */
    @Override
    @NonNull
    protected ViewGroup getAdViewWrapper(ViewGroup parent) {
        return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.web_ad_container,
                parent, false);
    }
}

