package com.example.dmitry.diplom_averin.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dmitry.diplom_averin.view.IView;

/**
 * Created by dmitry on 05.02.18.
 */

public class Presenter {
    private final int CAMERA_PERMISSION_CODE = 1;
    private final String LOG_TAG = "Presenter";

    private IView view;

    public void attachView(IView attaching_view) {
        this.view = attaching_view;
    }

    public void detachView() {
        this.view = null;
    }

    public void getCameraPermission() {

        //check permissions
        if (ContextCompat.checkSelfPermission((AppCompatActivity) this.view, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            //if need the exploration for user
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) this.view,
                    Manifest.permission.CAMERA)) {
                view.cameraPermittion(false);
            } else {

                //No exploration need, we can request permission

                ActivityCompat.requestPermissions((AppCompatActivity) this.view,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }

        } else {
            //if camera is permitted for using
            view.cameraPermittion(true);
        }

    }


    public void hasGotCameraPermission(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Permission of camera is grant");
                    this.view.cameraPermittion(true);
                } else {
                    Log.i(LOG_TAG, "Permission of camera is not grant");
                    this.view.cameraPermittion(false);
                }
                break;
            }

        }
    }
}
