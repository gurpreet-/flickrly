package com.flickrly.flickrly.api;

import com.flickrly.flickrly.models.Photo;
import io.reactivex.Observable;
import retrofit2.http.GET;

import java.util.List;

public interface RestApi {

    @GET("services/feeds/photos_public.gne?format=json")
    Observable<List<Photo>> getPublicPhotos();


}
