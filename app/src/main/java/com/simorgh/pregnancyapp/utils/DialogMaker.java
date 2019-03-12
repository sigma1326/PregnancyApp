package com.simorgh.pregnancyapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.simorgh.pregnancyapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

@SuppressLint("InflateParams")
public class DialogMaker {


    public interface FontSizeChangeListener {
        void onFontSizeChanged(int value);
    }

    public interface BloodTypeChangeListener {
        void onBloodTypeChanged(String type, boolean isNegative);
    }

    public static void createFontChangeDialog(@NonNull final Context context, @NonNull FontSizeChangeListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_font, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


        Button tv14 = view.findViewById(R.id.tv_font14);
        Button tv16 = view.findViewById(R.id.tv_font16);
        Button tv18 = view.findViewById(R.id.tv_font18);
        Button tv20 = view.findViewById(R.id.tv_font20);
        Button tv22 = view.findViewById(R.id.tv_font22);

        tv14.setOnClickListener(v -> {
            listener.onFontSizeChanged(14);
            alertDialog.dismiss();
        });

        tv16.setOnClickListener(v -> {
            listener.onFontSizeChanged(16);
            alertDialog.dismiss();
        });

        tv18.setOnClickListener(v -> {
            listener.onFontSizeChanged(18);
            alertDialog.dismiss();
        });

        tv20.setOnClickListener(v -> {
            listener.onFontSizeChanged(20);
            alertDialog.dismiss();
        });

        tv22.setOnClickListener(v -> {
            listener.onFontSizeChanged(22);
            alertDialog.dismiss();
        });
    }

    public static void createBloodTypeDialog(@NonNull final Context context, @NonNull BloodTypeChangeListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_blood_type, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


        Button tvAp = view.findViewById(R.id.tv_ap);
        tvAp.setText(Html.fromHtml("A<sup>+</sup>"));
        Button tvAn = view.findViewById(R.id.tv_an);
        tvAn.setText(Html.fromHtml("A<sup>-</sup>"));

        Button tvBp = view.findViewById(R.id.tv_bp);
        tvBp.setText(Html.fromHtml("B<sup>+</sup>"));
        Button tvBn = view.findViewById(R.id.tv_bn);
        tvBn.setText(Html.fromHtml("B<sup>-</sup>"));


        Button tvABp = view.findViewById(R.id.tv_abp);
        tvABp.setText(Html.fromHtml("AB<sup>+</sup>"));
        Button tvABn = view.findViewById(R.id.tv_abn);
        tvABn.setText(Html.fromHtml("AB<sup>-</sup>"));


        Button tvOp = view.findViewById(R.id.tv_op);
        tvOp.setText(Html.fromHtml("O<sup>+</sup>"));
        Button tvOn = view.findViewById(R.id.tv_on);
        tvOn.setText(Html.fromHtml("O<sup>-</sup>"));

        tvAp.setOnClickListener(v -> {
            listener.onBloodTypeChanged("A", false);
            alertDialog.dismiss();
        });


        tvAn.setOnClickListener(v -> {
            listener.onBloodTypeChanged("A", true);
            alertDialog.dismiss();
        });


        tvBp.setOnClickListener(v -> {
            listener.onBloodTypeChanged("B", false);
            alertDialog.dismiss();
        });
        tvBn.setOnClickListener(v -> {
            listener.onBloodTypeChanged("B", true);
            alertDialog.dismiss();
        });

        tvABp.setOnClickListener(v -> {
            listener.onBloodTypeChanged("AB", false);
            alertDialog.dismiss();
        });
        tvABn.setOnClickListener(v -> {
            listener.onBloodTypeChanged("AB", true);
            alertDialog.dismiss();
        });

        tvOp.setOnClickListener(v -> {
            listener.onBloodTypeChanged("O", false);
            alertDialog.dismiss();
        });
        tvOn.setOnClickListener(v -> {
            listener.onBloodTypeChanged("O", true);
            alertDialog.dismiss();
        });

    }
}
