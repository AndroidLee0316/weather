package com.pasc.business.weather.viewmodel;

import android.view.View;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

public class WeatherAqiModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_aqi;

    public String aqiValue;
    public String aqiName;
    public WeatherAqiModel(String aqiValue, String aqiName) {
        this.aqiName = aqiName;
        this.aqiValue = aqiValue;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static class AqiViewHolder extends BaseHolder {
        TextView aqiValueView;
        TextView aqiName;

        public AqiViewHolder(View itemView) {
            super(itemView);
            aqiValueView = itemView.findViewById(R.id.aqi_value);
            aqiName = itemView.findViewById(R.id.aqi_name);
        }
    }

    public static class AqiWorker extends SimpleMainWorker<AqiViewHolder, WeatherAqiModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public AqiViewHolder createViewHolder(View itemView) {
            return new AqiViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(AqiViewHolder viewHolder, WeatherAqiModel model) {
            viewHolder.aqiName.setText(model.aqiName);
            viewHolder.aqiValueView.setText(model.aqiValue);
        }
    }
}
