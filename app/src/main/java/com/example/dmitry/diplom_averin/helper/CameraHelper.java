package com.example.dmitry.diplom_averin.helper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dmitry.diplom_averin.view.IView;

/**
 * Created by DELL on 11.02.2018.
 */

public class CameraHelper {
    public boolean getCameraPermission(IView view, int CAMERA_PERMISSION_CODE) {

        //check permissions
        if (ContextCompat.checkSelfPermission((AppCompatActivity) view, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

//            if need the exploration for user
//            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) view,
//                    Manifest.permission.CAMERA)) {
//
//
//            } else {
                //No exploration need, we can request permission

                ActivityCompat.requestPermissions((AppCompatActivity) view,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                return (ContextCompat.checkSelfPermission((AppCompatActivity) view, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED);

//            }

        }
        return true; //if camera is permitted for using
    }

}
