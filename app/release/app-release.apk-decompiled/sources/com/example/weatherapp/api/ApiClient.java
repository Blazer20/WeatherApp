package com.example.weatherapp.api;

import com.example.weatherapp.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends MainActivity {
    public static final String BASE_URL = "https://api.weatherbit.io/";
    private static ApiClient mInstance;
    private ApiInterface apiService;
    private Retrofit retrofit;

    private ApiClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        Retrofit build = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()).addConverterFactory(GsonConverterFactory.create()).build();
        this.retrofit = build;
        this.apiService = (ApiInterface) build.create(ApiInterface.class);
    }

    public static ApiClient getInstance() {
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public ApiInterface getApiInterface() {
        return this.apiService;
    }
}
