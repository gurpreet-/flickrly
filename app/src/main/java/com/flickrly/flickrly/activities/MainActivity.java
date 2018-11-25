package com.flickrly.flickrly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.adapters.PhotoAdapter;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.helpers.IconHelper;
import com.flickrly.flickrly.models.Photo;
import com.flickrly.flickrly.models.PhotoShell;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    @Inject
    RestApi api;
    private PhotoAdapter photoAdapter;
    private RecyclerView photosRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialButton sortBtn;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sortBtn = findViewById(R.id.sort_btn);
        photosRv = findViewById(R.id.photos_rv);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        bottomLayout = findViewById(R.id.bottom_sheet);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomLayout);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //bg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //alphaBg(slideOffset);
            }

        });

        IconHelper.setupButton(this, sortBtn, GoogleMaterial.Icon.gmd_keyboard_arrow_down);
        sortBtn.setOnClickListener(view -> {

        });

        photoAdapter = new PhotoAdapter(new ArrayList<>());
        photosRv.setLayoutManager(new LinearLayoutManager(this));
        photosRv.setAdapter(photoAdapter);

        Disposable d = photoAdapter
                .clickedListener()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoClicked, Throwable::printStackTrace);
        registerDisposable(d);

        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        refresh();
    }

    private void onPhotoClicked(Photo photo) {
        Intent i = new Intent(this, PhotoActivity.class);
        startActivity(i);
    }

    private void refresh() {
        Disposable d = api
                .getPublicPhotos()
                .subscribe(this::gotPhotos, Throwable::printStackTrace);
        registerDisposable(d);
    }

    private void gotPhotos(PhotoShell photoShell) {
        photoAdapter.addAll(photoShell.getItems());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuNav) {
        MenuItem refreshMenuItem = menuNav.add(Menu.NONE, 0, 100, "Refresh");
        IconHelper.setupMenuItem(this, refreshMenuItem, GoogleMaterial.Icon.gmd_search);

        refreshMenuItem.setOnMenuItemClickListener(menuItem -> true);

        return super.onCreateOptionsMenu(menuNav);
    }



}
