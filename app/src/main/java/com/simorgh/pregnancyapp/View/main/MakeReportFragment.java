package com.simorgh.pregnancyapp.View.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.persianmaterialdatepicker.date.DatePickerDialog;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.MakeReportViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.reportutil.ReportState;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class MakeReportFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private MakeReportViewModel mViewModel;
    private PersianCalendar temp = new PersianCalendar();

    @BindView(R.id.chb_drugs)
    CheckBox mDrugs;

    @BindView(R.id.chb_blood_pressure)
    CheckBox mBloodPressure;

    @BindView(R.id.chb_mother_weight)
    CheckBox mMotherWeight;

    @BindView(R.id.chb_fever)
    CheckBox mFever;

    @BindView(R.id.chb_cigarette)
    CheckBox mCigarette;

    @BindView(R.id.chb_alcohol)
    CheckBox mAlcohol;

    @BindView(R.id.chb_sleep_time)
    CheckBox mSleepTime;

    @BindView(R.id.chb_exercise)
    CheckBox mExerciseTime;


    @BindView(R.id.btn_start_range)
    Button mStartRange;

    @BindView(R.id.btn_end_range)
    Button mEndRange;

    @BindView(R.id.btn_make_report)
    Button mMakeReport;

    @BindView(R.id.img_back)
    ImageButton back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MakeReportViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);


        mViewModel.getStartDateString().observe(this, date -> {
            mStartRange.setText(date);
        });

        mViewModel.getEndDateString().observe(this, date -> {
            mEndRange.setText(date);
        });


        mStartRange.setOnClickListener(v -> {
            createDateDialog(true);
        });

        mEndRange.setOnClickListener(v -> {
            createDateDialog(false);
        });

        mMakeReport.setOnClickListener(v -> {

            ReportState reportState = new ReportState();
            reportState.drugs = mDrugs.isChecked();
            reportState.bloodPressure = mBloodPressure.isChecked();
            reportState.motherWeight = mMotherWeight.isChecked();
            reportState.fever = mFever.isChecked();
            reportState.cigarette = mCigarette.isChecked();
            reportState.alcohol = mAlcohol.isChecked();
            reportState.sleepTime = mSleepTime.isChecked();
            reportState.exerciseTime = mExerciseTime.isChecked();

            reportState.startDate = mViewModel.getStartDate().getValue();
            reportState.endDate = mViewModel.getEndDate().getValue();

            mViewModel.makeReport(getActivity(), repository, reportState);
        });

        back.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

    }

    private void createDateDialog(boolean start) {
        mViewModel.setStart(start);
        PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(start ? mViewModel.getStartDate().getValue().getCalendar()
                : mViewModel.getEndDate().getValue().getCalendar());


        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getChildFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        temp.setPersianDate(year, monthOfYear + 1, dayOfMonth);
        if (mViewModel.getStart()) {
            mViewModel.setStartDate(new Date(CalendarTool.PersianToGregorian(temp), true));
        } else {
            mViewModel.setEndDate(new Date(CalendarTool.PersianToGregorian(temp), true));
        }
    }
}
