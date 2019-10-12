package com.example.thirukural;

import com.google.gson.annotations.SerializedName;

public class authDetails {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("expires_at")
    private long expires_at;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setExpires_at(long expires_at) {
        this.expires_at = expires_at;
    }

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires_at() {
        return expires_at;
    }
}
