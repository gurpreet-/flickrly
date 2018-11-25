package com.flickrly.flickrly.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.dagger.GlideApp;
import com.flickrly.flickrly.models.Photo;
import dagger.android.AndroidInjection;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PhotoActivity extends BaseActivity {

    private static final int SAVE_TO_GALLERY_CODE = 2333;

    @Inject
    RestApi api;

    private DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.systemDefault());
    private Photo photo;
    private AppCompatImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        photo = (Photo) getIntent().getSerializableExtra("photo");

        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.description);
        image = findViewById(R.id.image);
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

        addToGalleryBtn.setOnClickListener(view -> saveImageToGallery());

        browserBtn.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photo.getLink()));
            startActivity(browserIntent);
        });

        shareBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, photo.getLink());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
    }

    private void saveImageToGallery() {
        if (!grantedStorage()) {
            requestStorage();
        }
        if (isExternalStorageWritable()) {
            BitmapDrawable bmp = ((BitmapDrawable) image.getDrawable());
            bitmapToGallery(bmp.getBitmap());
        } else {
            Toast.makeText(
                    PhotoActivity.this,
                    "Your device has no external storage so cannot save to gallery.",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == SAVE_TO_GALLERY_CODE) {
                saveImageToGallery();
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void bitmapToGallery(Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, photo.getTitle() , "");
        Toast.makeText(this, "Saved image to gallery.", Toast.LENGTH_LONG).show();
    }

    public void requestStorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_TO_GALLERY_CODE);
    }

    public boolean grantedStorage() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
