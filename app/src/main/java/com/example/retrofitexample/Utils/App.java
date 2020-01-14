package com.example.retrofitexample.Utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.retrofitexample.Adapters.UsersListAdapter;
import com.example.retrofitexample.Interfaces.InternetConnectionListener;
import com.example.retrofitexample.Interfaces.UserService;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private UserService userService;

    private InternetConnectionListener mInternetConnectionListener;

    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    public void setmInternetConnectionListener(InternetConnectionListener listerner) {
        this.mInternetConnectionListener = listerner;
    }

    public void removeInternetConnectionListener(){

        this.mInternetConnectionListener = null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserService getUserService(){

        if(userService == null)
            userService = getRetrofit(UserService.BASE_URL).create(UserService.class);

        return userService;

    }

    private Retrofit getRetrofit(String url){

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo
                = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private OkHttpClient getOkHttpClient(){

        OkHttpClient.Builder okHttpClientBuilder
                = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.cache(getCache());

        okHttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {

                return App.this.isNetworkAvailable();
            }

            @Override
            public void onInternetUnavailable() {

                if(mInternetConnectionListener != null)
                    mInternetConnectionListener.onInternetUnavailable();
            }

            @Override
            public void onCacheUnavailable() {

                if(mInternetConnectionListener != null)
                    mInternetConnectionListener.onCacheUnavailable();
            }
        });

        return okHttpClientBuilder.build();
    }

    public Cache getCache() {
        File cacheDir = new File(getCacheDir(), "cache");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        return cache;
    }
}
