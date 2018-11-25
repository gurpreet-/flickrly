package com.flickrly.flickrly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.adapters.PhotoAdapter;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.models.Photo;
import dagger.android.AndroidInjection;
import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.flickrly.flickrly.models.Photo.PHOTO;
import static com.flickrly.flickrly.models.Photo.PHOTOS;

public class SearchActivity extends BaseActivity implements TextWatcher {

    @Inject
    RestApi api;

    private PhotoAdapter photoAdapter;
    private RecyclerView photosRv;
    private AppCompatEditText searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();
        setupRecycler();
        setupSearch();
    }

    private void setupSearch() {
        searchView = findViewById(R.id.search_box);
        searchView.addTextChangedListener(this);
    }

    private void setupRecycler() {
        photosRv = findViewById(R.id.photos_rv);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        photosRv.setLayoutManager(ll);
        photoAdapter = new PhotoAdapter(new ArrayList<>());
        photosRv.setAdapter(photoAdapter);

        Disposable d = photoAdapter
                .clickedListener()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoClicked, Throwable::printStackTrace);
        registerDisposable(d);
    }

    private void onPhotoClicked(Photo photo) {
        Intent i = new Intent(this, PhotoActivity.class);
        Paper.book().write(PHOTO, photo);
        startActivity(i);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * When the user types a query,
     * we search the locally persisted photo
     * list and any relevant tags.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String searchText = editable.toString();
        int length = searchText.trim().length();
        if (length <= 2) {
            photoAdapter.replaceAll(new ArrayList<>());
            return;
        }
        List<Photo> photos = Paper.book().read(PHOTOS);
        List<Photo> results = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.getTags().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(photo);
            }
        }

        photoAdapter.replaceAll(results);
    }
}
