package com.example.thirukural;

import com.google.gson.annotations.SerializedName;

public class kural {

    @SerializedName("Number")
    private int Number;
    @SerializedName("paal")
    private String paal;
    @SerializedName("iyal")
    private String iyal;
    @SerializedName("agaradhi")
    private String agaradhi;
    @SerializedName("Line1")
    private String line1;
    @SerializedName("Line2")
    private String line2;


    @SerializedName("transliteration1")
    private String transliteration1;
    @SerializedName("transliteration2")
    private String transliteration2;


    @SerializedName("couplet")
    private String explanation;
    @SerializedName("explanation")
    private String translation;

    @SerializedName("mv")
    private String mv;
    @SerializedName("sp")
    private String sp;
    @SerializedName("mk")
    private String mk;
    @SerializedName("amma")
    private String amma;

    //Setters
    public void setNumber(int Number){
        this.Number = Number;
    }

    public void setPaal(String paal){
        this.paal = paal;
    }

    public void setIyal(String iyal){
        this.iyal = iyal;
    }

    public void setAgaradhi(String agaradhi){
        this.agaradhi = agaradhi;
    }

    public void setLine1(String line1){
        this.line1 = line1;
    }

    public void setLine2(String line2){
        this.line2 = line2;
    }

    public void setTransliteration1(String transliteration1){
        this.transliteration1 = transliteration1;
    }

    public void setTransliteration2(String transliteration2){
        this.transliteration2 = transliteration2;
    }

    public void setTranslation(String translation){
        this.translation = translation;
    }

    public void setExplanation(String explanation){
        this.explanation = explanation;
    }

    public void setMv(String mv){
        this.mv = mv;
    }

    public void setSp(String sp){
        this.sp = sp;
    }

    public void setMk(String mk){
        this.mk = mk;
    }

    public void setAmma(String amma){
        this.amma = amma;
    }

    //Getters...

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public int getNumber() {
        return Number;
    }

    public String getAgaradhi() {
        return agaradhi;
    }

    public String getAmma() {
        return amma;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getIyal() {
        return iyal;
    }

    public String getMk() {
        return mk;
    }

    public String getMv() {
        return mv;
    }

    public String getPaal() {
        return paal;
    }

    public String getSp() {
        return sp;
    }

    public String getTranslation() {
        return translation;
    }

    public String getTransliteration1() {
        return transliteration1;
    }

    public String getTransliteration2() {
        return transliteration2;
    }
}
