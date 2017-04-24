package com.clockbyte.admobadapter.sampleapp.express;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.sampleapp.R;
import com.clockbyte.admobadapter.sampleapp.RecyclerExampleAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity_RecyclerView_Express extends Activity {

    RecyclerView rvMessages;
    AdmobExpressRecyclerAdapterWrapper adapterWrapper;

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
     * Inits an adapter with items, wrapping your adapter with a {@link AdmobExpressRecyclerAdapterWrapper} and setting the recyclerview to this wrapper
     * FIRST OF ALL Please notice that the following code will work on a real devices but emulator!
     */
    private void initRecyclerViewItems() {
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        //creating your adapter, it could be a custom adapter as well
        RecyclerExampleAdapter adapter  = new RecyclerExampleAdapter(this);

        //your test devices' ids
        String[] testDevicesIds = new String[]{getString(R.string.testDeviceID),AdRequest.DEVICE_ID_EMULATOR};
        //when you'll be ready for release please use another ctor with admobReleaseUnitId instead.
        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(this, testDevicesIds){
            @NonNull
            @Override
            protected ViewGroup getAdViewWrapper(ViewGroup parent) {
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT);
                CardView cardView = new CardView(MainActivity_RecyclerView_Express.this);
                cardView.setLayoutParams(lp);

                TextView textView = new TextView(MainActivity_RecyclerView_Express.this);
                textView.setLayoutParams(lp);
                textView.setText("Ad is loading...");
                textView.setTextColor(Color.RED);

                cardView.addView(textView);
                //return wrapper view
                return cardView;
            }
        };
        //By default the ad size is set to FULL_WIDTHx150
        //To set a custom size you should use an appropriate ctor
        //adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(this, testDevicesIds, new AdSize(AdSize.FULL_WIDTH, 150));

        adapterWrapper.setAdapter(adapter); //wrapping your adapter with a AdmobExpressRecyclerAdapterWrapper.

        //Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
        adapterWrapper.setLimitOfAds(10);

        //Sets the number of your data items between ad blocks, by default it equals to 10.
        //You should set it according to the Admob's policies and rules which says not to
        //display more than one ad block at the visible part of the screen,
        // so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
        adapterWrapper.setNoOfDataBetweenAds(10);
        adapterWrapper.setFirstAdIndex(2);

        //if you use several view types in your source adapter then you have to set the biggest view type value with the following method
        //adapterWrapper.setViewTypeBiggestSource(100);

        rvMessages.setAdapter(adapterWrapper); // setting an AdmobExpressRecyclerAdapterWrapper to a RecyclerView
        //use the following commented block to use a grid layout with spanning ad blocks
       /* GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapterWrapper.getItemViewType(position) == adapterWrapper.getViewTypeAdExpress())
                    return 2;
                else return 1;
            }
        });
        rvMessages.setLayoutManager(mLayoutManager);*/

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
