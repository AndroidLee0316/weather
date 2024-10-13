package com.pasc.lib.weather.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.weather.api.UrlManager;
import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.WeatherInfo;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.LibWeatherGeneratedDatabaseHolder;

public class WeatherDataManager {

    private @ReqType int mReqType = WEATHER_TYPE_BODY;
    private static final String CURRENT_SELECT_CITY = "current_select_city";

    public static WeatherDataManager getInstance() {
        return SingletonHolder.instance;
    }


    private static class SingletonHolder {
        private static final WeatherDataManager instance = new WeatherDataManager();
    }

    public static final int WEATHER_TYPE_BODY = 1;//body体
    public static final int WEATHER_TYPE_FIELD = 2;//表单

    @IntDef({WEATHER_TYPE_BODY, WEATHER_TYPE_FIELD})
    public @interface ReqType {

    }

    public void init(Context context) {
        initWeatherDb();
    }

    public void setReqType(@ReqType int type) {
        mReqType = type;
    }

    public @ReqType  int getReqType() {
       return mReqType;
    }

    public void setWeatherInfoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        UrlManager.setWeatherInfoUrl(url);
    }

    public void setGateway(String gateway) {
        if (TextUtils.isEmpty(gateway)) {
            return;
        }
        String host = AppProxy.getInstance().getHost();
        if (TextUtils.isEmpty(host)) {
            return;
        }
        if (!host.endsWith("/") && !gateway.startsWith("/")) {
            UrlManager.setsBaseUrl(host + "/" + gateway + "/");
        } else {
            UrlManager.setsBaseUrl(host + gateway + "/");
        }
    }

    public void setWeatherDetailInfoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        UrlManager.setWeatherDetailInfoUrl(url);
    }


    private void initWeatherDb() {
        FlowConfig flowConfig = new FlowConfig.Builder(AppProxy.getInstance().getContext())
                .addDatabaseHolder(LibWeatherGeneratedDatabaseHolder.class)
                .build();
        FlowManager.init(flowConfig);
    }

    public WeatherInfo getWeatherInfoFromNet(String city) {
        return WeatherDataUtil.getWeatherInfoFromNet(city);
    }

    public WeatherDetailsInfo getWeatherDetailsInfoFromNet(String city) {
        return WeatherDataUtil.getWeatherDetailsInfoFromNet(city);
    }

    public WeatherInfo getWeatherInfoFromCache(String city) {
        return WeatherDataUtil.getWeatherInfoFromCache(city);
    }

    public WeatherDetailsInfo getWeatherDetailsInfoFromCache(String city) {
        return WeatherDataUtil.getWeatherDetailsInfoFromCache(city);
    }

    public WeatherInfo getWeatherInfoFromNet(WeatherCityInfo cityInfo) {
        return WeatherDataUtil.getWeatherInfoFromNet(cityInfo);
    }

    public WeatherDetailsInfo getWeatherDetailsInfoFromNet(WeatherCityInfo cityInfo) {
        return WeatherDataUtil.getWeatherDetailsInfoFromNet(cityInfo);
    }

    public WeatherInfo getWeatherInfoFromCache(WeatherCityInfo cityInfo) {
        return WeatherDataUtil.getWeatherInfoFromCache(cityInfo);
    }

    public WeatherDetailsInfo getWeatherDetailsInfoFromCache(WeatherCityInfo cityInfo) {
        return WeatherDataUtil.getWeatherDetailsInfoFromCache(cityInfo);
    }


    public int getWeatherState(String state) {
        return WeatherDataDefinition.getStateInt(state);
    }

    public int getWeatherStateIcon(Context context, String state) {
        return WeatherDataDefinition.getWeatherStateIcon(context, state);
    }

    public void saveCurrentSelectedCity(WeatherCityInfo cityInfo) {
        SPUtils.getInstance().setParam(CURRENT_SELECT_CITY, cityInfo.toSaveSPString());
    }

    public WeatherCityInfo getCurrentSelectedCity() {
        return WeatherCityInfo.toObject((String) SPUtils.getInstance().getParam(CURRENT_SELECT_CITY, ""));
    }

}
