package com.pasc.business.weather.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pasc.business.weather.R;
import com.pasc.business.weather.viewmodel.WeatherAqiModel;
import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.base.util.DeviceUtils;
import com.pasc.lib.weather.data.WeatherAqiInfo;
import com.pasc.lib.widget.seriesadapter.base.ISeriesPresenter;
import com.pasc.lib.widget.seriesadapter.base.SeriesAdapter;
import com.pasc.lib.widget.seriesadapter.base.SeriesPresenter;

import java.util.ArrayList;

public class WeatherAqiDialog extends Dialog {

    private static final int COUNT = 3;

    RecyclerView mRecyclerView;
    ImageView ivClose;
    WeatherAqiCircleView mWeatherAqiCircleView;

    SeriesAdapter mAdapter;
    ArrayList<WeatherAqiModel> mWeatherAqiModelList = new ArrayList<>();

    public WeatherAqiDialog(@NonNull Context context) {
        super(context, R.style.common_loading_dialog);
        setContentView(R.layout.weather_aqi_layout);
        mRecyclerView = findViewById(R.id.weather_aqi_recyclerview);
        ivClose = findViewById(R.id.weather_aqi_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mWeatherAqiCircleView = findViewById(R.id.weather_aqi_circle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, COUNT));
        ISeriesPresenter presenter = new SeriesPresenter.Builder()
                .addWorker(new WeatherAqiModel.AqiWorker())
                .build();
        mAdapter = new SeriesAdapter(presenter);
        mRecyclerView.setAdapter(mAdapter);
        initLayoutParams();
    }

    public Dialog setAqiData(WeatherAqiInfo aqiInfo) {
        mWeatherAqiModelList.clear();
        Resources resources = getContext().getResources();
        if (!TextUtils.isEmpty(aqiInfo.pm10)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.pm10, resources.getString(R.string.weather_pm10)));
        }
        if (!TextUtils.isEmpty(aqiInfo.pm25)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.pm25, resources.getString(R.string.weather_pm25)));
        }
        if (!TextUtils.isEmpty(aqiInfo.no2)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.no2, resources.getString(R.string.weather_no2)));
        }
        if (!TextUtils.isEmpty(aqiInfo.so2)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.so2, resources.getString(R.string.weather_so2)));
        }
        if (!TextUtils.isEmpty(aqiInfo.co)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.co, resources.getString(R.string.weather_co)));
        }
        if (!TextUtils.isEmpty(aqiInfo.o3)) {
            mWeatherAqiModelList.add(new WeatherAqiModel(aqiInfo.o3, resources.getString(R.string.weather_o3)));
        }
        try {
            mWeatherAqiCircleView.setValue(Integer.valueOf(aqiInfo.aqi));
        } catch (NumberFormatException e) {
            mWeatherAqiCircleView.setValue(0);
        }
        mWeatherAqiCircleView.setAqiType(aqiInfo.aqiType);
        return this;
    }

    private void initLayoutParams() {
        int widthPixels = DeviceUtils.getWindowWidth(getContext());
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        lp.width = widthPixels - DensityUtils.dp2px(30);
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }

    @Override
    public void show() {
        if (mWeatherAqiModelList != null && !mWeatherAqiModelList.isEmpty()) {
            mAdapter.getItemModels().clear();
            mAdapter.getItemModels().addAll(mWeatherAqiModelList);
            mAdapter.notifyDataSetChanged();
        }
        super.show();
    }

}
