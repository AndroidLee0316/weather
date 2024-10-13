package com.pasc.lib.weather.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.param.BaseParam;
import com.pasc.lib.net.resp.BaseResp;
import com.pasc.lib.weather.api.UrlManager;
import com.pasc.lib.weather.api.WeatherApi;
import com.pasc.lib.weather.data.WeatherAqiInfo;
import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.WeatherForecastInfo;
import com.pasc.lib.weather.data.WeatherHourForecastInfo;
import com.pasc.lib.weather.data.WeatherIndexOfLife;
import com.pasc.lib.weather.data.WeatherInfo;
import com.pasc.lib.weather.data.WeatherInfo_Table;
import com.pasc.lib.weather.data.WeatherLiveInfo;
import com.pasc.lib.weather.data.WeatherLiveInfo_Table;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.data.params.WeatherParams;
import com.pasc.lib.weather.data.resp.WeatherResp;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.List;

import retrofit2.Call;

import static com.pasc.lib.weather.presenter.WeatherPresenter.WEATHER_LOG_TAG;

class WeatherDataUtil {

    /**
     * 同步请求网络天气简洁接口数据
     *
     * @param cityInfo
     * @return
     */
    protected static WeatherInfo getWeatherInfoFromNet(@NonNull WeatherCityInfo cityInfo) {

        String city = cityInfo.getRequestWeatherParam();
        if (TextUtils.isEmpty(city)) {
            return null;
        }
        WeatherParams params = new WeatherParams(city);

        try {
            WeatherInfo weatherInfo = requestWeatherInfoFromNet(params);
            PascLog.d(WEATHER_LOG_TAG, "weatherInfo = " + weatherInfo);
            if (weatherInfo != null) {
                String saveCity = cityInfo.getShowName();
                if (TextUtils.isEmpty(saveCity)) {
                    saveCity = city;
                }
                weatherInfo.city = saveCity;
                saveWeatherInfo2Db(weatherInfo);
            }
            return weatherInfo;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "getWeatherInfoFromNet error " + e.toString());
        }
        return null;
    }

    /**
     * 同步请求网络天气详情接口数据
     *
     * @param cityInfo
     * @return
     */
    protected static WeatherDetailsInfo getWeatherDetailsInfoFromNet(@NonNull WeatherCityInfo cityInfo) {
        String city = cityInfo.getRequestWeatherParam();
        if (TextUtils.isEmpty(city)) {
            return null;
        }
        WeatherParams params = new WeatherParams(city);

        try {
            WeatherDetailsInfo info = requestWeatherDetailsInfoFromNet(params);
            PascLog.d(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet " + info);
            if (info != null) {
                String saveCity = cityInfo.getShowName();
                if (TextUtils.isEmpty(saveCity)) {
                    saveCity = city;
                }
                info.setCity(saveCity);
                saveWeatherDetailsInfo2Db(info);
            }
            return info;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error " + e.toString());
        }
        return null;
    }

    /**
     * 从缓存获取天气简洁数据
     */
    protected static WeatherInfo getWeatherInfoFromCache(WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }

        String city = cityInfo.getShowName();
        if (TextUtils.isEmpty(city)) {
            city = cityInfo.getRequestWeatherParam();
            if (TextUtils.isEmpty(city)) {
                return null;
            }
        }
        return getWeatherInfoFromCache(city);
    }

    /**
     * 从缓存获取天气详情数据
     */
    protected static WeatherDetailsInfo getWeatherDetailsInfoFromCache(WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }

        String city = cityInfo.getShowName();
        if (TextUtils.isEmpty(city)) {
            city = cityInfo.getRequestWeatherParam();
            if (TextUtils.isEmpty(city)) {
                return null;
            }
        }
        return getWeatherDetailsInfoFromCache(city);
    }


    /**
     * 同步请求网络天气简洁接口数据
     *
     * @param city
     * @return
     */
    protected static WeatherInfo getWeatherInfoFromNet(@NonNull String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        try {
            WeatherInfo weatherInfo = requestWeatherInfoFromNet(new WeatherParams(city));
            PascLog.d(WEATHER_LOG_TAG, "weatherInfo = " + weatherInfo);
            if (weatherInfo != null) {
                weatherInfo.city = city;
                saveWeatherInfo2Db(weatherInfo);
            }
            return weatherInfo;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "getWeatherInfoFromNet error " + e.toString());
        }
        return null;
    }

    /**
     * 同步请求网络天气详情接口数据
     *
     * @param city
     * @return
     */
    protected static WeatherDetailsInfo getWeatherDetailsInfoFromNet(@NonNull String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        try {
            WeatherDetailsInfo info = requestWeatherDetailsInfoFromNet(new WeatherParams(city));
            PascLog.d(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet " + info);
            if (info != null) {
                info.setCity(city);
                saveWeatherDetailsInfo2Db(info);
            }
            return info;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error " + e.toString());
        }
        return null;
    }

    /**
     * 从缓存获取天气简洁数据
     */
    protected static WeatherInfo getWeatherInfoFromCache(String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        WeatherInfo info = SQLite.select().from(WeatherInfo.class)
                .where(WeatherInfo_Table.city.eq(city))
                .querySingle();
        return info;
    }

    /**
     * 从缓存获取天气详情数据
     */
    protected static WeatherDetailsInfo getWeatherDetailsInfoFromCache(String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        WeatherLiveInfo liveInfo = SQLite.select().from(WeatherLiveInfo.class)
                .where(WeatherLiveInfo_Table.city.eq(city))
                .querySingle();
        if (liveInfo == null) {
            PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromCache error");
            return null;
        }
        PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromCache " + city);
        WeatherAqiInfo airInfo = SQLite.select().from(WeatherAqiInfo.class).querySingle();
        List<WeatherHourForecastInfo> hourLists = SQLite.select().from(WeatherHourForecastInfo.class).queryList();
        List<WeatherForecastInfo> forecastInfos = SQLite.select().from(WeatherForecastInfo.class).queryList();
        List<WeatherIndexOfLife> indexOfLives = SQLite.select().from(WeatherIndexOfLife.class).queryList();
        WeatherDetailsInfo info = new WeatherDetailsInfo();
        info.setLiveInfo(liveInfo);
        info.setAqiInfo(airInfo);
        info.setHourForecastInfos(hourLists);
        info.setIndexofLifes(indexOfLives);
        info.setSevenDayInfoList(forecastInfos);
        info.setCity(liveInfo.city);
        return info;
    }


    private static WeatherInfo requestWeatherInfoFromNet(WeatherParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }

        try {
            Call<BaseResp<WeatherResp>> respCall;

            if (WeatherDataManager.getInstance().getReqType() == WeatherDataManager.WEATHER_TYPE_BODY) {
                respCall = ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class).weatherInfoSync(params, UrlManager.getWeatherInfoUrl());
            } else {
                respCall = ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class).weatherInfoSyncByField(new BaseParam<>(params), UrlManager.getWeatherInfoUrl());
            }

            BaseResp<WeatherResp> baseResp = respCall.execute().body();
            if (baseResp == null) {
                return null;
            }
            WeatherResp weatherResp = baseResp.data;
            if (weatherResp == null) {
                return null;
            }
            return weatherResp.weatherInfo;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "requestWeatherInfoFromNet error " + e.toString());
        }
        return null;
    }

    private static WeatherDetailsInfo requestWeatherDetailsInfoFromNet(WeatherParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }

        try {
            Call<BaseResp<WeatherDetailsInfo>> respCall;
            if (WeatherDataManager.getInstance().getReqType() == WeatherDataManager.WEATHER_TYPE_BODY) {
                respCall = ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class).weatherDetailsInfoSync(params, UrlManager.getWeatherDetailInfoUrl());
            } else {
                respCall = ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class).weatherDetailsInfoSyncByField(new BaseParam<>(params), UrlManager.getWeatherDetailInfoUrl());
            }
            BaseResp<WeatherDetailsInfo> baseResp = respCall.execute().body();
            if (baseResp == null) {
                return null;
            }
            return baseResp.data;
        } catch (Exception e) {
            PascLog.d(WEATHER_LOG_TAG, "requestWeatherDetailsInfoFromNet error " + e.toString());
        }
        return null;
    }


    /**
     * 保存天气简洁数据到数据库
     *
     * @param weatherInfo
     */
    private static void saveWeatherInfo2Db(final WeatherInfo weatherInfo) {
        FlowManager.getDatabase(WeatherDb.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                databaseWrapper.delete(FlowManager.getTableName(WeatherInfo.class), null, null);
                weatherInfo.save();
            }
        });
    }

    /**
     * 保存天气详情数据到数据库
     *
     * @param weatherDetailsInfo
     */
    private static void saveWeatherDetailsInfo2Db(final WeatherDetailsInfo weatherDetailsInfo) {
        FlowManager.getDatabase(WeatherDb.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                weatherDetailsInfo.delete(databaseWrapper);
                weatherDetailsInfo.save();
                WeatherInfo simpleWeatherInfo = new WeatherInfo();
                simpleWeatherInfo.city = weatherDetailsInfo.getCity();
                WeatherLiveInfo liveInfo = weatherDetailsInfo.getLiveInfo();
                if (liveInfo != null) {
                    simpleWeatherInfo.cond_txt = liveInfo.weatherState;
                    simpleWeatherInfo.tmp = liveInfo.tmp;
                }
                databaseWrapper.delete(FlowManager.getTableName(WeatherInfo.class), null, null);
                simpleWeatherInfo.save();
            }
        });
    }
}
