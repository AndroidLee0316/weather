package com.pasc.business.weather.viewmodel;

import android.view.View;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

public class WeatherOtherChildModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_other_child;

    public String index;
    public String indexType;

    public WeatherOtherChildModel(String index, String indexType) {
        this.index = index;
        this.indexType = indexType;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static class WeatherOtherChildViewHolder extends BaseHolder {
        TextView indexView;
        TextView indexTypeView;

        public WeatherOtherChildViewHolder(View itemView) {
            super(itemView);
            indexTypeView = itemView.findViewById(R.id.index_brf);
            indexView = itemView.findViewById(R.id.index);
        }
    }

    public static class WeatherOtherChildWorker extends SimpleMainWorker<WeatherOtherChildViewHolder, WeatherOtherChildModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WeatherOtherChildViewHolder createViewHolder(View itemView) {
            return new WeatherOtherChildViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WeatherOtherChildViewHolder viewHolder, WeatherOtherChildModel model) {
            viewHolder.indexView.setText(model.index);
            viewHolder.indexTypeView.setText(model.indexType);
        }
    }
}
