package com.example.weatherdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.weather.data.params.WeatherCityInfo;
import com.pasc.lib.weather.utils.WeatherDataManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,WeatherDetailsActivity.class);
//                intent.putExtra("city","南通");
//                startActivity(intent);
                openWeatherDetail();

            }
        });
    }

    public void openWeatherDetail() {
        Bundle bundle = new Bundle();
        WeatherCityInfo cityInfo = WeatherDataManager.getInstance().getCurrentSelectedCity();
        if (cityInfo == null) {
            WeatherCityInfo info = new WeatherCityInfo("南通");
            info.setDistrict("启东");
            info.setShowName("启东");
            info.setIsLocation(false);
            bundle.putSerializable("cityInfo", info);
        } else {
            bundle.putString("cityName", cityInfo.getCity());
            bundle.putString("districtName", cityInfo.getDistrict());
            bundle.putString("showName", cityInfo.getShowName());
            bundle.putBoolean("isLocation", cityInfo.isLocation());
            bundle.putDouble("longitude", cityInfo.getLongitude());
            bundle.putDouble("latitude", cityInfo.getLatitude());
        }
        BaseJumper.jumpBundleARouter("/weather/detail/main", bundle);
    }

    @Override
    protected void onResume() {
        WeatherCityInfo cityInfo = WeatherDataManager.getInstance().getCurrentSelectedCity();
        if (cityInfo != null) {
            String city = cityInfo.getShowNameByProduct("南通市");
            String city1 = cityInfo.getShowNameByProduct("南通");
            String city2 = cityInfo.getShowNameByProduct("深圳");
            String city3 = cityInfo.getShowNameByProduct("福田区");
        }


        super.onResume();
    }
}
