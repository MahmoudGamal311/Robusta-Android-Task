package com.robusta.photoweather.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.robusta.photoweather.Data.remote.APIService;
import com.robusta.photoweather.Data.remote.ApiUtils;
import com.robusta.photoweather.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SplashScreen extends AppCompatActivity {

    public static Context context;
    public static APIService mService;


    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String HISTORY_LIST_TAG = "historyArrayList";
//    public static Set<String> History;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mService = ApiUtils.getApiService();
        context = getApplicationContext();

        sharedPreferences = getSharedPreferences("localeData", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        History = sharedPreferences.getStringSet(SplashScreen.HISTORY_LIST_TAG, new HashSet<String>());

        finishSplashAfterDelay();

    }


    private void finishSplashAfterDelay() {
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashScreen.this, HomeScreen.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();

        }, SPLASH_DISPLAY_LENGTH);
    }

}