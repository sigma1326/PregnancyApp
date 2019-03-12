package com.simorgh.pregnancyapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import static android.util.TypedValue.COMPLEX_UNIT_PX;

@Keep
public class EmbryoWeekInfoView extends ConstraintLayout {
    private ExpandableLayout expandableLayout;
    private TextSwitcher summary;
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

    private final ViewSwitcher.ViewFactory mFactory = () -> {
        TextView t = new TextView(getContext());
        t.setGravity(Gravity.START);
        t.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getContext()).getAssets(), "fonts/iransans_medium.ttf"));
        t.setTextSize(COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tv_summary)
                + SizeConverter.spToPx(getContext(), 2));
        t.setTextColor(Color.parseColor("#511011"));
        return t;
    };

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_embryo_week_info, this);
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setBackground(getResources().getDrawable(R.drawable.expandable_layout_bkg));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.gravity = Gravity.TOP;
        setLayoutParams(params);


        expandableLayout = findViewById(R.id.expandable_layout);
        summary = findViewById(R.id.tv_summary);
        TextView readMore = findViewById(R.id.tv_read_more);
        title = findViewById(R.id.tv_title);

        summary.setFactory(mFactory);

        title.setOnClickListener(v1 -> expandableLayout.toggle());

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
