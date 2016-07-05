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
import android.view.View;

import cn.ac.iscas.customviewtest.R;

/**
 * Created by liushen on 2016/7/1.
 */
public class CustomCircleControlBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mDotCount;
    private int mCurrentCount;
    private int mSplitSize;
    private Bitmap mImage;

    private Paint mPaint;
    private Rect mRect;

    public CustomCircleControlBar(Context context) {
        this(context, null);
    }

    public CustomCircleControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCircleControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircleControlBar, defStyleAttr, 0);
        mFirstColor = a.getColor(R.styleable.CustomCircleControlBar_firstColor, Color.WHITE);
        mSecondColor = a.getColor(R.styleable.CustomProgressBar_secondColor, Color.GREEN);
        mCircleWidth = a.getDimensionPixelSize(R.styleable.CustomCircleControlBar_circleWidth, 20);
        mDotCount = a.getInt(R.styleable.CustomCircleControlBar_dotCount, 20);
        mCurrentCount = a.getInt(R.styleable.CustomCircleControlBar_currentCount, 0);
        mSplitSize = a.getInt(R.styleable.CustomCircleControlBar_splitSize, 30);
        mImage = BitmapFactory.decodeResource(getResources(),
                a.getResourceId(R.styleable.CustomCircleControlBar_bg, R.mipmap.ic_launcher));
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
        float dotSize = 360 * 1.0f / mDotCount - mSplitSize;
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);

        mPaint.setColor(mFirstColor);
        for(int i=0; i<mDotCount; i++){
            canvas.drawArc(oval, i * (mSplitSize + dotSize), dotSize, false, mPaint);
        }

        mPaint.setColor(mSecondColor);
        for(int i=0; i<mCurrentCount; i++){
            canvas.drawArc(oval, i * (mSplitSize + dotSize) - 90, dotSize, false, mPaint);//-90从正上方开始绘制
        }
    }


    public void setCurrentCount(int currentCount){
        this.mCurrentCount = currentCount;
    }

}
