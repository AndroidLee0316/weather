package com.pasc.business.weather;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.business.weather.adapter.CitiesPopAdapter;
import com.pasc.business.weather.router.RouterPath;
import com.pasc.business.weather.util.RxJavaWeatherDataUtil;
import com.pasc.business.weather.util.WeatherDefinition;
import com.pasc.business.weather.util.WeatherTool;
import com.pasc.business.weather.util.WeatherUIManager;
import com.pasc.business.weather.view.CityListPopView;
import com.pasc.business.weather.view.WeatherRecyclerView;
import com.pasc.business.weather.view.WeatherSevenDayView.DayWeatherInfo;
import com.pasc.business.weather.viewmodel.WHourChildModel;
import com.pasc.business.weather.viewmodel.WHourForecastModel;
import com.pasc.business.weather.viewmodel.WSevenDayForecastModel;
import com.pasc.business.weather.viewmodel.WeatherHeaderModel;
import com.pasc.business.weather.viewmodel.WeatherIndexChildModel;
import com.pasc.business.weather.viewmodel.WeatherIndexModel;
import com.pasc.business.weather.viewmodel.WeatherOtherChildModel;
import com.pasc.business.weather.viewmodel.WeatherOtherModel;
import com.pasc.business.weather.viewmodel.WeatherTitleModel;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.permission.PermissionUtils;
import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.base.util.NetworkUtils;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.lbs.LbsManager;
import com.pasc.lib.lbs.location.LocationException;
import com.pasc.lib.lbs.location.PascLocationListener;
import com.pasc.lib.lbs.location.bean.PascLocationData;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.weather.data.WeatherDetailsInfo;
import com.pasc.lib.weather.data.WeatherForecastInfo;
import com.pasc.lib.weather.data.WeatherHourForecastInfo;
import com.pasc.lib.weather.data.WeatherIndexOfLife;
import com.pasc.lib.weather.data.WeatherLiveInfo;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.utils.WeatherDataManager;
import com.pasc.lib.widget.dialognt.LoadingDialog;
import com.pasc.lib.widget.seriesadapter.base.ItemModel;
import com.pasc.lib.widget.toast.Toasty;
import com.pasc.lib.widget.toolbar.PascToolbar;
import com.pasc.lib.widget.viewcontainer.ViewContainer;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.pasc.business.weather.util.WeatherDefinition.WEATHER_LOG_TAG;
import static com.pasc.business.weather.util.WeatherUIManager.CITY_NAME_SPLIT;

@Route(path = RouterPath.PATH_WEATHER_DETAIL_ACTIVITY)
public class WeatherDetailsActivity extends BaseActivity {
    private static final String TAG = "WeatherDetailsActivity";
    public static final String CITY = "city";
    public static final String CITY_INFO = "cityInfo";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ISLOCATION = "isLocation";
    public static final String CITY_NAME = "cityName";
    public static final String DISTRICT_NAME = "districtName";
    public static final String SHOW_NAME = "showName";
    public static final String TITLE_CITY_NAME_END = "...";
    public static final int TITLE_CITY_NAME_MAX = 8;


    WeatherRecyclerView mWeatherRecyclerView;
    private List<ItemModel> mWeatherItemLists;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private WeatherCityInfo mCurrentCityInfo;
    private WeatherCityInfo mLocationCityInfo;
    private boolean mIsShowCityPop;
    private CityListPopView mSelectPopupWindow;
    public CitiesPopAdapter mCitiesPopAdapter;

    private List<WeatherCityInfo> mSelectCityItems;
    private String[] mCitiesList;
    private boolean mIsLocating = false;//是否正在定位
    private boolean mIsTitileShowWeather = false;
    private String mTitleWeather;
    private LoadingDialog mWeatherLoadingDialog;

    //update
    public static final String ARG_TITLE = "title"; //外部传入参数：标题

    private PascToolbar mToolbar;
    private ViewContainer mViewContainer;

