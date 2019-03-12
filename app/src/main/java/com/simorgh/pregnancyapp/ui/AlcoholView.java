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
    private Drawable selectedBkg;
    private Drawable unSelectedBkg;
    private final int unSelectedTextColor = Color.parseColor("#80545454");
    private final int selectedTextColor = Color.parseColor("#ffffff");


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
        return alcohol.isUseAlcohol();
    }

    @Override
    public void setSelected(boolean selected) {
        alcohol.setUseAlcohol(selected);
        toggleSelected(selected, false);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_alcohol, this);
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

        imgDescription.setAlpha(0.5f);
        imgDescription.setEnabled(false);

        selectedBkg = context.getResources().getDrawable(R.drawable.btn_have_bkg);
        unSelectedBkg = context.getResources().getDrawable(R.drawable.btn_have_not_bkg);


        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
            Utils.hideKeyboard((Activity) v1.getContext());
        });


        have.setOnClickListener(v1 -> toggleSelected(true, true));

        haveNot.setOnClickListener(v1 -> toggleSelected(false, true));

    }

    public void setAlcohol(Alcohol value) {
        if (value == null) {
            alcohol.clear();
            alcohol.setHasData(false);
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        }
        if (haveNot != null && have != null && value != null) {
            alcohol.set(value);
            alcohol.setHasData(true);
            updateViewData();
        }
    }

    private void toggleSelected(boolean selection, boolean fromUser) {
        boolean clear = false;
        if (alcohol.hasData()) {
            if (selection == alcohol.isUseAlcohol() && fromUser) {
                clear = true;
                alcohol.setHasData(false);
            }
        } else {
            if (fromUser) {
                alcohol.setHasData(true);
            }
        }

        if (fromUser) {
            alcohol.setEvaluate(true);
            alcohol.setUseAlcohol(selection);
        }
        if (selection) {
            select(have, haveNot, clear);
        } else {
            select(haveNot, have, clear);
        }
        imgDescription.setEnabled(alcohol.hasData());
        imgDescription.animate().alpha(alcohol.hasData() ? 1f : 0.5f);
        if (!alcohol.hasData()) {
            collapse();
        }
    }

    private void select(Button have, Button haveNot, boolean clearSelection) {
        if (alcohol.hasData() && !clearSelection) {
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

    private void updateViewData() {
        toggleSelected(alcohol.isUseAlcohol(), false);
        boolean enabled = alcohol.getInfo() != null && !alcohol.getInfo().isEmpty();
        if (enabled) {
            description.setText(alcohol.getInfo());
        } else {
            description.setText(null);
        }
        if (!isEnabled()) {
            imgDescription.setEnabled(enabled);
            imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        } else {
            imgDescription.setEnabled(alcohol.hasData());
            imgDescription.animate().alpha(alcohol.hasData() ? 1f : 0.5f);
            if (!alcohol.hasData()) {
                collapse();
            }
        }
    }

    public Alcohol getAlcohol() {
        if (haveNot != null && have != null) {
            alcohol.setInfo(description.getText().toString());
        }
        return alcohol;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        description.setEnabled(enabled);
        have.setEnabled(enabled);
        haveNot.setEnabled(enabled);
        description.setEnabled(enabled);
        if (!enabled) {
            collapse();
        }
        updateViewData();
    }

    private void collapse() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getAlcohol();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), alcohol);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setAlcohol(((State) state).alcohol);
        }
    }

    public static final class State extends BaseSavedState {
        private final Alcohol alcohol;


        public State(Parcel source, boolean selected, Alcohol alcohol) {
            super(source);
            this.alcohol = alcohol;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, Alcohol alcohol) {
            super(source, loader);
            this.alcohol = alcohol;
        }

        State(Parcelable superState, Alcohol alcohol) {
            super(superState);
            this.alcohol = alcohol;
        }
    }

}
