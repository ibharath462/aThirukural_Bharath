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

    @POST("/*deleted for making public*/")
    Call<authDetails> getAuthDetails(@Query("refresh_token") String at);
}
