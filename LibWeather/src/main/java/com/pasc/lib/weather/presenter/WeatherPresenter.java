package com.pasc.lib.weather.presenter;

import android.text.TextUtils;

import com.pasc.lib.base.activity.presenter.BasePresenter;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.transform.RespTransformer;
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
import com.pasc.lib.weather.data.params.WeatherParams;
import com.pasc.lib.weather.data.resp.WeatherResp;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * Created by zhangxu678 on 2018/11/20.
 */
public class WeatherPresenter extends BasePresenter<WeatherView> {
    public static final String WEATHER_LOG_TAG = "weather_log";

    /**
     * 从缓存获取天气详情
     */
    public void getWeatherDetailsInfoFromCache(final String city, final boolean showEmpty) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        disposables.add(Flowable.create(new FlowableOnSubscribe<WeatherDetailsInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherDetailsInfo> emitter) throws Exception {
                WeatherLiveInfo liveInfo = SQLite.select().from(WeatherLiveInfo.class)
                        .where(WeatherLiveInfo_Table.city.eq(city))
                        .querySingle();
                if (liveInfo == null) {
                    PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromCache error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromCache " + city);
                WeatherAqiInfo airInfo = SQLite.select().from(WeatherAqiInfo.class).querySingle();
                List<WeatherHourForecastInfo> hourLists =
                        SQLite.select().from(WeatherHourForecastInfo.class).queryList();
                List<WeatherForecastInfo> forecastInfos =
                        SQLite.select().from(WeatherForecastInfo.class).queryList();
                List<WeatherIndexOfLife> indexOfLives =
                        SQLite.select().from(WeatherIndexOfLife.class).queryList();
                WeatherDetailsInfo info = new WeatherDetailsInfo();
                info.setLiveInfo(liveInfo);
                info.setAqiInfo(airInfo);
                info.setHourForecastInfos(hourLists);
                info.setIndexofLifes(indexOfLives);
                info.setSevenDayInfoList(forecastInfos);
                info.setCity(liveInfo.city);
                PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromCache " + city);
                emitter.onNext(info);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherDetailsInfo>() {
                    @Override
                    public void accept(WeatherDetailsInfo weatherDetailsInfo) throws Exception {
                        if (baseView == null) return;
                        baseView.showWeatherInfo(weatherDetailsInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (showEmpty) {
                            if (baseView == null) return;
                            baseView.showEmpty();
                        }
                    }
                }));
    }

    /**
     * 从网络获取天气详情，并更新数据库
     */
    public void getWeatherDetailsInfoFromNet(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }
        baseView.showLoading();
        disposables.add(ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class)
                .weatherDetailsInfo(new WeatherParams(city), UrlManager.getWeatherDetailInfoUrl())
                .compose(RespTransformer.<WeatherDetailsInfo>newInstance())
                .filter(new Predicate<WeatherDetailsInfo>() {
                    @Override
                    public boolean test(WeatherDetailsInfo info) throws Exception {
                        return info.getLiveInfo() != null && !TextUtils.isEmpty(
                                info.getLiveInfo().weatherState);
                    }
                })
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<WeatherDetailsInfo>() {
                    @Override
                    public void accept(WeatherDetailsInfo weatherDetailsInfo) throws Exception {
                        //网络获取到的数据缓存到db
                        weatherDetailsInfo.setCity(city);
                        PascLog.i(WEATHER_LOG_TAG, "getWeatherDetailsInfoFromNet " + weatherDetailsInfo);
                        saveWeatherDetailsInfo2Db(weatherDetailsInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherDetailsInfo>() {
                    @Override
                    public void accept(final WeatherDetailsInfo weatherDetailsInfo)
                            throws Exception {
                        if (baseView == null) return;
                        baseView.hideLoading();
                        baseView.showWeatherInfo(weatherDetailsInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (baseView == null) return;
                        baseView.hideLoading();
                        baseView.showError(throwable != null ? throwable.getMessage() : "");
                    }
                }));
    }

    private void saveWeatherDetailsInfo2Db(final WeatherDetailsInfo weatherDetailsInfo) {
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

    /**
     * 从网络获取简洁天气的接口
     */
    public void getWeatherFromNet(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }

        disposables.add(ApiGenerator.createApi(UrlManager.getBaseUrl(), WeatherApi.class)
                .weatherInfo(new WeatherParams(city), UrlManager.getWeatherInfoUrl())
                .compose(RespTransformer.<WeatherResp>newInstance())
                .flatMap(new Function<WeatherResp, SingleSource<WeatherInfo>>() {
                    @Override
                    public SingleSource<WeatherInfo> apply(WeatherResp weatherResp) {
                        return Single.just(weatherResp.weatherInfo);
                    }
                })
                .filter(new Predicate<WeatherInfo>() {
                    @Override
                    public boolean test(WeatherInfo weatherInfo) {
                        return !TextUtils.isEmpty(weatherInfo.cond_txt);
                    }
                })
                .toFlowable()
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(WeatherInfo weatherInfo) {
                        //网络获取到的数据缓存到db
                        weatherInfo.city = city;
                        PascLog.d(WEATHER_LOG_TAG, "getWeatherFromNet " + city);
                        saveWeatherInfo2Db(weatherInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(final WeatherInfo weatherInfo)
                            throws Exception {
                        if (baseView == null) return;
                        baseView.hideLoading();
                        baseView.showWeatherInfo(weatherInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (baseView == null) return;
                        baseView.hideLoading();
                        baseView.showError(throwable != null ? throwable.getMessage() : "");
                    }
                }));
    }

    /**
     * 从缓存获取简洁天气
     */
    public void getWeatherInfoFromCache(final String city) {
        if (TextUtils.isEmpty(city)) {
            throw new IllegalArgumentException("city is null");
        }

        disposables.add(Flowable.create(new FlowableOnSubscribe<WeatherInfo>() {
            @Override
            public void subscribe(FlowableEmitter<WeatherInfo> emitter) {
                PascLog.i("weather_Log", "getWeatherInfoFromCache " + city);
                WeatherInfo info = SQLite.select().from(WeatherInfo.class)
                        .where(WeatherInfo_Table.city.eq(city))
                        .querySingle();
                if (info == null) {
                    PascLog.i("weather_Log", "getWeatherInfoFromCache error");
                    emitter.onError(new Throwable("无内容"));
                    return;
                }
                emitter.onNext(info);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(WeatherInfo weatherInfo) throws Exception {
                        if (baseView == null) return;
                        baseView.showWeatherInfo(weatherInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (baseView == null) return;
                        baseView.showEmpty();
                    }
                }));
    }

    /**
     * 保存天气简洁数据到数据库
     *
     * @param weatherInfo
     */
    protected void saveWeatherInfo2Db(final WeatherInfo weatherInfo) {
        FlowManager.getDatabase(WeatherDb.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                databaseWrapper.delete(FlowManager.getTableName(WeatherInfo.class), null, null);
                weatherInfo.save();
            }
        });
    }


}
