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
import com.simorgh.database.Repository;
import com.simorgh.logger.Logger;
import com.simorgh.persianmaterialdatepicker.date.DatePickerDialog;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.SettingsViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.pregnancyapp.utils.DialogMaker;
import com.simorgh.sweetalertdialog.SweetAlertDialog;

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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private UserViewModel mViewModel;
    private SettingsViewModel mSettingsViewModel;
    private TextView makeReport;
    private NavController navController;


    private volatile PersianCalendar tempPersianDate = CalendarTool.GregorianToPersian(Calendar.getInstance());
    private Calendar min = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar max = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar temp = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar now = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

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

    private final CompositeDisposable disposable = new CompositeDisposable();


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
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        makeReport.setOnClickListener(null);
        navController = null;
        makeReport = null;
        super.onDestroy();
    }

    private void clearHourMinuteSecond(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Observable
                .just(mSettingsViewModel)
                .filter(settingsViewModel -> settingsViewModel != null)
                .flatMap(settingsViewModel -> Observable.fromCallable(() -> {
                    mViewModel.loadFirstLog(repository);
                    min.setTimeInMillis(now.getTimeInMillis());
                    max.setTimeInMillis(now.getTimeInMillis());
                    tempPersianDate.setPersianDate(year, monthOfYear + 1, dayOfMonth);
                    temp = CalendarTool.PersianToGregorian(tempPersianDate);

                    if (mSettingsViewModel.getDateType() == 1) {
                        min.add(Calendar.DAY_OF_MONTH, -280);
                        Date date = repository.getFirstLoggedDate();
                        if (date == null) {
                            max.setTimeInMillis(now.getTimeInMillis());
                        } else {
                            max.setTimeInMillis(date.getCalendar().getTimeInMillis());
                        }
                        clearHourMinuteSecond(min);
                        clearHourMinuteSecond(max);
                        clearHourMinuteSecond(temp);

                        boolean invalid = temp.getTimeInMillis() < min.getTimeInMillis()
                                || temp.getTimeInMillis() > now.getTimeInMillis();
                        if (invalid) {
                            return 0;
                        } else if (temp.getTimeInMillis() > max.getTimeInMillis()) {
                            return -1;
                        }
                        mViewModel.updatePregnancyStartDate(repository, new Date(temp));
                        return 1;
                    } else if (mSettingsViewModel.getDateType() == 2) {
                        min.add(Calendar.YEAR, -50);
                        max.add(Calendar.YEAR, -16);

                        clearHourMinuteSecond(min);
                        clearHourMinuteSecond(max);
                        clearHourMinuteSecond(temp);

                        boolean invalid = temp.getTimeInMillis() < min.getTimeInMillis()
                                || temp.getTimeInMillis() > max.getTimeInMillis();
                        if (invalid) {
                            return 0;
                        } else {
                            mViewModel.updateBirthDate(repository, new Date(temp));
                        }
                    }
                    return 1;
                }))
                .compose(Repository.apply())
                .subscribeWith(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Integer invalid) {
                        switch (invalid) {
                            case -1:
                                if (getContext() != null) {
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("پاک کردن رکوردهای قبلی")
                                            .setContentText("آیا اطمینان دارید؟")
                                            .setConfirmText("بله")
                                            .setCancelText("بی‌خیال")
                                            .setConfirmClickListener(sweetAlertDialog -> {
                                                mSettingsViewModel.clearAllData(repository);
                                                sweetAlertDialog.dismissWithAnimation();
                                            })
                                            .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                                            .show();
                                }
                                break;
                            case 0:
                                Toast.makeText(getContext(),
                                        getString(R.string.wrong_selected_date), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
