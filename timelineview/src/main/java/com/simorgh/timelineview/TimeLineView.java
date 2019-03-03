package com.simorgh.timelineview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.simorgh.logger.Logger;

import androidx.annotation.ColorInt;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public class TimeLineView extends View {
    private Boolean showStartLine;
    private Boolean showEndLine;
    private int mTextSize;
    private String mText;
    private int mLineSize;
    private int mLinePadding;
    private boolean mMarkerInCenter;
    private Rect mBounds = new Rect();

    private Typeface typeface;
    @ColorInt
    private int textColor = Color.WHITE;

    @ColorInt
    private int backGroundColor = Color.TRANSPARENT;
    private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);


    //default view height and width
    private static final int DEFAULT_HEIGHT = 80;
    private static final int DEFAULT_WIDTH = 30;

    public TimeLineView(Context context) {
        super(context);
        initView(context, null);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        initAttrs(context, attrs);

        textPaint.setTextSize(mTextSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(dp2px(2));

        try {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iransans_medium.ttf");
            textPaint.setTypeface(typeface);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }


    private void initAttrs(@NonNull final Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimeLineView);
        mText = typedArray.getString(R.styleable.TimeLineView_text);
        textColor = typedArray.getColor(R.styleable.TimeLineView_textColor, Color.WHITE);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TimeLineView_textSize, (int) sp2px(13));
        mMarkerInCenter = typedArray.getBoolean(R.styleable.TimeLineView_markerInCenter, true);
        mLineSize = typedArray.getDimensionPixelSize(R.styleable.TimeLineView_lineSize, (int) dp2px(2));
        mLinePadding = typedArray.getDimensionPixelSize(R.styleable.TimeLineView_linePadding, 0);
        showStartLine = typedArray.getBoolean(R.styleable.TimeLineView_showStartLine, true);
        showEndLine = typedArray.getBoolean(R.styleable.TimeLineView_showEndLine, true);
        typedArray.recycle();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(backGroundColor);

        if (mText != null) {
            try {
                canvas.drawText(mText.split(" ")[0], getWidth() / 2f, getHeight() / 2f - 0.8f * textPaint.getFontMetrics().descent, textPaint);
                canvas.drawText(mText.split(" ")[1], getWidth() / 2f, getHeight() / 2f + 1.2f * textPaint.getFontMetrics().descent, textPaint);
            } catch (Exception e) {
                Logger.printStackTrace(e);
                canvas.drawText("20", getWidth() / 2f, getHeight() / 2f - 0.8f * textPaint.getFontMetrics().descent, textPaint);
                canvas.drawText("فرورودین", getWidth() / 2f, getHeight() / 2f + 1.2f * textPaint.getFontMetrics().descent, textPaint);
            }


            if (showStartLine) {
                canvas.drawLine(getWidth() / 2f, getHeight() / 2f + 0.2f * getHeight(), getWidth() / 2f, getHeight(), textPaint);
            }
            if (showEndLine) {
                canvas.drawLine(getWidth() / 2f, getHeight() / 2f - 0.2f * getHeight(), getWidth() / 2f, 0, textPaint);
            }

        }

    }


    /**
     * Gets timeline view type.
     *
     * @param position   the position
     * @param total_size the total size
     * @return the time line view type
     */
    public static int getTimeLineViewType(int position, int total_size) {
        if (total_size == 1) {
            return LineType.ONLYONE;
        } else if (position == 0) {
            return LineType.BEGIN;
        } else if (position == total_size - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }


    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public String getmText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        postInvalidate();
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
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
