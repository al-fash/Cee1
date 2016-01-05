package com.example.bootchai.cee;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by MankinG on 4/22/2015.
 */
public class MyCustomView extends View {

    public String LOG_TAG = MyUtility.LOG_TAG;

    private Paint mPiePaint;
    private Paint mPiePaintInner;
    private Paint mPiePaintButton;
    private Paint mTextPaint;
    private GestureDetector mDetector;
    private boolean flipE = false;

    int mTextHeight = 60;
    int progress = 100;

    private OnCustomViewMotionListener onCustomViewMotionListener;

    public interface OnCustomViewMotionListener{
        void onCustomViewSingleTab();
        void onCustomViewFlingUp();
        void onCustomViewFlingDown();
    }

    public void setOnCustomViewMotionListener(OnCustomViewMotionListener cvml){
        onCustomViewMotionListener = cvml;
    }

    private void init() {
        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);

        mPiePaintInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaintInner.setStyle(Paint.Style.FILL);

        mPiePaintButton = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaintButton.setStyle(Paint.Style.FILL);
        mPiePaintButton.setColor(getResources().getColor(R.color.cee_default));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(getResources().getColor(R.color.cee_caption));
        mTextPaint.setTextSize(mTextHeight);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getFontMetrics(new Paint.FontMetrics());
        mTextPaint.setFakeBoldText(true);

        mDetector = new GestureDetector(getContext(), new GestureListener());
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setProgress(int p){
        progress = p;
        invalidate();
    }

    public void setClicked(boolean clicked){
        if(clicked) {
            mPiePaintButton.setColor(getResources().getColor(R.color.cee_press));
            flipE = true;
        }else{
            mPiePaintButton.setColor(getResources().getColor(R.color.cee_default));
            flipE = false;
            progress = 100;
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = getWidth() - 15;


        drawProgress(canvas);
        RectF mShadowBounds = new RectF(30, 30, scale - 30, scale - 30);
        canvas.drawOval(mShadowBounds, mPiePaintButton);

        float xPos = (scale / 2);
        float yPos = (((getHeight() - 15) / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2)) ;
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        float[] ws = new float[1];
        mTextPaint.getTextWidths("a", ws);
        canvas.drawText("Ce", xPos - ws[0]/2, yPos, mTextPaint);

        float[] ws2 = new float[2];
        mTextPaint.getTextWidths("Ce", ws2);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate((ws2[0] + ws2[1])/2, 0);
        if(flipE) {
            canvas.scale(-1, 1, scale / 2, scale / 2);
        }
        canvas.drawText("e", xPos, yPos, mTextPaint);
        canvas.restore();

    }

    private void drawProgress(Canvas canvas){

        float scale = getWidth() - 15;
        RectF mShadowBounds = new RectF(0, 0, scale, scale);

        //*************************Drawing shadow
        Paint mPiePaintTemp = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaintTemp.setStyle(Paint.Style.FILL);

        mPiePaintTemp.setColor(getResources().getColor(R.color.divider));
        float startAngle = 180 - ((progress*3.6f)/2);
        float sweepAngle = progress*3.6f;
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(15, 15);
        canvas.drawArc(mShadowBounds,startAngle, sweepAngle, true, mPiePaintTemp);
        canvas.restore();

        RectF mShadowBoundsInner = new RectF(20, 20, scale - 20, scale - 20);
        int progressInner = progress;
        startAngle = 180 - ((progressInner*3.6f)/2);
        sweepAngle = progressInner*3.6f;

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(180, scale/2, scale/2);
        canvas.translate(15, 15);
        canvas.drawArc(mShadowBoundsInner,startAngle, sweepAngle, true, mPiePaintTemp);
        canvas.restore();

        mShadowBounds = new RectF(30, 30, scale - 30, scale - 30);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(15, 15);
        canvas.drawOval(mShadowBounds, mPiePaintTemp);
        canvas.restore();
        //*************************

        mShadowBounds = new RectF(0, 0, scale, scale);

        int[] mColors = new int[]{getResources().getColor(R.color.slate), getResources().getColor(R.color.bluegrass)};
        Shader s = new SweepGradient(scale/2, scale/2, mColors, null);
        mPiePaint.setShader(s);


        startAngle = 180 - ((progress*3.6f)/2);
        sweepAngle = progress*3.6f;
        canvas.drawArc(mShadowBounds,startAngle, sweepAngle, true, mPiePaint);

        mColors = new int[]{getResources().getColor(R.color.chartreuse), getResources().getColor(R.color.bluegrass)};
        s = new SweepGradient(scale/2, scale/2, mColors, null);
        mPiePaintInner.setShader(s);

        mShadowBoundsInner = new RectF(20, 20, scale - 20, scale - 20);
        progressInner = progress;
        startAngle = 180 - ((progressInner*3.6f)/2);
        sweepAngle = progressInner*3.6f;

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(180, scale/2, scale/2);
        canvas.drawArc(mShadowBoundsInner,startAngle, sweepAngle, true, mPiePaintInner);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);

        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //Log.i(LOG_TAG, "single tab confirmed");
            if(onCustomViewMotionListener != null){
                onCustomViewMotionListener.onCustomViewSingleTab();
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Log.i(LOG_TAG, "scroll (" + distanceX + ", " + distanceY + ")");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Log.i(LOG_TAG, "fling (" + velocityX + ", " + velocityY + ")");
            if(velocityY > 3000){
                if(onCustomViewMotionListener != null){
                    onCustomViewMotionListener.onCustomViewFlingDown();
                }
            }else if(velocityY < -5000){
                if(onCustomViewMotionListener != null){
                    onCustomViewMotionListener.onCustomViewFlingUp();
                }
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //Log.i(LOG_TAG, "down");
            return true;
        }
    }
}
