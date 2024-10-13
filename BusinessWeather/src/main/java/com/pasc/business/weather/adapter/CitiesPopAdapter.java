package com.pasc.business.weather.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pasc.business.weather.R;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.utils.WeatherDataManager;

import java.util.List;

public class CitiesPopAdapter extends BaseQuickAdapter<WeatherCityInfo, BaseViewHolder> {

    public CitiesPopAdapter(List<WeatherCityInfo> data) {
        super(R.layout.weather_item_city, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherCityInfo item) {
        WeatherCityInfo currentCity = WeatherDataManager.getInstance().getCurrentSelectedCity();
        boolean isSelected = true;
        if (currentCity == null || currentCity.isLocation() || !item.getShowName().equals(currentCity.getShowName())) {
            isSelected = false;
        }
        int color = mContext.getResources().getColor(isSelected ? R.color.weather_city_text_selected : R.color.weather_city_text_normal);
        int background = isSelected ? R.drawable.weather_city_item_bg_selected : R.drawable.weather_city_item_bg_normal;
        helper.setTextColor(R.id.city_name, color);
        helper.setBackgroundRes(R.id.city_name, background);
        helper.setText(R.id.city_name, item.getSimpleShowName());
    }
}
