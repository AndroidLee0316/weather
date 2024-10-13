package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * created by lishanshan790 on 2018/7/27
 * 天气的生活指数
 */

@Table(database = WeatherDb.class)
public class WeatherIndexOfLife extends BaseModel {

    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(name = "city")
    public String city;

    @Column(name = "brf")
    @SerializedName("brf")
    public String brf;  //指数简述

    @Column(name = "txt")
    @SerializedName("txt")
    public String txt;  //指数详情


    @Column(name = "type")
    @SerializedName("type")
    public String type; //指数类型


    @Override
    public String toString() {
        return "WeatherIndexOfLife {" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", brf='" + brf + '\'' +
                ", txt='" + txt + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
