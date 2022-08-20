package com.robusta.photoweather.Data.remote;

import com.robusta.photoweather.Data.Model.APIResponseModel;
import retrofit2.http.GET;
import retrofit2.Call ;
import retrofit2.http.Url;

public interface APIService {

    @GET
    Call<APIResponseModel> getWeatherDetails(@Url String url);


}