package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * created by lishanshan790 on 2018/7/27
 * 天气七天预报
 *
 */
@Table(database = WeatherDb.class)
public class WeatherForecastInfo extends BaseModel {

    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(name = "city")
    public String city;

    @Column(name = "date")
    @SerializedName("date")
    public String date;  //日期


    @Column(name = "time")
    @SerializedName("time")
    public String time;

    @Column(name = "tmp_max")
    @SerializedName("tmp_max")
    public String tmp_max;  //最高温度


    @Column(name = "tmp_min")
    @SerializedName("tmp_min")
    public String tmp_min; //最低温度

    @Column(name = "cond_txt_d")
    @SerializedName("cond_txt_d")
    public String weatherState;  //天气状况

    @Override
    public String toString() {
        return new StringBuffer("WeatherForecastInfo [ city = ").append(city)
                .append("date = ").append(date)
                .append("time = ").append(time)
                .append("tmp_max = ").append(tmp_max)
                .append("tmp_min = ").append(tmp_min)
                .append("weatherState = ").append(weatherState)
                .append("]").toString();

    }
}
