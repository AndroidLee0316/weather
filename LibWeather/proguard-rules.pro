# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep class com.pasc.lib.weather.data.*{
	public <fields>;
	public <methods>;
}
-keep class com.pasc.lib.weather.utils.WeatherDataManager{
	public <fields>;
	public <methods>;
}

-keep class  com.pasc.lib.weather.data.params.WeatherCityInfo{
	public <fields>;
	public <methods>;
}

-keep class com.pasc.lib.weather.utils.WeatherDataManager$ReqType

-keep class com.pasc.lib.weather.presenter.*{
	public <fields>;
	public <methods>;
}
# dbflow
-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
-keep class * extends com.raizlabs.android.dbflow.structure.BaseModel { *; }

# 阿里路由
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
#阿里所有包都不混淆
-keep public class com.alibaba.**{*;}