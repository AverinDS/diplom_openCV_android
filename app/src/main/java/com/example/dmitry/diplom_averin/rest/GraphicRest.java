package com.example.dmitry.diplom_averin.rest;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dmitry on 02.03.18.
 */

public class GraphicRest {

    @SerializedName("points")
    private List<Pair<Double,Double>> points;

    public List<Pair<Double, Double>> getPoints() {
        return points;
    }


}
