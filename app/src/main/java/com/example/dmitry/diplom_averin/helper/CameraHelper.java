package com.example.dmitry.diplom_averin.helper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dmitry.diplom_averin.interfaces.IMyActivity;

/**
 * Created on 11.02.2018.
 * @author Averin Dmitry
 * Класс-помощник для взаимодействия с камерой
 */
public class CameraHelper {

    /**
     * Функция для запроса permission на камеру
     * @param activity Activity в которое придёт значание Permission при запросе
     * @param CAMERA_PERMISSION_CODE код, который описывает permission
     * @return Результат запроса Permission, как одно значение из перечисления CameraPermission
     * @see CameraPermission
     */
    public CameraPermission getCameraPermission(IMyActivity activity, int CAMERA_PERMISSION_CODE) {

        //check permissions
        if (ContextCompat.checkSelfPermission((AppCompatActivity) activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

//            if need the exploration for user
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) activity,
                    Manifest.permission.CAMERA)) { //should i explain main functional of app?
            } else {
//                No exploration need, we can request permission
                ActivityCompat.requestPermissions((AppCompatActivity) activity,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                return CameraPermission.QUERY;
            }
        }
        return CameraPermission.PERMIT; //if camera is permitted for using
    }

}
