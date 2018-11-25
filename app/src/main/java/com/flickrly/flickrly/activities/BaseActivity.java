package com.flickrly.flickrly.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.flickrly.flickrly.FlickrlyApplication;
import com.flickrly.flickrly.R;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    protected void hideViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    public FlickrlyApplication getApp() {
        return (FlickrlyApplication) getApplication();
    }


    public void showError(Throwable throwable) {
        Snackbar.make(getView(), "There was an error.", Snackbar.LENGTH_LONG).show();
    }

    public View getView() {
        return findViewById(R.id.top);
    }
}
