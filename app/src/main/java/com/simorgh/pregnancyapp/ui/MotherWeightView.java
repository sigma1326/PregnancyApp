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
import com.simorgh.database.model.Weight;
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
public class MotherWeightView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private EditText weightView;
    private ImageView imgDescription;
    private final Weight weight = new Weight();


    public MotherWeightView(Context context) {
        super(context);
        initView(context, null);
    }

    public MotherWeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MotherWeightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.mother_weight_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        weightView = v.findViewById(R.id.et_weight);
        imgDescription = v.findViewById(R.id.img_description);
        imgDescription.setEnabled(false);
        imgDescription.setAlpha(0.5f);

        imgDescription.setOnClickListener(v1 -> {
            if (v1.isEnabled()) {
                expandableLayout.toggle(true);
            }
            Utils.hideKeyboard((Activity) v1.getContext());
        });


        weightView.addTextChangedListener(new TextWatcher() {
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
                        if (value < 0 || value > 200) {
                            weightView.setText(null);
                        }
                    } else {
                        if (value < 0 || value > 200) {
                            weightView.setText(null);
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.printStackTrace(e);
                }
            }
        });

    }


    public void setDescription(String summaryText) {
        boolean enabled = summaryText != null && !summaryText.isEmpty();
        if (enabled) {
            weight.setInfo(summaryText);
            description.setText(summaryText);
        } else {
            weight.setInfo(null);
            description.setText(null);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        description.setEnabled(enabled);
        weightView.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void setWeight(Weight value) {
        if (value == null) {
            weight.clear();
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        } else {
            if (weightView != null && description != null) {
                weight.set(value);
                updateViewData();
            }
        }
    }


    private void updateViewData() {
        if (weight.getWeight() != 0) {
            weightView.setText(String.valueOf((int) weight.getWeight()));
        } else {
            weightView.setText(null);
        }

        boolean descriptionEnabled = weight.getInfo() != null && !weight.getInfo().isEmpty();
        if (descriptionEnabled) {
            description.setText(weight.getInfo());
        } else {
            description.setText(null);
        }
        imgDescription.setEnabled(descriptionEnabled);
        imgDescription.animate().alpha(descriptionEnabled ? 1f : 0.5f);
    }

    public Weight getWeight() {
        if (weightView != null && description != null) {
            try {
                weight.setInfo(description.getText().toString());
                weight.setWeight(Float.parseFloat(weightView.getText().toString()));
                weight.setEvaluate(true);
            } catch (NumberFormatException e) {
                Logger.printStackTrace(e);
                weight.setEvaluate(false);
            }
        }
        return weight;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getWeight();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), weight);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setWeight(((State) state).weight);
        }
    }

    public static final class State extends BaseSavedState {
        private Weight weight;

        public State(Parcel source, Weight weight) {
            super(source);
            this.weight = weight;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, Weight weight) {
            super(source, loader);
            this.weight = weight;
        }

        public State(Parcelable superState, Weight weight) {
            super(superState);
            this.weight = weight;
        }
    }
}
