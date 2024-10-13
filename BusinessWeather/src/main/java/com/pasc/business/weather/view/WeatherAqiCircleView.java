package com.pasc.business.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.pasc.business.weather.R;
import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.base.util.DeviceUtils;

public class WeatherAqiCircleView extends View {

    private Paint circlePaint;
    private Paint paint;
    private int mCircleColor = R.color.weather_aqi_level1;

    private static final int MAX_VALUE = 400;
    private static final int DURATION_MIN = 300;
    private static final int DURATION = 600;

    private static final int AQI_LEVEL_1 = 50;
    private static final int AQI_LEVEL_2 = 100;
    private static final int AQI_LEVEL_3 = 150;
    private static final int AQI_LEVEL_4 = 200;
    private static final int AQI_LEVEL_5 = 300;
    private static final int AQI_LEVEL_6 = 400;

    private int mValue;
    private Context mContext;
    private float mInterpolatedTime = 0;
    private Rect textBounds = new Rect();
    private float mSweepAngle;
    private float mStartAngle = 120f;
    private String mAqiType;
    private int mViewWidth;
    private int mViewHeight;
    private RectF mRectf;
//    private int mLevel = AQI_LEVEL_1;
    private PaintFlagsDrawFilter mDrawFilter;

    public WeatherAqiCircleView(Context context) {
        super(context, null);
    }

    public WeatherAqiCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mViewWidth = (int) (DeviceUtils.getWindowWidth(mContext) * 0.48);
        mViewHeight = mViewWidth;
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
//        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setAlpha(200);
        circlePaint.setStrokeWidth(DensityUtils.dp2px(4));

        paint = new Paint();
        paint.setAntiAlias(true);

        final int lineWidthHaf = DensityUtils.dp2px(2);
        mRectf = new RectF(lineWidthHaf, lineWidthHaf, mViewWidth - lineWidthHaf, mViewHeight - lineWidthHaf);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

    }

    public void setValue(int value) {
        if (value < 0) {
            value = 0;
            mSweepAngle = 0;
        } else if (value > MAX_VALUE) {
            mSweepAngle = 300;
        } else {
            mSweepAngle = value * 300f / MAX_VALUE;
        }
        mValue = value;
        mCircleColor = getColorByAqi(mValue);
        circlePaint.setColor(mContext.getResources().getColor(mCircleColor));
    }

    public void setAqiType(String aqiType) {
        mAqiType = aqiType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float degree = mInterpolatedTime * mSweepAngle;
        float value = mInterpolatedTime * mValue;

        float centerX = mViewWidth * 0.5f;
        float centerY = mViewHeight * 0.5f;
        canvas.setDrawFilter(mDrawFilter);
//        setPainShader(centerX, centerY, value, circlePaint);
        canvas.drawArc(mRectf, mStartAngle, degree, false, circlePaint);
        paint.setTextSize(DensityUtils.dp2px(12));
        paint.setColor(mContext.getResources().getColor(R.color.weather_explain_text));
        drawText(canvas, centerX, mViewHeight * 0.325f, mContext.getResources().getString(R.string.weather_aqi_index), paint);
        paint.setTextSize(DensityUtils.dp2px(60));
        paint.setColor(mContext.getResources().getColor(R.color.weather_primary_text));
        drawText(canvas, centerX, mViewHeight * 0.55f, Integer.toString((int)value), paint);
        paint.setTextSize(DensityUtils.dp2px(17));
        paint.setColor(mContext.getResources().getColor(mCircleColor));
        drawText(canvas, centerX, mViewHeight * 0.9f, mAqiType, paint);
    }

    //圆环渐变效果
//    private void setPainShader(float x, float y, float value, Paint paint) {
//        Matrix matrix = new Matrix();
//        matrix.setRotate(-92f, x, y);
//        SweepGradient sweepGradient = new SweepGradient(x, y, mCircleColors, new float[]{0f, value / mLevel});
//        sweepGradient.setLocalMatrix(matrix);
//        paint.setShader(sweepGradient);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (modeWidth == MeasureSpec.UNSPECIFIED) {
            int wrap_width = mViewWidth + getPaddingLeft() + getPaddingRight();
            sizeWidth = wrap_width;
            modeWidth = MeasureSpec.EXACTLY;
        }

        if (modeHeight == MeasureSpec.UNSPECIFIED) {
            int wrap_height = mViewHeight + getPaddingTop() + getPaddingBottom();
            sizeHeight = wrap_height;
            modeHeight = MeasureSpec.EXACTLY;
        }

        if (modeWidth == MeasureSpec.AT_MOST) {
            int wrap_width = mViewWidth + getPaddingLeft() + getPaddingRight();
            sizeWidth = Math.min(wrap_width, sizeWidth);
            modeWidth = MeasureSpec.EXACTLY;
        }

        if (modeHeight == MeasureSpec.AT_MOST) {
            int wrap_height = mViewHeight + getPaddingTop() + getPaddingBottom();
            sizeHeight = Math.min(wrap_height, sizeHeight);
            modeHeight = MeasureSpec.EXACTLY;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawText(Canvas canvas, float centerX, float centerY, String text, Paint paint) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float textWidth = paint.measureText(text);
        float textHeight = textBounds.height();
        canvas.drawText(text, centerX - textWidth / 2f, centerY + textHeight / 2f, paint);
    }

    private class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    public void startAnimation() {
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(DURATION_MIN + (int) (DURATION * mSweepAngle / 360));
        startAnimation(move);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 100);
    }

    private int getColorByAqi(int aqiValue) {

        if (aqiValue <= AQI_LEVEL_2) {
            return R.color.weather_aqi_level1;
        } else if (aqiValue <= AQI_LEVEL_4) {
            return R.color.weather_aqi_level2;
        }
        return R.color.weather_aqi_level3;
    }

    /*private int[] getColorByAqi(int aqiValue) {

        if (aqiValue <= AQI_LEVEL_1) {
            mLevel = AQI_LEVEL_1;
            return new int[]{0xffa6db74, 0xff8bce56};
        } else if (aqiValue <= AQI_LEVEL_2) {
            mLevel = AQI_LEVEL_2;
            return new int[]{0xffd3f261, 0xffbae637};
        } else if (aqiValue <= AQI_LEVEL_3) {
            mLevel = AQI_LEVEL_3;
            return new int[]{0xffffd666, 0xffffc53d};
        } else if (aqiValue <= AQI_LEVEL_4) {
            mLevel = AQI_LEVEL_4;
            return new int[]{0xffffc069, 0xffffa940};
        } else if (aqiValue <= AQI_LEVEL_5) {
            mLevel = AQI_LEVEL_5;
            return new int[]{0xffff9c6e, 0xffff7a45};
        } else if (aqiValue <= AQI_LEVEL_6) {
            mLevel = AQI_LEVEL_6;
            return new int[]{0xffff7875, 0xffff4d4f};
        }
        return new int[]{0xffa6db74, 0xff8bce56};
    }*/

}
