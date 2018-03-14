package com.example.dmitry.diplom_averin.rest;


import android.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by dmitry on 02.03.18.
 */

public interface IRestService {
    String BASE_URL = "http://185.246.64.174/serverML/";

    @Multipart
    @POST ("perceptron")
    Observable<List<Pair<Integer,Integer>>> getPredictPoints(@Part("points") GraphicRest trainPoints);

}
