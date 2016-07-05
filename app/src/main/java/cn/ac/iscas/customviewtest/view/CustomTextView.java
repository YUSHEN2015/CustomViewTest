package cn.ac.iscas.customviewtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.ac.iscas.customviewtest.R;

/**
 * Created by liushen on 2016/6/30.
 */
public class CustomTextView extends View{

    private int IMAGE_SCALE_FITXY = 0;
    private String mText;
    private int mTextColor;
    private int mTextSize;
    private Bitmap mImage;
    private int mImageScale;

    private int mWidth;
    private int mHeight ;

    private Rect mBound;
    private Rect mRect;
    private Paint mPaint;

    public CustomTextView(Context context){
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        mText = ta.getString(R.styleable.CustomTextView_text);
        mTextColor = ta.getInt(R.styleable.CustomTextView_textColor, Color.BLACK);
        mTextSize = ta.getDimensionPixelSize(R.styleable.CustomTextView_textSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mImage = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.CustomTextView_image, 0));
        mImageScale = ta.getInt(R.styleable.CustomTextView_imageScaleType, IMAGE_SCALE_FITXY);
        ta.recycle();*/
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);

            switch (attr)
            {
                case R.styleable.CustomTextView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomTextView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomTextView_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.CustomTextView_textColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_textSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics()));
                    break;

            }
        }
        a.recycle();
        mRect = new Rect();
        mPaint =new Paint();
        mPaint.setTextSize(mTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            // 由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mBound.width();
            int desire = Math.max(desireByImg, desireByTitle);
            mWidth = desire; //当自定义view被ScrollView包裹时，widthMode与widthSize都为0
            if (widthMode == MeasureSpec.AT_MOST) {// wrap_content
                mWidth = Math.min(desire, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mBound.height();
            mHeight = desire;//当自定义view被ScrollView包裹时，heightMode与heightSize都为0,导致view无法显示，所以要设置值
            if (heightMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, heightSize ==0 ? desire:heightSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //super.onDraw(canvas);
        /**
         * 边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (mBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mText, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);

        } else {
            //正常情况，将字体居中
            Log.e("Custom", mText);
            canvas.drawText(mText, mWidth / 2 - mBound.width() * 1.0f / 2, mHeight - getPaddingBottom(), mPaint);
        }

        //取消使用掉的快
        mRect.bottom -= mBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY)
        {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else
        {
            //计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mBound.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }
    }
}
