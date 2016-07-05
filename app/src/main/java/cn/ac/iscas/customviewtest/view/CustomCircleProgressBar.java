package cn.ac.iscas.customviewtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.ac.iscas.customviewtest.R;

/**
 * Created by liushen on 2016/7/1.
 */
public class CustomCircleProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mDotCount;
    private int mCurrentCount;
    private int mSplitSize;
    private Bitmap mImage;

    private Paint mPaint;
    private Rect mRect;

    public CustomCircleProgressBar(Context context) {
        this(context, null);
    }

    public CustomCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircleProgressBar, defStyleAttr, 0);
        mFirstColor = a.getColor(R.styleable.CustomCircleProgressBar_firstColor, Color.WHITE);
        mSecondColor = a.getColor(R.styleable.CustomCircleProgressBar_secondColor, Color.GREEN);
        mCircleWidth = a.getDimensionPixelSize(R.styleable.CustomCircleProgressBar_circleWidth, 20);
        mDotCount = a.getInt(R.styleable.CustomCircleProgressBar_dotCount, 20);
        mCurrentCount = a.getInt(R.styleable.CustomCircleProgressBar_currentCount, 0);
        mSplitSize = a.getInt(R.styleable.CustomCircleProgressBar_splitSize, 30);
        mImage = BitmapFactory.decodeResource(getResources(),
                a.getResourceId(R.styleable.CustomCircleProgressBar_bg, R.mipmap.ic_launcher));
        a.recycle();

        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
        //mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);//空心
        int centre = getWidth() / 2;
        int radius = centre - mCircleWidth / 2;
        drawDot(canvas, centre, radius);

        int realRadius = centre - mCircleWidth;//内切圆半径
        //图片显示在内切正方形的一半
        mRect.left = getPaddingLeft() + mCircleWidth + (int)(realRadius * (1 - Math.sqrt(2) / 4));
        mRect.top = getPaddingTop() + mCircleWidth + (int)(realRadius * (1 - Math.sqrt(2) / 4));
        mRect.right = getPaddingLeft() + mCircleWidth + (int)(realRadius * (1 + Math.sqrt(2) / 4));
        mRect.bottom = getPaddingTop() + mCircleWidth + (int)(realRadius * (1 + Math.sqrt(2) / 4));

        //如果图片

        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    private void drawDot(Canvas canvas, int centre, int radius){
        float dotSize = 300 * 1.0f / mDotCount - mSplitSize; //总弧度300,剩下60不绘制
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);

        mPaint.setColor(mFirstColor);
        for(int i=0; i<mDotCount; i++){
            canvas.drawArc(oval, i * (mSplitSize + dotSize) - 240, dotSize, false, mPaint);
        }

        mPaint.setColor(mSecondColor);
        for(int i=0; i<mCurrentCount; i++){
            canvas.drawArc(oval, i * (mSplitSize + dotSize) - 240, dotSize, false, mPaint);
        }
    }


    public void setCurrentCount(int currentCount){
        if(currentCount > mDotCount){
            this.mCurrentCount = mDotCount;
        }else{
            this.mCurrentCount = currentCount;
        }

    }

    public boolean isDone(){
        if(mCurrentCount >= mDotCount)
            return true;
        return false;
    }
}
