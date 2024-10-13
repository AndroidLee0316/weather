package com.pasc.business.weather.viewmodel;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.weather.R;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.weather.utils.WeatherDataManager;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

/**
 * Created by liss on 2018/8/16.
 */

public class WHourChildModel extends ItemModel {
    static final int LAYOUT_ID = R.layout.weather_item_24h_child;
    static final int COUNT = 6;

    public String time;
    public String weatherState;
    public String temp;
    public int width;


    public WHourChildModel(String time, String weatherState, String temp) {
        this.time = time;
        this.weatherState = weatherState;
        this.temp = temp;
        width = DensityUtils.getScreenWidth(AppProxy.getInstance().getContext()) / COUNT;
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static class W12HChildViewHolder extends BaseHolder {
        TextView timeView;
        ImageView iconView;
        TextView tempView;

        public W12HChildViewHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.time);
            iconView = itemView.findViewById(R.id.icon);
            tempView = itemView.findViewById(R.id.temp);
        }
    }

    public static class W12HChildWorker extends SimpleMainWorker<W12HChildViewHolder, WHourChildModel> {

        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public W12HChildViewHolder createViewHolder(View itemView) {
            return new W12HChildViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(W12HChildViewHolder viewHolder, WHourChildModel model) {
            Resources resources = viewHolder.iconView.getContext().getResources();
            String nowString = viewHolder.iconView.getContext().getResources().getString(R.string.weather_now);
            if (nowString.equals(model.time)) {
                viewHolder.timeView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                viewHolder.tempView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                int boldColor = AppProxy.getInstance().getContext().getResources().getColor(R.color.weather_primary_text);
                viewHolder.timeView.setTextColor(boldColor);
                viewHolder.tempView.setTextColor(boldColor);
            } else {
                viewHolder.timeView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                viewHolder.tempView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                int normalColor = AppProxy.getInstance().getContext().getResources().getColor(R.color.weather_secondary_text);
                viewHolder.timeView.setTextColor(normalColor);
                viewHolder.tempView.setTextColor(normalColor);
            }
            viewHolder.timeView.setText(model.time);
            viewHolder.iconView.setImageResource(WeatherDataManager.getInstance().getWeatherStateIcon(viewHolder.iconView.getContext(), model.weatherState));
            viewHolder.tempView.setText(model.temp);
            viewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(model.width, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
