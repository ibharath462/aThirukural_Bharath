package com.example.thirukural;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("searchKural/{kNo}")
    Call<kural> getKuralDetails(@Path("kNo") int kNo);

    @GET("activities?")
    Call<List<strava>> getStravaStats(@Query("access_token") String at);

    @POST("token?client_id=38934&client_secret=8db554338963efb30aa9d56450219103469dd8dc&grant_type=refresh_token")
    Call<authDetails> getAuthDetails(@Query("refresh_token") String at);
}