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

import com.simorgh.database.Date;
import com.simorgh.database.model.Fever;
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
public class FeverView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private Button have;
    private Button haveNot;
    private ImageView imgDescription;
    private boolean selected = false;
    private final Fever fever = new Fever();


    public FeverView(Context context) {
        super(context);
        initView(context, null);
    }

    public FeverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FeverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.fever_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        have = v.findViewById(R.id.btn_have);
        haveNot = v.findViewById(R.id.btn_have_not);
        imgDescription = v.findViewById(R.id.img_description);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
        });

        have.setOnClickListener(v1 -> {
            if (!selected) {
                selectHave(have, haveNot, true);
                selected = true;
                fever.setHasFever(true);
            }
            Utils.hideKeyboard((Activity) v1.getContext());
        });

        haveNot.setOnClickListener(v1 -> {
            if (selected) {
                selectHave(haveNot, have, true);
                selected = false;
                fever.setHasFever(false);
            }
            Utils.hideKeyboard((Activity) v1.getContext());
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
            fever.setEvaluate(true);
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        have.setEnabled(enabled);
        haveNot.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        fever.setHasFever(selected);
        if (selected) {
            selectHave(haveNot, have, false);
        } else {
            selectHave(have, haveNot, false);
        }
    }

    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            fever.setInfo(summaryText);
            description.setText(summaryText);
        }
    }

    public void setFever(Fever value) {
        if (value == null) {
            if (selected) {
                setSelected(false);
            }
            description.setText(null);
            fever.setDate(null);
            fever.setEvaluate(false);
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (have != null && haveNot != null && value!=null) {
            fever.setDate(value.getDate());
            if (selected != value.isHasFever()) {
                setSelected(value.isHasFever());
            }
            setDescription(value.getInfo());
        }
    }

    public Fever getFever() {
        if (have != null && haveNot != null) {
            fever.setInfo(description.getText().toString());
            fever.setHasFever(selected);
        }
        return fever;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getFever();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), fever.isHasFever(), fever.getInfo(), fever.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setSelected(((State) state).isSelected());
            setDescription(((State) state).getDescription());
            fever.setDate(((State) state).getDate());
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
