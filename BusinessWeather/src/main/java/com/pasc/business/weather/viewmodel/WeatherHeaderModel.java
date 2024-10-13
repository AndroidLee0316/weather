package com.pasc.business.weather.viewmodel;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.business.weather.util.WeatherDefinition;
import com.pasc.business.weather.view.WeatherAqiDialog;
import com.pasc.lib.weather.data.WeatherAqiInfo;
import com.pasc.lib.weather.data.WeatherLiveInfo;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

import static com.pasc.business.weather.util.WeatherDefinition.WEATHER_TEMP_UNIT;

/**
 * Created by lishanshan790 on 2018/7/28.
 */

public class WeatherHeaderModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_header;

    public WeatherLiveInfo liveWeather;
    public WeatherAqiInfo aqiInfo;

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static final class WeatherHeaderHolder extends BaseHolder {
        TextView currentTempView;
        TextView currentTempUnitView;
        TextView weatherView;
        TextView feelView;
        RelativeLayout aqiLayout;
        TextView aqiValue;
        TextView aqi;
        TextView tempUpView;
        TextView tempDownView;

        public WeatherAqiInfo mAqiInfo;

        public WeatherHeaderHolder(View itemView) {
            super(itemView);
            currentTempView = itemView.findViewById(R.id.currentTemp);
            currentTempUnitView = itemView.findViewById(R.id.temp_unit);
            weatherView = itemView.findViewById(R.id.weather_state);
            feelView = itemView.findViewById(R.id.feel);
            aqiLayout = itemView.findViewById(R.id.aqi_layout);
            aqiValue = itemView.findViewById(R.id.aqi_value);
            aqi = itemView.findViewById(R.id.aqi);
            tempUpView = itemView.findViewById(R.id.temp_up);
            tempDownView = itemView.findViewById(R.id.temp_down);
            aqiLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new WeatherAqiDialog(view.getContext()).setAqiData(mAqiInfo).show();
                }
            });
        }

    }

    public static final class WeatherHeaderWorker
            extends SimpleMainWorker<WeatherHeaderHolder, WeatherHeaderModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WeatherHeaderHolder createViewHolder(View itemView) {
            return new WeatherHeaderHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WeatherHeaderHolder viewHolder,
                                           WeatherHeaderModel model) {
            if (model.liveWeather == null) return;
            viewHolder.itemView.setBackgroundResource(WeatherDefinition.getWeatherBg(model.liveWeather.weatherState));
            if (!TextUtils.isEmpty(model.liveWeather.tmp)){
                viewHolder.currentTempView.setText(model.liveWeather.tmp.replace(WEATHER_TEMP_UNIT, ""));
            }else {
                viewHolder.currentTempView.setText("");
            }
            viewHolder.currentTempUnitView.setVisibility(TextUtils.isEmpty(model.liveWeather.tmp) ? View.INVISIBLE : View.VISIBLE);
            String tempMaxMin = model.liveWeather.maxMin;
            String[] temps = null;
            if (!TextUtils.isEmpty(tempMaxMin)) {
                temps = tempMaxMin.replace(WEATHER_TEMP_UNIT, "").split("～");
            }
            if (temps != null) {
                if (temps.length > 0) {
                    setView(viewHolder.tempDownView, temps[0] + WEATHER_TEMP_UNIT);
                }
                if (temps.length > 1) {
                    setView(viewHolder.tempUpView, temps[1] + WEATHER_TEMP_UNIT);
                }
            }
            viewHolder.weatherView.setText(model.liveWeather.weatherState);
            viewHolder.feelView.setText(model.liveWeather.feel);

            if (model.aqiInfo == null || TextUtils.isEmpty(model.aqiInfo.aqi)) {
                viewHolder.aqiLayout.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.aqiLayout.setVisibility(View.VISIBLE);
                viewHolder.aqi.setText(model.aqiInfo.aqiType);
                viewHolder.aqiValue.setText(model.aqiInfo.aqi);
                int aqiBg = WeatherDefinition.getAqiBgIcon(model.aqiInfo.aqi);
                viewHolder.aqiValue.setCompoundDrawablesWithIntrinsicBounds(aqiBg, 0, 0, 0);
            }
            viewHolder.mAqiInfo = model.aqiInfo;
        }

        private void setView(TextView view, String text) {
            view.setVisibility(TextUtils.isEmpty(text) ? View.INVISIBLE : View.VISIBLE);
            view.setText(text);
        }
    }
}
