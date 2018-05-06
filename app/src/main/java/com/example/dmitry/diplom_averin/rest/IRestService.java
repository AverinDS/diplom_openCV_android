package com.example.dmitry.diplom_averin.rest;


import android.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Интерфейс объявляющий свойства сервера
 * Created by dmitry on 02.03.18.
 */

public interface IRestService {
    String BASE_URL = "http://185.246.64.174/serverML/";

    @Multipart
    @POST("perceptron")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsPerceptron
            (@Part("points") GraphicRest trainPoints);

    @Multipart
    @POST("linear")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsLinear
            (@Part("points") GraphicRest trainPoints);

    @Multipart
    @POST("mlpclassifier")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsMlpClsfr
            (@Part("points") GraphicRest trainPoints);

    @Multipart
    @POST("mlpregressor")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsMlpRgrsr
            (@Part("points") GraphicRest trainPoints);


    @Multipart
    @POST("mynetwork")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsCustomNetwork
            (@Part("points") GraphicRest trainPoints);

    @Multipart
    @POST("fblib")
    Observable<List<Pair<Integer, Integer>>> getPredictPointsFblib
            (@Part("points") GraphicRest trainPoints);

}
