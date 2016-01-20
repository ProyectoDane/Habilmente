package com.belatrix.apadea.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static final String JSON_RESOURCE_FILE = "datos.json";

    public static String fetchJsonFromFile(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(JSON_RESOURCE_FILE);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static Bitmap convertBytesToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static int getImageColor(int index) {

        int color = index % 4;
        int hexColor;

        switch (color) {

            case 0:
                hexColor = 0xFFFFBA83;
                break;
            case 1:
                hexColor = 0xFF37474B;
                break;

            case 2:
                hexColor = 0xFF05161A;
                break;

            default:
                hexColor = 0xFFC85900;
                break;
        }

        return hexColor;
    }
}
