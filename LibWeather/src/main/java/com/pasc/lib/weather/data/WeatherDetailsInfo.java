package com.pasc.lib.weather.data;

import android.util.Log;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.List;

public class WeatherDetailsInfo {
    private static final String TAG = "WeatherDetailsInfo";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        liveInfo.city = city;
    }

    private String city;

    @SerializedName("weather")
    private WeatherLiveInfo liveInfo;

    @SerializedName("air")
    private WeatherAqiInfo aqiInfo;


    @SerializedName("lifeStyle")
    private List<WeatherIndexOfLife> indexofLifes;

    @SerializedName("sevenDayInfoList")
    private List<WeatherForecastInfo> sevenDayInfoList;

    @SerializedName("hourly")
    private List<WeatherHourForecastInfo> hourForecastInfos;

    public void save() {
        Log.d(TAG, "save " + liveInfo);
        if (liveInfo != null) {
            liveInfo.save();
        }
        if (aqiInfo != null) {
            aqiInfo.save();
        }
        if (indexofLifes != null) {
            for (int i = 0, j = indexofLifes.size(); i < j; i++) {
                indexofLifes.get(i).save();
            }
        }

        if (sevenDayInfoList != null) {
            for (int i = 0, j = sevenDayInfoList.size(); i < j; i++) {
                sevenDayInfoList.get(i).save();
            }
        }
        if (hourForecastInfos != null) {
            for (int i = 0, j = hourForecastInfos.size(); i < j; i++) {
                hourForecastInfos.get(i).save();
            }
        }
    }

    public WeatherLiveInfo getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(WeatherLiveInfo liveInfo) {
        this.liveInfo = liveInfo;
    }

    public WeatherAqiInfo getAqiInfo() {
        return aqiInfo;
    }

    public void setAqiInfo(WeatherAqiInfo aqiInfo) {
        this.aqiInfo = aqiInfo;
    }

    public List<WeatherIndexOfLife> getIndexofLifes() {
        return indexofLifes;
    }

    public void setIndexofLifes(List<WeatherIndexOfLife> indexofLifes) {
        this.indexofLifes = indexofLifes;
    }

    public List<WeatherForecastInfo> getSevenDayInfoList() {
        return sevenDayInfoList;
    }

    public void setSevenDayInfoList(List<WeatherForecastInfo> sevenDayInfoList) {
        this.sevenDayInfoList = sevenDayInfoList;
    }

    public List<WeatherHourForecastInfo> getHourForecastInfos() {
        return hourForecastInfos;
    }

    public void setHourForecastInfos(List<WeatherHourForecastInfo> hourForecastInfos) {
        this.hourForecastInfos = hourForecastInfos;
    }

    public void delete(DatabaseWrapper databaseWrapper) {
        databaseWrapper.delete(FlowManager.getTableName(WeatherLiveInfo.class), null, null);
        databaseWrapper.delete(FlowManager.getTableName(WeatherAqiInfo.class), null, null);
        databaseWrapper.delete(FlowManager.getTableName(WeatherIndexOfLife.class), null, null);
        databaseWrapper.delete(FlowManager.getTableName(WeatherForecastInfo.class), null, null);
        databaseWrapper.delete(FlowManager.getTableName(WeatherHourForecastInfo.class), null, null);
    }

    @Override
    public String toString() {
        return new StringBuffer("WeatherDatailInfo[city= ").append(city)
                .append("weatherLive = ").append(liveInfo == null ? "null" : liveInfo.toString())
                .append("aqiInfo = ").append(aqiInfo == null ? "null" : aqiInfo.toString())
                .append("indexofLifes = ").append(indexofLifes == null ? "null" : indexofLifes.toString())
                .append("sevenDayInfoList = ").append(sevenDayInfoList == null ? "null" : sevenDayInfoList.toString())
                .append("hourForecastInfos = ").append(hourForecastInfos == null ? "null" : hourForecastInfos.toString()).toString();
    }
}
