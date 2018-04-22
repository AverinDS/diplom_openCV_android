package com.example.dmitry.diplom_averin.interfaces;

import android.graphics.Bitmap;
import android.util.Pair;

import com.example.dmitry.diplom_averin.helper.CameraPermission;
import com.example.dmitry.diplom_averin.rest.GraphicRest;

import java.util.List;

/**
 * Интерфейс для активити для взаимодействия с презентером
 * Created on 05.02.18
 * @author AverinDmitry
 *
 */
public interface IMyActivity {
    void onReceived(List<Pair<Integer,Integer>> point);//for getting data from server
    void onFailureGettingData();

    void onCompleteSendData();//for sending data to server
    void onFailureSendData();


    void cameraPermission(CameraPermission success);
    void messageToUser(String message);
    void updateUI(Bitmap bm, String pointsInfo);
    void onFailureRecognise();
}
