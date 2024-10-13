package com.pasc.business.weather.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.pasc.business.weather.util.WeatherDefinition;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.base.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import com.pasc.business.weather.R;

import static com.pasc.business.weather.util.WeatherDefinition.WEATHER_LOG_TAG;

/**
 * Created by lishanshan790 on 2018/8/8.
 */

public class WeatherSevenDayView extends View {
    private static final float SCREEN_DRAW_MAX_COUNT = 4.5f;
    private static final int CURVE_COLOR = R.color.weather_forecast_line;
    private Paint paint;
    private Paint curvePaint;
    private Rect textBounds = new Rect();

    private int iconHalfSize;

    private List<DayWeatherInfo> mInfos = new ArrayList<>();
    private int tempCircleRadius;
    private int minTemp;
    private int maxTemp;

    private int dayColor;
    private int tempColor;
    private int boldColor;

    private int dayTextSize;
    private int stateTextSize;
    private int tempTextSize;

    private int curveViewTop;
    private int curveHeight;
    private int mViewWidth;
    private int mViewHeight;
    private float itemWidth;
    private float oneQuarterItemWidth;
    private int unitSpaceHeight;
    private int leftSide;
    private int rightSide;
    private int divideSpace;
    private int curveTempSpace;

    private Path curvePath;
    private Context mContext;
    private int mScreenWidth;

    public WeatherSevenDayView(Context context) {
        this(context, null);
    }

