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

    public String getBloodType() {
        if (npBloodType == null) {
            return null;
        }
        return bloodTypes[npBloodType.getValue() - 1];
    }

    public Boolean isNegative() {
        if (npNegative == null) {
            return null;
        }
        return npNegative.getValue() == 1;
    }

    private void initView(@NonNull Context context, AttributeSet attributeSet) {
        View v = View.inflate(context, R.layout.blood_type_picker_layout, this);

        npBloodType = v.findViewById(R.id.np_blood_type);
        npNegative = v.findViewById(R.id.np_negative);
        bloodTypeTextView = v.findViewById(R.id.tv_blood_type);

        npBloodType.setMinValue(1);
        npBloodType.setMaxValue(bloodTypes.length);
        npBloodType.setDisplayedValues(bloodTypes);
        npBloodType.setValue(2);

        npNegative.setMinValue(1);
        npNegative.setMaxValue(negative.length);
        npNegative.setDisplayedValues(negative);
        npNegative.setValue(2);

        updateBubbleText();

        npBloodType.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateBubbleText();
        });

        npNegative.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateBubbleText();
        });

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
        }
    }
}
