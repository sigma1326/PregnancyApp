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
    private final Fever fever = new Fever();
    private Drawable selectedBkg;
    private Drawable unSelectedBkg;
    private int unSelectedTextColor = Color.parseColor("#80545454");
    private int selectedTextColor = Color.parseColor("#ffffff");


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
        LayoutInflater.from(context).inflate(R.layout.view_fever, this);
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
        if (fever.hasData()) {
            if (selection == fever.hasFever() && fromUser) {
                clear = true;
                fever.setHasData(false);
            }
        } else {
            if (fromUser) {
                fever.setHasData(true);
            }
        }


        if (fromUser) {
            fever.setEvaluate(true);
            fever.setHasFever(selection);
        }
        if (selection) {
            select(have, haveNot, clear);
        } else {
            select(haveNot, have, clear);
        }
        imgDescription.setEnabled(fever.hasData());
        imgDescription.animate().alpha(fever.hasData() ? 1f : 0.5f);
        if (!fever.hasData()) {
            collapse();
        }
    }

    private void select(Button have, Button haveNot, boolean clearSelection) {
        if (fever.hasData() && !clearSelection) {
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        description.setEnabled(enabled);
        have.setEnabled(enabled);
        haveNot.setEnabled(enabled);
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


    @Override
    public void setSelected(boolean selected) {
        fever.setHasFever(selected);
        toggleSelected(selected, false);
    }

    @Override
    public boolean isSelected() {
        return fever.hasFever();
    }

    public void setFever(Fever value) {
        if (value == null) {
            fever.clear();
            updateViewData();
            if (expandableLayout.isExpanded()) {
                expandableLayout.collapse(true);
            }
        } else {
            if (have != null && haveNot != null) {
                fever.set(value);
                fever.setHasData(true);
                updateViewData();
            }
        }
    }


    private void updateViewData() {
        toggleSelected(fever.hasFever(), false);
        boolean enabled = fever.getInfo() != null && !fever.getInfo().isEmpty();
        if (enabled) {
            description.setText(fever.getInfo());
        } else {
            description.setText(null);
        }
        if (!isEnabled()) {
            imgDescription.setEnabled(enabled);
            imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        } else {
            imgDescription.setEnabled(fever.hasData());
            imgDescription.animate().alpha(fever.hasData() ? 1f : 0.5f);
        }
    }

    public Fever getFever() {
        if (have != null && haveNot != null) {
            fever.setInfo(description.getText().toString());
        }
        return fever;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getFever();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), fever);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setFever(((State) state).fever);
        }
    }

    public static final class State extends BaseSavedState {
        private final Fever fever;


        public State(Parcel source, Fever fever) {
            super(source);
            this.fever = fever;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, Fever fever) {
            super(source, loader);
            this.fever = fever;
        }

        public State(Parcelable superState, Fever fever) {
            super(superState);
            this.fever = fever;
        }
    }
}
