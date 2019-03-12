package com.simorgh.bottombar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class BottomBar extends View {

    private final ArrayList<BottomItem> items = new ArrayList<>();

    private float realHeight = -1;
    private float realWidth = -1;

    private float circleRadius = -1;
    private float circleX = -1;
    private float circleY = -1;

    private float itemTextSize = -1;

    private Paint shadowPaint;
    private int shadowColor;

    private Paint backgroundPaint;
    private int backgroundColor;
    private float backgroundLeftY;

    private Paint mainBackgroundPaint;
    private int mainBackgroundColor;

    private Paint circlePaint;
    private int circleColor;
    private Paint circleIconPaint;
    private int circleIconColor;

    private Paint selectedIconPaint;
    private Paint unSelectedIconPaint;
    private TextPaint itemTextPaint;
    private static Bitmap circleIcon;
    private int selectedIconColor;
    private int unSelectedIconColor;

    //default view height and width
    private static final int DEFAULT_HEIGHT = 70;
    private static final int DEFAULT_WIDTH = 360;


    private final RectF circleIconRect = new RectF();
    private final Rect settingsIconRect = new Rect();
    private final Rect logsIconRect = new Rect();
    private final Rect articlesIconRect = new Rect();
    private final Rect homeIconRect = new Rect();
    private float icons_y_center = -1;
    private float icons_x_center = -1;

    private int selectedIndex = 4;

    private OnItemClickListener itemClickListener;
    private OnCircleItemClickListener circleItemClickListener;

    public class BottomItem {
        private int index;
        private String text;
        private Bitmap icon;

        BottomItem(int index, String text, Bitmap icon) {
            this.index = index;
            this.text = text;
            this.icon = icon;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }
    }


    public BottomBar(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
        Resources resources = getResources();

        backgroundColor = typedArray.getColor(R.styleable.BottomBar_backgroundColor, resources.getColor(R.color.background_color));
        mainBackgroundColor = typedArray.getColor(R.styleable.BottomBar_mainBackgroundColor, resources.getColor(R.color.main_background_color));

        circleColor = typedArray.getColor(R.styleable.BottomBar_circleColor, resources.getColor(R.color.circle_color));
        circleIconColor = typedArray.getColor(R.styleable.BottomBar_circleColor, resources.getColor(R.color.circle_icon_color));

        selectedIconColor = typedArray.getColor(R.styleable.BottomBar_selectedColor, resources.getColor(R.color.selected_color));
        unSelectedIconColor = typedArray.getColor(R.styleable.BottomBar_unSelectedColor, resources.getColor(R.color.unselected_color));
        shadowColor = typedArray.getColor(R.styleable.BottomBar_unSelectedColor, resources.getColor(R.color.unselected_color));

        itemTextSize = typedArray.getDimension(R.styleable.BottomBar_textSize, resources.getDimension(R.dimen.textSize));

        typedArray.recycle();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
            setElevation(10);
        }

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(shadowColor);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        mainBackgroundPaint = new Paint();
        mainBackgroundPaint.setAntiAlias(true);
        mainBackgroundPaint.setStyle(Paint.Style.FILL);
        mainBackgroundPaint.setColor(mainBackgroundColor);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);

        circleIconPaint = new Paint();
        circleIconPaint.setAntiAlias(true);
        circleIconPaint.setStyle(Paint.Style.FILL);
        circleIconPaint.setColor(circleIconColor);

        ColorFilter colorFilter = new LightingColorFilter(Color.TRANSPARENT, selectedIconColor);

        selectedIconPaint = new Paint();
        selectedIconPaint.setAntiAlias(true);
        selectedIconPaint.setStyle(Paint.Style.FILL);
        selectedIconPaint.setColor(selectedIconColor);
        selectedIconPaint.setColorFilter(colorFilter);

        colorFilter = new LightingColorFilter(Color.TRANSPARENT, unSelectedIconColor);
        unSelectedIconPaint = new Paint();
        unSelectedIconPaint.setAntiAlias(true);
        unSelectedIconPaint.setStyle(Paint.Style.FILL);
        unSelectedIconPaint.setColor(unSelectedIconColor);
        unSelectedIconPaint.setColorFilter(colorFilter);


        AssetManager assetMgr = getContext().getAssets();
        //noinspection SpellCheckingInspection
        Typeface typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        itemTextPaint = new TextPaint();
        itemTextPaint.setTextSize(itemTextSize);
        itemTextPaint.setColor(selectedIconColor);
        itemTextPaint.setFakeBoldText(true);
        itemTextPaint.setTextAlign(Paint.Align.CENTER);
        itemTextPaint.setTypeface(typeface);

        circleIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_center);
        Bitmap icon_settings = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings);
        Bitmap icon_logs = BitmapFactory.decodeResource(getResources(), R.drawable.ic_log);
        Bitmap icon_article = BitmapFactory.decodeResource(getResources(), R.drawable.ic_article);
        Bitmap icon_home = BitmapFactory.decodeResource(getResources(), R.drawable.ic_home);

        BottomItem bottomItem;
        bottomItem = new BottomItem(1, "تنظیمات", icon_settings);
        items.add(bottomItem);

        bottomItem = new BottomItem(2, "حساب کاربری", icon_logs);
        items.add(bottomItem);

        bottomItem = new BottomItem(3, "تقویم", icon_article);
        items.add(bottomItem);

        bottomItem = new BottomItem(4, "دوره فعلی", icon_home);
        items.add(bottomItem);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = (int) dp2px(DEFAULT_WIDTH);
        int minHeight = (int) dp2px(DEFAULT_HEIGHT);

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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        realWidth = px2dp(getWidth());
        realHeight = px2dp(getHeight());

        circleRadius = realHeight / 2.6f;
        circleX = realWidth / 2f;
        circleY = realHeight / 3f + 5;

        backgroundLeftY = realHeight / 3f;
        icons_y_center = backgroundLeftY + 22;
        icons_x_center = realWidth / 8.4f;
    }


    private static final Rect r = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.DST);
        r.set(0, 0, getWidth(), (int) dp2px(backgroundLeftY - 1));
        canvas.clipRect(r);
        canvas.restore();


        drawBackground(canvas);

        drawMainCircle(canvas);

        drawItems(canvas);


    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, dp2px(realWidth), dp2px(realHeight / 3f), mainBackgroundPaint);
        canvas.drawLine(0, dp2px(backgroundLeftY - 1), dp2px(realWidth), dp2px(backgroundLeftY - 1), shadowPaint);
        canvas.drawRect(0, dp2px(backgroundLeftY), dp2px(realWidth), dp2px(realHeight), backgroundPaint);
    }

    private void drawMainCircle(Canvas canvas) {
        circleIconRect.set((int) dp2px(circleX - 10), (int) dp2px(circleY - 10), (int) dp2px(circleX + 10), (int) dp2px(circleY + 10));
        canvas.drawCircle(dp2px(circleX), dp2px(circleY), dp2px(circleRadius), circlePaint);
        canvas.drawBitmap(circleIcon, null, circleIconRect, circlePaint);
    }


    private void drawItems(Canvas canvas) {
        settingsIconRect.set((int) dp2px(icons_x_center - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(icons_x_center + 10), (int) dp2px(icons_y_center + 10));

        logsIconRect.set((int) dp2px(icons_x_center * 2.7f - 10), (int) dp2px(icons_y_center - 12)
                , (int) dp2px(icons_x_center * 2.7f + 10), (int) dp2px(icons_y_center + 12));

        homeIconRect.set((int) dp2px(realWidth - icons_x_center - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(realWidth - icons_x_center + 10), (int) dp2px(icons_y_center + 10));

        articlesIconRect.set((int) dp2px(realWidth - icons_x_center * 2.7f - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(realWidth - icons_x_center * 2.7f + 10), (int) dp2px(icons_y_center + 10));

        for (int i = 1; i <= items.size(); i++) {
            canvas.drawBitmap(items.get(i - 1).getIcon(), null, getIconRect(i), getItemIconPaint(i));
//            canvas.drawText(items.get(i - 1).getText(), getIconRect(i).centerX(), dp2px(realHeight - 10), getItemTextPaint(i));
        }

    }

    private boolean isAnimating = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.sqrt(Math.pow(Math.abs(event.getX() - dp2px(circleX)), 2) + Math.pow(Math.abs(event.getY() - dp2px(circleY)), 2))
                        < (getIconRect(2).centerX() - getIconRect(1).centerX() - 70)) {
                    if (circleItemClickListener != null && !isAnimating) {
                        ValueAnimator animator = ValueAnimator.ofFloat(circleRadius, circleRadius - 3);
                        animator.setDuration(150);
                        animator.setRepeatMode(ValueAnimator.REVERSE);
                        animator.setRepeatCount(1);
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.addUpdateListener(animation -> {
                            circleRadius = (float) animation.getAnimatedValue();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                postInvalidateOnAnimation();
                            } else {
                                postInvalidate();
                            }
                        });
                        animator.addListener(new ValueAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimating = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimating = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                                circleItemClickListener.onClick(true);
                            }
                        });
                        animator.start();
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (Math.sqrt(Math.pow(Math.abs(event.getX() - getIconRect(i + 1).centerX()), 2) + Math.pow(Math.abs(event.getY() - getIconRect(i + 1).centerY()), 2))
                                < (getIconRect(2).centerX() - getIconRect(1).centerX() - 50)) {
                            selectedIndex = i + 1;
                            if (itemClickListener != null) {
                                itemClickListener.onClick(items.get(selectedIndex - 1), true);
                            }
                            break;
                        }
                    }
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                invalidate();
                return true;
            default:
                return false;
        }
    }

    private Rect getIconRect(final int i) {
        switch (i) {
            case 1:
                return settingsIconRect;
            case 2:
                return logsIconRect;
            case 3:
                return articlesIconRect;
            case 4:
                return homeIconRect;
            default:
                return settingsIconRect;
        }
    }

    private Paint getItemIconPaint(final int i) {
        if (i == selectedIndex) {
            return selectedIconPaint;
        } else {
            return unSelectedIconPaint;
        }
    }

    private Paint getItemTextPaint(final int i) {
        if (i == selectedIndex) {
            itemTextPaint.setColor(selectedIconColor);
        } else {
            itemTextPaint.setColor(unSelectedIconColor);
        }
        return itemTextPaint;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param selectedIndex: starts from left to right (1 to 4) without counting the main circle
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if (itemClickListener != null) {
            if (selectedIndex == -1) {
                circleItemClickListener.onClick(false);
            } else {
                itemClickListener.onClick(items.get(selectedIndex - 1), false);
            }
        }
        postInvalidate();
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


    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnCircleItemClickListener getCircleItemClickListener() {
        return circleItemClickListener;
    }

    public void setCircleItemClickListener(OnCircleItemClickListener circleItemClickListener) {
        this.circleItemClickListener = circleItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(BottomItem item, boolean fromUser);
    }

    public interface OnCircleItemClickListener {
        void onClick(boolean fromUser);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0, (int) dp2px(backgroundLeftY - 1), getWidth(), getBottom(), 10);
                outline.setRoundRect(0, 0, getWidth(), getHeight(), getHeight() / 2f);
            }
        };
    }
}
