package com.pasc.lib.weather.data.params;

import com.google.gson.annotations.SerializedName;

/**
 * 天气接口params
 */

public class WeatherParams {
    @SerializedName("localCity")
    public String localCity;

    public WeatherParams(String localCity) {
        this.localCity = localCity;
    }
}
