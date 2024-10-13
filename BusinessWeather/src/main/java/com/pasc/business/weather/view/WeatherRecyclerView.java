package com.pasc.business.weather.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.pasc.business.weather.viewmodel.WHourForecastModel;
import com.pasc.business.weather.viewmodel.WSevenDayForecastModel;
import com.pasc.business.weather.viewmodel.WeatherHeaderModel;
import com.pasc.business.weather.viewmodel.WeatherIndexModel;
import com.pasc.business.weather.viewmodel.WeatherOtherModel;
import com.pasc.business.weather.viewmodel.WeatherTitleModel;
import com.pasc.lib.widget.seriesadapter.base.ISeriesPresenter;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SeriesAdapter;
import com.pasc.lib.widget.seriesadapter.base.SeriesPresenter;

import java.util.List;

/**
 * Created by lishanshan790 on 2018/7/28.
 */

public class WeatherRecyclerView extends RecyclerView {
    private SeriesAdapter seriesAdapter;
    private LinearLayoutManager layoutManager;

    public WeatherRecyclerView(Context context) {
        super(context);
    }

    public WeatherRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setItemAnimator(null);
        setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        ISeriesPresenter presenter = new SeriesPresenter.Builder()
                .addWorker(new WeatherHeaderModel.WeatherHeaderWorker())
                .addWorker(new WHourForecastModel.W12HForecastWorker())
                .addWorker(new WeatherTitleModel.TitleWorker())
                .addWorker(new WeatherIndexModel.WeatherIndexWorker())
                .addWorker(new WSevenDayForecastModel.WSevenDayForecastWorker())
                .addWorker(new WeatherOtherModel.WeatherOtherWorker())
                .build();
        seriesAdapter = new SeriesAdapter(presenter);
        setAdapter(seriesAdapter);
    }

    public void setItems(List<ItemModel> items) {
        if (seriesAdapter != null) {
            seriesAdapter.updateItems(items);
        }
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }
}
