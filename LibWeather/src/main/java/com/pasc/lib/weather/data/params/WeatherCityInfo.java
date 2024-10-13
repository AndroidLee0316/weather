package com.pasc.lib.weather.data.params;

import android.text.TextUtils;
import android.util.Base64;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class WeatherCityInfo implements Serializable {
    private static final String HEFENG_CITY_DIVIDER = ",";
    private boolean isLocation;
    private double latitude;
    private double longitude;
    private String city;
    private String district;
    private String showName;

    public WeatherCityInfo(String city) {
        this.city = city;
        isLocation = false;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setCity(String city) {
        if (!TextUtils.isEmpty(city)) {
            city = city.trim();
        }
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setIsLocation(boolean isLocation) {
        this.isLocation = isLocation;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setDistrict(String district) {
        if (!TextUtils.isEmpty(district)) {
            district = district.trim();
        }
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public String getShowName() {
        if (TextUtils.isEmpty(showName)) {
            String newShowName = "";
            if (!TextUtils.isEmpty(district)) {
                if (!isLocation) {
                    return district;
                }
                newShowName = district;
            }
            if (!TextUtils.isEmpty(city)) {
                if (!isLocation) {
                    return city;
                } else {
                    newShowName = city + " " + newShowName;
                }
            }
            setShowName(newShowName);
        }
        return showName;
    }

    public void setShowName(String showName) {
        if (!TextUtils.isEmpty(showName)) {
            showName = showName.trim();
        }
        this.showName = showName;
    }

    public String getRequestWeatherParam() {
        if (isLocation) {
            //删除传经纬度，经纬度服务器不好缓存天气数据
            //if ((latitude != 0 || longitude != 0)) {
            //    return latitude + HEFENG_CITY_DIVIDER + longitude;
            //} else {
            //    if (!TextUtils.isEmpty(city)) {
            //        return city;
            //    }
            //    if (!TextUtils.isEmpty(district)) {
            //        return district;
            //    }
            //}
            String param = "";
            if (!TextUtils.isEmpty(city)) {
                param = city.replace("市","");
            }
            if (!TextUtils.isEmpty(district)) {
                param = district + HEFENG_CITY_DIVIDER + param;
            }
            return param;

        } else {
            String param = "";
            if (!TextUtils.isEmpty(city)) {
                param = city;
            }
            if (!TextUtils.isEmpty(district)) {
                param = district + HEFENG_CITY_DIVIDER + param;
            }
            return param;
        }
    }

    public String getShowNameByProduct(String productCity) {
        if (isLocation && !TextUtils.isEmpty(productCity)) {
            if (!TextUtils.isEmpty(city)) {
                if (city.startsWith(productCity)) {
                    return TextUtils.isEmpty(district) ? city : district;
                }
            }
            if (!TextUtils.isEmpty(district)) {
                if (district.startsWith(productCity)) {
                    return district;
                }
            }
            return city;
        }
        return getSimpleShowName();
    }

    public String getSimpleShowName() {
        if (!TextUtils.isEmpty(showName) && showName.contains(" ")) {
            int index = showName.indexOf(" ");
            String simpleShowName = showName.substring(index);
            return simpleShowName.trim();
        }
        return getShowName();
    }


    @Override
    public String toString() {
        return new StringBuffer("WeatherCityInfo [ city=").append(city)
                .append(", district=").append(district)
                .append(", showName= ").append(showName)
                .append(", isLocation=").append(isLocation)
                .append(", latitude=").append(latitude)
                .append(", longitude=").append(longitude)
                .append("]").toString();
    }

    public String toSaveSPString() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String objectStr = new String(Base64.encode(baos.toByteArray(),
                Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectStr;
    }

    public static WeatherCityInfo toObject(String spValue) {
        try {
            if (TextUtils.isEmpty(spValue)) {
                return null;
            }
            byte[] objBytes = Base64.decode(spValue.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return (WeatherCityInfo) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
