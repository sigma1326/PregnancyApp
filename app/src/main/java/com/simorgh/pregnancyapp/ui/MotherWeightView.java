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

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public MotherWeightView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

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
                imgDescription.animate().alpha(!s.toString().isEmpty() ? 1f : 0.5f);
                try {
                    float value = Float.parseFloat(s.toString());
                    if (s.length() >= 2) {
                        if (value < 0 || value > 200) {
                            weightView.setText("");
                        }
                    } else {
                        if (value < 0 || value > 200) {
                            weightView.setText("");
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.printStackTrace(e);
                }
            }
        });

    }


    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
            weight.setInfo(summaryText);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        weightView.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void setWeight(Weight value) {
        if (value == null) {
            weightView.setText(null);
            description.setText(null);
            weight.setEvaluate(false);
            weight.setDate(null);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (weightView != null && description != null && value != null) {
            weight.setDate(value.getDate());
            if (value.getWeight() > 0) {
                weightView.setText(String.valueOf(value.getWeight()));
            } else {
                weightView.setText(null);
            }
            description.setText(value.getInfo());
        }
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
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), weight.getWeight(), weight.getInfo(), weight.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            weight.setWeight(((State) state).getWeight());
            setDescription(((State) state).getDescription());
            weight.setDate(((State) state).getDate());
            setWeight(weight);
        }
    }

    public static final class State extends BaseSavedState {
        private final float weight;
        private final String description;
        private final Date date;


        public State(Parcel source, float weight, String description, Date date) {
            super(source);
            this.weight = weight;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, float weight, String description, Date date) {
            super(source, loader);
            this.weight = weight;
            this.description = description;
            this.date = date;
        }

        public State(Parcelable superState, float weight, String description, Date date) {
            super(superState);
            this.weight = weight;
            this.description = description;
            this.date = date;
        }

        public float getWeight() {
            return weight;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }
    }
}
