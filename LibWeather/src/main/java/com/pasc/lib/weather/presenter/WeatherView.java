package com.pasc.lib.weather.presenter;

import com.pasc.lib.base.activity.presenter.IBaseView;
import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.WeatherInfo;

/**
 * Created by zhangxu678 on 2018/11/20.
 */
public interface WeatherView extends IBaseView {
  void showWeatherInfo(WeatherDetailsInfo weatherDetailsInfo);

  void showWeatherInfo(WeatherInfo weatherInfo);

  void showEmpty();

  void showError(String errorMsg);


}
