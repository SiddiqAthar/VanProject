package com.example.privatevanmanagement.retrofit;

import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static RetrofitClient instance = new RetrofitClient();
    private Retrofit retrofit;

    public static RetrofitClient getInstance() {
        return instance;
    }

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(300, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getDirection(String ori, String des, String key, CallbackGenric result) {
        IRetrofitService service = retrofit.create(IRetrofitService.class);
        Call<ResponseBody> call = service.getDirection(ori, des, key);
        sendResultGeneric(call, result);
    }

    private <T> void sendResultGeneric(Call<T> call, final CallbackGenric result) {

        call.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                if (response.isSuccessful()) {
                    result.onResult(true, response);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        result.onError(jObjError.getString("message"));
                    } catch (Exception e) {
                        Log.i("ex", "API Manager Exception");
                        result.onError(e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                result.onError(t.getMessage());
            }
        });
    }

    public interface CallbackGenric<T> {
        void onResult(boolean z, Response response);

        void onError(String error);
    }

}
