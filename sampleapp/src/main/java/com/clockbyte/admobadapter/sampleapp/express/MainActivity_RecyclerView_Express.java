package com.clockbyte.admobadapter.sampleapp.express;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.sampleapp.R;
import com.clockbyte.admobadapter.sampleapp.RecyclerExampleAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity_RecyclerView_Express extends Activity {

    RecyclerView rvMessages;
    AdmobExpressRecyclerAdapterWrapper adapterWrapper;
    Timer updateAdsTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycleview);

        //highly-recommended in Firebase docs to initialize things early as possible
        //test_admob_app_id is different with unit_id! you could get it in your Admob console
        MobileAds.initialize(getApplicationContext(), getString(R.string.test_admob_app_id));

        initRecyclerViewItems();
        initUpdateAdsTimer();
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

        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(this);
        //TODO it's important to set your test device ID (you can find it in LogCat after launching the debug session i.e. by word "test")
        //if you launch app on emulator and experience some troubles
        // try passing the constant AdRequest.DEVICE_ID_EMULATOR
        adapterWrapper.addTestDeviceId(getString(R.string.testDeviceID));//set an unique test device ID
        adapterWrapper.addTestDeviceId(AdRequest.DEVICE_ID_EMULATOR);
        //TODO set the custom ads layout if you wish. NOTE you have to set your admob unit ID in this XML.
        //It doesn't work for me if I set the unit ID in code with the method setAdUnitID() so it seems to be a bug
        //adapterWrapper.setExpressAdsLayoutId(R.layout.adexpresslistview_item);
        adapterWrapper.setAdapter(adapter); //wrapping your adapter with a AdmobExpressRecyclerAdapterWrapper.

        //Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
        adapterWrapper.setLimitOfAds(3);

        //Sets the number of your data items between ad blocks, by default it equals to 10.
        //You should set it according to the Admob's policies and rules which says not to
        //display more than one ad block at the visible part of the screen,
        // so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
        adapterWrapper.setNoOfDataBetweenAds(10);

        adapterWrapper.setFirstAdIndex(2);
        //due to the docs you should set the ad size before ads will be loaded
        //AdSize.FULL_WIDTH x 150 is default size.
        adapterWrapper.setAdSize(new AdSize(AdSize.FULL_WIDTH,150));
        adapterWrapper.setAdsUnitId(getString(R.string.test_admob_express_unit_id));

        rvMessages.setAdapter(adapterWrapper); // setting an AdmobExpressRecyclerAdapterWrapper to a RecyclerView

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
    * Could be omitted. It's only for updating an ad blocks in each 60 seconds without refreshing the list
     */
    private void initUpdateAdsTimer() {
        updateAdsTimer = new Timer();
        updateAdsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterWrapper.requestUpdateAd();
                    }
                });
            }
        }, 20*1000, 20*1000);
    }

    /*
    * Seems to be a good practice to destroy all the resources you have used earlier :)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(updateAdsTimer!=null)
            updateAdsTimer.cancel();
        adapterWrapper.destroyAds();
    }
}
