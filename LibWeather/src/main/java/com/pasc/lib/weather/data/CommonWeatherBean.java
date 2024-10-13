package com.pasc.lib.weather.data;

/**
 * Created by lanshaomin
 * Date: 2018/11/8 上午10:21
 * Desc:天气基础bean
 */
public class CommonWeatherBean<T> {
    /**
     * 数据源
     */
    private int dataType;
    /**
     * 数据
     */
    private T data;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
