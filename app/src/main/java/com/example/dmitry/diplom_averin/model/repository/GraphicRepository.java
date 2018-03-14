package com.example.dmitry.diplom_averin.model.repository;

import android.util.Pair;

import com.example.dmitry.diplom_averin.helper.MethodML;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.rest.GraphicRest;
import com.example.dmitry.diplom_averin.rest.IRestService;
import com.example.dmitry.diplom_averin.rest.RestServiceProvider;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by dmitry on 02.03.18.
 */

public class GraphicRepository {
    private IRestService restService = RestServiceProvider.newInstance().getRestService();

    public Observable<List<Pair<Integer,Integer>>> getPredictPoints(MethodML methodML) {
        switch (methodML) {
            case Perceptron:
                return restService.getPredictPointsPerceptron(new GraphicRest(Graphic.getInstance().pointsTrain));
            case LinearRegression:
                return restService.getPredictPointsLinear(new GraphicRest(Graphic.getInstance().pointsTrain));
            case MLPClassifier:
                return restService.getPredictPointsMlpClsfr(new GraphicRest(Graphic.getInstance().pointsTrain));
            case MLPRegressor:
                return restService.getPredictPointsMlpRgrsr(new GraphicRest(Graphic.getInstance().pointsTrain));
        }
        return null;
    }

}
