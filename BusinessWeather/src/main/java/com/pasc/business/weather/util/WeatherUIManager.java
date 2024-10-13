package com.pasc.business.weather.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.pasc.business.weather.bean.WeatherConfigBean;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.weather.utils.WeatherDataManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


public class WeatherUIManager {
    //城市数据源名称
    private static final String WEATHER_DEFAULT_CITIES_FILE_NAME = "weather_default_cities";
    //城市数据源的标签
    private static final String CITY_LIST_KEY = "city_list";
    private static final String CITY_ITEM_DIVIDER = ",";
    public static final String CITY_NAME_SPLIT = ":";

    //城市列表
    private String[] mCitiesList;

    //字体选中颜色
    private int mSelectCityColor;
    //重新定位图标
    private int mLocationDrawableIcon;
    //标题下拉箭头点击图标
    private int mToolbarSelectDrawableIcon;
    //标题下拉箭头默认图标
    private int mToolbarDefaultDrawableIcon;
    //返回按钮
    private int mBackDrawableIcon;
    //配置
    private WeatherConfigBean.WeatherBean weatherBean;

    public static WeatherUIManager getInstance() {
        return SingletonHolder.instance;
    }


    private static class SingletonHolder {
        private static final WeatherUIManager instance = new WeatherUIManager();
    }

    public void init(Context context) {
        if (context != null) {
            initDefaultCity(context);
            WeatherDataManager.getInstance().init(context);
        }
    }

    public void setWeatherInfoUrl(String url) {
        WeatherDataManager.getInstance().setWeatherInfoUrl(url);
    }

    public void setGateway(String gateway) {
        WeatherDataManager.getInstance().setGateway(gateway);
    }

    public void setWeatherDetailInfoUrl(String url) {
        WeatherDataManager.getInstance().setWeatherDetailInfoUrl(url);
    }

    public void setReqType(@WeatherDataManager.ReqType int type) {
        WeatherDataManager.getInstance().setReqType(type);
    }


    /**
     * 设置字体选中对字体颜色
     *
     * @param color
     * @return
     */
    public WeatherUIManager setSelectCityTextColor(int color) {
        mSelectCityColor = color;
        return this;
    }

    /**
     * 设置返回图标
     *
     * @param drawableIcon
     * @return
     */
    public WeatherUIManager setBackDrawableIcon(int drawableIcon) {
        mBackDrawableIcon = drawableIcon;
        return this;
    }

    /**
     * 设置重新定位的图标
     *
     * @param drawableIcon
     * @return
     */
    public WeatherUIManager setLocationDrawableIcon(int drawableIcon) {
        mLocationDrawableIcon = drawableIcon;
        return this;
    }

    /**
     * 设置toolbar下拉图标
     *
     * @param selectDrawableIcon
     * @param defaultDrawableIcon
     * @return
     */
    public WeatherUIManager setToolbarDrawableIcon(int selectDrawableIcon, int defaultDrawableIcon) {
        mToolbarSelectDrawableIcon = selectDrawableIcon;
        mToolbarDefaultDrawableIcon = defaultDrawableIcon;
        return this;
    }

    private void initDefaultCity(Context context) {
        InputStream inputStream = null;
        try {
            Resources resources = context.getResources();
            int rawId = resources.getIdentifier(WEATHER_DEFAULT_CITIES_FILE_NAME, "raw", context.getPackageName());
            if (rawId <= 0) {
                return;
            }
            inputStream = resources.openRawResource(rawId);
            Properties properties = new Properties();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
            properties.load(reader);
            String cities = (String) properties.get(CITY_LIST_KEY);
            if (TextUtils.isEmpty(cities)) {
                return;
            }
            if (cities.contains(CITY_ITEM_DIVIDER)) {
                mCitiesList = cities.split(CITY_ITEM_DIVIDER);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String[] getCitiesList() {
        return mCitiesList;
    }

    public int getmSelectCityColor() {
        return mSelectCityColor;
    }

    public int getmLocationDrawableIcon() {
        return mLocationDrawableIcon;
    }

    public int getmToolbarSelectDrawableIcon() {
        return mToolbarSelectDrawableIcon;
    }

    public int getmToolbarDefaultDrawableIcon() {
        return mToolbarDefaultDrawableIcon;
    }

    public int getmBackDrawableIcon() {
        return mBackDrawableIcon;
    }

    public void initConfig(Context context, String jsonPath) {
        if (TextUtils.isEmpty(jsonPath)) {
            throw new NullPointerException("请传入正确的serviceConfigPath");
        }
        try {
            WeatherConfigBean weatherConfigBean = new Gson().fromJson(AssetsUtil.parseFromAssets(context, jsonPath), WeatherConfigBean.class);
            if (weatherConfigBean != null) {
                weatherBean = weatherConfigBean.weatherBean;
            }

        } catch (Exception e) {
            PascLog.v("WeatherUrlDispatcher", e.getMessage());
        }
    }

    public boolean isEnable() {
        return weatherBean == null || weatherBean.enable;
    }

    public boolean showPredictionOf24Hours() {
        return weatherBean == null || weatherBean.predictionOf24Hours;
    }

    public boolean showPredictionOf7Days() {
        return weatherBean == null || weatherBean.predictionOf7Days;
    }

    public boolean showIndexOfLiving() {
        return weatherBean == null || weatherBean.indexOfLiving;
    }

    public boolean showIndexOfOthers() {
        return weatherBean == null || weatherBean.indexOfOthers;
    }
}
