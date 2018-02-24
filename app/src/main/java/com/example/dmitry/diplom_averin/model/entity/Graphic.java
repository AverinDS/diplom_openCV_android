package com.example.dmitry.diplom_averin.model.entity;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 11.02.2018.
 */

public class Graphic {
    private static Graphic INSTANCE = new Graphic();

    private Graphic() {
    }

    public static Graphic getInstance() {
        return INSTANCE;
    }

    public List<Pair<Double, Double>> pointsTrain = new ArrayList<>();
    public List<Pair<Double, Double>> pointsPredict = new ArrayList<>();
}
