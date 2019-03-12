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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_sleep, this);
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setOrientation(VERTICAL);
        setBackground(null);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        expandableLayout = findViewById(R.id.expandable_layout);
        description = findViewById(R.id.et_description);
        time = findViewById(R.id.et_time);
        imgDescription = findViewById(R.id.img_description);
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

    private void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        description.setEnabled(enabled);
        time.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }


    public void setSleepTime(SleepTime value) {
        if (value == null) {
            sleepTime.clear();
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (time != null && description != null && value != null) {
            sleepTime.set(value);
            updateViewData();
        }
    }

    private void updateViewData() {
        if (sleepTime.getHour() != 0) {
            time.setText(String.valueOf((int) sleepTime.getHour()));
        } else {
            time.setText(null);
        }

        boolean descriptionEnabled = sleepTime.getInfo() != null && !sleepTime.getInfo().isEmpty();
        if (descriptionEnabled) {
            description.setText(sleepTime.getInfo());
        } else {
            description.setText(null);
        }
        imgDescription.setEnabled(descriptionEnabled);
        imgDescription.animate().alpha(descriptionEnabled ? 1f : 0.5f);
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
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), sleepTime);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setSleepTime(((State) state).sleepTime);
        }
    }

    public static final class State extends BaseSavedState {
        private final SleepTime sleepTime;


        public State(Parcel source, SleepTime sleepTime) {
            super(source);
            this.sleepTime = sleepTime;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, SleepTime sleepTime) {
            super(source, loader);
            this.sleepTime = sleepTime;
        }

        public State(Parcelable superState, SleepTime sleepTime) {
            super(superState);
            this.sleepTime = sleepTime;
        }
    }

}
