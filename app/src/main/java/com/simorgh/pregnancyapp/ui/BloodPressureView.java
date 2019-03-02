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
import com.simorgh.database.model.BloodPressure;
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
public class BloodPressureView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private EditText min;
    private EditText max;
    private ImageView imgDescription;
    private final BloodPressure bloodPressure = new BloodPressure();


    public BloodPressureView(Context context) {
        super(context);
        initView(context, null);
    }

    public BloodPressureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BloodPressureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public BloodPressureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.blood_pressure_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        min = v.findViewById(R.id.et_min);
        max = v.findViewById(R.id.et_max);
        imgDescription = v.findViewById(R.id.img_description);
        imgDescription.setAlpha(0.5f);
        imgDescription.setEnabled(false);
        min.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (max.getText().toString().isEmpty()) {
                    imgDescription.animate().alpha(s.length() > 0 ? 1f : 0.5f);
                    enableExpand(s.length() > 0);
                }
                try {
                    float value = Float.parseFloat(s.toString());
                    if (s.length() >= 2) {
                        if (value < 7 || value > 19) {
                            min.setText(null);
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.printStackTrace(e);
                }
            }
        });

        max.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (min.getText().toString().isEmpty()) {
                    imgDescription.animate().alpha(s.length() > 0 ? 1f : 0.5f);
                    enableExpand(s.length() > 0);
                }
                try {
                    float value = Float.parseFloat(s.toString());
                    if (s.length() >= 2) {
                        if (value < 7 || value > 19) {
                            max.setText(null);
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.printStackTrace(e);
                }
            }
        });

        imgDescription.setOnClickListener(v1 -> {
            if (v1.isEnabled()) {
                expandableLayout.toggle(true);
                Utils.hideKeyboard((Activity) v1.getContext());
            }
        });

    }

    public void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        min.setEnabled(enabled);
        max.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
        }
    }

    public void setMin(float value) {
        if (min != null) {
            min.setText(String.valueOf(value));
        }
    }

    public void setMax(float value) {
        if (max != null) {
            max.setText(String.valueOf(value));
        }
    }

    public void setBloodPressure(BloodPressure value) {
        if (value == null) {
            min.setText(null);
            max.setText(null);
            description.setText(null);
            bloodPressure.setEvaluate(false);
            bloodPressure.setDate(null);
            bloodPressure.setMaxPressure(0);
            bloodPressure.setMinPressure(0);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (max != null && min != null && value != null) {
            min.setText(String.valueOf(value.getMinPressure()));
            max.setText(String.valueOf(value.getMaxPressure()));
            bloodPressure.setDate(value.getDate());
            bloodPressure.setMinPressure(value.getMinPressure());
            bloodPressure.setMaxPressure(value.getMaxPressure());
            description.setText(value.getInfo());
        }
    }

    public BloodPressure getBloodPressure() {
        if (max != null && min != null && description != null) {
            try {
                bloodPressure.setMinPressure(Float.parseFloat(min.getText().toString()));
                bloodPressure.setMaxPressure(Float.parseFloat(max.getText().toString()));
                bloodPressure.setInfo(description.getText().toString());
                bloodPressure.setEvaluate(true);
            } catch (NumberFormatException e) {
                Logger.printStackTrace(e);
                bloodPressure.setEvaluate(false);
            }
        }
        return bloodPressure;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getBloodPressure();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), bloodPressure.getInfo(), bloodPressure.getDate(), bloodPressure.getMinPressure(), bloodPressure.getMaxPressure());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setDescription(((State) state).getDescription());
            bloodPressure.setDate(((State) state).getDate());
            bloodPressure.setMinPressure(((State) state).getMin());
            bloodPressure.setMaxPressure(((State) state).getMax());
            setBloodPressure(bloodPressure);
        }
    }

    public static final class State extends BaseSavedState {
        private final String description;
        private final Date date;
        private final float min;
        private final float max;


        public State(Parcel source, String description, Date date, float min, float max) {
            super(source);
            this.description = description;
            this.date = date;
            this.min = min;
            this.max = max;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, String description, Date date, float min, float max) {
            super(source, loader);
            this.description = description;
            this.date = date;
            this.min = min;
            this.max = max;
        }

        public State(Parcelable superState, String description, Date date, float min, float max) {
            super(superState);
            this.description = description;
            this.date = date;
            this.min = min;
            this.max = max;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }

        public float getMin() {
            return min;
        }

        public float getMax() {
            return max;
        }
    }
}
