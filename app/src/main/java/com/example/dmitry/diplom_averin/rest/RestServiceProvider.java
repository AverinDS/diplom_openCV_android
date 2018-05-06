package com.example.dmitry.diplom_averin.rest;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс, предоставляющий доступ к серверу, используя свойства IRestService
 * @author Averin Dmitry
 * @see IRestService
 * Created on 02.03.18.
 */

public class RestServiceProvider {
    private String LOG_TAG = "RestServiceProvider";
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

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.i(LOG_TAG, message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }

}
