package com.example.dmitry.diplom_averin.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.support.v4.content.FileProvider.getUriForFile;

public class FileSaver {

    public Uri saveImg(Bitmap bm, Context context) {
        String fileName = "111111.jpg";

        FileOutputStream fileOutputStream = null;
        File path = new File(context.getFilesDir().toString());
//        File path = new File(context.getFilesDir(), "images");
        File file = new File(path, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } finally { //close stream
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Uri.fromFile(file);
//        return getUriForFile(context,context.getPackageName(), file);
    }
}
