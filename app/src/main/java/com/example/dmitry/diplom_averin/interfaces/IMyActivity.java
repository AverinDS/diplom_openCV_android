package com.example.dmitry.diplom_averin.interfaces;

import android.graphics.Bitmap;

import com.example.dmitry.diplom_averin.helper.CameraPermission;

/**
 * Created by dmitry on 05.02.18.
 */

public interface IMyActivity {
    void onReceived();//for getting data from server
    void cameraPermission(CameraPermission success);
    void messageToUser(String message);
    void updateUI(Bitmap bm, String pointsInfo);
    void onFailureRecognise();
}
