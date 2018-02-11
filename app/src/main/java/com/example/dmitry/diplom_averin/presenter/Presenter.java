package com.example.dmitry.diplom_averin.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dmitry.diplom_averin.helper.CameraHelper;
import com.example.dmitry.diplom_averin.view.IView;

/**
 * Created by dmitry on 05.02.18.
 */

public class Presenter {

    private final String LOG_TAG = "Presenter";
    private CameraHelper cameraHelper = new CameraHelper();
    private IView view;

    public void attachView(IView attaching_view) {
        this.view = attaching_view;
    }

    public void detachView() {
        this.view = null;
    }

    public void getCameraPermission(int CAMERA_PERMISSION_CODE) {
        view.cameraPermission(cameraHelper.getCameraPermission(this.view, CAMERA_PERMISSION_CODE));
    }

}
