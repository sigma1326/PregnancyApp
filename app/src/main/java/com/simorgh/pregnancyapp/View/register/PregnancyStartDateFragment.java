package com.simorgh.pregnancyapp.View.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.nicedatepicker.NiceDatePicker;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;
import com.simorgh.pregnancyapp.ViewModel.register.RegisterViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class PregnancyStartDateFragment extends BaseFragment {

    private RegisterViewModel mViewModel;
    private TitleChangeListener titleChangeListener;

    @BindView(R.id.pregnancy_start_date)
    NiceDatePicker datePicker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pregnancy_start_date, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RegisterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(titleChangeListener).onTitleChanged(getString(R.string.enter_pregnancy_start_date));


        Calendar min = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        min.add(Calendar.DAY_OF_MONTH, -280);

        Calendar max = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        datePicker.setDateRange(min, max);

        Date value = mViewModel.getPregnancyStartDate().getValue();
        if (value == null) {
            Date date = datePicker.getSelectedDate();
            mViewModel.setPregnancyStartDate(date);
        } else {
            datePicker.setSelectedDate(value);
        }
        datePicker.setOnDateSelectedListener(date -> mViewModel.setPregnancyStartDate(date));
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
        titleChangeListener = null;
        super.onDetach();
    }

}
