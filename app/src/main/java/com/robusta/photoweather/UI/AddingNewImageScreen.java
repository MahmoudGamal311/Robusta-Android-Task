package com.robusta.photoweather.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.robusta.Other.Config;
import com.robusta.Other.MyLoadingProgressDialog;
import com.robusta.photoweather.Data.Model.APIResponseModel;
import com.robusta.photoweather.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingNewImageScreen extends AppCompatActivity {

    ImageView deleteButton, CameraResult;
    TextView backToHomeButton, takePhotoButton, weatherInfoResult, getWeatherInfoButton, saveButton;

    RelativeLayout finalResult;
    Bitmap finalResultBitmap;

    private Location currentLocation;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_LOCATION_PERMISSION_CODE = 200;

    private MyLoadingProgressDialog loadingProgressDialog;


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new_image_screen);

        finalResult = findViewById(R.id.final_result_layout);

        backToHomeButton = findViewById(R.id.back_to_home_btn);
        backToHomeButton.setOnClickListener(view -> backToHome());

        deleteButton = findViewById(R.id.delete_image_btn);
        deleteButton.setVisibility(View.GONE);
        deleteButton.setOnClickListener(view -> resetImage());

        CameraResult = findViewById(R.id.camera_photo_result);

        takePhotoButton = findViewById(R.id.take_photo_btn);
        takePhotoButton.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }

        });

        weatherInfoResult = findViewById(R.id.weather_info);
        weatherInfoResult.setVisibility(View.GONE);

        getWeatherInfoButton = findViewById(R.id.get_weather_info_btn);
        getWeatherInfoButton.setVisibility(View.GONE);
        getWeatherInfoButton.setOnClickListener(view -> getCurrentLocation());

        saveButton = findViewById(R.id.save_btn);
        saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(view -> {

            finalResult.setDrawingCacheEnabled(true);
            finalResult.buildDrawingCache();
            finalResultBitmap = finalResult.getDrawingCache();

            if (Config.addImageToHistoryArray(Config.encodeBitmapToString(finalResultBitmap))) {
                backToHome();
            } else {
                Toast.makeText(this, "Something went wrong,Image can't be saved successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void backToHome() {
        Intent mainIntent = new Intent(AddingNewImageScreen.this, HomeScreen.class);
        AddingNewImageScreen.this.startActivity(mainIntent);
        AddingNewImageScreen.this.finish();
    }

    private void resetImage() {
        CameraResult.setImageBitmap(null);
        takePhotoButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        weatherInfoResult.setVisibility(View.GONE);
        getWeatherInfoButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    private void callWeatherAPI() {
        String URL = "data/2.5/weather?lat=" + currentLocation.getLatitude() +
                "&lon=" + currentLocation.getLongitude() +
                "&APPID=" + Config.getAPIKeyFromMetaData(AddingNewImageScreen.this) +
                "&units=metric";

        SplashScreen.mService.getWeatherDetails(URL).enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<APIResponseModel> call, @NonNull Response<APIResponseModel> response) {
                if (response.body() != null) {
                    weatherInfoResult.setText("Place : " + response.body().getName()
                            + "\nTemp : " + response.body().getMain().getTemp() + " C"
                            + "\nCondition : " + response.body().getWeather().get(0).getDescription()
                            + "\nPressure : " + response.body().getMain().getPressure()
                            + "\nHumidity : " + response.body().getMain().getHumidity() + " %");

                    weatherInfoResult.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);

                    loadingProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(AddingNewImageScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == MY_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            CameraResult.setImageBitmap(photo);

            takePhotoButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
            getWeatherInfoButton.setVisibility(View.VISIBLE);
        }
    }

    private void getCurrentLocation() {

        loadingProgressDialog = new MyLoadingProgressDialog(AddingNewImageScreen.this);
        loadingProgressDialog.show();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Activity context = AddingNewImageScreen.this;

        if (!Config.checkLocationPermission(context, context)) {
            loadingProgressDialog.dismiss();
            return;
        }
        try {
            if (ContextCompat.checkSelfPermission(context.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
                loadingProgressDialog.dismiss();
            } else {
                currentLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (currentLocation != null) {
                    callWeatherAPI();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled) {
            int locationUpdateTimeInterval = 10 * 1000;
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    locationUpdateTimeInterval,
                    10, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            currentLocation = location;
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });

            currentLocation = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (currentLocation != null) {
                callWeatherAPI();
            }
        }

        if (currentLocation == null) {
            loadingProgressDialog.dismiss();
            Toast.makeText(context, "Failed to get current location\nPlease enable GPS signal and try again", Toast.LENGTH_SHORT).show();
        }

    }
}

