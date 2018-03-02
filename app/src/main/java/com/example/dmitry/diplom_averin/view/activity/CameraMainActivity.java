package com.example.dmitry.diplom_averin.view.activity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.helper.CameraPermission;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.presenter.Presenter;
import com.example.dmitry.diplom_averin.interfaces.IMyActivity;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class CameraMainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2, IMyActivity, View.OnClickListener {


    private final int CAMERA_PERMISSION_CODE = 1;
    private CameraBridgeViewBase cameraViewCV;
    private TextView points;
    private String LOG_TAG = "CameraMainActivity";
    private Presenter presenter = new Presenter();
    private Mat bufer;
    private ImageView image;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Log.i(LOG_TAG, "onCreate");

        image = findViewById(R.id.activity_main_image_view);
        points = findViewById(R.id.activity_main_points);
        progressBar = findViewById(R.id.actMainProgressBar);

        findViewById(R.id.activity_main_camera_view).setOnClickListener(this);

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
                    this.cameraPermission(CameraPermission.PERMIT);
                } else {
                    Log.i(LOG_TAG, "Permission of camera is not grant");
                    this.cameraPermission(CameraPermission.NOTPERMIT);
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
    public void cameraPermission(CameraPermission success) {
        Log.i(LOG_TAG, "success camera permission: " + String.valueOf(success));
        switch (success) {
            case PERMIT: {
                cameraViewCV = findViewById(R.id.activity_main_camera_view);
                cameraViewCV.setVisibility(View.VISIBLE);
                cameraViewCV.setCvCameraViewListener(this);
                break;
            }
            case NOTPERMIT: {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG)
                        .show();
                break;
            }
            case QUERY: {
                //nothing to do. Wait onRequestPermissionResult
                break;
            }
        }

    }


    @Override
    public void messageToUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateUI(Bitmap bm, String pointsInfo) {
        Log.d(LOG_TAG, "upfateUI work");
        progressBar.setVisibility(View.INVISIBLE);
        image.setImageBitmap(bm);
        String s = "Points:" + Graphic.getInstance().pointsTrain.size() + "\n" + pointsInfo;
        points.setText(s);

    }

    @Override
    public void onFailureRecognise() {
        Log.d(LOG_TAG, "OnFailRecognise");
        Toast.makeText(this, "Распознавание уже работает. Подождите немного",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "Click to Screen");

        progressBar.setVisibility(View.VISIBLE);
        presenter.recogniseStart(bufer);
    }

}

