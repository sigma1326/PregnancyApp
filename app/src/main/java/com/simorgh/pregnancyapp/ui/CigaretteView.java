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
import com.simorgh.database.model.Cigarette;
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
public class CigaretteView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private Button have;
    private Button haveNot;
    private ImageView imgDescription;
    private final Cigarette cigarette = new Cigarette();
    private boolean selected = false;


    public CigaretteView(Context context) {
        super(context);
        initView(context, null);
    }

    public CigaretteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CigaretteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.cigarette_layout, this);
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
            Utils.hideKeyboard((Activity) v1.getContext());
            if (!selected) {
                selectHave(have, haveNot);
                selected = true;
            }
        });

        haveNot.setOnClickListener(v1 -> {
            Utils.hideKeyboard((Activity) v1.getContext());
            if (selected) {
                selectHave(haveNot, have);
                selected = false;
            }
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
    public void setSelected(boolean selected) {
        this.selected = selected;
        cigarette.setUseCigarette(selected);
        if (selected) {
            selectHave(haveNot, have);
        } else {
            selectHave(have, haveNot);
        }
    }

    public Cigarette getCigarette() {
        if (haveNot != null && have != null) {
            cigarette.setInfo(description.getText().toString());
            cigarette.setUseCigarette(selected);
        }
        return cigarette;
    }


    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
            cigarette.setInfo(summaryText);
        }
    }

    public void setCigarette(@NonNull Cigarette value) {
        if (have != null && haveNot != null) {
            cigarette.setId(value.getId());
            cigarette.setDate(value.getDate());
            if (selected != value.isUseCigarette()) {
                setSelected(value.isUseCigarette());
            }
            setDescription(value.getInfo());
        }
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

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getCigarette();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), cigarette.getId(), cigarette.isUseCigarette(), cigarette.getInfo(), cigarette.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setSelected(((State) state).isSelected());
            setDescription(((State) state).getDescription());
            cigarette.setId(((State) state).getId());
            cigarette.setDate(((State) state).getDate());
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
