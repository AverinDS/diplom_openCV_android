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
 * "Хранилище" данных. из этого класса вызываются методы получения данных с сервера
 * @author Awerin Dmitry
 * Created on 02.03.18.
 */
public class GraphicRepository {
    /**
     * вызов объекта класса типа синглтон для получения доступа к методам загрузки данных
     */
    private IRestService restService = RestServiceProvider.newInstance().getRestService();

    /**
     * метод возвращающий предсказанные точки с сервера
     * @param methodML Метод анализа, выбранный пользователем
     * @return Список точек (x,y) предсказанных
     */
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
