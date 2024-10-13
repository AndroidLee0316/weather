package com.pasc.business.weather.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pasc.business.weather.R;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.utils.WeatherDataManager;

import java.util.List;

public class CityListPopView extends PopupWindow {

    private BaseQuickAdapter mAdapter;
    private LayoutInflater layoutInflater;
    private RecyclerView rvCityListView;
    private int mWidth;
    private View popupWindowView;
    private TextView mRestartLocationTv;
    private TextView mLocationFailedTv;
    private TextView mLocationLoading;
    private TextView mLocationCityTv;
    private ProgressBar mProgressView;
    private IPopWindow mCallback;
    private WeatherCityInfo mLocationInfo;//定位当前城市
    private Context mContext;

    public CityListPopView(Activity context, int width, BaseQuickAdapter adapter, IPopWindow callback) {
        super(context);
        mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWidth = width;
        mAdapter = adapter;
        mCallback = callback;
        initView(context);
    }

    private void initView(Context context) {
        popupWindowView = layoutInflater.inflate(R.layout.weather_citylist_pop, null);
        rvCityListView = (RecyclerView) popupWindowView.findViewById(R.id.city_list_recyclerview);
        mRestartLocationTv = (TextView) popupWindowView.findViewById(R.id.restart_location);
        mLocationCityTv = (TextView) popupWindowView.findViewById(R.id.location_city);
        mLocationLoading = (TextView) popupWindowView.findViewById(R.id.location_loading);
        mLocationFailedTv = (TextView) popupWindowView.findViewById(R.id.location_failed);
        mProgressView = (ProgressBar) popupWindowView.findViewById(R.id.location_progress);

        setContentView(popupWindowView);
        setWidth(mWidth);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        rvCityListView.setLayoutManager(new GridLayoutManager(context, 4));
        rvCityListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<WeatherCityInfo> data = adapter.getData();
                WeatherCityInfo cityInfo = data.get(position);
                if (cityInfo == null) {
                    return;
                }
                dismiss();
                if (mCallback != null) {
                    mCallback.onCityClick(cityInfo);
                }
            }
        });

        mLocationCityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mCallback != null) {
                    mCallback.onCityClick(mLocationInfo);
                }
            }
        });

        mRestartLocationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.doLocation();
                }
            }
        });
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = popupWindowView.findViewById(R.id.pop_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP && y > height) {
                    dismiss();
                }
                return true;
            }
        });
    }

    public CityListPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public void showAsDropDownOnN(View view) {
        if (Build.VERSION.SDK_INT == 24) {
            int[] a = new int[2];
            view.getLocationInWindow(a);
            showAtLocation(view, 0, 0, a[1] + view.getHeight());
        } else {
            super.showAsDropDown(view);
        }

    }

    public void doLocationStart() {
        mLocationCityTv.setVisibility(View.GONE);
        mLocationFailedTv.setVisibility(View.GONE);
        mLocationLoading.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void setLocation(boolean isLocating, WeatherCityInfo locationInfo) {
        mLocationInfo = locationInfo;
        if (isLocating) {
            doLocationStart();
            return;
        }
        if (locationInfo == null || TextUtils.isEmpty(locationInfo.getCity())) {
            mLocationFailedTv.setText("定位失败");
            mLocationFailedTv.setVisibility(View.VISIBLE);
            mLocationCityTv.setVisibility(View.GONE);
        } else {
            mLocationCityTv.setText(locationInfo.getShowName());
            WeatherCityInfo currentInfo = WeatherDataManager.getInstance().getCurrentSelectedCity();
            boolean isSelected = true;
            if (currentInfo == null || !currentInfo.isLocation() || !locationInfo.getShowName().equals(currentInfo.getShowName())) {
                isSelected = false;
            }

            int color = isSelected ? R.color.weather_city_text_selected : R.color.weather_city_text_normal;
            int background = isSelected ? R.drawable.weather_city_item_bg_selected : R.drawable.weather_city_item_bg_normal;
            mLocationCityTv.setTextColor(mContext.getResources().getColor(color));
            mLocationCityTv.setBackgroundResource(background);
            mLocationFailedTv.setVisibility(View.GONE);
            mLocationCityTv.setVisibility(View.VISIBLE);
        }
        mProgressView.setVisibility(View.GONE);
        mLocationLoading.setVisibility(View.GONE);
    }

    public interface IPopWindow {
        void onCityClick(WeatherCityInfo cityInfo);

        void doLocation();
    }
}
