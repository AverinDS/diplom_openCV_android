package com.example.dmitry.diplom_averin.presenter;


import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import com.example.dmitry.diplom_averin.helper.CameraHelper;
import com.example.dmitry.diplom_averin.model.businessLogic.Recognition;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.interfaces.IMyPresenter;
import com.example.dmitry.diplom_averin.interfaces.IMyActivity;
import com.example.dmitry.diplom_averin.model.repository.GraphicRepository;

import org.opencv.core.Mat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dmitry on 05.02.18.
 */

public class Presenter implements IMyPresenter {

    private final String LOG_TAG = "Presenter";
    private CameraHelper cameraHelper = new CameraHelper();
    private IMyActivity activity;
    private Recognition recognition = new Recognition(this);
    private boolean recogniseInWork = false;
    private GraphicRepository repository = new GraphicRepository();

    public void attachView(IMyActivity attaching_view) {
        this.activity = attaching_view;
    }

    public void detachView() {
        this.activity = null;
    }

    public void getCameraPermission(int CAMERA_PERMISSION_CODE) {
        activity.cameraPermission(cameraHelper.getCameraPermission(this.activity, CAMERA_PERMISSION_CODE));
    }

    public void recogniseStart(Mat sampleImage) {
        if (!recogniseInWork) {
            recogniseInWork = true;
            Graphic.getInstance().pointsTrain.clear();
            Graphic.getInstance().pointsPredict.clear();
            Log.d(LOG_TAG, "recognise start");
            recognition.recognise(sampleImage);
        } else {
            Log.d(LOG_TAG, "recognise abort");
            activity.onFailureRecognise();
        }
    }

    @Override
    public void recogniseOnComplete(Bitmap bm) {
        Log.d(LOG_TAG, "recognise complete");
        recogniseInWork = false;

        //generate debug data with coordinates of lines
        StringBuilder stringBuilderPoints = new StringBuilder();
        for (Pair<Double, Double> i : Graphic.getInstance().pointsTrain) {
            stringBuilderPoints.append("\n (")
                    .append(i.first.toString())
                    .append(":")
                    .append(i.second.toString())
                    .append(")");
        }

        activity.updateUI(bm, stringBuilderPoints.toString());
    }

    public void getPredictPoints() {
        repository.getPredictPoints()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pointsObj -> activity.onReceived(pointsObj),
                        throwable -> activity.onFailureGettingData());
    }
}
