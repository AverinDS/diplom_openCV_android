package com.example.dmitry.diplom_averin.view.activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.helper.DrawerPoints;

public class PredictActivity extends AppCompatActivity {

    View drawerView;
    ImageView recogniceGraphic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        drawerView = findViewById(R.id.activity_predict_drawerView);
        recogniceGraphic = findViewById(R.id.activity_predict_image_view);

        drawPredict();
    }

    private void drawPredict() {
        DrawerPoints drawer = new DrawerPoints(drawerView.getContext());
        setContentView(drawer);

    }
}
