package com.flickrly.flickrly;

import android.app.Activity;
import android.app.Application;
import com.flickrly.flickrly.dagger.DaggerAppComponent;
import com.flickrly.flickrly.dagger.MainModule;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.paperdb.Paper;

import javax.inject.Inject;


public class FlickrlyApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(getViewPump());
        DaggerAppComponent.builder().mainModule(new MainModule(this)).build().inject(this);

        Paper.init(this);
    }

    private ViewPump getViewPump() {
        return ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(getFontConfig()))
                .build();
    }

    private CalligraphyConfig getFontConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/brother-light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
