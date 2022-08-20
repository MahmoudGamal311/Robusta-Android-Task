package com.robusta.photoweather.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.robusta.photoweather.Data.remote.APIService;
import com.robusta.photoweather.Data.remote.ApiUtils;
import com.robusta.photoweather.R;

public class SplashScreen extends AppCompatActivity {

    public static APIService mService;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String HISTORY_LIST_TAG = "historyArrayList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mService = ApiUtils.getApiService();

        sharedPreferences = getSharedPreferences("localeData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        finishSplashAfterDelay();

    }


    private void finishSplashAfterDelay() {
        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashScreen.this, HomeScreen.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();

        }, SPLASH_DISPLAY_LENGTH);
    }

}