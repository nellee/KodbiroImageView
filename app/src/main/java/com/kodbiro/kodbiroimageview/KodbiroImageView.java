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
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Danijel Kunkic on 7.5.2015
 *
 * KodbiroImageView class is custom ImageView class for displaying image
 * Can load images from various sources (such as resources or content providers)
 * Provides custom shadow around image and custom border
 * Can display image in rectangular and circular shape
 * Preserve aspect ratio of image
 */
public class KodbiroImageView extends ImageView{

    private Bitmap scaledBitmap;

    private int viewWidth;
    private int viewHeight;

    private Rect rectForImage;

    //border default
    private boolean isBorderVisible = false;
    private int borderWidth = 4;
    private Paint borderPaint;

    //shadow default
    private boolean isShadowVisible = false;
    private float shadowRadius = 6.0f;
    private Paint shadowPaint;
    private int shadowColor = Color.BLACK;

    //circular imageview
    private boolean isCircularImageView;
    private int circularImageViewRadius;
    private Paint circularShaderPaint;


    /**
     * border visibility
     * @param showBorder    if this is true border will show with default border width and white color
     */
    public void showBorder(boolean showBorder) {
        this.isBorderVisible = showBorder;
        this.invalidate();
    }

    /**
     * set border width
     * @param borderWidth default value is 4
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    /**
     * set border color direct to border paint
     * @param borderColor resource color
     */
    public void setBorderColor(int borderColor) {
        if (borderPaint != null) {
            borderPaint.setColor(borderColor);
        }
        this.invalidate();
    }

    /**
     * set border textures from bitmap, shader will be create with repeat tileX and tileY
     * @param bitmap texture
     */
    public void setBorderTextures(Bitmap bitmap){
        if (borderPaint != null){
            borderPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }
        this.invalidate();
    }

    /**
     * shadow visibility
     * @param showShadow if this is true shadow will show with default radius and black color
     */
    public void showShadow(boolean showShadow) {
        this.isShadowVisible = showShadow;
        this.invalidate();
    }

