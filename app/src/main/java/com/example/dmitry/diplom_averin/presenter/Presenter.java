package com.example.dmitry.diplom_averin.presenter;


import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.helper.CameraHelper;
import com.example.dmitry.diplom_averin.helper.MethodML;
import com.example.dmitry.diplom_averin.model.businessLogic.Recognition;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.interfaces.IMyPresenter;
import com.example.dmitry.diplom_averin.interfaces.IMyActivity;
import com.example.dmitry.diplom_averin.model.repository.GraphicRepository;

import org.opencv.core.Mat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter для CameraMainActivity
 * Created 05.02.18.
 * @author Averin Dmitry
 */
public class Presenter implements IMyPresenter {

    /**
     * Лог тэг для логов
     */
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


        //save recognise graphic
        Graphic.getInstance().recogniseGraphic = bm;
        Graphic.getInstance().sortPoints();

        //generate debug data with coordinates of lines
        StringBuilder stringBuilderPoints = new StringBuilder();
        for (Pair<Integer, Integer> i : Graphic.getInstance().pointsTrain) {
            stringBuilderPoints.append("\n (")
                    .append(i.first.toString())
                    .append(":")
                    .append(i.second.toString())
                    .append(")");
        }

        recogniseInWork = false;
        activity.updateUI(bm, stringBuilderPoints.toString());
    }

    public void getPredictPoints(MethodML methodML) {
        repository.getPredictPoints(methodML)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pointsObj -> activity.onReceived(pointsObj),
                        throwable -> {
                    if (throwable.getMessage() != null) {
                        Log.e(LOG_TAG, throwable.getMessage());
                    } else {
                        Log.e(LOG_TAG, "Фигня какая-то...");
                    }
                    activity.onFailureGettingData();
                        });
    }

    public void incValSensitivity(EditText valSensitivity, AppCompatActivity context) {

        int maxSensivity = context.getResources().getInteger(R.integer.max_sensitivity);

        if (Integer.parseInt(valSensitivity.getText().toString()) < maxSensivity) {
            valSensitivity.setText(String.valueOf(Integer.parseInt(valSensitivity.getText().toString())+1));
            setSentivity(Integer.parseInt(valSensitivity.getText().toString())+1, context);
        }
    }

    public void decrValSensitivity(EditText valSensitivity, AppCompatActivity context) {

        int minSensivity = context.getResources().getInteger(R.integer.min_sensitivity);

        if (Integer.parseInt(valSensitivity.getText().toString()) > minSensivity) {
            valSensitivity.setText(String.valueOf(Integer.parseInt(valSensitivity.getText().toString())-1));
        }
        setSentivity(Integer.parseInt(valSensitivity.getText().toString())-1, context);
    }

    //convert sensitivity to Graphic threshold
    private void setSentivity(int sens, AppCompatActivity context) {
        Graphic.getInstance().sensitivityOfRecognition =
                context.getResources().getInteger(R.integer.max_sensitivity) - sens;
    }
}
