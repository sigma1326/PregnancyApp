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

//    public FeverView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

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
                selectHave(have, haveNot);
                selected = true;
                fever.setHasFever(true);
            }
            Utils.hideKeyboard((Activity) v1.getContext());
        });

        haveNot.setOnClickListener(v1 -> {
            if (selected) {
                selectHave(haveNot, have);
                selected = false;
                fever.setHasFever(false);
            }
            Utils.hideKeyboard((Activity) v1.getContext());
        });

    }

    private void selectHave(Button have, Button haveNot) {
        int color = have.getCurrentTextColor();
        Drawable drawable = have.getBackground();
        have.setTextColor(haveNot.getCurrentTextColor());
        have.setBackground(haveNot.getBackground());

        haveNot.setTextColor(color);
        haveNot.setBackground(drawable);
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
            selectHave(haveNot, have);
        } else {
            selectHave(have, haveNot);
        }
    }

    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            fever.setInfo(summaryText);
            description.setText(summaryText);
        }
    }

    public void setFever(@NonNull Fever value) {
        if (have != null && haveNot != null) {
            fever.setId(value.getId());
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
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), fever.getId(), fever.isHasFever(), fever.getInfo(), fever.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setSelected(((State) state).isSelected());
            setDescription(((State) state).getDescription());
            fever.setId(((State) state).getId());
            fever.setDate(((State) state).getDate());
        }
    }

    public static final class State extends BaseSavedState {
        private final long id;
        private final boolean selected;
        private final String description;
        private final Date date;



        public State(Parcel source, long id, boolean selected, String description, Date date) {
            super(source);
            this.id = id;
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, long id, boolean selected, String description, Date date) {
            super(source, loader);
            this.id = id;
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        public State(Parcelable superState, long id, boolean selected, String description, Date date) {
            super(superState);
            this.id = id;
            this.selected = selected;
            this.description = description;
            this.date = date;
        }

        public long getId() {
            return id;
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
