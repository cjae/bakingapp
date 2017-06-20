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

public class APIClient {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(40,TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor());

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
