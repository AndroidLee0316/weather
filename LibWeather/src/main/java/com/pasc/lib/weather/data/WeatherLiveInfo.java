package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by lishanshan790 on 2018/7/27.
 * 天气实况
 */

@Table(database = WeatherDb.class)
public class WeatherLiveInfo extends BaseModel {

    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(name = "city")
    public String city;

    @Column(name = "cond_txt")
    @SerializedName("cond_txt")
    public String weatherState;//实况天气状况描述

    @Column(name = "tmp")
    @SerializedName("tmp")
    public String tmp;//温度

    @Column(name = "cond_image_url")
    @SerializedName("cond_image_url")
    public String cond_image_url;//实况天气状况图片

    @Column(name = "fl")
    @SerializedName("fl")
    public String feel;//体感温度

    @Column(name = "wind_dir")
    @SerializedName("wind_dir")
    public String wind_dir;//风向

    @Column(name = "wind_sc")
    @SerializedName("wind_sc")
    public String wind_sc;//风力

    @Column(name = "vis")
    @SerializedName("vis")
    public String vis;//能见度

    @Column(name = "hum")
    @SerializedName("hum")
    public String hum;//湿度

    @Column(name = "pres")
    @SerializedName("pres")
    public String pres;//大气压

    @Column(name = "maxMin")
    @SerializedName("maxMin")
    public String maxMin;//最高最低温度

    @Override
    public String toString() {
        return new StringBuffer("WeatherLiveInfo [ city = ").append(city)
                .append("weatherState = ").append(weatherState)
                .append("tmp = ").append(tmp)
                .append("cond_image_url = ").append(cond_image_url)
                .append("fl = ").append(feel)
                .append(" wind_dir = ").append(wind_dir)
                .append("wind_sc = ").append(wind_sc)
                .append("vis = ").append(vis)
                .append("hum = ").append(hum)
                .append("pres = ").append(pres)
                .append("maxMin = ").append(maxMin)
                .append("]").toString();

    }
}
