package com.kodbiro.kodbiroimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Danijel on 7.5.2015..
 */
public class KodbiroImageView extends ImageView{

    private Bitmap image;

    private int viewWidth;
    private int viewHeight;

    //border default
    private boolean isBorderVisible = false;
    private int borderWidth = 4;
    private Paint borderPaint;

    //shadow default
    private boolean isShadowVisible = false;
    private float shadowRadius = 6.0f;
    private Paint shadowPaint;

    //circular imageview
    private boolean isCircularImageView;
    private int circularImageViewRadius;
    private Paint circularShaderPaint;

    public void showBorder(boolean showBorder) {
        this.isBorderVisible = showBorder;
        this.invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (borderPaint != null) {
            borderPaint.setColor(borderColor);
        }
        this.invalidate();
    }

    public void showShadow(boolean showShadow) {
        this.isShadowVisible = showShadow;
        this.invalidate();
    }

    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
        this.invalidate();
    }

    public void setShadowColor(int shadowColor) {
        if (shadowPaint != null){
            shadowPaint.setShadowLayer(shadowRadius, 2.0f, 2.0f, shadowColor);
        }
        this.invalidate();
    }

    public void setCircularImageView(boolean isCircularImageView) {
        this.isCircularImageView = isCircularImageView;
        this.invalidate();
    }


    public int getCircularImageViewRadius() {
        return circularImageViewRadius;
    }

    public void setCircularImageViewRadius(int circularImageViewRadius) {
        this.circularImageViewRadius = circularImageViewRadius;
        this.invalidate();
    }

    public KodbiroImageView(Context context) {
        super(context);
        setup();
    }

    public KodbiroImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public KodbiroImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }


    private void setup() {
        circularShaderPaint = new Paint();
        circularShaderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        borderPaint = new Paint();
        setBorderColor(Color.WHITE);
        borderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        shadowPaint = new Paint();
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        this.setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        shadowPaint.setShadowLayer(shadowRadius, 2.0f, 2.0f, Color.BLACK);
    }

    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null) {
            image = bitmapDrawable.getBitmap();
            this.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (image != null) {
            //create scaled bitmap
            Bitmap bitmap = Bitmap.createScaledBitmap(image, viewWidth, viewHeight, false);
            if (!isCircularImageView){
                //canvas horizontal center
                int cx = (canvas.getWidth() - bitmap.getWidth()) / 2;
                drawRectImageView(canvas, cx, bitmap);
            } else {
                drawCircularImageView(canvas, bitmap);
            }

        } else {
            loadBitmap();
        }
    }

    private void drawRectImageView(Canvas canvas, int cx, Bitmap bitmap) {
        if (isBorderVisible) {
            //draw border rect
            canvas.drawRect(cx - borderWidth, borderWidth, (bitmap.getWidth() + cx) + borderWidth, bitmap.getHeight() - borderWidth, borderPaint);
        }

        if (isShadowVisible){
            //draw shadow rect
            canvas.drawRect(cx + borderWidth, borderWidth, (bitmap.getWidth() + cx) - borderWidth, bitmap.getHeight() - borderWidth, shadowPaint);
        }

        //rect for bitmap
        Rect rect = new Rect(cx + borderWidth, borderWidth, bitmap.getWidth() - borderWidth, bitmap.getHeight() - borderWidth);
        canvas.drawBitmap(bitmap, null, rect, null);
    }


    private void drawCircularImageView(Canvas canvas, Bitmap bitmap) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        circularShaderPaint.setShader(shader);
        int circleCenter = viewWidth / 2;

        if (isShadowVisible){
            canvas.drawCircle(circleCenter, circleCenter, circleCenter - (2 * shadowRadius), shadowPaint);
        }
        if (isBorderVisible){
            canvas.drawCircle(circleCenter, circleCenter, circleCenter - (2 * shadowRadius) - borderWidth, borderPaint);
        } else {
            borderWidth = 0;
        }

        canvas.drawCircle(circleCenter, circleCenter, circleCenter - (2 * shadowRadius) - borderWidth, circularShaderPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                float imageSideRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
                float viewSideRatio = (float) MeasureSpec.getSize(widthMeasureSpec) / (float) MeasureSpec.getSize(heightMeasureSpec);
                if (!isCircularImageView){
                    if (imageSideRatio >= viewSideRatio) {
                        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
                        viewHeight = (int) (viewWidth / imageSideRatio);
                        setMeasuredDimension(viewWidth, viewHeight);
                    } else {
                        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
                        viewWidth = (int) (viewHeight * imageSideRatio);
                        setMeasuredDimension(viewWidth, viewHeight);
                    }
                } else {
                    if (MeasureSpec.getSize(widthMeasureSpec) <= MeasureSpec.getSize(heightMeasureSpec)){
                        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
                        viewHeight = (int) (viewWidth / imageSideRatio);
                        setMeasuredDimension(viewWidth, viewWidth);
                    } else {
                        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
                        viewWidth = (int) (viewHeight * imageSideRatio);
                        setMeasuredDimension(viewHeight, viewHeight);
                    }
                }
            } else {
                setMeasuredDimension(0, 0);
            }
        } catch (Exception e){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
