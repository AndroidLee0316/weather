package com.pasc.business.weather.viewmodel;

import android.view.View;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

/**
 * Created by liss on 2018/8/16.
 */

public class WeatherTitleModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_title;

    public String title;

    public WeatherTitleModel(String title) {
        this.title = title;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static final class TitleViewHolder extends BaseHolder {
        TextView titleView;
        public TitleViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title_view);
        }
    }

    public static final class TitleWorker extends SimpleMainWorker<TitleViewHolder, WeatherTitleModel> {
        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public TitleViewHolder createViewHolder(View itemView) {
            return new TitleViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(TitleViewHolder viewHolder, WeatherTitleModel model) {
            viewHolder.titleView.setText(model.title);
        }
    }
}
