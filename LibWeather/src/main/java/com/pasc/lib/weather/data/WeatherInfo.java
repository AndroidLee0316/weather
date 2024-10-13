package com.pasc.lib.weather.data;

import com.google.gson.annotations.SerializedName;
import com.pasc.lib.weather.db.WeatherDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by ruanwei489 on 2018/2/27.
 * 我们接口天气的bean类
 */

@Table(database = WeatherDb.class)
public class WeatherInfo extends BaseModel {
    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    @SerializedName("id")
    public Long id;
    @Column(name = "cond_txt")
    @SerializedName("cond_txt")
    public String cond_txt;//实况天气状况描述
    @Column(name = "tmp")
    @SerializedName("tmp")
    public String tmp;//温度
    @Column(name = "cond_image_url")
    @SerializedName("cond_image_url")
    public String cond_image_url;//实况天气状况图片
    @Column(name = "qlty")
    @SerializedName("qlty")
    public String qlty;//实况天气状况图片

    @Column(name = "city")
    public String city;

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "id=" + id +
                ", cond_txt='" + cond_txt + '\'' +
                ", tmp='" + tmp + '\'' +
                ", cond_image_url='" + cond_image_url + '\'' +
                ", qlty='" + qlty + '\'' +
                '}';
    }
}
