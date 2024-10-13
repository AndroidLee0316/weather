package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by lishanshan790 on 2018/7/27.
 * 天气空气质量指数
 */

@Table(database = WeatherDb.class)
public class WeatherAqiInfo extends BaseModel {
    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(name = "city")
    public String city;

    @Column(name = "aqi")
    @SerializedName("aqi")
    public String aqi; //空气质量指数

    @Column(name = "qlty")
    @SerializedName("qlty")
    public String aqiType; //空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染


    @Column(name = "no2")
    @SerializedName("no2")
    public String no2; //二氧化氮

    @Column(name = "so2")
    @SerializedName("so2")
    public String so2; //二氧化硫

    @Column(name = "co")
    @SerializedName("co")
    public String co; //一氧化碳


    @Column(name = "pm10")
    @SerializedName("pm10")
    public String pm10;

    @Column(name = "pm25")
    @SerializedName("pm25")
    public String pm25;

    @Column(name = "o3")
    @SerializedName("o3")
    public String o3; //臭氧

    @Override
    public String toString() {
        return "WeatherAqiInfo{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", aqi='" + aqi + '\'' +
                ", aqitype ='" + aqiType + '\'' +
                ", cond_txt='" + no2 + '\'' +
                ", cond_txt='" + so2 + '\'' +
                ", qlty='" + pm10 + '\'' +
                ", cond_txt='" + pm25 + '\'' +
                ", tmp='" + o3 + '\'' +
                ", cond_image_url='" + co + '\'' +
                '}';
    }
}
