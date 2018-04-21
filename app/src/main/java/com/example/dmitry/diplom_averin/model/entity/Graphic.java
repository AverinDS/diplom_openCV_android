package com.example.dmitry.diplom_averin.model.entity;

import android.graphics.Bitmap;
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

    public List<Pair<Integer, Integer>> pointsTrain = new ArrayList<>();
    public List<Pair<Integer, Integer>> pointsPredict = new ArrayList<>();

    public Bitmap recogniseGraphic;

    public int sensitivityOfRecognition = 50;

    //it is need for drawing lines
    public float[] getPredictForDrawing() {

        float[] points = new float[pointsPredict.size()*2]; // *2 because list is List<Pair>

        int index = 0;
        for(Pair<Integer,Integer> pair: pointsPredict) {
            points[index] = pair.first;
            index++;
            points[index] = pair.second;
            index++;
        }

        return points;
    }
}
