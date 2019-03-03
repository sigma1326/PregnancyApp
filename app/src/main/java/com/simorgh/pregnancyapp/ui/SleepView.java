package com.simorgh.pregnancyapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.simorgh.database.Date;
import com.simorgh.database.model.SleepTime;
import com.simorgh.expandablelayout.ExpansionLayout;
import com.simorgh.expandablelayout.viewgroup.ExpansionsViewGroupLinearLayout;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.utils.Utils;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

@Keep
public class SleepView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private EditText time;
    private ImageView imgDescription;
    private final SleepTime sleepTime = new SleepTime();


    public SleepView(Context context) {
        super(context);
        initView(context, null);
    }

    public SleepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SleepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public MotherWeightView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.sleep_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        time = v.findViewById(R.id.et_time);
        imgDescription = v.findViewById(R.id.img_description);
        imgDescription.setAlpha(0.5f);
        imgDescription.setEnabled(false);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
            Utils.hideKeyboard((Activity) v1.getContext());
        });

        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableExpand(!s.toString().isEmpty());
                try {
                    float value = Float.parseFloat(s.toString());
                    if (s.length() >= 2) {
                        if (value < 0 || value > 24) {
                            time.setText("");
                        }
                    } else {
                        if (value < 0 || value > 24) {
                            time.setText("");
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.printStackTrace(e);
                }
            }
        });
    }

    public void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        description.setEnabled(enabled);
        time.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }


    public void setDescription(String summaryText) {
        boolean enabled = summaryText != null && !summaryText.isEmpty();
        if (enabled) {
            sleepTime.setInfo(summaryText);
            description.setText(summaryText);
        } else {
            sleepTime.setInfo(null);
            description.setText(null);
        }
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
    }

    public void setSleepTime(SleepTime value) {
        if (value == null) {
            time.setText(null);
            description.setText(null);
            sleepTime.setEvaluate(false);
            sleepTime.setDate(null);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (time != null && description != null && value != null) {
            sleepTime.setDate(value.getDate());
            if (value.getHour() > 0) {
                time.setText(String.valueOf(value.getHour()));
            }
            setDescription(value.getInfo());
        }
    }

    public SleepTime getSleepTime() {
        if (time != null && description != null) {
            try {
                sleepTime.setHour(Float.parseFloat(time.getText().toString()));
                sleepTime.setInfo(description.getText().toString());
                sleepTime.setEvaluate(true);
            } catch (NumberFormatException e) {
                Logger.printStackTrace(e);
                sleepTime.setEvaluate(false);
            }
        }
        return sleepTime;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getSleepTime();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), sleepTime.getHour(), sleepTime.getInfo(), sleepTime.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            sleepTime.setHour(((State) state).getHour());
            setDescription(((State) state).getDescription());
            sleepTime.setDate(((State) state).getDate());
        }
    }

    public static final class State extends BaseSavedState {
        private final float hour;
        private final String description;
        private final Date date;


        public State(Parcel source, float hour, String description, Date date) {
            super(source);
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, float hour, String description, Date date) {
            super(source, loader);
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        public State(Parcelable superState, float hour, String description, Date date) {
            super(superState);
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        public float getHour() {
            return hour;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }
    }

}
