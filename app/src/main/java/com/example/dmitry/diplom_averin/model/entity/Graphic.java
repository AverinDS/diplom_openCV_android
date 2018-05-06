package com.example.dmitry.diplom_averin.model.entity;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public void sortPoints() {
       // Collections.sort(this.pointsTrain, (a,b)-> a.first - b.first );
//        int max = 0;
//        List<Pair<Integer, Integer>> deleting = new ArrayList<>();
//        for (Pair<Integer, Integer> i: this.pointsTrain) {
//            if (i.first < max) {
//                deleting.add(i);
//            } else {
//                max = i.first;
//            }
//        }
//
//        for (Pair<Integer,Integer> i: deleting) {
//            pointsTrain.remove(i);
//        }
    }
}
