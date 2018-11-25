package com.flickrly.flickrly.api;

import com.flickrly.flickrly.models.PhotoShell;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("services/feeds/photos_public.gne?format=json")
    Observable<PhotoShell> getPublicPhotos(@Query("sort") String sort);


}
