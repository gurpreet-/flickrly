package com.flickrly.flickrly.activities;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.helpers.IconHelper;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import dagger.android.AndroidInjection;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    RestApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        MaterialButton sortBtn = findViewById(R.id.sort_btn);
        RecyclerView photosRv = findViewById(R.id.photos_rv);

        setSupportActionBar(toolbar);
        IconHelper.setupButton(this, sortBtn, GoogleMaterial.Icon.gmd_keyboard_arrow_down);

        photosRv.setLayoutManager(new LinearLayoutManager(this));


        //photosRv.setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuNav) {
        MenuItem refreshMenuItem = menuNav.add(Menu.NONE, 0, 100, "Refresh");
        IconHelper.setupMenuItem(this, refreshMenuItem, GoogleMaterial.Icon.gmd_search);

        refreshMenuItem.setOnMenuItemClickListener(menuItem -> true);

        return super.onCreateOptionsMenu(menuNav);
    }


}