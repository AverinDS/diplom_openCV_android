package com.example.dmitry.diplom_averin.rest;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Класс для сериализации в форме данных
 * Created  on 02.03.18.
 * @author Averin Dmitry
 */

public class GraphicRest {

    public GraphicRest(List<Pair<Integer,Integer>> list) {
        points = list;
    }

    @SerializedName("points")
    private List<Pair<Integer,Integer>> points;

    public List<Pair<Integer, Integer>> getPoints() {
        return points;
    }

    public void setPoints(List<Pair<Integer,Integer>> _points) {
        points = _points;
    }


}
