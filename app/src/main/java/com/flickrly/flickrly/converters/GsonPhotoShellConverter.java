package com.flickrly.flickrly.converters;

import com.flickrly.flickrly.dagger.MainModule;
import com.flickrly.flickrly.models.PhotoShell;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class GsonPhotoShellConverter extends Converter.Factory {

    private Gson gson;

    public GsonPhotoShellConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (PhotoShell.class.equals(type)) {
            return (Converter<ResponseBody, PhotoShell>) value -> {
                String photoShell = value.string();
                String noStartingWords = photoShell
                        .trim()
                        .replace("jsonFlickrFeed(", "");

                String finished = removeLastChar(noStartingWords, ')');
                return gson.fromJson(finished, PhotoShell.class);
            };
        }
        return null;
    }

    private static String removeLastChar(String str, Character toReplace) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == toReplace) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}