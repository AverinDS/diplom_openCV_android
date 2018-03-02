package com.example.dmitry.diplom_averin.rest;



import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by dmitry on 02.03.18.
 */

public interface IRestService {
    String BASE_URL = "";

    @POST
    Observable<BaseResponse<GraphicRest>> getPredictPoints();

    @POST
    Observable<BaseResponse<GraphicRest>> sendTrainPoints();

}
