package com.kickbackapps.ghostcall;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Ynott on 7/27/15.
 */
public class CalligraphyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/helveticaneue_light.ttf").build());
        JodaTimeAndroid.init(this);
    }
}