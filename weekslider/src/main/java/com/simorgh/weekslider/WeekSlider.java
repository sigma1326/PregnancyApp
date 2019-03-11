package com.simorgh.weekslider;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

@Keep
public class WeekSlider extends View {
    private StateUpdateListener stateUpdateListener;
    private Bitmap clouds;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int unreachedColor = Color.parseColor("#fcc0bb");
    private int reachedColor = Color.parseColor("#ffffff");
    private int textColorSlider = Color.parseColor("#8e1618");
    private int textColorLabels = Color.parseColor("#5a0a0d");
    private int colorSmallCircle = Color.parseColor("#f2776e");


    private String startTextDay = "1";
    private String startTextMonth = "فروردین";
    private String endTextDay = "13";
    private String endTextMonth = "آذر";
    private String bubbleText = "";


    private RectF cloudRect = new RectF();
    private RectF reachedRect = new RectF();
    private RectF unreachedRect = new RectF();
    private RectF bubbleRect = new RectF();

    private Path indicatorPath = new Path();

    private float max = 40;
    private int weekNumber;
    private final String weekLabel = "هفته ";
    private final long DURATION = 1200;


    public WeekSlider(Context context) {
        super(context);
        initView(context, null);
    }

    public WeekSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public WeekSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WeekSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        int h;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                h = (int) dp2px(100);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                h = (int) dp2px(60);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                h = (int) dp2px(55);
                break;
            default:
                h = (int) dp2px(60);
        }


        int minHeight = h;
        int minWidth = 6 * h;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(minWidth, widthSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
            height = heightSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(minHeight, heightSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
            width = widthSize;
        }

        int paddingHeight = getPaddingBottom() + getPaddingTop();
        int paddingWidth = getPaddingRight() + getPaddingLeft();
        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth;
            height = minHeight;
        }

        setMeasuredDimension((width + paddingWidth), (height + paddingHeight));
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        paint.setColor(reachedColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(dp2px(8));

        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iransans_medium.ttf");
            paint.setTypeface(typeface);

            clouds = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clouds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (typeface != null) {
            paint.setTypeface(typeface);
        }


    }


    public void setWeekNumber(int weekNumber) {
        float p = (float) weekNumber / max * 100;
        p = (float) Math.ceil(p);
        reachedX = getNewX(p / 100f * (unreachedRect.right - unreachedRect.left) + unreachedRect.left);
        postInvalidate();
    }

    public void goToWeekNumber(int weekNumber) {
        if (weekNumber < 1 || weekNumber > max) {
            weekNumber = 1;
        }
        float p = (weekNumber / max) * 100f;
        p = (float) Math.ceil(p);
        float duration = ((float) weekNumber / max) * DURATION;
        float newReachedX = p / 100f * (unreachedRect.right - unreachedRect.left) + unreachedRect.left;
        if (newReachedX < unreachedRect.left + dp2px(8)) {
            newReachedX = unreachedRect.left + dp2px(8);
        }
        ValueAnimator animator = ValueAnimator.ofFloat(reachedX, getNewX(newReachedX));
        animator.setDuration((long) Math.max(300, Math.abs(duration)));
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(animation -> {
            reachedX = (float) animation.getAnimatedValue();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                postInvalidate();
            }
        });
        animator.start();
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        unreachedRect.set(0.095f * getWidth() + 0.7f * getHeight(), 0.65f * getHeight() - dp2px(4f)
                , getWidth() - 0.7f * getHeight() - 0.080f * getWidth(), 0.65f * getHeight() + dp2px(4f));
        reachedX = unreachedRect.left + dp2px(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawUnreachedLine(canvas);

        weekNumber = (int) ((reachedX - unreachedRect.left) / (unreachedRect.right - unreachedRect.left) * max);

        if (weekNumber < 1 || weekNumber > max) {
            weekNumber = 1;
        }

        drawReachedLine(canvas);


        drawReachedCircle(canvas);


        drawBubble(canvas);

        drawBubbleText(canvas);


        drawBubbleIndicator(canvas);


        drawClouds(canvas);

        if (stateUpdateListener != null) {
            stateUpdateListener
                    .onStateUpdated(((reachedX - unreachedRect.left) / (unreachedRect.right - unreachedRect.left))
                            , weekNumber);
        }
    }

    private void drawBubbleIndicator(Canvas canvas) {
        indicatorPath.reset();
        paint.setColor(reachedColor);
        indicatorPath.moveTo(bubbleRect.centerX(), bubbleRect.bottom - dp2px(1));
        indicatorPath.rLineTo(dp2px(7), 0);
        indicatorPath.rLineTo(-dp2px(7f), 0.2f * getHeight());
        indicatorPath.rLineTo(-dp2px(7f), -0.2f * getHeight());
        indicatorPath.close();

        canvas.drawPath(indicatorPath, paint);

    }

    private void drawBubbleText(Canvas canvas) {
        paint.setColor(textColorSlider);
        paint.setTextSize(sp2px(10));
        bubbleText = String.format("%s%s", weekLabel, numbers[weekNumber - 1]);
        canvas.drawText(bubbleText, reachedX - 0.025f * getWidth(), 0.21f * getHeight(), paint);
    }

    private void drawBubble(Canvas canvas) {
        paint.setColor(reachedColor);
        bubbleRect.set(reachedX - 0.13f * getWidth(), 0, reachedX + 0.08f * getWidth(), 0.33f * getHeight());
        canvas.drawRoundRect(bubbleRect, 50, 50, paint);
    }

    private void drawReachedCircle(Canvas canvas) {
        paint.setColor(colorSmallCircle);
        canvas.drawCircle(reachedX - 0.025f * getWidth(), 0.65f * getHeight(), dp2px(2.5f), paint);
    }

    private void drawReachedLine(Canvas canvas) {
        paint.setColor(reachedColor);
        reachedRect.set(0.075f * getWidth() + 0.7f * getHeight(), 0.65f * getHeight() - dp2px(6f), reachedX
                , 0.65f * getHeight() + dp2px(6f));
        canvas.drawRoundRect(reachedRect, 50, 50, paint);
    }

    private void drawUnreachedLine(Canvas canvas) {
        paint.setColor(unreachedColor);
        canvas.drawRoundRect(unreachedRect, 0, 0, paint);
    }

    private void drawClouds(Canvas canvas) {
        paint.setTextSize(sp2px(10));
        ColorFilter filter = new PorterDuffColorFilter(reachedColor, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        cloudRect.set(0.085f * getWidth(), 0.30f * getHeight()
                , 0.085f * getWidth() + 0.7f * getHeight(), 0.30f * getHeight() + 0.7f * getHeight());
        canvas.drawBitmap(clouds, null, cloudRect, paint);

        paint.setColorFilter(null);
        paint.setColor(textColorLabels);
        canvas.drawText(startTextDay, cloudRect.centerX(), cloudRect.centerY() - dp2px(2), paint);
        canvas.drawText(startTextMonth, cloudRect.centerX(), cloudRect.centerY() + dp2px(8), paint);


        cloudRect.set(getWidth() - 0.085f * getWidth() - 0.7f * getHeight(), 0.30f * getHeight()
                , getWidth() - 0.085f * getWidth(), 0.30f * getHeight() + 0.7f * getHeight());
        if (weekNumber == max) {
            filter = new PorterDuffColorFilter(reachedColor, PorterDuff.Mode.SRC_IN);
        } else {
            filter = new PorterDuffColorFilter(unreachedColor, PorterDuff.Mode.SRC_IN);
        }
        paint.setColorFilter(filter);
        canvas.drawBitmap(clouds, null, cloudRect, paint);

        paint.setColorFilter(null);
        paint.setColor(textColorLabels);
        canvas.drawText(endTextDay, cloudRect.centerX(), cloudRect.centerY() - dp2px(4), paint);
        canvas.drawText(endTextMonth, cloudRect.centerX(), cloudRect.centerY() + dp2px(6), paint);

    }

    private volatile float reachedX = dp2px(120);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float newX = getNewX(event.getX());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                boolean animate = true;
                float w = (bubbleRect.right - bubbleRect.left) / 4f;
                if (event.getX() >= bubbleRect.left + w && event.getX() <= bubbleRect.right - w) {
                    animate = false;
                }
                if (animate) {
                    if (newX != -1) {
                        animate(newX);
                    }
                } else {
                    if (newX != -1) {
                        reachedX = newX;
                        if (stateUpdateListener != null) {
                            stateUpdateListener
                                    .onStateUpdated(((reachedX - unreachedRect.left) / (unreachedRect.right - unreachedRect.left))
                                            , weekNumber);
                        }
                        postInvalidate();
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (newX != -1) {
                    reachedX = newX;
                    if (stateUpdateListener != null) {
                        stateUpdateListener
                                .onStateUpdated(((reachedX - unreachedRect.left) / (unreachedRect.right - unreachedRect.left))
                                        , weekNumber);
                    }
                    postInvalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            default:
                return false;
        }
    }

    public interface StateUpdateListener {
        public void onStateUpdated(float percent, int weekNumber);
    }

    public float getNewX(float newX) {
        if (newX - dp2px(8f) >= unreachedRect.left && newX <= unreachedRect.right) {
            return newX;
        }
        if (newX - dp2px(8f) <= unreachedRect.left) {
            if (weekNumber == 1) {
                return -1;
            }
            return unreachedRect.left + dp2px(8);
        }
        if (newX >= unreachedRect.right) {
            if (weekNumber == max) {
                return -1;
            }
            return unreachedRect.right;
        }
        return newX;
    }


    private static String[] numbers = {
            "اول",
            "دوم",
            "سوم",
            "چهارم",
            "پنجم",
            "ششم",
            "هفتم",
            "هشتم",
            "نهم",
            "دهم",
            "یازدهم",
            "دوازدهم",
            "سیزدهم",
            "چهاردهم",
            "پانزدهم",
            "شانزدهم",
            "هفدهم",
            "هجدهم",
            "نوزدهم",
            "بیستم",
            "بیست و یکم",
            "بیست و دوم",
            "بیست و سوم",
            "بیست و چهارم",
            "بیست و پنجم",
            "بیست و ششم",
            "بیست و هفتم",
            "بیست و هشتم",
            "بیست و نهم",
            "سی‌ام",
            "سی و یکم",
            "سی و دوم",
            "سی و سوم",
            "سی و چهارم",
            "سی و پنجم",
            "سی و ششم",
            "سی و هفتم",
            "سی و هشتم",
            "سی و نهم",
            "چهلم",
    };

    private void animate(float newX) {
        float duration = ((Math.abs((newX - reachedX)) - unreachedRect.left) / (unreachedRect.right - unreachedRect.left)) * DURATION;
        ValueAnimator animator = ValueAnimator.ofFloat(reachedX, newX);
        animator.setDuration((long) Math.max(300, Math.abs(duration)));
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(animation -> {
            reachedX = (float) animation.getAnimatedValue();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                postInvalidate();
            }

        });

        animator.start();
    }


    public String getStartTextDay() {
        return startTextDay;
    }

    public void setStartTextDay(String startTextDay) {
        this.startTextDay = startTextDay;
        postInvalidate();
    }

    public String getStartTextMonth() {
        return startTextMonth;
    }

    public void setStartTextMonth(String startTextMonth) {
        this.startTextMonth = startTextMonth;
        postInvalidate();
    }

    public String getEndTextDay() {
        return endTextDay;
    }

    public void setEndTextDay(String endTextDay) {
        this.endTextDay = endTextDay;
        postInvalidate();
    }

    public String getEndTextMonth() {
        return endTextMonth;
    }

    public void setEndTextMonth(String endTextMonth) {
        this.endTextMonth = endTextMonth;
        postInvalidate();
    }

    public void setStateUpdateListener(StateUpdateListener stateUpdateListener) {
        this.stateUpdateListener = stateUpdateListener;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), reachedX);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            reachedX = ((State) state).getReachedX();
            postInvalidate();
        }
    }

    public static final class State extends BaseSavedState {

        private final float reachedX;

        public State(Parcelable superState, float reachedX) {
            super(superState);
            this.reachedX = reachedX;
        }

        public float getReachedX() {
            return reachedX;
        }
    }

    private float dp2px(float dp) {
        return SizeConverter.dpToPx(getContext(), dp);
    }

    private float px2dp(float px) {
        return SizeConverter.pxToDp(getContext(), px);
    }

    private float sp2px(float sp) {
        return SizeConverter.spToPx(getContext(), sp);
    }


}
