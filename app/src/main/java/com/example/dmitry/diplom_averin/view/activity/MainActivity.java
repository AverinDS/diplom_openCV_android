package com.example.dmitry.diplom_averin.view.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.model.businessLogic.Recognition;
import com.example.dmitry.diplom_averin.presenter.Presenter;
import com.example.dmitry.diplom_averin.view.IView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2, IView {


    private final int CAMERA_PERMISSION_CODE = 1;
    private CameraBridgeViewBase cameraViewCV;
    private String LOG_TAG = "MainActivity";
    private Presenter presenter = new Presenter();
    private Mat bufer;
    ImageView image;
    public Bitmap bm;

    Recognition rec = new Recognition();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Log.i(LOG_TAG, "onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        image = findViewById(R.id.activity_main_image_view);

        findViewById(R.id.activity_main_camera_view).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Mat mat = rec.recognise(bufer);
                        bm = Bitmap.createBitmap(mat.cols(), mat.rows(),Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(mat, bm);
                        image.setImageBitmap(bm);
                    }
                });

        presenter.attachView(this);
        presenter.getCameraPermission(CAMERA_PERMISSION_CODE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraViewCV != null) {
            cameraViewCV.disableView();
        }
        presenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initDebug();
        if (cameraViewCV != null) {
            cameraViewCV.enableView();
        }
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (cameraViewCV != null) {
            cameraViewCV.disableView();
        }
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult work");
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Permission of camera is grant");
                    this.cameraPermission(true);
                } else {
                    Log.i(LOG_TAG, "Permission of camera is not grant");
                    this.cameraPermission(false);
                }
                break;
            }

        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        bufer = inputFrame.rgba();
        return inputFrame.rgba();
    }

    @Override
    public void onReceived() {

    }

    @Override
    public void cameraPermission(boolean success) {
        Log.i(LOG_TAG, "success camera permission: "+String.valueOf(success));
        if(success) {
            cameraViewCV = (CameraBridgeViewBase)findViewById(R.id.activity_main_camera_view);
            cameraViewCV.setVisibility(View.VISIBLE);
            cameraViewCV.setCvCameraViewListener(this);
        } else {
            Toast.makeText(this,"Camera permission denied",Toast.LENGTH_LONG).show();
            //do i need smth anymore in this case?
        }

    }

    @Override
    public void messageToUser(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }



}
