Admob Adapter
======================

Admob Adapter is an Android library that makes it easy to integrate ![Admob native ads](https://support.google.com/admob/answer/6240809 "Admob native ads") into ```ListView``` in the way that is shown in the following image/animation.

![](https://raw.githubusercontent.com/clockbyte/admobadapter/master/screenshots/device-2015-08-28-012121.png)

![](https://raw.githubusercontent.com/clockbyte/admobadapter/master/screenshots/ezgif.com-gif-maker.gif "Demo gif")

#Main features

* Publish of the native ads without changing the logic of your ```Adapter```
* Customization: you can easily set the maximum count of ads per list, the count of items shown between ad blocks and a custom layouts for ads blocks.
* Easy to update ad blocks periodically

The admobadapter-sampleapp shows the available features and customizations.
 
# Installation

##Cloning
First of all you will have to clone the library.
```shell
git clone https://github.com/clockbyte/admobadapter.git
```

Now that you have the library you will have to import it into Android Studio.
In Android Studio navigate the menus like this.
```
File -> Import Project ...
```
In the following dialog navigate to ```admobadapter-master``` which you cloned to your computer in the previous steps and select the `build.gradle`.

##Or 
you also can simply copy the following sources
```
admobadapter/admobadapter/src/main/java/com/clockbyte/admobadapter/AdmobAdapterWrapper.java
admobadapter/admobadapter/src/main/java/com/clockbyte/admobadapter/AdmobFetcher.java
```
to your ```java``` sources folder (feel free to edit the package names in all files but please leave the License header as is).

and the following resources
```
admobadapter/admobadapter/src/main/res/layout/adcontentlistview_item.xml
admobadapter/admobadapter/src/main/res/layout/adinstalllistview_item.xml
```
to your ```res/layout``` folder.
Also please don't forget to copy the string resource ```test_admob_unit_id``` from ```admobadapter/admobadapter/src/main/res/values/strings.xml``` to your ```strings.xml``` file. Then kindly use it as demonstrated in the sampleapp demo. 
When you'll be ready to deploy your app to Release you'll have to register in the Admob and create Ad unit ID there. Then you'd kindly replace the ```test_admob_unit_id``` with your real Ad unit ID. And please don't forget to use the test ID instead of real one when you're debugging/testing your app otherwise Admob can ban your account (artificial inflating of impressions and so on).

#Base usage

The Developer's guide on native ads could be found ![here](https://developers.google.com/admob/android/native "here").

Let's start from your XML layout with the list. It could look like this:
```xml
    <ListView
            android:id="@+id/lvMessages"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
```

Then in the corresponding java you'll have something like this:
```java
    ListView lvMessages;
    AdmobAdapterWrapper adapterWrapper;
//...

 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListViewItems();
    }
    
    /**
     * Inits an adapter with items, wrapping your adapter with a {@link AdmobAdapterWrapper} and setting the listview to this wrapper
     * FIRST OF ALL Please notice that the following code will work on a real devices but emulator!
     */
    private void initListViewItems() {
        lvMessages = (ListView) findViewById(R.id.lvMessages);

        //creating your adapter, it could be a custom adapter as well
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        adapterWrapper = new AdmobAdapterWrapper(this);
        adapterWrapper.setAdapter(adapter); //wrapping your adapter with a AdmobAdapterWrapper.
        //here you can use the following string to set your custom layouts for a different types of native ads
        //adapterWrapper.setInstallAdsLayoutId(R.layout.your_installad_layout);
        //adapterWrapper.setcontentAdsLayoutId(R.layout.your_installad_layout);

        //Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
        adapterWrapper.setLimitOfAds(3);

        //Sets the number of your data items between ad blocks, by default it equals to 10.
        //You should set it according to the Admob's policies and rules which says not to
        //display more than one ad block at the visible part of the screen,
        // so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
        adapterWrapper.setNoOfDataBetweenAds(10);

        //It's a test admob ID. Please replace it with a real one only when you will be ready to deploy your product to the Release!
        //Otherwise your Admob account could be banned
        //String admobUnitId = getResources().getString(R.string.banner_admob_unit_id);
        //adapterWrapper.setAdmobReleaseUnitId(admobUnitId);

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
        adapterWrapper.destroyAds();
    }
```

#Contributions
Contributions are very welcome. If you find a bug in the library or want an improvement and feel you can work on it yourself, fork + pull request and i'll appreciate it much!
