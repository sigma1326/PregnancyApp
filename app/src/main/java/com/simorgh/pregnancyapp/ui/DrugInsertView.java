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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.simorgh.database.Date;
import com.simorgh.database.model.Drug;
import com.simorgh.expandablelayout.ExpansionLayout;
import com.simorgh.expandablelayout.viewgroup.ExpansionsViewGroupLinearLayout;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.utils.Utils;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

@Keep
public class DrugInsertView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private ImageView imgDescription;
    private ImageView imgApply;
    private EditText name;
    private InsertDrugListener insertDrugListener;
    private Drug drug = new Drug();


    public DrugInsertView(Context context) {
        super(context);
        initView(context, null);
    }

    public DrugInsertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DrugInsertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(@NonNull final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_drug_insert, this);
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
        imgDescription = findViewById(R.id.img_description);
        imgApply = findViewById(R.id.img_apply);
        name = findViewById(R.id.et_drug_name);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
            Utils.hideKeyboard((Activity) v1.getContext());
        });

        imgDescription.setAlpha(0.5f);
        imgApply.setAlpha(0.5f);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgApply.animate().alpha(s.length() > 0 ? 1f : 0.5f);
                imgApply.setEnabled(s.length() > 0);
                enableExpand(s.length() > 0);
            }
        });

        imgApply.setOnClickListener(v1 -> {
            Utils.hideKeyboard((Activity) v1.getContext());
            if (v1.isEnabled()) {
                if (insertDrugListener != null) {
                    getDrug();
                    if (!drug.getDrugName().isEmpty()) {
                        insertDrugListener.insertDrug(drug);
                        name.setText("");
                        description.setText("");
                        drug = new Drug();
                    } else {
                        ThreadUtils.onUI(() ->
                                Toast.makeText(context, " فیلد دارو نمی‌تواند خالی باشد", Toast.LENGTH_SHORT).show());
                    }
                }
            } else {
                ThreadUtils.onUI(() ->
                        Toast.makeText(context, " فیلد دارو نمی‌تواند خالی باشد", Toast.LENGTH_SHORT).show());
            }
        });

    }

    private void enableExpand(boolean enabled) {
        imgDescription.setEnabled(enabled);
        imgDescription.animate().alpha(enabled ? 1f : 0.5f);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    private void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
            drug.setInfo(summaryText);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imgDescription.setEnabled(enabled);
        imgApply.setEnabled(enabled);
        name.setEnabled(enabled);
        description.setEnabled(enabled);
        if (!enabled && expandableLayout.isExpanded()) {
            expandableLayout.collapse(true);
        }
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description.getText().toString();
    }

    public String getDrugName() {
        if (name == null) {
            return "";
        }
        return name.getText().toString();
    }

    private void setName(@NonNull String value) {
        if (name != null) {
            drug.setDrugName(value);
            name.setText(value);
        }
    }


    public void setDrug(Drug value) {
        if (value == null) {
            drug.setDate(null);
            drug.setDrugName(null);
            name.setText(null);
            description.setText(null);
            drug.setInfo(null);
        }
        if (name != null && description != null && value != null) {
            drug.setId(value.getId());
            drug.setDate(value.getDate());
            setName(value.getDrugName());
            setDescription(value.getInfo());
        }
    }

    public Drug getDrug() {
        if (name != null && description != null) {
            drug.setDrugName(name.getText().toString());
            drug.setInfo(description.getText().toString());
        }
        return drug;
    }

    public InsertDrugListener getInsertDrugListener() {
        return insertDrugListener;
    }

    public void setInsertDrugListener(InsertDrugListener insertDrugListener) {
        this.insertDrugListener = insertDrugListener;
    }

    public interface InsertDrugListener {
        void insertDrug(Drug drug);
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        getDrug();
        return new State(Objects.requireNonNull(super.onSaveInstanceState()), drug.getId(), drug.getDrugName(), drug.getInfo(), drug.getDate());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            setName(((State) state).getName());
            setDescription(((State) state).getDescription());
            drug.setId(((State) state).getId());
            drug.setDate(((State) state).getDate());
        }
    }

    public static final class State extends BaseSavedState {
        private final long id;
        private final String name;
        private final String description;
        private final Date date;


        public State(Parcel source, long id, String name, String description, Date date) {
            super(source);
            this.id = id;
            this.name = name;
            this.description = description;
            this.date = date;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public State(Parcel source, ClassLoader loader, long id, String name, String description, Date date) {
            super(source, loader);
            this.id = id;
            this.name = name;
            this.description = description;
            this.date = date;
        }

        State(Parcelable superState, long id, String name, String description, Date date) {
            super(superState);
            this.id = id;
            this.name = name;
            this.description = description;
            this.date = date;
        }

        long getId() {
            return id;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }

        Date getDate() {
            return date;
        }
    }

}
