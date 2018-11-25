package com.flickrly.flickrly.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.flickrly.flickrly.FlickrlyApplication;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void registerDisposable(Disposable... ds) {
        compositeDisposable.addAll(ds);
    }

    protected void registerDisposable(Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    public FlickrlyApplication getApp() {
        return (FlickrlyApplication) getApplication();
    }


}
