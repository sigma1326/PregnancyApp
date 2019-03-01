package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.persianmaterialdatepicker.date.DatePickerDialog;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.SettingsViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.pregnancyapp.utils.DialogMaker;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class SettingsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private UserViewModel mViewModel;
    private SettingsViewModel mSettingsViewModel;
    private TextView makeReport;
    private NavController navController;


    @BindView(R.id.tv_pregnancy_start_date)
    TextView mPregnancyStartDate;

    @BindView(R.id.tv_pregnancy_start_date_title)
    TextView mPregnancyStartDateTitle;

    @BindView(R.id.tv_birth_date)
    TextView mBirthDateStartDate;

    @BindView(R.id.tv_birth_date_title)
    TextView mBirthDateStartDateTitle;

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
        mSettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
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


        mFontSizeTitle.setOnClickListener(this::createFontSizeDialog);

        mFontSize.setOnClickListener(this::createFontSizeDialog);

        mBloodTypeTitle.setOnClickListener(this::createBloodTypeDialog);

        mBloodType.setOnClickListener(this::createBloodTypeDialog);

        mBirthDateStartDateTitle.setOnClickListener(v -> {
            createDateDialog(2);
        });

        mBirthDateStartDate.setOnClickListener(v -> {
            createDateDialog(2);
        });

        mPregnancyStartDateTitle.setOnClickListener(v -> {
            createDateDialog(1);
        });

        mPregnancyStartDate.setOnClickListener(v -> {
            createDateDialog(1);
        });

        mViewModel.getMotherBirthDate().observe(this, s -> mBirthDateStartDate.setText(s));
        mViewModel.getPregnancyStartDate().observe(this, s -> mPregnancyStartDate.setText(s));
        mViewModel.getBloodType().observe(this, s -> mBloodType.setText(Html.fromHtml((s))));
        mViewModel.getFontSize().observe(this, s -> mFontSize.setText(s));


    }

    private void createFontSizeDialog(View v) {
        DialogMaker.createFontChangeDialog(v.getContext(), value -> {
            mViewModel.updateFontSize(repository, value);
        });
    }

    private void createBloodTypeDialog(View v) {
        DialogMaker.createBloodTypeDialog(v.getContext(), (type, isNegative) -> {
            mViewModel.updateBloodType(repository, type, isNegative);
        });
    }

    private void createDateDialog(int type) {
        mSettingsViewModel.setDateType(type);
        PersianCalendar persianCalendar = null;
        if (type == 1) {
            persianCalendar = CalendarTool
                    .GregorianToPersian(Objects.requireNonNull(Objects.requireNonNull(mViewModel.getUser().getValue())
                            .getPregnancyStartDate()).getCalendar());
        } else if (type == 2) {
            persianCalendar = CalendarTool
                    .GregorianToPersian(Objects.requireNonNull(Objects.requireNonNull(mViewModel.getUser().getValue())
                            .getBirthDate().getCalendar()));
        }

        if (persianCalendar != null) {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    this,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getChildFragmentManager(), "Datepickerdialog");
        }
    }

    @Override
    public void onDestroy() {
        makeReport.setOnClickListener(null);
        navController = null;
        makeReport = null;
        super.onDestroy();
    }

    private volatile PersianCalendar tempPersianDate = CalendarTool.GregorianToPersian(Calendar.getInstance());
    private Calendar min = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar max = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar temp = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar now = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (mSettingsViewModel != null) {
            min.setTimeInMillis(now.getTimeInMillis());
            max.setTimeInMillis(now.getTimeInMillis());
            tempPersianDate.setPersianDate(year, monthOfYear + 1, dayOfMonth);
            temp = CalendarTool.PersianToGregorian(tempPersianDate);

            if (mSettingsViewModel.getDateType() == 1) {
                min.add(Calendar.DAY_OF_MONTH, -280);
                boolean invalid = temp.getTimeInMillis() < min.getTimeInMillis()
                        || temp.getTimeInMillis() > max.getTimeInMillis();
                if (invalid) {
                    Toast.makeText(getContext(), getString(R.string.wrong_selected_date), Toast.LENGTH_SHORT).show();
                } else {
                    mViewModel.updatePregnancyStartDate(repository, new Date(temp));
                }
            } else if (mSettingsViewModel.getDateType() == 2) {
                min.add(Calendar.YEAR, -50);
                max.add(Calendar.YEAR, -16);

                boolean invalid = temp.getTimeInMillis() < min.getTimeInMillis()
                        || temp.getTimeInMillis() > max.getTimeInMillis();
                if (invalid) {
                    Toast.makeText(getContext(), getString(R.string.wrong_selected_date), Toast.LENGTH_SHORT).show();
                } else {
                    mViewModel.updateBirthDate(repository, new Date(temp));
                }
            }
        }
    }
}
