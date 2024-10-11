package com.dsp.dspattendenceapp.network;

import android.app.Activity;

import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientServer {
    public static RetrofitClientServer instance;
        public static Activity activity;

        private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (Utillity.isNetworkAvailable(activity)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };

        public static ApiCall getInstance(Activity act) {

            activity = act;
            if (instance == null)
                instance = new RetrofitClientServer();
            return instance.getApiClient();
        }

//    private OkHttpClient createOkHttpClient() {
//
//
////        File httpCacheDirectory = new File(activity.getCacheDir(), "responses");
////        int cacheSize = 100 * 1024 * 1024; // 100 MiB
////        Cache cache = new Cache(httpCacheDirectory, cacheSize);
//
//
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.readTimeout(120, TimeUnit.SECONDS);
//        httpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
//        httpClient.connectTimeout(20, TimeUnit.SECONDS);
////        httpClient.cache(cache);
//
//        return httpClient.build();
//    }

        private OkHttpClient createOkHttpClient() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(120, TimeUnit.SECONDS);
            httpClient.connectTimeout(20, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));
            // httpClient.addInterceptor(new MyInterceptor());

            return httpClient.build();
        }


        private Retrofit createRetrofit() {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            return new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_SERVER)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(createOkHttpClient())
                    .build();
        }

        public ApiCall getApiClient() {
            final Retrofit retrofit = createRetrofit();
            return retrofit.create(ApiCall.class);
        }

}
