package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.pregnancyapp.utils.DialogMaker;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class SettingsFragment extends BaseFragment {

    private UserViewModel mViewModel;
    private TextView makeReport;
    private NavController navController;


    @BindView(R.id.tv_pregnancy_start_date)
    TextView mPregnancyStartDate;

    @BindView(R.id.tv_birth_date)
    TextView mBirthDateStartDate;

    @BindView(R.id.tv_blood_type)
    TextView mBloodType;

    @BindView(R.id.tv_blood_type_title)
    TextView mBloodTypeTitle;

    @BindView(R.id.tv_font_size)
    TextView mFontSize;

    @BindView(R.id.tv_font_size_title)
    TextView mFontSizeTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        makeReport = view.findViewById(R.id.tv_make_report);
        if (getActivity() != null) {
            navController = Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment);
        }


        makeReport.setOnClickListener(v -> {
            try {
                navController.navigate(R.id.action_settingsFragment_to_makeReportFragment);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });


        mFontSizeTitle.setOnClickListener(v -> {
            DialogMaker.createFontChangeDialog(v.getContext(), value -> {
                mViewModel.updateFontSize(repository, value);
            });
        });

        mBloodTypeTitle.setOnClickListener(v -> {
            DialogMaker.createBloodTypeDialog(v.getContext(),(type, isNegative) -> {
                mViewModel.updateBloodType(repository, type, isNegative);
            });
        });

        mBirthDateStartDate.setOnClickListener(v -> {

        });

        mPregnancyStartDate.setOnClickListener(v -> {

        });


        mViewModel.getUser().observe(getActivity(), user -> {
            if (user != null && makeReport != null) {
                StringBuilder bt = new StringBuilder(user.getBloodType());
                bt.append(user.isNegative() ? "<sup>-</sup>" : "<sup>+</sup>");
                mBloodType.setText(Html.fromHtml(String.valueOf(bt)));
                mFontSize.setText(String.valueOf(user.getFontSize()));

                ThreadUtils.execute(() -> {
                    PersianCalendar p = CalendarTool.GregorianToPersian(user.getPregnancyStartDate().getCalendar());
                    int year = p.getPersianYear();
                    int month = p.getPersianMonth() + 1;
                    int day = p.getPersianDay();


                    ThreadUtils.runOnUIThread(() -> {
                        mPregnancyStartDate.setText(String.format("%04d/%02d/%02d", year, month, day));
                    });
                });

                ThreadUtils.execute(() -> {
                    PersianCalendar p = CalendarTool.GregorianToPersian(user.getBirthDate().getCalendar());
                    int year = p.getPersianYear();
                    int month = p.getPersianMonth() + 1;
                    int day = p.getPersianDay();


                    ThreadUtils.runOnUIThread(() -> {
                        mBirthDateStartDate.setText(String.format("%04d/%02d/%02d", year, month, day));
                    });
                });


            }
        });
    }

    @Override
    public void onDestroy() {
        makeReport.setOnClickListener(null);
        navController = null;
        makeReport = null;
        super.onDestroy();
    }
}
