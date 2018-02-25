package com.example.dmitry.diplom_averin.model.businessLogic;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.dmitry.diplom_averin.model.entity.Graphic;
import com.example.dmitry.diplom_averin.interfaces.IMyPresenter;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by DELL on 11.02.2018.
 */

public class Recognition {

    private IMyPresenter presenter;

    public Recognition(IMyPresenter _presenter) {
        presenter = _presenter;
    }

    public void recognise(Mat sampledImage) {
        new FindLines().execute(sampledImage);
    }

    private class FindLines extends AsyncTask<Mat, Void, Bitmap> {
        private String LOG_TAG = "FindLines(asyncTask)";

        @Override
        protected Bitmap doInBackground(Mat... sampledImage) {
            Log.d(LOG_TAG, "doInBackground start");

            Mat binaryImage=new	Mat();
            Imgproc.cvtColor(sampledImage[0], binaryImage, Imgproc.COLOR_RGB2GRAY);
            Imgproc.Canny(binaryImage, binaryImage, 80, 100);
            Mat	lines = new Mat();

            //sensitive of recognition 50
            int	threshold = 50;

            Log.d(LOG_TAG, "FindLines");
            Imgproc.HoughLinesP(binaryImage, lines,	1, Math.PI/180, threshold);

            Imgproc.cvtColor(binaryImage, binaryImage, Imgproc.COLOR_GRAY2RGB);

            Log.d(LOG_TAG, "Save result of points:" + lines.cols());
            for (int i = 0; i < lines.cols(); i++)
            {
                double[] line = lines.get(0, i);
                double xStart = line[0],
                        yStart = line[1],
                        xEnd = line[2],
                        yEnd = line[3];

                //remember recognised points to Graphic class
                Graphic.getInstance().pointsTrain.add(new Pair<>(xStart, yStart));
                Graphic.getInstance().pointsTrain.add(new Pair<>(xEnd, yEnd));


                org.opencv.core.Point lineStart = new org.opencv.core.Point(xStart, yStart);
                org.opencv.core.Point lineEnd = new org.opencv.core.Point(xEnd,	yEnd);
                Core.line(binaryImage, lineStart, lineEnd, new Scalar(0,0,255),	3);
            }

            Bitmap bm = Bitmap.createBitmap(binaryImage.cols(), binaryImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(binaryImage, bm);
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            Log.d(LOG_TAG, "doInBackground complete");
            presenter.recogniseOnComplete(bm);
        }
        //may be i should add OnCancelled?
    }
}
