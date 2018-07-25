package circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import com.example.circleprogressbar.R;

/**
 * 创建时间:2018-6-22
 * 修改时间:2018-6-22
 */

public class CircleProgressBar extends android.support.v7.widget.AppCompatImageView{
    /**
     * 进度条风格:圆弧
     * 进度条,缓存条是圆弧,进度条背景是圆环
     */
    public static final int STYLE_ARC = 0;

    /**
     * 进度条风格:实心圆
     * 进度条,缓存条是实心圆,进度条背景是圆环
     */
    public static final int STYLE_CIRCLE = 1;

    private int mProgressColor;
    private int mProgressBarWidth;
    private int mCacheColor;
    private int mProgressBackgroundColor;
    private String mText;
    private int mTextColor;
    private float mTextSize;

    private float mStartDegree;
    private float mProgressSweepDegree;
    private float mCacheSweepDegree;

    private float mProgress;
    private float mCacheProgress;

    private float mProgressRadius;

    private int mStyle;

    /**
     * 是否有重力效果
     * 若为true则进度条和缓存条都有重力效果
     */
    private boolean hasGravity;

    private Paint mProgressPaint;
    private Paint mProgressBackgroundPaint;
    private Paint mTextPaint;

    private Context mContext;

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.CircleProgressBar);
        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_proressColor, Color.DKGRAY);
        mProgressBarWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_progressBarWidth,0);
        mCacheColor = typedArray.getColor(R.styleable.CircleProgressBar_cacheColor,Color.LTGRAY);
        mProgress = typedArray.getFloat(R.styleable.CircleProgressBar_progress,0);
        mCacheProgress = typedArray.getFloat(R.styleable.CircleProgressBar_cacheProgress,0);
        mText = typedArray.getString(R.styleable.CircleProgressBar_text);
        if(mText == null){
            mText = "";
        }
        mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor,Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_textSize,10);
        mStyle  =typedArray.getInteger(R.styleable.CircleProgressBar_style, STYLE_ARC);
        mProgressBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_progressBackgroundColor,Color.DKGRAY);
        hasGravity = typedArray.getBoolean(R.styleable.CircleProgressBar_hasGravity,false);
        typedArray.recycle();

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mProgressBarWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        mProgressBackgroundPaint = new Paint();
        mProgressBackgroundPaint.setAntiAlias(true);
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);

        switch(mStyle){
            case STYLE_ARC:
                mProgressPaint.setStyle(Paint.Style.STROKE);
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mProgressBackgroundPaint.setStyle(Paint.Style.STROKE);
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mProgressPaint.setPathEffect(null);
                mProgressBackgroundPaint.setPathEffect(null);
                mProgressBackgroundPaint.setStrokeWidth(mProgressBarWidth);
                mTextPaint.setXfermode(null);
                break;

            case STYLE_CIRCLE:
                mProgressPaint.setStyle(Paint.Style.FILL);
                mProgressBackgroundPaint.setStyle(Paint.Style.FILL);
                mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        /**
         * 进度条半径取自身宽高的最小值
         */
        mProgressRadius = Math.min(width,height) / 2;

        if(mStyle == STYLE_ARC){
            mProgressRadius -= mProgressBarWidth / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBackground(canvas);
        drawCacheBar(canvas);
        drawProgressBar(canvas);
        drawText(canvas);
    }

    /**
     * 绘制进度条
     * @param canvas
     */
    private void drawProgressBar(Canvas canvas){
        if(hasGravity){
            mStartDegree = 90 - mProgress / 100 * 360 / 2;
        }else{
            mStartDegree = -90;
        }
        mProgressSweepDegree = mProgress / 100 * 360;

        mProgressPaint.setColor(mProgressColor);

        float offset = mStyle == STYLE_CIRCLE ? 0 : mProgressBarWidth / 2;
        canvas.drawArc(offset,offset,
                mProgressRadius * 2 + offset,mProgressRadius * 2 + offset,
                mStartDegree,mProgressSweepDegree,
                mStyle == STYLE_CIRCLE && !hasGravity,mProgressPaint);
    }

    /**
     * 绘制缓存条
     * @param canvas
     */
    private void drawCacheBar(Canvas canvas){
        if(hasGravity){
            mStartDegree = 90 - mCacheProgress / 100 * 360 / 2;
        }else{
            mStartDegree = -90;
        }
        mCacheSweepDegree = mCacheProgress / 100 * 360;

        mProgressPaint.setColor(mCacheColor);

        float offset = mStyle == STYLE_CIRCLE ? 0 : mProgressBarWidth / 2;
        canvas.drawArc(offset,offset,
                mProgressRadius * 2 + offset,mProgressRadius * 2 + offset,
                mStartDegree,mCacheSweepDegree,
                mStyle == STYLE_CIRCLE && !hasGravity,mProgressPaint);
    }

    /**
     * 绘制进度条的背景
     * @param canvas
     */
    private void drawProgressBackground(Canvas canvas){
        float offset = mStyle == STYLE_CIRCLE ? 0 : mProgressBarWidth / 2;
        float radius = mStyle == STYLE_CIRCLE ? mProgressRadius - mProgressBarWidth / 2 : mProgressRadius;
        canvas.drawCircle(mProgressRadius + offset,
                mProgressRadius + offset,radius ,mProgressBackgroundPaint);
    }

    /**
     * 绘制进度条的字符串
     * @param canvas
     */
    private void drawText(Canvas canvas){
        final float width = mTextPaint.measureText(mText);
        int offset = mStyle == STYLE_CIRCLE ? 0 : mProgressBarWidth / 2;
        float x = mProgressRadius + offset - width / 2;
        float y = mProgressRadius + offset;
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /**
     * 设置进度条的风格
     */
    public void setStyle(int style){
        mStyle = style;
        switch(mStyle){
            case STYLE_ARC:
                mProgressPaint.setStyle(Paint.Style.STROKE);
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mProgressBackgroundPaint.setStyle(Paint.Style.STROKE);
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mProgressPaint.setPathEffect(null);
                mProgressBackgroundPaint.setPathEffect(null);
                mTextPaint.setXfermode(null);
                break;

            case STYLE_CIRCLE:
                mProgressPaint.setStyle(Paint.Style.FILL);
                mProgressBackgroundPaint.setStyle(Paint.Style.FILL);
                mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                break;
        }

        invalidate();
    }

    /**
     * 设置进度条宽度
     * @param width
     */
    public void setProgressBarWidth(int width){
        mProgressBarWidth = width;
        mProgressPaint.setStrokeWidth(width);
        mProgressBackgroundPaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * 设置进度条进度
     * @param progress 取值0 - 100
     */
    public void setProgress(float progress){
        if(progress < 0){
            progress = 0;
        }else if(progress > 100){
            progress = 100;
        }
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置缓存条进度
     * @param cacheProgress 取值0 - 100
     */
    public void setCacheProgress(float cacheProgress){
        if(cacheProgress < 0){
            cacheProgress = 0;
        }else if(cacheProgress > 100){
            cacheProgress = 100;
        }
        mCacheProgress = cacheProgress;
        invalidate();
    }

    /**
     * 是否有重力效果
     * @param allow  若为true则进度条和缓存条都有重力效果
     */
    public void allowGravity(boolean allow){
        hasGravity = allow;
        invalidate();
    }

    /**
     * 获取进度条进度
     * @return
     */
    public float getProgress(){
        return mProgress;
    }

    /**
     * 获取缓存条进度
     * @return
     */
    public float getCacheProgress(){
        return mCacheProgress;
    }

    public void setTextColor(int textColor){
        mTextColor = textColor;
    }

    public void setTextSize(int textSize){
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
    }
}
