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

public class WeatherIndexModel extends ItemModel {
    private static final int COUNT = 3;

    static final int LAYOUT_ID = R.layout.weather_item_index;

    private List<ItemModel> itemModels = new ArrayList<>();

    public WeatherIndexModel(List<ItemModel> itemModels) {
        this.itemModels.clear();
        this.itemModels.addAll(itemModels);
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static final class WeatherIndexViewHolder extends BaseHolder {
        RecyclerView recyclerView;
        public SeriesAdapter adapter;

        public WeatherIndexViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), COUNT));
            ISeriesPresenter presenter = new SeriesPresenter.Builder()
                    .addWorker(new WeatherIndexChildModel.WeatherIndexChildWorker())
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

    public static final class WeatherIndexWorker extends SimpleMainWorker<WeatherIndexViewHolder, WeatherIndexModel> {
        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WeatherIndexViewHolder createViewHolder(View itemView) {
            return new WeatherIndexViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WeatherIndexViewHolder viewHolder,
                                           WeatherIndexModel model) {
            viewHolder.updateItems(model.itemModels);
        }
    }
}
