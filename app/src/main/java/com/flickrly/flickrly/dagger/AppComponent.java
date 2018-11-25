package com.flickrly.flickrly.dagger;

import com.flickrly.flickrly.FlickrlyApplication;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

import javax.inject.Singleton;

@Singleton
@Component(modules = { AndroidInjectionModule.class, AppModule.class, MainModule.class})
public interface AppComponent extends AndroidInjector<FlickrlyApplication> {

}