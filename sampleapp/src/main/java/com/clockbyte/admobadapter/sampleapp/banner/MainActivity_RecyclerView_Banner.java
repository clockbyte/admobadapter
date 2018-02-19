package com.clockbyte.admobadapter.sampleapp.banner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.bannerads.AdmobBannerRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.bannerads.BannerAdViewWrappingStrategy;
import com.clockbyte.admobadapter.bannerads.BannerAdViewWrappingStrategyBase;
import com.clockbyte.admobadapter.sampleapp.R;
import com.clockbyte.admobadapter.sampleapp.RecyclerExampleAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity_RecyclerView_Banner extends Activity {

    RecyclerView rvMessages;
    AdmobBannerRecyclerAdapterWrapper adapterWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycleview);

        //highly-recommended in Firebase docs to initialize things early as possible
        //test_admob_app_id is different with unit_id! you could get it in your Admob console
        MobileAds.initialize(getApplicationContext(), getString(R.string.test_admob_app_id));

        initRecyclerViewItems();
    }

    /**
     * Inits an adapter with items, wrapping your adapter with a {@link AdmobBannerRecyclerAdapterWrapper} and setting the recyclerview to this wrapper
     * FIRST OF ALL Please notice that the following code will work on a real devices but emulator!
     */
    private void initRecyclerViewItems() {
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        //creating your adapter, it could be a custom adapter as well
        RecyclerExampleAdapter adapter  = new RecyclerExampleAdapter(this);

        //your test devices' ids
        String[] testDevicesIds = new String[]{getString(R.string.testDeviceID), AdRequest.DEVICE_ID_EMULATOR};
        //when you'll be ready for release please use another ctor with admobReleaseUnitId instead.
        adapterWrapper = AdmobBannerRecyclerAdapterWrapper.builder(this)
                .setLimitOfAds(10)
                .setFirstAdIndex(2)
                .setNoOfDataBetweenAds(10)
                .setTestDeviceIds(testDevicesIds)
                .setAdapter(adapter)
                //Use the following for the default Wrapping behaviour
//                .setAdViewWrappingStrategy(new BannerAdViewWrappingStrategy())
                // Or implement your own custom wrapping behaviour:
                .setAdViewWrappingStrategy(new BannerAdViewWrappingStrategyBase() {
                    @NonNull
                    @Override
                    protected ViewGroup getAdViewWrapper(ViewGroup parent) {
                        return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container,
                                parent, false);
                    }

                    @Override
                    protected void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = (ViewGroup) wrapper.findViewById(R.id.ad_container);
                        //iterating through all children of the container view and remove the first occured {@link NativeExpressAdView}. It could be different with {@param ad}!!!*//*
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View v = container.getChildAt(i);
                            if (v instanceof AdView) {
                                container.removeViewAt(i);
                                break;
                            }
                        }
                    }

                    @Override
                    protected void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = (ViewGroup) wrapper.findViewById(R.id.ad_container);
                        //add the {@param ad} directly to the end of container*//*
                        container.addView(ad);
                    }
                })
                .build();

        rvMessages.setAdapter(adapterWrapper); // setting an AdmobBannerRecyclerAdapterWrapper to a RecyclerView
        //use the following commented block to use a grid layout with spanning ad blocks
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapterWrapper.getItemViewType(position) == adapterWrapper.getViewTypeAdBanner())
                    return 2;
                else return 1;
            }
        });
        rvMessages.setLayoutManager(mLayoutManager);

        //preparing the collection of data
        final String sItem = "item #";
        ArrayList<String> lst = new ArrayList<String>(100);
        for(int i=1;i<=100;i++)
            lst.add(sItem.concat(Integer.toString(i)));

        //adding a collection of data to your adapter and rising the data set changed event
        adapter.addAll(lst);
        adapter.notifyDataSetChanged();
    }

    /*
    * Seems to be a good practice to destroy all the resources you have used earlier :)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapterWrapper.release();
    }


    @Override
    protected void onPause() {
        adapterWrapper.pauseAll();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterWrapper.resumeAll();
    }
}
