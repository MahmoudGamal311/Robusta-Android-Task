package com.robusta.photoweather.Data.remote;

public class ApiUtils {


    public static final String BASE_URL = "https://api.openweathermap.org/";

    public static APIService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}