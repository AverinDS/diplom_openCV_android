package com.example.dmitry.diplom_averin.model.entity;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

/**
 * Класс сущности "график" реализованный в single tone
 * Created on 11.02.2018.
 *
 * @author Averin Dmitry
 */
public class Graphic {
    /**
     * Instanse для реализации single ton
     */
    private static Graphic INSTANCE = new Graphic();

    /**
     * Конструктор пустой
     */
    private Graphic() {
    }

    /**
     * Gets instance.
     *
     * @return Возвращает единственный в системе экземпляр класса Graphic
     */
    public static Graphic getInstance() {
        return INSTANCE;
    }

    /**
     * Список кортежей распознанных точек на графике
     */
    public List<Pair<Integer, Integer>> pointsTrain = new ArrayList<>();

    /**
     * Список предсказанных точек графика
     */
    public List<Pair<Integer, Integer>> pointsPredict = new ArrayList<>();

    /**
     * Битовая карта распознанного графика
     */
    public Bitmap recogniseGraphic;

    /**
     * Переменная, отвечающая за чувствительность распознавания графика. По умолчанию равна 50
     */
    public int sensitivityOfRecognition = 50;

    /**
     * Метод для преобразования списка кортежей предсказанных точек в массив типа float
     *
     * @return Предсказанные точки в виде массива типа float
     * @see Graphic#pointsPredict Graphic#pointsPredictGraphic#pointsPredict
     */
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

    public void prepareData() {

        List<Pair<Integer, Integer>> normalPoints = new ArrayList<>();
//
        Collections.sort(pointsTrain, ((o1, o2) ->  o1.first - o2.first));

//        int sum = 0;
//        int count = 0;
//        int currentX = pointsTrain.get(0).first;
//
//        for (Pair<Integer, Integer> item: pointsTrain) {
//            if (currentX == item.first) {
//                sum += item.second;
//                count += 1;
//            }
//            else {
//                normalPoints.add(new Pair<>(currentX, sum / count));//возможно окруление!
//                sum = item.first;
//                currentX = item.first;
//                count = 1;
//            }
//        }
//
//        pointsTrain = normalPoints;

    }
}
