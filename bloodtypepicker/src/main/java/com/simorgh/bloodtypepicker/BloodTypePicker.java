package com.simorgh.bloodtypepicker;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.simorgh.numberpicker.NumberPicker;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

@Keep
public class BloodTypePicker extends ConstraintLayout {
    private NumberPicker npNegative;
    private NumberPicker npBloodType;

    private TextView bloodTypeTextView;

    private static final String[] bloodTypes = {"A", "B", "AB", "O"};
    private static final String[] negative = {"منفی", "مثبت"};

    private OnBloodTypePickedListener bloodTypePickedListener;
    private final BloodType bloodType = new BloodType("O", true);


    public BloodTypePicker(Context context) {
        super(context);
        initView(context, null);
    }

    public BloodTypePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BloodTypePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public BloodTypePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public String getBloodTypeString() {
        if (npBloodType == null) {
            return null;
        }
        return bloodTypes[npBloodType.getValue() - 1];
    }

    public OnBloodTypePickedListener getBloodTypePickedListener() {
        return bloodTypePickedListener;
    }

    public void setBloodTypePickedListener(OnBloodTypePickedListener bloodTypePickedListener) {
        this.bloodTypePickedListener = bloodTypePickedListener;
    }

    public Boolean isNegative() {
        if (npNegative == null) {
            return null;
        }
        return npNegative.getValue() == 1;
    }

    public BloodType getBloodType() {
        bloodType.setBloodType(bloodTypes[npBloodType.getValue() - 1]);
        bloodType.setNegative(npNegative.getValue() == 2);
        return bloodType;
    }

    public void setBloodType(BloodType value) {
        for (int i = 0; i < bloodTypes.length; i++) {
            if (value.getBloodType().equals(bloodTypes[i])) {
                npBloodType.setValue(i + 1);
                break;
            }
        }
        npNegative.setValue(value.isNegative() ? 1 : 2);
        updateBubbleText();
    }

    private void initView(@NonNull Context context, AttributeSet attributeSet) {
        View v = View.inflate(context, R.layout.blood_type_picker_layout, this);

        npBloodType = v.findViewById(R.id.np_blood_type);
        npNegative = v.findViewById(R.id.np_negative);
        bloodTypeTextView = v.findViewById(R.id.tv_blood_type);

        npBloodType.setMinValue(1);
        npBloodType.setMaxValue(bloodTypes.length);
        npBloodType.setDisplayedValues(bloodTypes);
        npBloodType.setValue(4);

        npNegative.setMinValue(1);
        npNegative.setMaxValue(negative.length);
        npNegative.setDisplayedValues(negative);
        npNegative.setValue(2);

        updateBubbleText();

        npBloodType.setOnValueChangedListener((picker, oldVal, newVal) -> {
            bloodType.setBloodType(bloodTypes[newVal - 1]);
            updateBubbleText();
        });

        npNegative.setOnValueChangedListener((picker, oldVal, newVal) -> {
            bloodType.setNegative(newVal == 1);
            updateBubbleText();
        });

    }

    public interface OnBloodTypePickedListener {
        public void onBloodTypePicked(BloodType bloodType);
    }


    private void updateBubbleText() {
        if (npNegative != null && npBloodType != null) {
            StringBuilder sb = new StringBuilder();
            if (npBloodType.getMaxValue() == bloodTypes.length) {
                sb.append("\n\n\t\t");
                sb.append(bloodTypes[npBloodType.getValue() - 1]);
            }
            switch (negative[npNegative.getValue() - 1]) {
                case "منفی":
                    sb.append("<sup>-</sup>");
                    break;
                case "مثبت":
                    sb.append("<sup>+</sup>");
                    break;
            }

            bloodTypeTextView.setText(Html.fromHtml(String.valueOf(sb)));

            if (bloodTypePickedListener != null) {
                bloodTypePickedListener.onBloodTypePicked(bloodType);
            }
        }
    }
}
