package com.flickrly.flickrly.dagger;

import android.content.Context;
import com.flickrly.flickrly.api.RestApi;
import com.flickrly.flickrly.converters.GsonDateTimeConverter;
import com.flickrly.flickrly.converters.LocalDateConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Module(includes = AppModule.class)
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Provides
    Cache provideOkHttpCache() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    Gson provideGson() {
        return getGson();
    }


    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {

        ArrayList<ConnectionSpec> specs = new ArrayList<>();
        ConnectionSpec specTLS1_2 = new
                ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
                )
                .build();
        specs.add(specTLS1_2);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(120L, TimeUnit.SECONDS)
                .writeTimeout(120L, TimeUnit.SECONDS)
                .connectTimeout(120L, TimeUnit.SECONDS)
                .followSslRedirects(true)
                .connectionSpecs(specs)
                .cache(cache)
                .build();
    }

    @Provides
    RxJava2CallAdapterFactory provideRxAdapter() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }


    @Provides
    RestApi provideRetrofit(Gson gson,
                            OkHttpClient okHttpClient,
                            RxJava2CallAdapterFactory adapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.flickr.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(adapterFactory)
                .build();
        return retrofit.create(RestApi.class);
    }

    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder
                .serializeNulls()
                .setLenient()
                .registerTypeAdapter(Instant.class, new GsonDateTimeConverter())
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
                .create();
    }

}