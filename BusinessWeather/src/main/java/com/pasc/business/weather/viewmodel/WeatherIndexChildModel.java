package com.pasc.business.weather.viewmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.business.weather.util.WeatherDefinition;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

public class WeatherIndexChildModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_index_child;

    public String index;
    public String indexType;
    public int indexIcon;

    public WeatherIndexChildModel(String index, String indexType, int indexIcon) {
        this.index = index;
        this.indexType = indexType;
        this.indexIcon = indexIcon;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static class WeatherIndexChildViewHolder extends BaseHolder {
        TextView indexView;
        TextView indexTypeView;
        ImageView indexIconView;

        public WeatherIndexChildViewHolder(View itemView) {
            super(itemView);
            indexTypeView = itemView.findViewById(R.id.index_brf);
            indexView = itemView.findViewById(R.id.index);
            indexIconView = itemView.findViewById(R.id.index_icon);
        }
    }

    public static class WeatherIndexChildWorker extends SimpleMainWorker<WeatherIndexChildViewHolder, WeatherIndexChildModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WeatherIndexChildViewHolder createViewHolder(View itemView) {
            return new WeatherIndexChildViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WeatherIndexChildViewHolder viewHolder, WeatherIndexChildModel model) {
            viewHolder.indexView.setText(model.index);
            viewHolder.indexIconView.setImageResource(model.indexIcon);
            viewHolder.indexTypeView.setText(WeatherDefinition.getWeatherIndexbyType(model.indexType));
        }
    }
}