    /**
     * set shadow radius
     * @param shadowRadius default size is 6.0f
     */
    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
        this.invalidate();
    }

    /**
     * set shadow color direct to shadow color
     * @param shadowColor resource color
     */
    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        this.shadowPaint.setShadowLayer(shadowRadius, 2.0f, 2.0f, shadowColor);
        this.invalidate();
    }

    /**
     * @param isCircularImageView if this is true view will be circular
     */
    public void setCircularImageView(boolean isCircularImageView) {
        this.isCircularImageView = isCircularImageView;
        this.invalidate();
    }

    /**
     * this will set circular view radius
     * @param circularImageViewRadius int radius
     */
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

        //create shader paint for circular ImageView
        //shader used to draw a bitmap as a texture
        this.circularShaderPaint = new Paint();
        this.circularShaderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //create rectangle for image
        this.rectForImage = new Rect();

        //create paint for drawing border
        this.borderPaint = new Paint();
        this.setBorderColor(Color.WHITE);
        this.borderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //create paint for drawing shadow
        this.shadowPaint = new Paint();
        this.shadowPaint.setColor(Color.WHITE);
        this.shadowPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //we need to enable hardware acceleration for this view for drawing shadow
        this.setLayerType(LAYER_TYPE_SOFTWARE, this.shadowPaint);

        //This draws a shadow layer below the main layer, with the specified
        //offset and color, and blur radius. If radius is 0, then the shadow
        //layer is removed.
        this.shadowPaint.setShadowLayer(shadowRadius, 2.0f, 2.0f, this.shadowColor);
    }

    /**
     * First get bitmap drawable
     * create scaled bitmap from bitmap drawable
     */
    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            //create scaled bitmap
            this.scaledBitmap = Bitmap.createScaledBitmap(bitmap, viewWidth, viewHeight, false);
            this.invalidate();
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas)
    {
        if (scaledBitmap != null) {
            if (!isCircularImageView){
                drawRectImageView(canvas);
            } else {
                drawCircularImageView(canvas);
            }
        } else {
            loadBitmap();
        }
    }

    /**
     * Draw rectangular ImageView with shadow, border and scaled bitmap on canvas
     *
     * @param canvas    canvas on which to draw
     */
    private void drawRectImageView(Canvas canvas) {

        //calculate canvas horizontal center
        int cx = (canvas.getWidth() - this.scaledBitmap.getWidth()) / 2;

        if (this.isBorderVisible) {
            //draw border rectangle
            canvas.drawRect(cx - this.borderWidth, this.borderWidth, (this.scaledBitmap.getWidth() + cx) + this.borderWidth, this.scaledBitmap.getHeight() - this.borderWidth, this.borderPaint);
        }

        if (this.isShadowVisible){
            //draw shadow rectangle
            canvas.drawRect(cx + this.borderWidth, this.borderWidth, (this.scaledBitmap.getWidth() + cx) - this.borderWidth, this.scaledBitmap.getHeight() - this.borderWidth, this.shadowPaint);
        }

        //rectangle for bitmap
        this.rectForImage.set(cx + this.borderWidth, this.borderWidth, this.scaledBitmap.getWidth() - this.borderWidth, this.scaledBitmap.getHeight() - this.borderWidth);
        canvas.drawBitmap(this.scaledBitmap, null, this.rectForImage, null);
    }

    /**
     * Draw circular ImageView with shadow, border and scaled bitmap on canvas
     *
     * @param canvas    canvas on which to draw
     */
    private void drawCircularImageView(Canvas canvas) {
        //create shader textures from scaledBitmap
        //CLAMP replicate the edge color if the shader draws outside of its original bounds
        BitmapShader shader = new BitmapShader(this.scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.circularShaderPaint.setShader(shader);

        //calculate circle center
        int circleCenter = canvas.getWidth() / 2;

        if (this.isShadowVisible){
            //first draw circle shadow
            canvas.drawCircle(circleCenter, circleCenter, circleCenter - this.shadowRadius, this.shadowPaint);
        }
        if (this.isBorderVisible){
            //draw circle border
            canvas.drawCircle(circleCenter, circleCenter, circleCenter - this.borderWidth + this.shadowRadius, this.borderPaint);
        } else {
            this.borderWidth = 0;
        }

        //draw circle with bitmap shader inside
        canvas.drawCircle(circleCenter, circleCenter, circleCenter - this.shadowRadius - this.borderWidth, this.circularShaderPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            //calculate image ration
            float imageSideRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
            //calculate view ratio
            float viewSideRatio = (float) MeasureSpec.getSize(widthMeasureSpec) / (float) MeasureSpec.getSize(heightMeasureSpec);

            if (!isCircularImageView){
                measuredDimensionForRectangle(imageSideRatio, viewSideRatio, widthMeasureSpec, heightMeasureSpec);
            } else {
                measuredDimensionForCircle(imageSideRatio, widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    /**
     * set new measured width and height of this view
     * circle view
     * width and height need to be same size, smaller measured size will apply
     *
     * @param imageSideRatio drawable width / height
     * @param widthMeasureSpec the measured width of this view
     * @param heightMeasureSpec the measured height of this view
     */
    private void measuredDimensionForCircle(float imageSideRatio, int widthMeasureSpec, int heightMeasureSpec) {
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

    /**
     * set new measured width and height of this view
     * rectangular view
     *
     * @param imageSideRatio drawable width / height
     * @param viewSideRatio view width / height
     * @param widthMeasureSpec the measured width of this view
     * @param heightMeasureSpec the measured height of this view
     */
    private void measuredDimensionForRectangle(float imageSideRatio, float viewSideRatio, int widthMeasureSpec, int heightMeasureSpec) {
        if (imageSideRatio >= viewSideRatio) {
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            viewHeight = (int) (viewWidth / imageSideRatio);
            setMeasuredDimension(viewWidth, viewHeight);
        } else {
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
            viewWidth = (int) (viewHeight * imageSideRatio);
            setMeasuredDimension(viewWidth, viewHeight);
        }
    }
}
