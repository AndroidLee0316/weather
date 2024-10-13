package com.pasc.lib.weather.api;

import com.pasc.lib.net.param.BaseParam;
import com.pasc.lib.net.resp.BaseResp;
import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.params.WeatherParams;
import com.pasc.lib.weather.data.resp.WeatherResp;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/9/5
 * @des
 * @modify
 **/
public interface WeatherApi {

  @POST
  Single<BaseResp<WeatherDetailsInfo>> weatherDetailsInfo(@Body WeatherParams param, @Url String url);

  @POST
  Call<BaseResp<WeatherDetailsInfo>> weatherDetailsInfoSync(@Body WeatherParams param, @Url String url);

  @POST
  Single<BaseResp<WeatherResp>> weatherInfo(@Body WeatherParams param, @Url String url);

  @POST
  Call<BaseResp<WeatherResp>> weatherInfoSync(@Body WeatherParams param, @Url String url);

  @FormUrlEncoded
  @POST
  Call<BaseResp<WeatherResp>> weatherInfoSyncByField(@Field("jsonData") BaseParam<WeatherParams> param, @Url String url);

  @FormUrlEncoded
  @POST
  Call<BaseResp<WeatherDetailsInfo>> weatherDetailsInfoSyncByField(@Field("jsonData") BaseParam<WeatherParams> param, @Url String url);

}
