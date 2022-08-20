package com.robusta.Other;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.robusta.photoweather.UI.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Config {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static boolean checkLocationPermission
            (Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    public static String getAPIKeyFromMetaData(Context context) {
        String apiKey = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                apiKey = bundle.getString("org.openweathermap.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    public static String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeStringToBitmap(String s) {
        byte[] imageAsBytes = Base64.decode(s.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    public static boolean addImageToHistoryArray(String encodedBitmap) {
        if (encodedBitmap.isEmpty())
            return false;

        Set<String> list = SplashScreen.sharedPreferences.getStringSet(SplashScreen.HISTORY_LIST_TAG, null);
        Set<String> newList = new TreeSet<>();
        if (list != null) {
            newList.addAll(list);
        }
        newList.add(encodedBitmap);
        SplashScreen.editor.putStringSet(SplashScreen.HISTORY_LIST_TAG, newList);
        SplashScreen.editor.apply();
        return true;

    }


    public static void ShareImage(Context context, Bitmap bitmap) {
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.robusta.photoweather.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

}
