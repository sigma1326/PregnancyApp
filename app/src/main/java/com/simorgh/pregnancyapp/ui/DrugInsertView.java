package com.simorgh.pregnancyapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.simorgh.expandablelayout.ExpandableLayout;
import com.simorgh.pregnancyapp.R;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

@Keep
public class DrugInsertView extends ConstraintLayout {
    private ExpandableLayout expandableLayout;
    private EditText description;
    private ImageView imgDescription;
    private ImageView imgApply;


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

    public DrugInsertView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.drug_insert_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        imgDescription = v.findViewById(R.id.img_description);
        imgApply = v.findViewById(R.id.img_apply);

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
