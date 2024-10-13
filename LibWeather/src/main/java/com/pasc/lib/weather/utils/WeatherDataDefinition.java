package com.pasc.lib.weather.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.pasc.lib.weather.R;

class WeatherDataDefinition {
    //晴
     static final int QIN = 100;

    //多云
    private static final int DUO_YUN = 101;
    private static final int SHAO_YUN = 102;
    private static final int QING_DUOYUN = 103;

    //阴
    private static final int YIN = 104;
    private static final int YOU_FENG = 200;
    private static final int PINGJING = 201;
    private static final int WEI_FENG = 202;
    private static final int HE_FENG = 203;
    private static final int QING_FENG = 204;
    private static final int QIANG_FENG = 205;
    private static final int JI_FENG = 206;
    private static final int DA_FENG = 207;
    private static final int LIE_FENG = 208;
    private static final int FENGBAO = 209;
    private static final int KUANGBAO_FENG = 210;
    private static final int JU_FENG = 211;
    private static final int LONGJUAN_FENG = 212;
    private static final int REDAIFENGBAO = 213;

    //雨
    private static final int ZHENGYU = 300;
    private static final int QIANG_ZHENYU = 301;
    private static final int LEI_ZHENGYU = 302;
    private static final int QIANG_LEIZHENYU = 303;
    private static final int LEIZHENYU_BINGBAO = 304;
    private static final int XIAO_YU = 305;
    private static final int ZHONG_YU = 306;
    private static final int DA_YU = 307;
    private static final int JIDUANJIANG_YU = 308;
    private static final int MAOMAO_YU = 309;
    private static final int BAO_YU = 310;
    private static final int DABAO_YU = 311;
    private static final int TEDABAO_YU = 312;
    private static final int DONG_YU = 313;
    private static final int XIAO_ZHONG_YU = 314;
    private static final int ZHONG_DA_YU = 315;
    private static final int DA_BAO_YU = 316;
    private static final int BAO_DABAO_YU = 317;
    private static final int DABAO_TEDABAO_YU = 318;
    private static final int YU = 399;

    //雪
    private static final int XIAO_XUE = 400;
    private static final int ZHONG_XUE = 401;
    private static final int DA_XUE = 402;
    private static final int BAO_XUE = 403;
    private static final int YU_JIA_XUE = 404;
    private static final int YU_XUE_WEATHER = 405;
    private static final int ZHENYU_JIA_XUE = 406;
    private static final int ZHENXUE = 407;
    private static final int XIAO_ZHONG_XUE = 408;
    private static final int ZHONG_DA_XUE = 409;
    private static final int DA_BAO_XUE = 410;
    private static final int XUE = 499;

    //雾
    private static final int BO_WU = 500;
    private static final int WU = 501;
    private static final int NONG_WU = 509;
    private static final int QIANG_NONG_WU = 510;
    private static final int DA_WU = 514;
    private static final int TEQIANG_NONG_WU = 515;

    //沙尘暴
    private static final int YANG_SHA = 503;
    private static final int FUCHENG = 504;
    private static final int SHAOCHENGBAO = 507;
    private static final int QIANG_SHACHENBAO = 508;

    //霾
    private static final int MAI = 502;
    private static final int MOD_MAI = 511;
    private static final int HEAVY_MAI = 512;
    private static final int YANZHONG_MAI = 513;

    //晴
    private static final int HOT = 900;
    private static final int COLD = 901;
    private static final int OTHER = 999;

    public static int getStateInt(String state) {
        if (TextUtils.isEmpty(state)) {
            return OTHER;
        }
        switch (state) {
            case "晴":
                return QIN;
            case "多云":
                return DUO_YUN;
            case "少云":
                return SHAO_YUN;
            case "晴间多云":
                return QING_DUOYUN;
            case "阴":
                return YIN;
            case "有风":
                return YOU_FENG;
            case "平静":
                return PINGJING;
            case "微风":
                return WEI_FENG;
            case "和风":
                return HE_FENG;
            case "清风":
                return QING_FENG;
            case "强风/劲风":
                return QIANG_FENG;
            case "疾风":
                return JI_FENG;
            case "大风":
                return DA_FENG;
            case "烈风":
                return LIE_FENG;
            case "风暴":
                return FENGBAO;
            case "狂暴风":
                return KUANGBAO_FENG;
            case "飓风":
                return JU_FENG;
            case "龙卷风":
                return LONGJUAN_FENG;
            case "热带风暴":
                return REDAIFENGBAO;
            case "阵雨":
                return ZHENGYU;
            case "强阵雨":
                return QIANG_ZHENYU;
            case "雷阵雨":
                return LEI_ZHENGYU;
            case "强雷阵雨":
                return QIANG_LEIZHENYU;
            case "雷阵雨伴有冰雹":
                return LEIZHENYU_BINGBAO;
            case "小雨":
                return XIAO_YU;
            case "中雨":
                return ZHONG_YU;
            case "大雨":
                return DA_YU;
            case "极端降雨":
                return JIDUANJIANG_YU;
            case "毛毛雨":
                return MAOMAO_YU;
            case "暴雨":
                return BAO_YU;
            case "大暴雨":
                return DABAO_YU;
            case "特大暴雨":
                return TEDABAO_YU;
            case "冻雨":
                return DONG_YU;
            case "小到中雨":
                return XIAO_ZHONG_YU;
            case "中到大雨":
                return ZHONG_DA_YU;
            case "大到暴雨":
                return DA_BAO_YU;
            case "暴雨到大暴雨":
                return BAO_DABAO_YU;
            case "大暴雨到特大暴雨":
                return DABAO_TEDABAO_YU;
            case "雨":
                return YU;
            case "小雪":
                return XIAO_XUE;
            case "中雪":
                return ZHONG_XUE;
            case "大雪":
                return DA_XUE;
            case "暴雪":
                return BAO_XUE;
            case "雨夹雪":
                return YU_JIA_XUE;
            case "雨雪天气":
                return YU_XUE_WEATHER;
            case "阵雨夹雪":
                return ZHENYU_JIA_XUE;
            case "阵雪":
                return ZHENXUE;
            case "小到中雪":
                return XIAO_ZHONG_XUE;
            case "中到大雪":
                return ZHONG_DA_XUE;
            case "大到暴雪":
                return DA_BAO_XUE;
            case "雪":
                return XUE;
            case "薄雾":
                return BO_WU;
            case "雾":
                return WU;
            case "霾":
                return MAI;
            case "扬沙":
                return YANG_SHA;
            case "浮尘":
                return FUCHENG;
            case "沙尘暴":
                return SHAOCHENGBAO;
            case "强沙尘暴":
                return QIANG_SHACHENBAO;
            case "浓雾":
                return NONG_WU;
            case "强浓雾":
                return QIANG_NONG_WU;
            case "中度霾":
                return MOD_MAI;
            case "重度霾":
                return HEAVY_MAI;
            case "严重霾":
                return YANZHONG_MAI;
            case "大雾":
                return DA_WU;
            case "特强浓雾":
                return TEQIANG_NONG_WU;
            case "热":
                return HOT;
            case "冷":
                return COLD;
            case "未知":
                return OTHER;
            default:
                return OTHER;

        }
    }

    public static int getWeatherStateIcon(Context context, String state) {
        int stateValue = getStateInt(state);
        Resources resources = context.getResources();
        int id = resources.getIdentifier("weather_icon_" + stateValue, "drawable", context.getPackageName());
        if (id == 0) {
            id = R.drawable.weather_icon_999;
        }
        return id;
    }
}
