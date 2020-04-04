package com.example.privatevanmanagement.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRetrofitService {
    @GET("json?mode=driving&transit_routing_preference=less_driving")
    Call<ResponseBody> getDirection(@Query("origin") String originLatLong, @Query("destination") String desLatLong,
                                    @Query("key") String key);
}
