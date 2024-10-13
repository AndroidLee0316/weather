package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by lishanshan790 on 2018/7/27.
 * 24小时预报
 */

@Table(database = WeatherDb.class)
public class WeatherHourForecastInfo extends BaseModel {
    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(name = "city")
    public String city;

    @Column(name = "hour")
    @SerializedName("time")
    public String hour;  //整点数

    @Column(name = "tmp")
    @SerializedName("tmp")
    public String tmp; //温度


    @Column(name = "weatherstate")
    @SerializedName("cond_txt")
    public String weatherState; //天气状况

    @Override
    public String toString() {
        return "WeatherHourForecastInfo{" +
                "id=" + id +
                ", city = '" + city + '\'' +
                ", hour = '" + hour + '\'' +
                ", tmp= '" + tmp + '\'' +
                ", weatherState='" + weatherState + '\'' +
                '}';
    }
}
