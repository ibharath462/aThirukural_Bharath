package com.example.thirukural;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("searchKural/{kNo}")
    Call<kural> getKuralDetails(@Path("kNo") int kNo);
}