package com.example.weatherapp.api;

import android.content.Context;

import com.example.weatherapp.MainActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends MainActivity {

    public static final String BASE_URL = "https://api.weatherapi.com/";

    private static ApiClient mInstance;
    private Retrofit retrofit;
    private ApiInterface apiService;

    private ApiClient() {

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiInterface.class);
    }

    public static ApiClient getInstance() {
        if(mInstance == null){
            mInstance = new ApiClient();
        }

        return mInstance;
    }

    public ApiInterface getApiInterface() {
        return apiService;
    }
}
