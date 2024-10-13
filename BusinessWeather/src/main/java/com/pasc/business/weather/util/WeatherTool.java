package com.pasc.business.weather.util;

import android.text.TextUtils;

public class WeatherTool {
    public static String getSubString(String dev, int max, String end) {
        if (TextUtils.isEmpty(dev)) {
            return dev;
        }
        int length = dev.replace(" ", "").length();
        if (length <= max) {
            return dev;
        }
        String newString = dev.substring(0, max);
        if (!TextUtils.isEmpty(end)) {
            return newString.trim() + end;
        }
        return newString;
    }
}
