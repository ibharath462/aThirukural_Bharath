package com.example.thirukural;

import com.google.gson.annotations.SerializedName;

public class strava {

    @SerializedName("distance")
    private float distance;

    @SerializedName("elapsed_time")
    private float elapsed_time;

    @SerializedName("type")
    private String type;


    public void setDistance(int distance){
        this.distance = distance;
    }

    public void setElapsed_time(int elapsed_time){
        this.elapsed_time = elapsed_time;
    }

    public void setType(String type){
        this.type = type;
    }

    public float getDistance() {
        return distance;
    }

    public float getElapsed_time() {
        return elapsed_time;
    }

    public String getType() {
        return type;
    }
}
