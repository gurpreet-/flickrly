package com.flickrly.flickrly.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.adapters.PhotoAdapter;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.enums.Sort;
import com.flickrly.flickrly.helpers.IconHelper;
import com.flickrly.flickrly.models.Photo;
import com.flickrly.flickrly.models.PhotoShell;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    @Inject
    RestApi api;

    public static String SHARED_PREFS =  "GLOBAL_SHARED_PREFS";
    public static String PREF_SORT =  "SORT";
    public static String PREF_SORT_CREATION =  "creation";
    public static String PREF_SORT_PUBLICATION =  "publication";


    private PhotoAdapter photoAdapter;
    private RecyclerView photosRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialButton sortBtn;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private LinearLayout bottomLayout;
    private View blackBg;

    private MaterialButton creationBtn;
    private MaterialButton publicationBtn;

    private int primaryColor;
    private int accentColor;
    private Sort currentSort;
    private SharedPreferences sharedPreferences;
    private View errorLayout;

    @Override
    @SuppressLint("ClickableViewAccessibility")
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
        blackBg = findViewById(R.id.black_bg);
        creationBtn = findViewById(R.id.creation_btn);
        publicationBtn = findViewById(R.id.publication_btn);
        errorLayout = findViewById(R.id.error);


        primaryColor = getColor(R.color.colorPrimaryL1);
        accentColor = getColor(R.color.colorAccent);

        View.OnClickListener onSortBtnsClick = view -> toggleSort();
        creationBtn.setOnClickListener(onSortBtnsClick);
        publicationBtn.setOnClickListener(onSortBtnsClick);

        bottomLayout.setBackground(getDrawableWithRadius());
        bottomSheetBehavior = BottomSheetBehavior.from(bottomLayout);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        IconHelper.setupButton(this, sortBtn, GoogleMaterial.Icon.gmd_keyboard_arrow_down);
        sortBtn.setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String sort = sharedPreferences.getString(PREF_SORT, PREF_SORT_CREATION);
        if (sort.equalsIgnoreCase(PREF_SORT_CREATION)) {
            setSort(Sort.CREATION);
        } else {
            setSort(Sort.PUBLICATION);
        }


        photoAdapter = new PhotoAdapter(new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        photosRv.setLayoutManager(linearLayoutManager);
        photosRv.setAdapter(photoAdapter);


        Disposable d = photoAdapter
                .clickedListener()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoClicked, Throwable::printStackTrace);
        registerDisposable(d);

        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        refresh();
    }

    private void toggleSort() {
        if (isSortingByCreation()) {
            setSort(Sort.PUBLICATION);
        } else {
            setSort(Sort.CREATION);
        }
        refresh();
    }

    private boolean isSortingByCreation() {
        return currentSort.equals(Sort.CREATION);
    }

    private void setSort(Sort sort) {
        this.currentSort = sort;
        if (photoAdapter != null) {
            photoAdapter.replaceAll(new ArrayList<>());
        }
        toggleButton(sort);
        if (sort.equals(Sort.CREATION)) {
            sharedPreferences.edit().putString(PREF_SORT, PREF_SORT_CREATION).apply();
            sortBtn.setText("Creation");
        } else {
            sharedPreferences.edit().putString(PREF_SORT, PREF_SORT_PUBLICATION).apply();
            sortBtn.setText("Publication");
        }
    }

    private void toggleButton(Sort sort) {
        if (sort.equals(Sort.CREATION)) {
            creationBtn.setBackgroundTintList(ColorStateList.valueOf(accentColor));
            publicationBtn.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
        } else {
            creationBtn.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
            publicationBtn.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        }
    }

    private void onPhotoClicked(Photo photo) {
        Intent i = new Intent(this, PhotoActivity.class);
        i.putExtra("photo", photo);
        startActivity(i);
    }

    private void refresh() {
        errorLayout.setVisibility(View.GONE);
        String sort;
        if (isSortingByCreation()) {
            // Is sorting by creation date
            sort = "date-taken-desc";
        } else {
            // Is sorting by publication date
            sort = "date-posted-desc";
        }
        Disposable d = api
                .getPublicPhotos(sort)
                .throttleWithTimeout(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::gotPhotos, this::onError);
        registerDisposable(d);
    }

    private void onError(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void gotPhotos(PhotoShell photoShell) {
        // Sort the list in descending order
        List<Photo> photoList = photoShell.getItems();
        if (photoList.size() == 0) {
            return;
        }
        Collections.sort(photoList, (p1, p2) -> {
            if (isSortingByCreation()) {
                if (p1.getDateTaken() != null && p2.getDateTaken() != null) {
                    return p1.getDateTaken().compareTo(p2.getDateTaken());
                }
            } else {
                if (p1.getPublished() != null && p2.getPublished() != null) {
                    return p1.getPublished().compareTo(p2.getPublished());
                }
            }
            return 0;
        });

        photoAdapter.replaceAll(photoList);
        swipeRefreshLayout.setRefreshing(false);
    }


    private Drawable getDrawableWithRadius() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.WHITE);
        return gradientDrawable;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuNav) {
        MenuItem refreshMenuItem = menuNav.add(Menu.NONE, 0, 100, "Refresh");
        IconHelper.setupMenuItem(this, refreshMenuItem, GoogleMaterial.Icon.gmd_search);

        refreshMenuItem.setOnMenuItemClickListener(menuItem -> true);

        return super.onCreateOptionsMenu(menuNav);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setBottomSheetCallback(null);
        }
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                blackBg.setAlpha(0);
                blackBg.setClickable(false);
                blackBg.setFocusable(false);
                blackBg.setFocusableInTouchMode(false);
            }
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                blackBg.setAlpha(1);
                blackBg.setClickable(true);
                blackBg.setFocusable(true);
                blackBg.setFocusableInTouchMode(true);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            float s = Math.abs(slideOffset);
            blackBg.setAlpha(1 - s);
        }

    };

    /**
     * Hides the bottom sheet if the user clicked outside of it.
     * @param event Event that triggered this touch.
     * @return True if handled
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomLayout.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
        return super.dispatchTouchEvent(event);
    }


}
