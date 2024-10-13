package com.pasc.business.weather.viewmodel;

import android.view.View;

import com.pasc.business.weather.R;
import com.pasc.business.weather.view.WeatherSevenDayView;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

import java.util.List;


public class WSevenDayForecastModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_7day;

    public List<WeatherSevenDayView.DayWeatherInfo> weatherInfos;

    public WSevenDayForecastModel(List<WeatherSevenDayView.DayWeatherInfo> weatherInfos) {
        this.weatherInfos = weatherInfos;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static class WSevenDayForecastViewHolder extends BaseHolder {
        WeatherSevenDayView weatherSevenDayView;

        public WSevenDayForecastViewHolder(View itemView) {
            super(itemView);
            weatherSevenDayView = itemView.findViewById(R.id.weather_seven_day_view);
        }
    }

    public static class WSevenDayForecastWorker extends SimpleMainWorker<WSevenDayForecastViewHolder, WSevenDayForecastModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WSevenDayForecastViewHolder createViewHolder(View itemView) {
            return new WSevenDayForecastViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WSevenDayForecastViewHolder viewHolder,
                                           WSevenDayForecastModel model) {
            viewHolder.weatherSevenDayView.setWeatherInfos(model.weatherInfos);
        }
    }
}