    @Override
    protected int layoutResId() {
        return R.layout.weather_detail_activity;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        StatusBarUtils.setStatusBarColor(this, true);
        setContentView(R.layout.weather_detail_activity);
        mToolbar = findViewById(R.id.ctv_title);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override public void accept(Throwable throwable) throws Exception {

            }
        });
        if (!WeatherUIManager.getInstance().isEnable()) {
            finish();
            return;
        }
        boolean toolbarEnable = true;

        // 如果标题为空，则设置Activity的title
        if (toolbarEnable) {
            mToolbar.setVisibility(View.VISIBLE);

            // 设置标题，途径1：在AndroidManifest.xml里设置；途径2：通过外部传入参数title设置；途径3：代码设置。
            String title = null;
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                title = extras.getString(ARG_TITLE);
            }
            if (TextUtils.isEmpty(title) && mToolbar.getTitle() != null) {
                title = mToolbar.getTitle().toString();
            }
            if (TextUtils.isEmpty(title)) {
                title = getTitle().toString();
            }
            mToolbar.setTitle(title);

        } else {
            mToolbar.setVisibility(View.GONE);
        }
        int drawableId = WeatherUIManager.getInstance().getmBackDrawableIcon();
        if (drawableId > 0) {
            mToolbar.addLeftImageButton(drawableId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            mToolbar.addCloseImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initViewContainer();
    }

    private void initViewContainer() {
        mViewContainer = findViewById(R.id.view_container);
        mViewContainer.setOnReloadCallback(new ViewContainer.OnReloadCallback() {
            @Override
            public void reload() {
                mWeatherItemLists.clear();
                mWeatherRecyclerView.setItems(mWeatherItemLists);
                getWeatherDetailInfo(mCurrentCityInfo, false);
            }
        });
        mViewContainer.showContent(R.id.weather_content);
    }

    private PascLocationListener mPascLocationListener = new PascLocationListener() {
        @Override
        public void onLocationSuccess(PascLocationData data) {
            mIsLocating = false;
            handleLocationCity(data);
        }

        @Override
        public void onLocationFailure(LocationException e) {
            mIsLocating = false;
            mLocationCityInfo = null;
            SPUtils.getInstance().setParam(SPUtils.LOCATION_CITY, "");
            if (mSelectPopupWindow != null && mSelectPopupWindow.isShowing()) {
                mSelectPopupWindow.setLocation(mIsLocating, mLocationCityInfo);
            }
        }
    };

    public static final void start(Activity context, String city) {
        Intent intent = new Intent(context, WeatherDetailsActivity.class);
        intent.putExtra(CITY, city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String city = getIntent().getStringExtra(CITY);

        if (TextUtils.isEmpty(city)) {
            mCurrentCityInfo = (WeatherCityInfo) getIntent().getSerializableExtra(CITY_INFO);
            if (mCurrentCityInfo == null) {
                String cityName = getIntent().getStringExtra(CITY_NAME);
                if (TextUtils.isEmpty(cityName)) {
                    finish();
                    return;
                } else {
                    mCurrentCityInfo = new WeatherCityInfo(cityName);
                    mCurrentCityInfo.setShowName(getIntent().getStringExtra(SHOW_NAME));
                    mCurrentCityInfo.setDistrict(getIntent().getStringExtra(DISTRICT_NAME));
                    mCurrentCityInfo.setIsLocation(getIntent().getBooleanExtra(ISLOCATION, false));
                    mCurrentCityInfo.setLatitude(getIntent().getDoubleExtra(LATITUDE, 0));
                    mCurrentCityInfo.setLongitude(getIntent().getDoubleExtra(LONGITUDE, 0));
                }
            }
        } else {
            mCurrentCityInfo = new WeatherCityInfo(city);
        }
        WeatherDataManager.getInstance().saveCurrentSelectedCity(mCurrentCityInfo);
        if (mCurrentCityInfo.isLocation()) {
            mLocationCityInfo = mCurrentCityInfo;
            SPUtils.getInstance().setParam(SPUtils.LOCATION_CITY, mLocationCityInfo.toSaveSPString());
        } else {
            mLocationCityInfo = WeatherCityInfo.toObject((String) SPUtils.getInstance().getParam(SPUtils.LOCATION_CITY, ""));
        }
        initView();
        mWeatherItemLists = new ArrayList<>();
        getWeatherDetailsInfoFromCache(mCurrentCityInfo, false);

        PascLog.i(WeatherDefinition.WEATHER_LOG_TAG, "city = " + mCurrentCityInfo.toString());
        getWeatherDetailInfo(mCurrentCityInfo, false);
    }

    private void initView() {
        mWeatherRecyclerView = findViewById(R.id.weather_recyclerview);
        mWeatherRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
                    int pos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    if (pos == 0 && mIsTitileShowWeather) {
                        mIsTitileShowWeather = false;
                    } else if (pos != 0 && !mIsTitileShowWeather) {
                        mIsTitileShowWeather = true;
                    }
                    setTitleText(mIsTitileShowWeather);
                    mToolbar.enableUnderDivider(mIsTitileShowWeather);
                }
            }
        });
        mCitiesList = WeatherUIManager.getInstance().getCitiesList();
        mToolbar.setTitle(getTitleCityName(mCurrentCityInfo.getShowName()));
        if (mCitiesList != null && mCitiesList.length > 0) {
            mToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCitiesPop();
                }
            });
            if (mToolbar.getTitleView() != null) {
                mToolbar.getTitleView().setCompoundDrawablePadding(DensityUtils.dp2px(4));
                mToolbar.getTitleView().setMinEms(0);
            }
            setTitleStyle();
        }
    }

    private void setTitleText(boolean isShowWeather) {
        String title;
        if (isShowWeather && !TextUtils.isEmpty(mTitleWeather)) {
            title = mTitleWeather;
        } else {
            title = getTitleCityName(mCurrentCityInfo.getShowName());
        }
        mToolbar.setTitle(title);
    }

    private String getTitleCityName(String name) {
        return WeatherTool.getSubString(name, TITLE_CITY_NAME_MAX, TITLE_CITY_NAME_END);
    }

    private void showCitiesPop() {
        mIsShowCityPop = !mIsShowCityPop;
        setTitleStyle();
        if (mSelectPopupWindow == null) {
            mCitiesPopAdapter = new CitiesPopAdapter(mSelectCityItems);
            mSelectPopupWindow = new CityListPopView(this, ViewGroup.LayoutParams.MATCH_PARENT, mCitiesPopAdapter, new CityListPopView.IPopWindow() {

                @Override
                public void onCityClick(WeatherCityInfo cityInfo) {
                    if (cityInfo != null) {
                        String showName = cityInfo.getShowName();
                        boolean isDiffShowName = TextUtils.isEmpty(showName) || !showName.equals(mCurrentCityInfo.getShowName());
                        boolean ischange = isDiffShowName || cityInfo.isLocation() != mCurrentCityInfo.isLocation();
                        if (ischange) {
                            mCurrentCityInfo = cityInfo;
                            WeatherDataManager.getInstance().saveCurrentSelectedCity(mCurrentCityInfo);
                            mToolbar.setTitle(getTitleCityName(cityInfo.getShowName()));
                            getWeatherDetailInfo(mCurrentCityInfo, false);
                        }
                    }
                }

                @Override
                public void doLocation() {
                    checkLocPermission();
                }
            });
            mSelectPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(mTitleWeather) || mIsTitileShowWeather) {
                        mToolbar.enableUnderDivider(true);
                    } else {
                        mToolbar.enableUnderDivider(false);
                    }
                    mIsShowCityPop = false;
                    setTitleStyle();
                }
            });
        }
        mCitiesPopAdapter.notifyDataSetChanged();
        mSelectPopupWindow.setLocation(mIsLocating, mLocationCityInfo);
        mSelectPopupWindow.showAsDropDownOnN(mToolbar);
        mToolbar.enableUnderDivider(true);
    }


    /**
     * 设置标题头样式
     */
    private void setTitleStyle() {
        int selectDrawable = WeatherUIManager.getInstance().getmToolbarSelectDrawableIcon();
        int defaultDrawable = WeatherUIManager.getInstance().getmToolbarDefaultDrawableIcon();
        if (selectDrawable <= 0) {
            selectDrawable = R.drawable.weather_city_list_icon_normal;
        }
        if (defaultDrawable <= 0) {
            defaultDrawable = R.drawable.weather_city_list_icon_selected;
        }
        Drawable drawable = getResources().getDrawable(
                mIsShowCityPop ? selectDrawable : defaultDrawable);
        if (mToolbar.getTitleView() != null) {
            mToolbar.getTitleView().setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        if (mIsShowCityPop) {
            mToolbar.setTitle(getTitleCityName(mCurrentCityInfo.getShowName()));
        } else {
            setTitleText(mIsTitileShowWeather);
        }
        if (mSelectCityItems == null) {
            initSelectCities();
        }
    }

    private void getWeatherDetailInfo(final WeatherCityInfo cityInfo, final boolean isLocationAction) {
        PascLog.d(WEATHER_LOG_TAG, TAG + " getWeatherDetailInfo " + mCurrentCityInfo);
        if (cityInfo == null) {
            return;
        }
        if (isFinishing()) {
            return;
        }
        if (!NetworkUtils.isNetworkAvailable()) {
            getWeatherDetailsInfoFromCache(cityInfo, true);
            return;
        }
        if (!isLocationAction) {
            showLoading();
        }
        mDisposables.add(RxJavaWeatherDataUtil.getWeatherDetailsInfoFromNet(cityInfo)
                .subscribe(new Consumer<WeatherDetailsInfo>() {
                    @Override
                    public void accept(WeatherDetailsInfo weatherDetailsInfo) throws Exception {
                        setWeatherInfo(weatherDetailsInfo);
                        doAfterGetWeatherFromNet(isLocationAction, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getWeatherDetailsInfoFromCache(cityInfo, true);
                        doAfterGetWeatherFromNet(isLocationAction, false);
                    }
                }));
    }

    private void doAfterGetWeatherFromNet(boolean isLocationAction, boolean isSucess) {
        if (isLocationAction) {
            if (mSelectPopupWindow != null && mSelectPopupWindow.isShowing()) {
                mSelectPopupWindow.dismiss();
            }
            setTitleStyle();
            Toasty.init(getApplicationContext()).setIconRes(R.drawable.weather_location_sucess_icon)
                    .setMessage("定位成功").show();
        } else {
            dismissLoading();
        }
    }

    private void getWeatherDetailsInfoFromCache(WeatherCityInfo cityInfo, final boolean isHandleError) {
        if (cityInfo == null) {
            return;
        }
        mDisposables.add(RxJavaWeatherDataUtil.getWeatherDetailsInfoFromCache(cityInfo)
                .subscribe(new Consumer<WeatherDetailsInfo>() {
                    @Override
                    public void accept(WeatherDetailsInfo weatherDetailsInfo) throws Exception {
                        setWeatherInfo(weatherDetailsInfo);
                        if (!NetworkUtils.isNetworkAvailable()) {
                            Toasty.init(getApplicationContext())
                                    .setMessage(getResources().getString(R.string.weather_network_unavailable)).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isHandleError) {
                            showEmpty();
                        }
                    }
                }));
    }

    private void showEmpty() {
        mIsTitileShowWeather = false;
        mTitleWeather = null;
        mToolbar.enableUnderDivider(true);
        if (NetworkUtils.isNetworkAvailable()) {
            mViewContainer.setErrorMessage(R.string.weather_no_weather_info_tip);
            mViewContainer.showError();
        } else {
            mViewContainer.showNetworkError();
        }
    }

    private void setWeatherInfo(WeatherDetailsInfo weatherDetailsInfo) {
        if (weatherDetailsInfo == null) {
            mIsTitileShowWeather = false;
            mTitleWeather = null;
            mViewContainer.showEmpty();
            mToolbar.enableUnderDivider(true);
            return;
        }
        WeatherLiveInfo liveInfo = weatherDetailsInfo.getLiveInfo();
        if (liveInfo != null) {
            mTitleWeather = getTitleCityName(mCurrentCityInfo.getShowName()) + " " + liveInfo.tmp + " " + liveInfo.weatherState;
        } else {
            mTitleWeather = null;
        }
        mViewContainer.showContent(R.id.weather_content);
        mWeatherItemLists.clear();
        //头部
        setHeaderModel(weatherDetailsInfo);

        //24 小时
        if (WeatherUIManager.getInstance().showPredictionOf24Hours()) {
            setHourForecastModel(weatherDetailsInfo);
        }
        //七天预报
        if (WeatherUIManager.getInstance().showPredictionOf7Days()) {
            setSevenDayModel(weatherDetailsInfo);
        }
        //生活指数
        if (WeatherUIManager.getInstance().showIndexOfLiving()) {
            setWeatherIndexModel(weatherDetailsInfo);
        }
        //其他指数
        if (WeatherUIManager.getInstance().showIndexOfOthers()) {
            setWeatherOtherModel(weatherDetailsInfo.getLiveInfo());
        }
        mWeatherRecyclerView.setItems(mWeatherItemLists);
        mWeatherRecyclerView.scrollToPosition(0);
        mToolbar.enableUnderDivider(false);
    }

    private void setHeaderModel(WeatherDetailsInfo weatherDetailsInfo) {
        WeatherLiveInfo liveInfo = weatherDetailsInfo.getLiveInfo();
        if (liveInfo != null) {
            WeatherHeaderModel weatherHeaderModel = new WeatherHeaderModel();
            weatherHeaderModel.liveWeather = liveInfo;
            weatherHeaderModel.aqiInfo = weatherDetailsInfo.getAqiInfo();
            mWeatherItemLists.add(weatherHeaderModel);
        }
    }

    private void setHourForecastModel(WeatherDetailsInfo weatherDetailsInfo) {
        List<WeatherHourForecastInfo> hourForecastInfos = weatherDetailsInfo.getHourForecastInfos();
        WeatherLiveInfo liveInfo = weatherDetailsInfo.getLiveInfo();
        if (hourForecastInfos != null && hourForecastInfos.size() > 0) {
            mWeatherItemLists.add(new WeatherTitleModel(getResources().getString(R.string.weather_24_hour)));

            List<ItemModel> wHourChildModels = new ArrayList<>();
            if (liveInfo != null) {
                WHourChildModel newHourChildModel =
                        new WHourChildModel(getResources().getString(R.string.weather_now), liveInfo.weatherState, liveInfo.tmp);
                wHourChildModels.add(newHourChildModel);
            }

            for (WeatherHourForecastInfo hourWeather : hourForecastInfos) {
                WHourChildModel wHourChildModel =
                        new WHourChildModel(hourWeather.hour, hourWeather.weatherState,
                                hourWeather.tmp);
                wHourChildModels.add(wHourChildModel);
            }
            WHourForecastModel wHourForecastModel = new WHourForecastModel(wHourChildModels);
            mWeatherItemLists.add(wHourForecastModel);
        }
    }

    private void setSevenDayModel(WeatherDetailsInfo weatherDetailsInfo) {
        List<WeatherForecastInfo> sevenDayInfoList = weatherDetailsInfo.getSevenDayInfoList();
        if (sevenDayInfoList != null && sevenDayInfoList.size() > 2) {
            mWeatherItemLists.add(new WeatherTitleModel(sevenDayInfoList.size()+getResources().getString(R.string.weather_seven_day)));
            final List<DayWeatherInfo> weathers = new ArrayList<>();
            WSevenDayForecastModel wSevenDayForecastModel = new WSevenDayForecastModel(weathers);
            mWeatherItemLists.add(wSevenDayForecastModel);

            PascLog.d(WEATHER_LOG_TAG, "sevenDayInfoList.size = " + sevenDayInfoList.size());
            for (WeatherForecastInfo info : sevenDayInfoList) {
                int icon = WeatherDataManager.getInstance().getWeatherStateIcon(this, info.weatherState);
                DayWeatherInfo weatherInfo =
                        new DayWeatherInfo(info.date, info.time, info.tmp_max, info.tmp_min, info.weatherState, icon);
                wSevenDayForecastModel.weatherInfos.add(weatherInfo);
            }
        }

    }

    private void setWeatherIndexModel(WeatherDetailsInfo weatherDetailsInfo) {
        List<WeatherIndexOfLife> indexOfLifeList = weatherDetailsInfo.getIndexofLifes();
        if (indexOfLifeList != null && indexOfLifeList.size() > 0) {
            // 12小时
            mWeatherItemLists.add(new WeatherTitleModel(getResources().getString(R.string.weather_indexoflife)));

            List<ItemModel> indexChildModelList = new ArrayList<>();

            for (WeatherIndexOfLife indexOfLife : indexOfLifeList) {
                if (WeatherDefinition.isIndexVisiable(indexOfLife.type)) {
                    WeatherIndexChildModel indexChildModel =
                            new WeatherIndexChildModel(indexOfLife.brf, indexOfLife.type, WeatherDefinition.getWeatherIndexIconByTpye(indexOfLife.type));
                    indexChildModelList.add(indexChildModel);
                }
            }
            WeatherIndexModel weatherIndexModel = new WeatherIndexModel(indexChildModelList);
            mWeatherItemLists.add(weatherIndexModel);
        }
    }

    private void setWeatherOtherModel(WeatherLiveInfo liveInfo) {
        if (liveInfo != null) {
            List<ItemModel> childModelList = new ArrayList<>();
            if (!TextUtils.isEmpty(liveInfo.wind_dir)) {
                String wind = liveInfo.wind_dir.replace("风风", "风");
                WeatherOtherChildModel childModel = new WeatherOtherChildModel(wind + liveInfo.wind_sc, "风力");
                childModelList.add(childModel);
            }

            if (!TextUtils.isEmpty(liveInfo.hum)) {
                WeatherOtherChildModel childModel = new WeatherOtherChildModel(liveInfo.hum, "湿度");
                childModelList.add(childModel);
            }

            if (!TextUtils.isEmpty(liveInfo.vis)) {
                WeatherOtherChildModel childModel = new WeatherOtherChildModel(liveInfo.vis, "能见度");
                childModelList.add(childModel);
            }

            if (!TextUtils.isEmpty(liveInfo.pres)) {
                WeatherOtherChildModel childModel = new WeatherOtherChildModel(liveInfo.pres, "气压");
                childModelList.add(childModel);
            }

            if (childModelList.size() > 0) {
                mWeatherItemLists.add(new WeatherTitleModel(getResources().getString(R.string.weather_other)));
                WeatherOtherModel otherModel = new WeatherOtherModel(childModelList);
                mWeatherItemLists.add(otherModel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LbsManager.getInstance().stopLocation(0, mPascLocationListener);
        mDisposables.clear();
    }

    private void initSelectCities() {
        mSelectCityItems = new ArrayList<>();
        for (String cityString : mCitiesList) {
            cityString = cityString.trim();
            WeatherCityInfo item = null;
            if (cityString.contains(":")) {
                String[] list = cityString.split(CITY_NAME_SPLIT);
                final int length = list.length;
                if (length == 3) {
                    if (!TextUtils.isEmpty(list[0])) {
                        item = new WeatherCityInfo(list[0]);
                        item.setDistrict(list[1]);
                        item.setShowName(list[length - 1]);
                    }
                }
            } else {
                item = new WeatherCityInfo(cityString);
            }
            if (item != null && !mSelectCityItems.contains(item)) {
                mSelectCityItems.add(item);
            }
        }
    }

    private void doLocation() {
        mIsLocating = true;
        if (mSelectPopupWindow != null && mSelectPopupWindow.isShowing()) {
            mSelectPopupWindow.doLocationStart();
        }
        LbsManager.getInstance().doLocation(0, mPascLocationListener);
    }

    private void handleLocationCity(PascLocationData location) {
        boolean isSuccess = location != null && !TextUtils.isEmpty(location.getCity());
        mLocationCityInfo = null;
        if (isSuccess) {
            mLocationCityInfo = new WeatherCityInfo(location.getCity());
            mLocationCityInfo.setDistrict(location.getDistrict());
            mLocationCityInfo.setIsLocation(true);
            mLocationCityInfo.setLatitude(location.getLatitude());
            mLocationCityInfo.setLongitude(location.getLongitude());
            SPUtils.getInstance().setParam(SPUtils.LOCATION_CITY, mLocationCityInfo.toSaveSPString());
            mCurrentCityInfo = mLocationCityInfo;
            WeatherDataManager.getInstance().saveCurrentSelectedCity(mCurrentCityInfo);
            getWeatherDetailInfo(mCurrentCityInfo, true);
        } else {
            SPUtils.getInstance().setParam(SPUtils.LOCATION_CITY, "");
        }

        if (mSelectPopupWindow != null && mSelectPopupWindow.isShowing()) {
            if (isSuccess) {
                mSelectPopupWindow.dismiss();
            } else {
                mSelectPopupWindow.setLocation(mIsLocating, mLocationCityInfo);
            }
        }
    }


    private void checkLocPermission() {
        Disposable disposable = PermissionUtils.requestWithDialog(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            doLocation();
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    public void showLoading() {
        if (mWeatherLoadingDialog == null) {
            mWeatherLoadingDialog = new LoadingDialog(this);
            mWeatherLoadingDialog.setHasContent(false);
        }
        if (!mWeatherLoadingDialog.isShowing()) {
            mWeatherLoadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (mWeatherLoadingDialog != null && mWeatherLoadingDialog.isShowing()) {
            mWeatherLoadingDialog.dismiss();
        }
    }
}
