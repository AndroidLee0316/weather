package com.pasc.business.weather.util;

import android.text.TextUtils;

import com.pasc.lib.log.PascLog;

import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.WeatherInfo;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.utils.WeatherDataManager;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;
import static com.pasc.business.weather.util.WeatherDefinition.WEATHER_LOG_TAG;

public class RxJavaWeatherDataUtil {

    /**
     * 从缓存获取天气详情
     */
    public static Flowable<WeatherDetailsInfo> getWeatherDetailsInfoFromCache(final WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }

        return Flowable.create(new FlowableOnSubscribe<WeatherDetailsInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherDetailsInfo> emitter) throws Exception {
                WeatherDetailsInfo info = WeatherDataManager.getInstance().getWeatherDetailsInfoFromCache(cityInfo);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error");
                    if(!emitter.isCancelled()){
                    emitter.onError(new Throwable("无内容"));
                    }
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 从网络获取天气详情，并更新数据库
     *
     * @param cityInfo
     */
    public static Flowable<WeatherDetailsInfo> getWeatherDetailsInfoFromNet(final WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherDetailsInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherDetailsInfo> emitter) throws Exception {
                WeatherDetailsInfo info = WeatherDataManager.getInstance().getWeatherDetailsInfoFromNet(cityInfo);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 从网络获取简洁天气的接口
     */
    public static Flowable<WeatherInfo> getWeatherFromNet(final WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherInfo> emitter) throws Exception {
                WeatherInfo info = WeatherDataManager.getInstance().getWeatherInfoFromNet(cityInfo);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherFromNet error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从缓存获取简洁天气
     */
    public static Flowable<WeatherInfo> getWeatherInfoFromCache(final WeatherCityInfo cityInfo) {
        if (cityInfo == null) {
            throw new IllegalArgumentException("cityInfo is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherInfo> emitter) {
                WeatherInfo info = WeatherDataManager.getInstance().getWeatherInfoFromCache(cityInfo);

                if (info == null) {
                    PascLog.i("weather_Log", "getWeatherInfoFromCache error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从缓存获取天气详情
     */
    public static Flowable<WeatherDetailsInfo> getWeatherDetailsInfoFromCache(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }

        return Flowable.create(new FlowableOnSubscribe<WeatherDetailsInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherDetailsInfo> emitter) throws Exception {
                WeatherDetailsInfo info = WeatherDataManager.getInstance().getWeatherDetailsInfoFromCache(city);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 从网络获取天气详情，并更新数据库
     *
     * @param city
     */
    public static Flowable<WeatherDetailsInfo> getWeatherDetailsInfoFromNet(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherDetailsInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherDetailsInfo> emitter) throws Exception {
                WeatherDetailsInfo info = WeatherDataManager.getInstance().getWeatherDetailsInfoFromNet(city);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 从网络获取简洁天气的接口
     */
    public static Flowable<WeatherInfo> getWeatherFromNet(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherInfo> emitter) throws Exception {
                WeatherInfo info = WeatherDataManager.getInstance().getWeatherInfoFromNet(city);
                if (info == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherFromNet error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从缓存获取简洁天气
     */
    public static Flowable<WeatherInfo> getWeatherInfoFromCache(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        return Flowable.create(new FlowableOnSubscribe<WeatherInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherInfo> emitter) {
                WeatherInfo info = WeatherDataManager.getInstance().getWeatherInfoFromCache(city);

                if (info == null) {
                    PascLog.i("weather_Log", "getWeatherInfoFromCache error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
