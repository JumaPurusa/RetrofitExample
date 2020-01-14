package com.example.retrofitexample.Utils;

import java.io.Serializable;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    private static OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();

    private static Retrofit.Builder builder
        = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

}
