package com.example.tryapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *  讓圖片變成圓形圖片
 */
public class CircleImageView extends ImageView {

    private Paint mPaint; //畫筆

    private int mRadius; //圓形圖片的半徑

    private float mScale; //圖片的縮放比例

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因為是圓形圖片，所以應該讓寬高保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canvas!=null){
            mPaint = new Paint();
            Bitmap bitmap = drawableToBitmap(getDrawable());
            if (bitmap!=null){
                //初始化BitmapShader，傳入bitmap對象
                BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                //計算縮放比例
                mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

                Matrix matrix = new Matrix();
                matrix.setScale(mScale, mScale);
                bitmapShader.setLocalMatrix(matrix);


                mPaint.setShader(bitmapShader);

                //畫圓形，指定好中心點座標、半徑、畫筆
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
            }

        }

    }

    //寫一個drawble轉BitMap的方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable!=null){
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) drawable;
                return bd.getBitmap();
            }

            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }
}

