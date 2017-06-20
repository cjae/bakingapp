package com.dreammesh.app.bakingapp.data.rest;

import com.dreammesh.app.bakingapp.data.model.BakingWrapper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.dreammesh.app.bakingapp.util.ServerUtil.BASE_URL;

/**
 * Created by Jedidiah on 18/04/2017.
 */

public interface ServiceGenerator {

    //--GET REQUEST---------------------------------------------------------------------------------
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<BakingWrapper>> getBakingData();
}
