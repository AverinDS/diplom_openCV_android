package com.example.dmitry.diplom_averin.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmitry on 02.03.18.
 */

public class RestServiceProvider {
    private static final RestServiceProvider INSTANCE = new RestServiceProvider();

    private IRestService restService;

    private RestServiceProvider() { }

    public static RestServiceProvider newInstance() { return INSTANCE;}

    public synchronized  final IRestService getRestService() {
        if (restService == null) {
            restService = createRestService();
        }
        return restService;
    }

    private IRestService createRestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRestService.BASE_URL)
                .client(provideClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(IRestService.class);
    }

    private OkHttpClient provideClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
