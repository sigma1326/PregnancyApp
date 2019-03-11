package com.simorgh.pregnancyapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.simorgh.expandablelayout.ExpandableLayout;
import com.simorgh.pregnancyapp.R;
import com.simorgh.weekslider.SizeConverter;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_FRACTION;
import static android.util.TypedValue.COMPLEX_UNIT_FRACTION_PARENT;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

@Keep
public class EmbryoWeekInfoView extends ConstraintLayout {
    private ExpandableLayout expandableLayout;
    private TextSwitcher summary;
    private TextView readMore;
    private TextView title;
    private OnReadMoreClickedListener clickedListener;


    public EmbryoWeekInfoView(Context context) {
        super(context);
        initView(context, null);
    }

    public EmbryoWeekInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EmbryoWeekInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public EmbryoWeekInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private ViewSwitcher.ViewFactory mFactory = () -> {
        TextView t = new TextView(getContext());
        t.setGravity(Gravity.START);
        t.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/iransans_medium.ttf"));
        t.setTextSize(COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tv_summary)
                + SizeConverter.spToPx(getContext(), 2));
        t.setTextColor(Color.parseColor("#511011"));
        return t;
    };

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.embryo_week_info_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        summary = v.findViewById(R.id.tv_summary);
        readMore = v.findViewById(R.id.tv_read_more);
        title = v.findViewById(R.id.tv_title);

        summary.setFactory(mFactory);

        title.setOnClickListener(v1 -> {
            expandableLayout.toggle();
        });

        readMore.setOnClickListener(v1 -> {
            if (clickedListener != null) {
                clickedListener.onReadMoreClicked(v1);
            }
        });

        readMore.setTextSize(COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tv_summary));
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        readMore.setOnClickListener(null);
//        summary = null;
//        readMore = null;
//        title = null;
//        super.onDetachedFromWindow();
//    }

    public void setTitle(@NonNull String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
    }

    public void setSummary(@NonNull String summaryText) {
        if (summary != null) {
            summary.setText(summaryText);
        }
    }

    public OnReadMoreClickedListener getClickedListener() {
        return clickedListener;
    }

    public void setClickedListener(OnReadMoreClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }
}
