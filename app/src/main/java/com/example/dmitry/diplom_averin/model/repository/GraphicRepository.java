package com.example.dmitry.diplom_averin.model.repository;

import com.example.dmitry.diplom_averin.rest.BaseResponse;
import com.example.dmitry.diplom_averin.rest.GraphicRest;
import com.example.dmitry.diplom_averin.rest.IRestService;
import com.example.dmitry.diplom_averin.rest.RestServiceProvider;

import io.reactivex.Observable;


/**
 * Created by dmitry on 02.03.18.
 */

public class GraphicRepository {
    private IRestService restService = RestServiceProvider.newInstance().getRestService();

    public Observable<GraphicRest> getPredictPoints() {
        return restService.getPredictPoints().map(BaseResponse::getData);
    }
}
