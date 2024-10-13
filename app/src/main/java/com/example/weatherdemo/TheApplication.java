package com.example.weatherdemo;

import android.app.Application;

import com.pasc.business.weather.util.WeatherUIManager;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.AppUtils;
import com.pasc.lib.lbs.LbsManager;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.net.NetConfig;
import com.pasc.lib.net.NetManager;
import com.pasc.lib.net.download.DownLoadManager;
import com.pasc.lib.router.RouterManager;

/**
 * Created by lanshaomin
 * Date: 2018/11/12 下午5:04
 * Desc:
 */
public class TheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (AppUtils.getPIDName(this).equals(getPackageName())) {//主进程
            AppProxy.getInstance().init(this, false)
                    .setIsDebug(BuildConfig.DEBUG)
                    .setProductType(1)
                    .setHost(UrlManager.BETA_HOST)
                    .setVersionName(BuildConfig.VERSION_NAME);
            PascLog.openCatchCrash(false);
            initNet();
            initARouter();
            initUrlDispatch();
            //天气模块
            WeatherUIManager.getInstance().init(this);
            WeatherUIManager.getInstance().setGateway("api");
        }
    }

    /****初始化网络****/
    private void initNet() {
        NetConfig config = new NetConfig.Builder(this)
                .baseUrl(UrlManager.BETA_HOST)
//                .headers(HeaderUtil.getHeaders(BuildConfig.DEBUG, null))
//                .gson(ConvertUtil.getConvertGson())
                .isDebug(BuildConfig.DEBUG)
                .build();
        NetManager.init(config);

        DownLoadManager.getDownInstance().init(this, 3, 5, 0);
    }



    /**
     * 初始化路由
     */
    private void initARouter() {
        RouterManager.initARouter(this, BuildConfig.DEBUG);
    }

    /**
     * 初始化配置文件
     */
    private void initUrlDispatch() {
        WeatherUIManager.getInstance().initConfig(this, "pasc.pingan.service.weather.json");
    }

}