    public WeatherSevenDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mInfos != null) {
            mScreenWidth = DensityUtils.getScreenWidth(mContext);
            leftSide = getPaddingLeft();
            rightSide = getPaddingRight();
            int days = mInfos.size();
            mViewWidth = (int) ((mScreenWidth - leftSide) * (days - 0.5) / SCREEN_DRAW_MAX_COUNT) + leftSide + rightSide;

        }
        setMeasuredDimension(mViewWidth, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        initChildPosition();
        PascLog.d(WEATHER_LOG_TAG, "mViewWidth = " + mViewWidth + ",  " + mViewHeight + ", itemWidth = " + itemWidth + "  , " + mInfos.size());
    }

    public WeatherSevenDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        dayColor = context.getResources().getColor(R.color.weather_secondary_text);
        tempColor = context.getResources().getColor(R.color.weather_primary_text);
        boldColor = tempColor;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        //曲线画笔
        curvePaint = new Paint();
        curvePaint.setAntiAlias(true);
        curvePaint.setFilterBitmap(true);
        curvePaint.setPathEffect(new CornerPathEffect(DensityUtils.dp2px(5)));
        curvePaint.setColor(mContext.getResources().getColor(CURVE_COLOR));
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeWidth(DensityUtils.dp2px(1));
        curvePaint.setTextSize(DensityUtils.dp2px(14));
        //曲线路径
        curvePath = new Path();

        dayTextSize = DensityUtils.dp2px(15);
        stateTextSize = DensityUtils.dp2px(13);
        tempTextSize = DensityUtils.dp2px(13);

        divideSpace = DensityUtils.dp2px(8);

        curveTempSpace = DensityUtils.dp2px(10);

        tempCircleRadius = DensityUtils.dp2px(3);
        iconHalfSize = DensityUtils.dp2px(14);
    }

    private void initChildPosition() {
        curveViewTop = mViewHeight / 2 + curveTempSpace;
        curveHeight = mViewHeight / 2 - 2 * curveTempSpace - tempTextSize;

        int range = maxTemp - minTemp;
        if (range != 0) {
            unitSpaceHeight = curveHeight / range;
        }
        tempCircleRadius = DensityUtils.dp2px(2);
        iconHalfSize = DensityUtils.dp2px(14);

        itemWidth = (mScreenWidth - leftSide) / SCREEN_DRAW_MAX_COUNT;
        oneQuarterItemWidth = itemWidth / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = mInfos.size();
        if (count <= 2) return;
        List<Point> pointUpPointList = new ArrayList<>(count);
        List<Point> pointDownPointList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final DayWeatherInfo weather = mInfos.get(i);
            final int centerX = (int) (i * itemWidth + oneQuarterItemWidth + leftSide);
            boolean isBold = (i == 0 && "今天".equals(weather.week));
            paint.setTypeface(isBold ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
            int color = isBold ? boldColor : dayColor;
            //绘制星期
            int canvasY = dayTextSize + getTop();
            paint.setColor(color);
            paint.setTextSize(dayTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(weather.week, centerX, canvasY, paint);

            //绘制日期
            paint.setColor(color);
            paint.setTextSize(stateTextSize);
            canvasY += (dayTextSize + divideSpace);
            canvas.drawText(weather.date, centerX, canvasY, paint);

            //绘制天气状态

            final String state = TextUtils.isEmpty(weather.weatherState) ? mContext.getResources().getString(R.string.weather_unknow) : weather.weatherState;
            paint.setColor(color);
            paint.setTextSize(stateTextSize);
            canvasY += (stateTextSize + divideSpace);
            canvas.drawText(state, centerX, canvasY, paint);

            //绘制图标
            canvasY += (stateTextSize + iconHalfSize);
            if (weather.icon != null) {
                weather.icon.setBounds(centerX - iconHalfSize, canvasY - iconHalfSize,
                        centerX + iconHalfSize, canvasY + iconHalfSize);
                weather.icon.draw(canvas);
            }

            int pointUpY = curveViewTop + (maxTemp - weather.upTemp) * unitSpaceHeight;
            int pointDownY = curveViewTop + (maxTemp - weather.downTemp) * unitSpaceHeight;
            pointUpPointList.add(new Point(centerX, pointUpY));
            pointDownPointList.add(new Point(centerX, pointDownY));
        }

        drawCurve(canvas, pointUpPointList, pointDownPointList);

    }

    private void drawCurve(Canvas canvas, List<Point> list) {
        if (list.size() <= 2) return;
        final int count = list.size();
        for (int i = 0; i < count; i++) {
            Point point1 = list.get(i);
            float x1, x2, x3, x4, y1, y2, y3, y4;
            x1 = point1.x;
            y1 = point1.y;
            if (i + 1 < count) {
                Point point2 = list.get(i + 1);
                x4 = point2.x;
                y4 = point2.y;
            } else {
                y4 = y1;
                x4 = x1;
            }

            x2 = x3 = (x1 + x4) / 2f;
            float dy = (y1 - y4) / 3f;
            y2 = y1 - dy;
            y3 = y4 + dy;
            if (i == 0) {
                curvePath.moveTo(x1, y1);
                curvePath.lineTo(x1, y1);
            }
            curvePath.cubicTo(x2, y2, x3, y3, x4, y4);
        }
        canvas.drawPath(curvePath, curvePaint);
        curvePath.reset();
    }

    private void drawCurve(Canvas canvas, List<Point> upList, List<Point> downList) {

        drawCurve(canvas, upList);
        drawCurve(canvas, downList);
        drawCircleAndTemp(canvas, upList, downList);

    }

    private void drawCircleAndTemp(Canvas canvas, List<Point> upList,  List<Point> downList) {
        // 绘制曲线里面的圆点
        int count = upList.size();
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setFilterBitmap(true);
        circlePaint.setStrokeWidth(DensityUtils.dp2px(1));

        Paint dashPaint = new Paint();
        dashPaint.setAntiAlias(true);
        dashPaint.setFilterBitmap(true);
        dashPaint.setColor(mContext.getResources().getColor(CURVE_COLOR));
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(DensityUtils.dp2px(0.5f));
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            Point upPoint = upList.get(i);
            Point downPoint = downList.get(i);
            path.moveTo(upPoint.x, upPoint.y);
            path.lineTo(upPoint.x, downPoint.y);
            canvas.drawPath(path, dashPaint);
            path.reset();

            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(mContext.getResources().getColor(CURVE_COLOR));
            canvas.drawCircle(upPoint.x, upPoint.y, tempCircleRadius, circlePaint);
            canvas.drawCircle(downPoint.x, downPoint.y, tempCircleRadius, circlePaint);

            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(Color.WHITE);
            canvas.drawCircle(upPoint.x, upPoint.y, tempCircleRadius / 2 + 1, circlePaint);
            canvas.drawCircle(downPoint.x, downPoint.y, tempCircleRadius / 2 + 1, circlePaint);

            paint.setColor(tempColor);
            canvas.drawText(mInfos.get(i).upTempText, upPoint.x, upPoint.y - curveTempSpace, paint);
            canvas.drawText(mInfos.get(i).downTempText, downPoint.x, downPoint.y + curveTempSpace + tempTextSize, paint);
        }

    }

    private void drawText(Canvas canvas, int centerX, int centerY, String text, Paint paint) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float textWidth = paint.measureText(text);
        float textHeight = textBounds.height();
        canvas.drawText(text, centerX - textWidth / 2f, centerY + textHeight / 2f, paint);
    }

    public void setWeatherInfos(List<DayWeatherInfo> infos) {
        mInfos = infos;
        minTemp = Integer.MAX_VALUE;
        maxTemp = Integer.MIN_VALUE;
        final int size = infos.size();
        for (int i = 0; i < size; i++) {
            final DayWeatherInfo info = mInfos.get(i);
            if (info.downTemp < minTemp) {
                minTemp = info.downTemp;
            }
            if (info.upTemp > maxTemp) {
                maxTemp = info.upTemp;
            }

            info.icon = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), info.icon_int));
        }
        initChildPosition();
    }

    public static class DayWeatherInfo {
        public String week;
        public String date;
        public int upTemp;
        public int downTemp;
        public String upTempText;
        public String downTempText;
        public BitmapDrawable icon;
        public String weatherState;
        private int icon_int;

        public DayWeatherInfo(String week, String date, String maxTemp, String minTemp, String state, int icon) {
            this.week = week;
            this.date = date;

            if (TextUtils.isEmpty(maxTemp)) {
                this.upTempText = "0" + WeatherDefinition.WEATHER_TEMP_UNIT;
                this.upTemp = 0;
            } else {
                this.upTempText = maxTemp;
                try {
                    this.upTempText = maxTemp;
                    this.upTemp = Integer.valueOf(maxTemp.replace("°", ""));
                } catch (NumberFormatException e) {
                    this.upTempText = "0°";
                    this.upTemp = 0;
                }
            }

            if (TextUtils.isEmpty(minTemp)) {
                this.downTempText = "0" + WeatherDefinition.WEATHER_TEMP_UNIT;
                this.downTemp = 0;
            } else {
                try {
                    this.downTempText = minTemp;
                    this.downTemp = Integer.valueOf(minTemp.replace("°", ""));
                } catch (NumberFormatException e) {
                    this.downTempText = "0°";
                    this.downTemp = 0;
                }
            }
            this.weatherState = state;
            this.icon_int = icon;
        }
    }
}
