package com.pasc.business.weather.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lanshaomin
 * Date: 2019/7/5 下午2:40
 * Desc:配置信息
 */
public class WeatherConfigBean {
    @SerializedName("weather")
    public WeatherBean weatherBean;

    public static class WeatherBean {
        @SerializedName("name")
        public String name;
        @SerializedName("enable")
        public boolean enable;
        @SerializedName("predictionOf24Hours")
        public boolean predictionOf24Hours;
        @SerializedName("predictionOf7Days")
        public boolean predictionOf7Days;
        @SerializedName("indexOfLiving")
        public boolean indexOfLiving;
        @SerializedName("indexOfOthers")
        public boolean indexOfOthers;
    }
}
