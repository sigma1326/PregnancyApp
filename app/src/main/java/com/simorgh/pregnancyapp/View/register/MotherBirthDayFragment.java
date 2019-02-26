package com.simorgh.pregnancyapp.View.register;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.nicedatepicker.NiceDatePicker;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;
import com.simorgh.pregnancyapp.ViewModel.register.MotherBirthDayViewModel;
import com.simorgh.pregnancyapp.ViewModel.register.RegisterViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class MotherBirthDayFragment extends BaseFragment {

    private RegisterViewModel mViewModel;
    private TitleChangeListener titleChangeListener;

    @BindView(R.id.mother_birth_date)
    NiceDatePicker datePicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mother_birth_day_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RegisterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(titleChangeListener).onTitleChanged(getString(R.string.enter_birth_date));

        Calendar min = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        min.add(Calendar.YEAR, -50);

        Calendar max = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        max.add(Calendar.YEAR, -16);
        datePicker.setDateRange(min, max);

        Date value = mViewModel.getMotherBirthDate().getValue();
        if (value == null) {
            Date date = datePicker.getSelectedDate();
            mViewModel.setMotherBirthDate(date);
        } else {
            datePicker.setSelectedDate(value);
        }
        datePicker.setOnDateSelectedListener(date -> {
            mViewModel.setMotherBirthDate(date);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            titleChangeListener = (TitleChangeListener) context;
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        titleChangeListener = null;
    }
}
