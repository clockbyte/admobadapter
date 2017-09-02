package com.clockbyte.admobadapter.sampleapp.express;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clockbyte.admobadapter.expressads.AdViewWrappingStrategyBase;
import com.clockbyte.admobadapter.expressads.AdmobExpressAdapterWrapper;
import com.clockbyte.admobadapter.sampleapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;

public class MainActivity_ListView_Express extends Activity {

    ListView lvMessages;
    AdmobExpressAdapterWrapper adapterWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listview);

        //highly-recommended in Firebase docs to initialize things early as possible
        //test_admob_app_id is different with unit_id! you could get it in your Admob console
        MobileAds.initialize(getApplicationContext(), getString(R.string.test_admob_app_id));

        initListViewItems();
    }

    /**
     * Inits an adapter with items, wrapping your adapter with a {@link AdmobExpressAdapterWrapper} and setting the listview to this wrapper
     * FIRST OF ALL Please notice that the following code will work on a real devices but emulator!
     */
    private void initListViewItems() {
        lvMessages = (ListView) findViewById(R.id.lvMessages);

        //creating your adapter, it could be a custom adapter as well
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        //your test devices' ids
        String[] testDevicesIds = new String[]{getString(R.string.testDeviceID),AdRequest.DEVICE_ID_EMULATOR};
        //when you'll be ready for release please use another ctor with admobReleaseUnitId instead.
        adapterWrapper = AdmobExpressAdapterWrapper.builder(this)
                .setLimitOfAds(10)
                .setFirstAdIndex(2)
                .setNoOfDataBetweenAds(10)
                .setTestDeviceIds(testDevicesIds)
                .setAdapter(adapter)
                .setAdViewWrappingStrategy(new AdViewWrappingStrategyBase() {
                    @NonNull
                    @Override
                    protected ViewGroup getAdViewWrapper(ViewGroup parent) {
                        return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container,
                                parent, false);
                    }

                    @Override
                    protected void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NonNull NativeExpressAdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = (ViewGroup) wrapper.findViewById(R.id.ad_container);
                        //iterating through all children of the container view and remove the first occured {@link NativeExpressAdView}. It could be different with {@param ad}!!!*//*
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View v = container.getChildAt(i);
                            if (v instanceof NativeExpressAdView) {
                                container.removeViewAt(i);
                                break;
                            }
                        }
                    }

                    @Override
                    protected void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NonNull NativeExpressAdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = (ViewGroup) wrapper.findViewById(R.id.ad_container);
                        //add the {@param ad} directly to the end of container*//*
                        container.addView(ad);
                    }
                })
                .build();

        lvMessages.setAdapter(adapterWrapper); // setting an AdmobAdapterWrapper to a ListView

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
}
