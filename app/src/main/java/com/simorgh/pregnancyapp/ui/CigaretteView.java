package com.simorgh.pregnancyapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private Drawable selectedBkg;
    private Drawable unSelectedBkg;
    private final int unSelectedTextColor = Color.parseColor("#80545454");
    private final int selectedTextColor = Color.parseColor("#ffffff");


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
        LayoutInflater.from(context).inflate(R.layout.view_cigarette, this);
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
        have = findViewById(R.id.btn_have);
        haveNot = findViewById(R.id.btn_have_not);
        imgDescription = findViewById(R.id.img_description);

        selectedBkg = context.getResources().getDrawable(R.drawable.btn_have_bkg);
        unSelectedBkg = context.getResources().getDrawable(R.drawable.btn_have_not_bkg);


        imgDescription.setAlpha(0.5f);
        imgDescription.setEnabled(false);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
            Utils.hideKeyboard((Activity) v1.getContext());
        });

        have.setOnClickListener(v1 -> {
            Utils.hideKeyboard((Activity) v1.getContext());
            toggleSelected(true, true);
        });

        haveNot.setOnClickListener(v1 -> {
            Utils.hideKeyboard((Activity) v1.getContext());
            toggleSelected(false, true);
        });

    }

    private void toggleSelected(boolean selection, boolean fromUser) {
        boolean clear = false;
        if (cigarette.hasData()) {
            if (selection == cigarette.isUseCigarette() && fromUser) {
                clear = true;
                cigarette.setHasData(false);
            }
        } else {
            if (fromUser) {
                cigarette.setHasData(true);
            }
        }


        if (fromUser) {
            cigarette.setEvaluate(true);
            cigarette.setUseCigarette(selection);
        }
        if (selection) {
            select(have, haveNot, clear);
        } else {
            select(haveNot, have, clear);
        }

        imgDescription.setEnabled(cigarette.hasData());
        imgDescription.animate().alpha(cigarette.hasData() ? 1f : 0.5f);
        if (!cigarette.hasData()) {
            collapse();
        }
    }

    private void select(Button have, Button haveNot, boolean clearSelection) {
        if (cigarette.hasData() && !clearSelection) {
            have.setBackground(selectedBkg);
            haveNot.setBackground(unSelectedBkg);
            have.setTextColor(selectedTextColor);
            haveNot.setTextColor(unSelectedTextColor);
        } else {
            have.setBackground(unSelectedBkg);
            haveNot.setBackground(unSelectedBkg);
            have.setTextColor(unSelectedTextColor);
            haveNot.setTextColor(unSelectedTextColor);
        }
    }


    @Override
    public boolean isSelected() {
        return cigarette.isUseCigarette();
    }

    @Override
    public void setSelected(boolean selected) {
        cigarette.setUseCigarette(selected);
        toggleSelected(selected, false);
    }

    public Cigarette getCigarette() {
        if (haveNot != null && have != null) {
            cigarette.setInfo(description.getText().toString());
        }
        return cigarette;
    }


    public void setCigarette(Cigarette value) {
        if (value == null) {
            cigarette.clear();
            cigarette.setHasData(false);
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        } else {
            if (have != null && haveNot != null) {
                cigarette.set(value);
                cigarette.setHasData(true);
                updateViewData();
            }
        }

    }

    private void updateViewData() {
        toggleSelected(cigarette.isUseCigarette(), false);
        boolean enabled = cigarette.getInfo() != null && !cigarette.getInfo().isEmpty();
        if (enabled) {
            description.setText(cigarette.getInfo());
        } else {
            description.setText(null);
        }
        if (!isEnabled()) {
            imgDescription.setEnabled(enabled);
            imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        } else {
            imgDescription.setEnabled(cigarette.hasData());
            imgDescription.animate().alpha(cigarette.hasData() ? 1f : 0.5f);
            if (!cigarette.hasData()) {
                collapse();
            }
        }
    }

    private void collapse() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        description.setEnabled(enabled);
        have.setEnabled(enabled);
        haveNot.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
        updateViewData();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getCigarette();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), cigarette);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setCigarette(((State) state).cigarette);
        }
    }

    public static final class State extends BaseSavedState {
        private final Cigarette cigarette;


        public State(Parcel source, Cigarette cigarette) {
            super(source);
            this.cigarette = cigarette;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, Cigarette cigarette) {
            super(source, loader);
            this.cigarette = cigarette;
        }

        State(Parcelable superState, Cigarette cigarette) {
            super(superState);
            this.cigarette = cigarette;
        }
    }

}
