package com.flickrly.flickrly.helpers;

import android.content.Context;
import android.support.design.button.MaterialButton;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import com.flickrly.flickrly.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class IconHelper {


    public static void setupMenuItem(Context context, MenuItem menuItem, GoogleMaterial.Icon icon) {
        int white = ContextCompat.getColor(context, R.color.white);
        IconicsDrawable drawable = new IconicsDrawable(context);
        drawable.icon(icon);
        drawable.color(white);
        drawable.sizeDp(18);
        menuItem.setIcon(drawable).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    public static void setupButton(Context context, MaterialButton materialButton, GoogleMaterial.Icon icon) {
        int white = ContextCompat.getColor(context, R.color.white);
        IconicsDrawable drawable = new IconicsDrawable(context);
        drawable.icon(icon);
        drawable.color(white);
        drawable.sizeDp(18);
        materialButton.setIcon(drawable);
    }

}