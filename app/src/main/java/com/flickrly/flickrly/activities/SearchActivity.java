package com.flickrly.flickrly.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.adapters.PhotoAdapter;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.models.Photo;
import dagger.android.AndroidInjection;
import io.paperdb.Paper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {


    @Inject
    RestApi api;


    private PhotoAdapter photoAdapter;
    private RecyclerView photosRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        photosRv = findViewById(R.id.photos_rv);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        photosRv.setLayoutManager(ll);

        List<Photo> photos = Paper.book().read("photos");
        photoAdapter = new PhotoAdapter(photos);
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");
    }
}
