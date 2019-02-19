package com.simorgh.pregnancyapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.expandablelayout.ExpandableLayout;
import com.simorgh.pregnancyapp.R;

import org.w3c.dom.Text;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

@Keep
public class BloodPressureView extends ConstraintLayout {
    private ExpandableLayout expandableLayout;
    private EditText description;
    private EditText min;
    private EditText max;
    private TextView title;
    private ImageView imgDescription;


    public BloodPressureView(Context context) {
        super(context);
        initView(context, null);
    }

    public BloodPressureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BloodPressureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public BloodPressureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.blood_pressure_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        title = v.findViewById(R.id.tv_title);
        min = v.findViewById(R.id.et_min);
        max = v.findViewById(R.id.et_max);
        imgDescription = v.findViewById(R.id.img_description);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle();
        });

    }


    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
        }
    }

}
