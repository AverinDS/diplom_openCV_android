package com.example.dmitry.diplom_averin.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Основное активити, которое взаимодействует с камерой
 * @author Averin Dmitry
 */
public class CameraMainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2, IMyActivity, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,RadioGroup.OnCheckedChangeListener {


    /**
     * Код для определения, что разрешение пришло именно она камеру
     */
    private final int CAMERA_PERMISSION_CODE = 1;

    /**
     * View, которое отображает камеру
     */
    private CameraBridgeViewBase cameraViewCV;

    /**
     * Этот текстовое View отображает в себе координаты растознанных точек, в виде [чб]
     */
    private TextView points;

    /**
     * Лог тэг
     */
    private String LOG_TAG = "CameraMainActivity";

    /**
     * Объект связанного с этой activity презентера
     * @see Presenter
     */
    private Presenter presenter = new Presenter();

    /**
     * Буферная переменная для сохранения изображения в виде Mat для возможности работы с ним
     */
    private Mat bufer;

    /**
     * Битовая карта для промежуточного сохранения распознанного изображения
     */
    private Bitmap bm = null;

    /**
     * View для отображения распознанного
     */
    private ImageView image;

    /**
     * ProgressBar для индикации работы распознавания и анализа изображения
     */
    private ProgressBar progressBar;

    /**
     * ImageButton как кнопка для запуска распознавания
     */
    private ImageButton btnTakePhoto;

    /**
     * Флаг, показывающий, произошло нажатие на экран или нет
     * (служит, для сохранения изображения с камеры во временную память)
     */
    private boolean isScreenClicked = false;

    /**
     * Флаг, показывающий, запущено ли распознавание и анализ графика или нет
     * (служит ограничителем на запуск процесссов распознавания и анализа(не более одного))
     */
    private boolean isCalculationWork = false;

    /**
     * Переменная, показываеющая, какой метод выбран для анализа
     */
    private MethodML method = MethodML.LinearRegression;

    /**
     * Тулбар необходим для корректной работы navigationDrawer
     */
    private Toolbar toolbar = null;

    /**
     * layout для navigationDrawer
     */
    private DrawerLayout drawer = null;

    /**
     * Toggle Для navigationDrawer
     */
    private ActionBarDrawerToggle toggle = null;

    /**
     * NavigationView - "шторка"
     */
    private NavigationView navigationView = null;

    /**
     * Группа RadioButtons для возможности выбора метода анализа
     */
    private RadioGroup radioGroupMl = null;

    /**
     * Кнопка для возможности инкрементирования значения чувствительности распознавания
     */
    private Button btnPlus = null;

    /**
     * Кнопка для возможности декрементирования значения чувствительности распознавания
     */
    private Button btnMinus = null;

    /**
     * EditText для отображения значения чуствительности
     */
    private EditText sensitivityVal = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Log.i(LOG_TAG, "onCreate");

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        points = findViewById(R.id.activity_main_points);
        image = findViewById(R.id.activity_main_image_view);
        progressBar = findViewById(R.id.actMainProgressBar);
        btnTakePhoto = findViewById(R.id.activity_main_btn_take_photo);

        radioGroupMl = navigationView.getMenu().findItem(R.id.nav_drawer_list_methods_ml)
                .getActionView().findViewById(R.id.nav_drawer_rg_ml_list);
        btnPlus = navigationView.getMenu().findItem(R.id.nav_drawer_sensitivity)
                .getActionView().findViewById(R.id.nav_drawer_plus);
        btnMinus = navigationView.getMenu().findItem(R.id.nav_drawer_sensitivity)
                .getActionView().findViewById(R.id.nav_drawer_minus);
        sensitivityVal = navigationView.getMenu().findItem(R.id.nav_drawer_sensitivity)
                .getActionView().findViewById(R.id.nav_drawer_text_sensitivity);

        btnTakePhoto.setOnClickListener(this);
        radioGroupMl.setOnCheckedChangeListener(this);

        btnPlus.setOnClickListener( v -> presenter.incValSensitivity(sensitivityVal, this));
        btnMinus.setOnClickListener(v -> presenter.decrValSensitivity(sensitivityVal, this));

        presenter.attachView(this);
        presenter.getCameraPermission(CAMERA_PERMISSION_CODE);

        //setting NavigationDrawer
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbLinearRegression: {
                method = MethodML.LinearRegression;
                return;
            }
            case R.id.rbMlpClassifier: {
                method = MethodML.MLPClassifier;
                return;
            }
            case R.id.rbMlpRegressor: {
                method = MethodML.MLPRegressor;
                return;
            }
            case R.id.rbPerceptron: {
                method = MethodML.Perceptron;
                return;
            }
            case R.id.rbCustomNetwork: {
                method = MethodML.CustomNetwork;
                return;
            }
            case R.id.rbFbLib: {
                method = MethodML.FbLib;
                return;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void startRecognition() {
        if (isCalculationWork) {
            this.onFailureRecognise();
            return;
        }
        isCalculationWork = true;

        bm = Bitmap.createBitmap(bufer.width(), bufer.height(), Bitmap.Config.ARGB_8888);

        //First, we should crop image
        Utils.matToBitmap(bufer, bm);
        FileSaver fileSaver = new FileSaver();
        Uri uri = fileSaver.saveImg(bm, this);

        if (uri != null) {
            CropImage.activity(uri)
                    .start(this);
        }

        //in ActivityResult we continue recognition
    }

    /**
     * Запускает activityPredict
     */
    public void startActivityPredict() {
        Intent intent = new Intent(CameraMainActivity.this, PredictActivity.class);
        startActivity(intent);
    }

    /**
     * Конвертирует uri-файл в Bitmap
     * @param uri uri до файла
     * @return Bitmap изображения
     */
    private Bitmap uriToBitmap(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}

