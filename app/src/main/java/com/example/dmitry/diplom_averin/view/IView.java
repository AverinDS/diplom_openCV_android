package com.example.dmitry.diplom_averin.view;

/**
 * Created by dmitry on 05.02.18.
 */

public interface IView {
    void onReceived();//for getting data from server
    void cameraPermittion(boolean success);
    void messageToUser(String message);
}
