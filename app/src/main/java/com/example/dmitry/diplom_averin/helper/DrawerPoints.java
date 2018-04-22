package com.example.dmitry.diplom_averin.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.dmitry.diplom_averin.R;
import com.example.dmitry.diplom_averin.model.entity.Graphic;

/**
 * Класс-отрисовки точек на View
 * Created on 28.03.18.
 * @author Averin Dmitry
 */
public class DrawerPoints extends View {
    private final int STROKE_WIDTH = 10;
    private final int TEXT_SIZE = 48;

    private Paint paintPredict;
    private Paint paintRecognice;
    private Paint paintText;
    private Paint paintBackground;

    private int widthScreen = 0;
    private int heightScreen = 0;


    public DrawerPoints(Context context) {
        super(context);
        init();
    }

    private void init() {
        paintPredict = new Paint();
        paintRecognice = new Paint();
        paintText = new Paint();
        paintBackground = new Paint();

        paintPredict.setColor(Color.GREEN);
        paintRecognice.setColor(Color.BLUE);
        paintText.setColor(Color.YELLOW);
        paintBackground.setColor(Color.BLACK);

        paintPredict.setStrokeWidth(STROKE_WIDTH);
        paintRecognice.setStrokeWidth(STROKE_WIDTH);
        paintText.setStrokeWidth(STROKE_WIDTH);

        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(TEXT_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        heightScreen = canvas.getHeight();
        widthScreen = canvas.getWidth();

        canvas.drawPaint(paintBackground);
        canvas.drawBitmap(Graphic.getInstance().recogniseGraphic,0,0,paintRecognice);

        canvas.drawText(getResources().getString(R.string.message_to_back),
                0,heightScreen - 10, paintText);
        canvas.drawPoints(Graphic.getInstance().getPredictForDrawing(), paintPredict);

    }
}
