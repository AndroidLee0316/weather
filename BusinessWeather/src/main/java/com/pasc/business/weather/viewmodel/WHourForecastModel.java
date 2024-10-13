package com.pasc.business.weather.viewmodel;

import android.support.v7.widget.LinearLayoutManager;
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

public class WHourForecastModel extends ItemModel {

    static final int LAYOUT_ID = R.layout.weather_item_24h;

    private List<ItemModel> itemModels = new ArrayList<>();

    public WHourForecastModel(List<ItemModel> itemModels){
        this.itemModels.clear();
        this.itemModels.addAll(itemModels);
    }

    @Override
    public int layoutId() {
        return LAYOUT_ID;
    }

    public static final class WHourForecastViewHolder extends BaseHolder {
        RecyclerView recyclerView;
        public SeriesAdapter adapter;

        public WHourForecastViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            ISeriesPresenter presenter = new SeriesPresenter.Builder()
                    .addWorker(new WHourChildModel.W12HChildWorker())
                    .build();
            adapter = new SeriesAdapter(presenter);
            recyclerView.setAdapter(adapter);
        }

        public void updateItems(List<ItemModel> items){
            adapter.getItemModels().clear();
            adapter.getItemModels().addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    public static final class W12HForecastWorker extends SimpleMainWorker<WHourForecastViewHolder, WHourForecastModel> {
        @Override
        public int type() {
            return LAYOUT_ID;
        }

        @Override
        public WHourForecastViewHolder createViewHolder(View itemView) {
            return new WHourForecastViewHolder(itemView);
        }

        @Override
        public void bindViewHolderAndModel(WHourForecastViewHolder viewHolder,
                                           WHourForecastModel model) {
            viewHolder.updateItems(model.itemModels);
        }
    }
}
