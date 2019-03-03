package com.simorgh.pregnancyapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.database.Date;
import com.simorgh.database.model.Alcohol;
import com.simorgh.expandablelayout.ExpansionLayout;
import com.simorgh.expandablelayout.viewgroup.ExpansionsViewGroupLinearLayout;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.utils.Utils;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

@Keep
public class AlcoholView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private Button have;
    private Button haveNot;
    private ImageView imgDescription;
    private final Alcohol alcohol = new Alcohol();
    private boolean selected = false;


    public AlcoholView(Context context) {
        super(context);
        initView(context, null);
    }

    public AlcoholView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public AlcoholView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        alcohol.setUseAlcohol(selected);
        if (selected) {
            selectHave(haveNot, have, false);
        } else {
            selectHave(have, haveNot, false);
        }
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.alcohol_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        have = v.findViewById(R.id.btn_have);
        haveNot = v.findViewById(R.id.btn_have_not);
        imgDescription = v.findViewById(R.id.img_description);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
            Utils.hideKeyboard((Activity) v1.getContext());
        });


        have.setOnClickListener(v1 -> {
            if (!selected) {
                selectHave(have, haveNot, true);
                selected = true;
            }
        });

        haveNot.setOnClickListener(v1 -> {
            if (selected) {
                selectHave(haveNot, have, true);
                selected = false;
            }
        });

    }

    private void selectHave(Button have, Button haveNot, boolean fromUser) {
        int color = have.getCurrentTextColor();
        Drawable drawable = have.getBackground();
        have.setTextColor(haveNot.getCurrentTextColor());
        have.setBackground(haveNot.getBackground());

        haveNot.setTextColor(color);
        haveNot.setBackground(drawable);
        if (fromUser) {
            alcohol.setEvaluate(true);
        }
    }


    public void setDescription(String summaryText) {
        boolean enabled = summaryText != null && !summaryText.isEmpty();
        if (enabled) {
            alcohol.setInfo(summaryText);
            description.setText(summaryText);
        } else {
            alcohol.setInfo(null);
            description.setText(null);
        }
        if (isEnabled()) {
            imgDescription.setEnabled(enabled);
            imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        }
    }

    public void setAlcohol(Alcohol value) {
        if (value == null) {
            if (selected) {
                setSelected(false);
            }
            alcohol.setEvaluate(false);
            description.setText(null);
            alcohol.setDate(null);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (haveNot != null && have != null && value!=null) {
            alcohol.setDate(value.getDate());
            if (selected != value.isUseAlcohol()) {
                setSelected(value.isUseAlcohol());
            }
            setDescription(value.getInfo());
        }
    }

    public Alcohol getAlcohol() {
        if (haveNot != null && have != null) {
            alcohol.setInfo(description.getText().toString());
            alcohol.setUseAlcohol(selected);
        }
        return alcohol;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
//        imgDescription.setEnabled(enabled);
//        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        description.setEnabled(enabled);
        have.setEnabled(enabled);
        haveNot.setEnabled(enabled);
        description.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        alcohol.setInfo(description.getText().toString());
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), alcohol.isUseAlcohol(), alcohol.getInfo(), alcohol.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setSelected(((State) state).isSelected());
            setDescription(((State) state).getDescription());
            alcohol.setDate(((State) state).getDate());
            setAlcohol(alcohol);
        }
    }

    public static final class State extends BaseSavedState {
        private final boolean selected;
        private final String description;
        private final Date date;


        public State(Parcel source, boolean selected, String description, Date date) {
            super(source);
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, boolean selected, String description, Date date) {
            super(source, loader);
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        public State(Parcelable superState, boolean selected, String description, Date date) {
            super(superState);
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        public boolean isSelected() {
            return selected;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }
    }

}
