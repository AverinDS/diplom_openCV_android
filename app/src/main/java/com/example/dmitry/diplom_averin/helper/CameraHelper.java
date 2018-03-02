package com.example.dmitry.diplom_averin.helper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dmitry.diplom_averin.interfaces.IMyActivity;

/**
 * Created by DELL on 11.02.2018.
 */

public class CameraHelper {
    public CameraPermission getCameraPermission(IMyActivity view, int CAMERA_PERMISSION_CODE) {

        //check permissions
        if (ContextCompat.checkSelfPermission((AppCompatActivity) view, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

//            if need the exploration for user
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) view,
                    Manifest.permission.CAMERA)) { //should i explain main functional of app?
            } else {
//                No exploration need, we can request permission
                ActivityCompat.requestPermissions((AppCompatActivity) view,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                return CameraPermission.QUERY;
            }
        }
        return CameraPermission.PERMIT; //if camera is permitted for using
    }

}
