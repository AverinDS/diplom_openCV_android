package com.example.dmitry.diplom_averin.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.helper.CameraPermission;
import com.example.dmitry.diplom_averin.helper.FileSaver;
import com.example.dmitry.diplom_averin.helper.MethodML;
import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.presenter.Presenter;
import com.example.dmitry.diplom_averin.interfaces.IMyActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;

public class CameraMainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2, IMyActivity, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {


    private final int CAMERA_PERMISSION_CODE = 1;
    private CameraBridgeViewBase cameraViewCV;
    private TextView points;
    private String LOG_TAG = "CameraMainActivity";
    private Presenter presenter = new Presenter();
    private Mat bufer;
    private Bitmap bm = null;
    private ImageView image;
    private ProgressBar progressBar;
    private ToggleButton tButtonLr;
    private ToggleButton tButtonPerceptron;
    private ToggleButton tButtonMlpRgrsr;
    private ToggleButton tButtonMlpClsfr;
    private ImageButton btnTakePhoto;
    private boolean isScreenClicked = false;
    private boolean isCalculationWork = false;
    private MethodML method = MethodML.LinearRegression;


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
        btnTakePhoto = findViewById(R.id.activity_main_btn_take_photo);

        tButtonLr = findViewById(R.id.activity_main_toggbtn_lr);
        tButtonMlpClsfr = findViewById(R.id.activity_main_toggbtn_mlpclsfr);
        tButtonMlpRgrsr = findViewById(R.id.activity_main_toggbtn_mlprgsr);
        tButtonPerceptron = findViewById(R.id.activity_main_toggbtn_perceptron);

        tButtonPerceptron.setOnCheckedChangeListener(this);
        tButtonLr.setOnCheckedChangeListener(this);
        tButtonMlpClsfr.setOnCheckedChangeListener(this);
        tButtonMlpRgrsr.setOnCheckedChangeListener(this);
        btnTakePhoto.setOnClickListener(this);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                bm = uriToBitmap(result.getUri());
                Utils.bitmapToMat(bm, bufer);
                presenter.recogniseStart(bufer);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(LOG_TAG, result.getError().getMessage());
            }
        }
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
        if (isScreenClicked) {
            isScreenClicked = false;
            bufer = inputFrame.rgba();
            startRecognition();

        }
        return inputFrame.rgba();
    }

    @Override
    public void onReceived(List<Pair<Integer, Integer>> point) {
        Graphic.getInstance().pointsPredict.addAll(point);
        Log.i(LOG_TAG, String.valueOf(Graphic.getInstance().pointsPredict.size()));
        isCalculationWork = false;
        startActivityPredict();
    }

    @Override
    public void onFailureGettingData() {
        Toast.makeText(this, "Fail getting data from server", Toast.LENGTH_LONG).show();
        isCalculationWork = false;
    }

    @Override
    public void onCompleteSendData() {
        Toast.makeText(this, "Data send successfully! Wait, while it is calculating",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailureSendData() {
        Toast.makeText(this, "Fail sending data to server. Check Internet connection"
                , Toast.LENGTH_LONG).show();
        isCalculationWork = false;
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

        if (getResources().getBoolean(R.bool.DEBUG)) {
            String s = "Points:" + Graphic.getInstance().pointsTrain.size() + "\n" + pointsInfo;
            points.setText(s);
        }

        //sendToServer
        presenter.getPredictPoints(method);

    }

    @Override
    public void onFailureRecognise() {
        Toast.makeText(this, "Распознавание уже работает. Подождите немного",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "Click to Screen");
        progressBar.setVisibility(View.VISIBLE);
        isScreenClicked = true;
    }

    private void startRecognition() {
        if (isCalculationWork) {
            this.onFailureRecognise();
            return;
        }
        isCalculationWork = true;

        bm = Bitmap.createBitmap(bufer.width(),bufer.height(), Bitmap.Config.ARGB_8888);

        //First, we should crop image
        Utils.matToBitmap(bufer, bm);
        FileSaver fileSaver = new FileSaver();
        Uri uri = fileSaver.saveImg(bm,this);

        if (uri != null) {
            CropImage.activity(uri)
                    .start(this);
        }

        //in ActivityResult we continue recognition

    }

    public void startActivityPredict() {
        Intent intent = new Intent(CameraMainActivity.this, PredictActivity.class);
        startActivity(intent);
    }


    private void changeMethodML(boolean perceptron, boolean linRegression,
                                boolean mlpClsfr, boolean mlpRgrsr) {
        tButtonPerceptron.setChecked(perceptron);
        tButtonMlpRgrsr.setChecked(mlpRgrsr);
        tButtonMlpClsfr.setChecked(mlpClsfr);
        tButtonLr.setChecked(linRegression);
    }

    private void checkEmptyMethod(){
        if(!tButtonMlpRgrsr.isChecked() && !tButtonMlpClsfr.isChecked() && !tButtonLr.isChecked() &&
                !tButtonPerceptron.isChecked()) {
            method = MethodML.LinearRegression;
            changeMethodML(false,true,false,false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(compoundButton.equals(tButtonLr)&& isChecked) {
            method = MethodML.LinearRegression;
            changeMethodML(false,true,false,false);
            return;
        }
        if(compoundButton.equals(tButtonMlpClsfr)&& isChecked) {
            method = MethodML.MLPClassifier;
            changeMethodML(false,false,true,false);
            return;
        }
        if(compoundButton.equals(tButtonMlpRgrsr)&& isChecked) {
            method = MethodML.MLPRegressor;
            changeMethodML(false,false,false,true);
            return;
        }
        if(compoundButton.equals(tButtonPerceptron)&& isChecked) {
            method = MethodML.Perceptron;
            changeMethodML(true,false,false,false);
            return;
        }

        checkEmptyMethod();
    }

    private Bitmap uriToBitmap(Uri uri){
        try {
           return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

