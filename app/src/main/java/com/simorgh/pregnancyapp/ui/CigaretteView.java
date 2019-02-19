package com.simorgh.pregnancyapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.expandablelayout.ExpansionLayout;
import com.simorgh.expandablelayout.viewgroup.ExpansionsViewGroupLinearLayout;
import com.simorgh.pregnancyapp.R;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

@Keep
public class CigaretteView extends ExpansionsViewGroupLinearLayout {
    private ExpansionLayout expandableLayout;
    private EditText description;
    private Button have;
    private Button haveNot;
    private TextView title;
    private ImageView imgDescription;


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

//    public FeverView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context, attrs);
//    }

    private void initView(@NonNull final Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.cigarette_layout, this);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);


        expandableLayout = v.findViewById(R.id.expandable_layout);
        description = v.findViewById(R.id.et_description);
        title = v.findViewById(R.id.tv_title);
        have = v.findViewById(R.id.btn_have);
        haveNot = v.findViewById(R.id.btn_have_not);
        imgDescription = v.findViewById(R.id.img_description);

        imgDescription.setOnClickListener(v1 -> {
            expandableLayout.toggle(true);
        });

    }


    public void setDescription(@NonNull String summaryText) {
        if (description != null) {
            description.setText(summaryText);
        }
    }

}
