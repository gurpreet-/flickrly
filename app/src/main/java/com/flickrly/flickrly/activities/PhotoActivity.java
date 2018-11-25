package com.flickrly.flickrly.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.adapters.PhotoAdapter;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.dagger.GlideApp;
import com.flickrly.flickrly.helpers.IconHelper;
import com.flickrly.flickrly.models.Photo;
import com.flickrly.flickrly.models.PhotoShell;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;

public class PhotoActivity extends BaseActivity {

    @Inject
    RestApi api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Photo photo = (Photo) getIntent().getSerializableExtra("photo");

        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.description);
        AppCompatImageView image = findViewById(R.id.image);

        title.setText(photo.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            desc.setText(Html.fromHtml(photo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            desc.setText(Html.fromHtml(photo.getDescription()));
        }

        GlideApp.with(this)
                .asBitmap()
                .load(Uri.parse(photo.getMedia().getHQ()))
                .centerCrop()
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(image);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }



}
