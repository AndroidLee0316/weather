package com.pasc.business.weather.util;

import android.text.TextUtils;

import com.pasc.business.weather.R;
import com.pasc.lib.weather.utils.WeatherDataManager;

public class WeatherDefinition {
    public static final String WEATHER_LOG_TAG = "weather_log";
    public static final String WEATHER_TEMP_UNIT = "°";

    public static int getWeatherBg(String weatherState) {
        return getWeatherBgByState(weatherState);
    }


    public static int getWeatherBgByState(String weatherState) {
        if (TextUtils.isEmpty(weatherState)) {
            return R.drawable.weather_bg_qing;
        }
        int state = WeatherDataManager.getInstance().getWeatherState(weatherState);
        if (state <= 100 || state >= 900) {
            return R.drawable.weather_bg_qing;
        } else if (state <= 103) {
            return R.drawable.weather_bg_duoyun;
        } else if (state == 104 || state == 201) {
            return R.drawable.weather_bg_yin;
        } else if (state <= 213) {
            return R.drawable.weather_bg_feng;
        } else if (state <= 399) {
            if (weatherState.contains("雷")) {
                return R.drawable.weather_bg_leizhenyu;
            } else if(weatherState.contains("暴雨")) {
                return R.drawable.weather_bg_dabaoyu;
            }
            return R.drawable.weather_bg_yu;
        } else if (state <= 499) {
            return R.drawable.weather_bg_xue;
        } else if (state >= 503 && state <= 508) {
            return R.drawable.weather_bg_shachenbao;
        } else if (weatherState.contains("雾")) {
            return R.drawable.weather_bg_wu;
        } else if (weatherState.contains("霾")) {
            return R.drawable.weather_bg_mai;
        }
        return R.drawable.weather_bg_qing;
    }

    public static int getAqiBgIcon(String aqi) {
        if (TextUtils.isEmpty(aqi)) {
            return 0;
        }
        int aqiValue;
        try {
            aqiValue = Integer.valueOf(aqi);
        } catch (NumberFormatException e) {
            aqiValue = 0;
        }

        if (aqiValue <= 100) {
            return R.drawable.weather_air_qulity_green_ic;
        } else if (aqiValue <= 200) {
            return R.drawable.weather_air_qulity_orange_ic;
        }
        return R.drawable.weather_air_qulity_red_ic;
    }

    public static int[] getColorByAqi(String aqi) {
        if (TextUtils.isEmpty(aqi)) {
            return new int[]{0xffa6db74, 0xff8bce56};
        }
        int aqiValue;
        try {
            aqiValue = Integer.valueOf(aqi);
        } catch (NumberFormatException e) {
            aqiValue = 0;
        }

        if (aqiValue <= 50) {
            return new int[]{0xffa6db74, 0xff8bce56};
        } else if (aqiValue <= 100) {
            return new int[]{0xffd3f261, 0xffbae637};
        } else if (aqiValue <= 150) {
            return new int[]{0xffffd666, 0xffffc53d};
        } else if (aqiValue <= 200) {
            return new int[]{0xffffc069, 0xffffa940};
        } else if (aqiValue <= 300) {
            return new int[]{0xffff9c6e, 0xffff7a45};
        } else if (aqiValue <= 400) {
            return new int[]{0xffff7875, 0xffff4d4f};
        }
        return new int[]{0xffff7875, 0xffff4d4f};
    }

    /**
     *
     * @param type comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、flu：感冒指数、sport：运动指数、trav：旅游指数、uv：紫外线指数、air：空气污染扩散条件指数、
     *             ac：空调开启指数、ag：过敏指数、gl：太阳镜指数、mu：化妆指数、airc：晾晒指数、ptfc：交通指数、fsh：钓鱼指数、spi：防晒指数
     * @return
     */

    public static String getWeatherIndexbyType(String type) {
        switch (type) {
            case "comf" :
                return "舒适度指数";
            case "cw" :
                return "洗车指数";
            case "drsg" :
                return "穿衣指数";
            case "flu" :
                return "感冒指数";
            case "sport" :
                return "运动指数";
            case "trav" :
                return "旅游指数";
            case "uv" :
                return "紫外线指数";
            case "air" :
                return "空气污染指数";
            case "ac" :
                return "空调开启指数";
            case "ag" :
                return "过敏指数";
            case "gl" :
                return "太阳镜指数";
            case "mu" :
                return "化妆指数";
            case "airc" :
                return "晾晒指数";
            case "ptfc" :
                return "交通指数";
            case "fsh" :
                return "钓鱼指数";
            case "spi" :
                return "防晒指数";
        }
        return "";
    }


    public static boolean isIndexVisiable(String type) {
        switch (type) {
            case "comf" :
                return true;
            case "cw" :
                return true;
            case "drsg" :
                return true;
            case "flu" :
                return true;
            case "sport" :
                return true;
            case "uv" :
                return true;
        }
        return false;
    }

    public static int getWeatherIndexIconByTpye(String type) {
        switch (type) {
            case "comf" :
                return R.drawable.weather_index_comfort;
            case "cw" :
                return R.drawable.weather_index_washcar;
            case "drsg" :
                return R.drawable.weather_index_dress;
            case "flu" :
                return R.drawable.weather_index_cold;
            case "sport" :
                return R.drawable.weather_index_sport;
            case "uv" :
                return R.drawable.weather_index_ultraviolet;
        }
        return R.drawable.weather_index_cold;
    }
}
