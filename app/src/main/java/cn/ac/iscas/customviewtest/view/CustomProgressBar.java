package cn.ac.iscas.customviewtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.ac.iscas.customviewtest.R;

/**
 * Created by liushen on 2016/6/30.
 */
public class CustomProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mSpeed;

    private Paint mPaint;
    private int mProgress = 0;
    private boolean isNext;

    public CustomProgressBar(Context context){
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
                    mFirstColor = a.getColor(R.styleable.CustomProgressBar_firstColor, Color.BLUE);


                    mSecondColor = a.getColor(R.styleable.CustomProgressBar_secondColor, Color.CYAN);

                    mCircleWidth = a.getDimensionPixelSize(R.styleable.CustomProgressBar_circleWidth, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));

                    mSpeed = a.getInt(R.styleable.CustomProgressBar_speed, 20);

        a.recycle();

        mPaint = new Paint();

        // 绘图线程
        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    mProgress++;
                    if (mProgress == 360)
                    {
                        mProgress = 0;
                        if (!isNext)
                            isNext = true;
                        else
                            isNext = false;
                    }
                    postInvalidate();
                    try
                    {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED){//解决当被ScrollView包围的问题
            setMeasuredDimension(200, 200);
        }else if(widthMode == MeasureSpec.UNSPECIFIED){
            setMeasuredDimension(heightSize, heightSize);
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            setMeasuredDimension(widthSize, widthSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        //super.onDraw(canvas);
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        if (!isNext)
        {// 第一颜色的圈完整，第二颜色跑
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
            mPaint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        } else
        {
            mPaint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        }
    }


}
