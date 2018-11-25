package com.flickrly.flickrly.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhotoActivity extends BaseActivity {

    @Inject
    RestApi api;

    DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.systemDefault());


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
        TextView dateTaken = findViewById(R.id.date_taken);
        TextView dateTakenTitle = findViewById(R.id.date_taken_title);
        TextView datePublished = findViewById(R.id.date_published);
        TextView datePublishedTitle = findViewById(R.id.date_published_title);
        ChipGroup tagChipGroup = findViewById(R.id.tags);
        HorizontalScrollView tagChipGroupContainer = findViewById(R.id.tags_container);
        TextView tagChipsTitle = findViewById(R.id.tags_title);

        ImageView addToGalleryBtn = findViewById(R.id.add_to_gallery);
        ImageView shareBtn = findViewById(R.id.share);
        ImageView browserBtn = findViewById(R.id.website);

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

        Instant dateTakenInstant = photo.getDateTaken();
        if (photo.getDateTaken() != null) {
            dateTaken.setText(formatter.format(dateTakenInstant));
        } else {
            dateTaken.setVisibility(View.GONE);
            dateTakenTitle.setVisibility(View.GONE);
        }

        Instant datePublishedInstant = photo.getPublished();
        if (photo.getPublished() != null) {
            datePublished.setText(formatter.format(datePublishedInstant));
        } else {
            datePublished.setVisibility(View.GONE);
            datePublishedTitle.setVisibility(View.GONE);
        }

        List<String> tags = photo.getListOfTags();
        for (String tag : tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            tagChipGroup.addView(chip);
        }

        if (tags.size() == 0) {
            tagChipGroup.setVisibility(View.GONE);
            tagChipGroupContainer.setVisibility(View.GONE);
            tagChipsTitle.setVisibility(View.GONE);
        }

        addToGalleryBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, photo.getLink());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        browserBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, photo.getLink());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        shareBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, photo.getLink());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }



}
