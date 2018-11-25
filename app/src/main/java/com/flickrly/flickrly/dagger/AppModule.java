package com.flickrly.flickrly.dagger;

import com.flickrly.flickrly.activities.MainActivity;
import com.flickrly.flickrly.activities.PhotoActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AppModule {


    @ContributesAndroidInjector
    abstract MainActivity contributeYourActivityInjector();

    @ContributesAndroidInjector
    abstract PhotoActivity contributeYourActivityInjectorPhoto();

}