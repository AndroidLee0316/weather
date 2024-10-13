package com.pasc.lib.weather.data.resp;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.data.WeatherInfo;

/**
 * 天气接口response
 */

public class WeatherResp {
    @SerializedName("weather")
    public WeatherInfo weatherInfo;
}
