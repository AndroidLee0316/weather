package com.pasc.business.weather.viewmodel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pasc.business.weather.R;
import com.pasc.lib.widget.seriesadapter.base.BaseHolder;
import com.pasc.lib.widget.seriesadapter.base.ISeriesPresenter;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.seriesadapter.base.SeriesAdapter;
import com.pasc.lib.widget.seriesadapter.base.SeriesPresenter;
import com.pasc.lib.widget.seriesadapter.base.SimpleMainWorker;

import java.util.ArrayList;
import java.util.List;

public class WeatherOtherModel extends ItemModel {
    private static final int COUNT = 4;

    static final int LAYOUT_ID = R.layout.weather_item_other;

    private List<ItemModel> itemModels = new ArrayList<>();

    public WeatherOtherModel(List<ItemModel> itemModels) {
        this.itemModels.clear();
        this.itemModels.addAll(itemModels);
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static final class WeatherOtherViewHolder extends BaseHolder {
        RecyclerView recyclerView;
        public SeriesAdapter adapter;

        public WeatherOtherViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), COUNT));
            ISeriesPresenter presenter = new SeriesPresenter.Builder()
                    .addWorker(new WeatherOtherChildModel.WeatherOtherChildWorker())
                    .build();
            adapter = new SeriesAdapter(presenter);
            recyclerView.setAdapter(adapter);
        }

        public void updateItems(List<ItemModel> items) {
            adapter.getItemModels().clear();
            adapter.getItemModels().addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    public static final class WeatherOtherWorker extends SimpleMainWorker<WeatherOtherViewHolder, WeatherOtherModel> {
        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WeatherOtherViewHolder createViewHolder(View itemView) {
            return new WeatherOtherViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WeatherOtherViewHolder viewHolder,
                                           WeatherOtherModel model) {
            viewHolder.updateItems(model.itemModels);
        }
    }
}
