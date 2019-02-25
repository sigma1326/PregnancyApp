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
import android.widget.TextView;

import com.simorgh.database.Date;
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

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public MotherWeightView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.exercise_layout, this);
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
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        time.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
            exerciseTime.setInfo(summaryText);
        }
    }

    public void setExerciseTime(ExerciseTime value) {
        if (time != null && description != null) {
            if (value.getHour() > 0) {
                time.setText(String.valueOf(value.getHour()));
            }
            exerciseTime.setDate(value.getDate());
            exerciseTime.setId(value.getId());
            setDescription(value.getInfo());
        }
    }

    public ExerciseTime getExerciseTime() {
        if (time != null && description != null) {
            try {
                exerciseTime.setHour(Float.parseFloat(time.getText().toString()));
                exerciseTime.setInfo(description.getText().toString());
            } catch (NumberFormatException e) {
                Logger.printStackTrace(e);
            }
        }
        return exerciseTime;
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getExerciseTime();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), exerciseTime.getId(), exerciseTime.getHour(), exerciseTime.getInfo(), exerciseTime.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            exerciseTime.setHour(((State) state).getHour());
            setDescription(((State) state).getDescription());
            exerciseTime.setId(((State) state).getId());
            exerciseTime.setDate(((State) state).getDate());
        }
    }

    public static final class State extends BaseSavedState {
        private final long id;
        private final float hour;
        private final String description;
        private final Date date;


        public State(Parcel source, long id, float hour, String description, Date date) {
            super(source);
            this.id = id;
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, long id, float hour, String description, Date date) {
            super(source, loader);
            this.id = id;
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        public State(Parcelable superState, long id, float hour, String description, Date date) {
            super(superState);
            this.id = id;
            this.hour = hour;
            this.description = description;
            this.date = date;
        }

        public long getId() {
            return id;
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
