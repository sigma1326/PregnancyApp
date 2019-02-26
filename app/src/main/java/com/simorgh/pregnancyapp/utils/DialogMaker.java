package com.simorgh.pregnancyapp.utils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.simorgh.pregnancyapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class DialogMaker {
//    public static void createDialog(@NonNull final Context context, @NonNull final String title, final int questionCount, final int time
//            , final View.OnClickListener onTestClickListener, final View.OnClickListener onPracticeClickListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.test_or_practice_dialog, null);
//        builder.setView(view);
//
//        String count = String.format("تعداد سوالات: %s", String.valueOf(questionCount));
//        String t = String.format("زمان پاسخگویی: %s دقیقه", String.valueOf(time));
//        ((TextView) view.findViewById(R.id.title)).setText(title);
//        ((TextView) view.findViewById(R.id.tv_question_count)).setText(count);
//        ((TextView) view.findViewById(R.id.tv_time)).setText(t);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(true);
//        alertDialog.show();
//
//
//        view.findViewById(R.id.view_test).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onTestClickListener.onClick(v);
//        });
//
//        view.findViewById(R.id.view_practice).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onPracticeClickListener.onClick(v);
//        });
//    }
//
//    public static void createTestEndDialog(@NonNull final Context context
//            , final View.OnClickListener onReturnAndContinueListener, final View.OnClickListener onShowResultListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.test_ended_dialog, null);
//        builder.setView(view);
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//
//
//        view.findViewById(R.id.view_return).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onReturnAndContinueListener.onClick(v);
//        });
//
//        view.findViewById(R.id.view_show_result).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onShowResultListener.onClick(v);
//        });
//    }
//
//    public static void createTestExitDialog(@NonNull final Context context
//            , final View.OnClickListener onExitListener, final View.OnClickListener onContinueListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.test_exit_dialog, null);
//        builder.setView(view);
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//
//
//        view.findViewById(R.id.view_exit).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onExitListener.onClick(v);
//        });
//
//        view.findViewById(R.id.view_continue).setOnClickListener(v -> {
//            alertDialog.dismiss();
//            onContinueListener.onClick(v);
//        });
//    }
//
//
//    public static void createCompareTestsDialog(@NonNull final Context context, final long milli, final int year, final int major, NavController navController) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.compare_tests_dialog, null);
//        builder.setView(view);
//
//        AppManager.getExecutor().execute(() -> {
//
//            List<TestLog> testLogs = AppManager.getTestRepository().getTestLogs(year, major);
//
//            AndroidUtils.runOnUIThread(() -> {
//                RecyclerView rvLogs;
//                rvLogs = view.findViewById(R.id.rv_test_log);
//                rvLogs.setNestedScrollingEnabled(false);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//                rvLogs.setLayoutManager(linearLayoutManager);
//                rvLogs.setNestedScrollingEnabled(false);
//                TestLogAdapter adapter = new TestLogAdapter(new TestLogAdapter.ItemDiffCallBack(), true, milli, year, major, navController);
//                rvLogs.setAdapter(adapter);
//                rvLogs.setHasFixedSize(true);
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.setCancelable(true);
//                alertDialog.show();
//
//                ((TestLogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(testLogs);
//
//
//                adapter.setOnItemClickListener((year1, major1, currentDate, prevDate) -> {
//                    try {
//                        navController.navigate(TestResultFragmentDirections.actionTestResultFragmentToCompareTestsResultFragment()
//                                .setCurrentDate(milli)
//                                .setCurrentMajor(major)
//                                .setCurrentYear(year)
//                                .setPrevDate(prevDate)
//                                .setPrevMajor(major1)
//                                .setPrevYear(year1));
//                        alertDialog.dismiss();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            });
//        });
//
//
//    }


    public interface FontSizeChangeListener {
        public void onFontSizeChanged(int value);
    }

    public interface BloodTypeChangeListener {
        public void onBloodTypeChanged(String type, boolean isNegative);
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
