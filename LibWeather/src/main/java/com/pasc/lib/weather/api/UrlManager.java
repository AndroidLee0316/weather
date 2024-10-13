package com.pasc.lib.weather.api;

import android.text.TextUtils;

import com.pasc.lib.base.AppProxy;

public class UrlManager {
    public static String WEATHER_INFO = "platform/weather/gainWeatherInfo";
    public static final String WEATHER_DETAILS_INFO = "platform/weather/queryWeatherDetail";

    private static String sWeatherInfoUrl = WEATHER_INFO;
    private static String sWeatherDetailInfoUrl = WEATHER_DETAILS_INFO;
    private static String sBaseUrl;

    public static void setWeatherInfoUrl(String url) {
        sWeatherInfoUrl = url;
    }

    public static void setsBaseUrl(String url) {
        sBaseUrl = url;
    }

    public static void setWeatherDetailInfoUrl(String url) {
        sWeatherDetailInfoUrl = url;
    }

    public static String getWeatherInfoUrl() {
        return sWeatherInfoUrl;
    }

    public static String getWeatherDetailInfoUrl() {
        return sWeatherDetailInfoUrl;
    }

    public static String getBaseUrl() {
        if (TextUtils.isEmpty(sBaseUrl)) {
            return AppProxy.getInstance().getHost();
        }
        return sBaseUrl;
    }

}
