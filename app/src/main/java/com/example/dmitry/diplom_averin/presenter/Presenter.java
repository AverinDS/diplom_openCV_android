package com.example.dmitry.diplom_averin.presenter;


import android.graphics.Bitmap;
import android.util.Pair;

import com.example.dmitry.diplom_averin.helper.CameraHelper;
import com.example.dmitry.diplom_averin.model.businessLogic.Recognition;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.interfaces.IMyPresenter;
import com.example.dmitry.diplom_averin.interfaces.IMyActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by dmitry on 05.02.18.
 */

public class Presenter implements IMyPresenter {

    private final String LOG_TAG = "Presenter";
    private CameraHelper cameraHelper = new CameraHelper();
    private IMyActivity activity;
    private Recognition recognition = new Recognition(this);
    private boolean recogniseInWork = false;

    public void attachView(IMyActivity attaching_view) {
        this.activity = attaching_view;
    }

    public void detachView() {
        this.activity = null;
    }

    public void getCameraPermission(int CAMERA_PERMISSION_CODE) {
        activity.cameraPermission(cameraHelper.getCameraPermission(this.activity, CAMERA_PERMISSION_CODE));
    }

    public void recogniseStart(Mat bufer) {
        if (!recogniseInWork) {
            recogniseInWork = true;
            recognition.recognise(bufer);
        } else {
            activity.onFailureRecognise();
        }
    }

    @Override
    public void recogniseOnComplete(Mat mat) {
        recogniseInWork = false;
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);

        //generate debug data with coordinates of lines
        StringBuilder stringBuilderPoints = new StringBuilder();
        for (Pair<Double, Double> i : Graphic.getInstance().pointsTrain) {
            stringBuilderPoints.append("\n (")
                    .append(i.first.toString())
                    .append(":")
                    .append(i.second.toString())
                    .append(")");
        }
         ;

        activity.updateUI(bm, stringBuilderPoints.toString());
    }
}
