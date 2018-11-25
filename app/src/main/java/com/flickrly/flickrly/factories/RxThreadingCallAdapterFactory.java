package com.flickrly.flickrly.factories;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * <=Modified from here: https://stackoverflow.com/a/40828724/3526705
 */
public class RxThreadingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory original;

    private RxThreadingCallAdapterFactory() {
        // Always call on background thread
        original = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    public static CallAdapter.Factory create() {
        return new RxThreadingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper <T, R> implements CallAdapter<R, Observable<T>> {
        private final CallAdapter<R, T> wrapped;

        public RxCallAdapterWrapper(CallAdapter<R, T> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Observable<T> adapt(Call<R> call) {
            Observable observable = (Observable) wrapped.adapt(call);

            // Always handle result on main thread
            return observable.observeOn(AndroidSchedulers.mainThread());
        }
    }
}