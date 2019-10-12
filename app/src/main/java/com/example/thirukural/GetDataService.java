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

    @POST("token?client_id=38934&client_secret=8db554338963efb30aa9d56450219103469dd8dc&code=fa164e3f334d44adace5449dcc41b5f71e3a2c9e&grant_type=authorization_code")
    Call<authDetails> getAuthDetails();
}