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
you also can simply copy all the *.java from
```
admobadapter/admobadapter/src/main/java/com/clockbyte/admobadapter/
```
to your ```java``` sources folder (feel free to edit the package names in all files but please leave the License header as is).

and all the *.xml from
```
admobadapter/admobadapter/src/main/res/layout/
```
to your ```res/layout``` folder.
Also please don't forget to copy the string resource ```test_admob_unit_id``` from ```admobadapter/admobadapter/src/main/res/values/strings.xml``` to your ```strings.xml``` file. Then kindly use it as demonstrated in the sampleapp demo. 
When you'll be ready to deploy your app to Release you'll have to register in the Admob and create Ad unit ID there. Then you'd kindly replace the ```test_admob_unit_id``` with your real Ad unit ID. And please don't forget to use the test ID instead of real one when you're debugging/testing your app otherwise Admob can ban your account (artificial inflating of impressions and so on).

#Base usage

The Developer's guide on native ads could be found ![here](https://developers.google.com/admob/android/native "here").

The cook recipes could be found in the Wiki of the project!

#Contributions
Contributions are very welcome. If you find a bug in the library or want an improvement and feel you can work on it yourself, fork + pull request and i'll appreciate it much!
