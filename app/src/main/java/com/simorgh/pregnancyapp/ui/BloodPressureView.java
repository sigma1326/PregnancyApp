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


    private void initView(@NonNull final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_blood_pressure, this);
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
        min = findViewById(R.id.et_min);
        max = findViewById(R.id.et_max);
        imgDescription = findViewById(R.id.img_description);
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
        min.setEnabled(enabled);
        max.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void setDescription(String summaryText) {
        boolean enabled = summaryText != null && !summaryText.isEmpty();
        if (enabled) {
            bloodPressure.setInfo(summaryText);
            description.setText(summaryText);
        } else {
            bloodPressure.setInfo(null);
            description.setText(null);
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
            bloodPressure.clear();
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        } else {
            if (max != null && min != null) {
                bloodPressure.set(value);
                updateViewData();
            }
        }
    }

    private void updateViewData() {
        if (bloodPressure.getMinPressure() != 0) {
            min.setText(String.valueOf((int) bloodPressure.getMinPressure()));
        } else {
            min.setText(null);
        }

        if (bloodPressure.getMaxPressure() != 0) {
            max.setText(String.valueOf((int) bloodPressure.getMaxPressure()));
        } else {
            max.setText(null);
        }


        boolean descriptionEnabled = bloodPressure.getInfo() != null && !bloodPressure.getInfo().isEmpty();
        if (descriptionEnabled) {
            description.setText(bloodPressure.getInfo());
        } else {
            description.setText(null);
        }
        imgDescription.setEnabled(descriptionEnabled);
        imgDescription.animate().alpha(descriptionEnabled ? 1f : 0.5f);
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
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), bloodPressure);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setBloodPressure(((State) state).bloodPressure);
        }
    }

    public static final class State extends BaseSavedState {
        private final BloodPressure bloodPressure;


        public State(Parcel source, BloodPressure bloodPressure) {
            super(source);
            this.bloodPressure = bloodPressure;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, BloodPressure bloodPressure) {
            super(source, loader);
            this.bloodPressure = bloodPressure;
        }

        State(Parcelable superState, BloodPressure bloodPressure) {
            super(superState);
            this.bloodPressure = bloodPressure;
        }
    }
}
