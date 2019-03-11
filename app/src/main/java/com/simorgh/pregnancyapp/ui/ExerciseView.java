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

import com.simorgh.database.model.ExerciseTime;
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
public class ExerciseView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private EditText time;
    private ImageView imgDescription;
    private final ExerciseTime exerciseTime = new ExerciseTime();


    public ExerciseView(Context context) {
        super(context);
        initView(context, null);
    }

    public ExerciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ExerciseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.view_exercise, this);
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
                imgDescription.animate().alpha(!s.toString().isEmpty() ? 1f : 0.5f);
                try {
                    float value = Float.parseFloat(s.toString());
                    if (s.length() >= 2) {
                        if (value < 0 || value > 24 * 60) {
                            time.setText("");
                        }
                    } else {
                        if (value < 0 || value > 24 * 60) {
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

    public void setExerciseTime(ExerciseTime value) {
        if (value == null) {
            exerciseTime.clear();
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        } else {
            if (time != null && description != null) {
                exerciseTime.set(value);
                updateViewData();
            }
        }

    }
    private void updateViewData() {
        if (exerciseTime.getMinute() != 0) {
            time.setText(String.valueOf((int) exerciseTime.getMinute()));
        } else {
            time.setText(null);
        }

        boolean descriptionEnabled = exerciseTime.getInfo() != null && !exerciseTime.getInfo().isEmpty();
        if (descriptionEnabled) {
            description.setText(exerciseTime.getInfo());
        } else {
            description.setText(null);
        }
        imgDescription.setEnabled(descriptionEnabled);
        imgDescription.animate().alpha(descriptionEnabled ? 1f : 0.5f);
    }
    public ExerciseTime getExerciseTime() {
        if (time != null && description != null) {
            try {
                exerciseTime.setMinute(Float.parseFloat(time.getText().toString()));
                exerciseTime.setInfo(description.getText().toString());
                exerciseTime.setEvaluate(true);
            } catch (NumberFormatException e) {
                Logger.printStackTrace(e);
                exerciseTime.setEvaluate(false);
            }
        }
        return exerciseTime;
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getExerciseTime();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), exerciseTime);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setExerciseTime(((State) state).exerciseTime);
        }
    }

    public static final class State extends BaseSavedState {
        private final ExerciseTime exerciseTime;


        public State(Parcel source, ExerciseTime exerciseTime) {
            super(source);
            this.exerciseTime = exerciseTime;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, ExerciseTime exerciseTime) {
            super(source, loader);
            this.exerciseTime = exerciseTime;
        }

        public State(Parcelable superState, ExerciseTime exerciseTime) {
            super(superState);
            this.exerciseTime = exerciseTime;
        }
    }
}
