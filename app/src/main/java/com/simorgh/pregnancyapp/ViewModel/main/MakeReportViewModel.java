package com.simorgh.pregnancyapp.ViewModel.main;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.logger.Logger;
import com.simorgh.reportutil.ReportState;
import com.simorgh.reportutil.ReportUtils;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Calendar;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MakeReportViewModel extends ViewModel {
    private final MutableLiveData<Date> startDate;
    private final MutableLiveData<Date> endDate;
    private LiveData<String> startDateString = new MutableLiveData<>();
    private LiveData<String> endDateString = new MutableLiveData<>();
    private boolean start;
    private final CompositeDisposable disposable = new CompositeDisposable();

    {
        Calendar calendar = Calendar.getInstance();
        endDate = new MutableLiveData<>(new Date(calendar, true));

        calendar.add(Calendar.DAY_OF_MONTH, -30);
        startDate = new MutableLiveData<>(new Date(calendar, true));

        init();
    }


    @SuppressLint("DefaultLocale")
    public void init() {
        startDateString = Transformations.map(startDate, input -> {
            String dateString = "";
            if (startDate.getValue() != null) {
                PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(startDate.getValue().getCalendar());
                dateString = String.format("%d/%d/%d", persianCalendar.getPersianYear(), persianCalendar.getPersianMonth() + 1, persianCalendar.getPersianDay());
            }
            return dateString;
        });

        endDateString = Transformations.map(endDate, input -> {
            String dateString = "";
            if (endDate.getValue() != null) {
                PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(endDate.getValue().getCalendar());
                dateString = String.format("%d/%d/%d", persianCalendar.getPersianYear(), persianCalendar.getPersianMonth() + 1, persianCalendar.getPersianDay());
            }
            return dateString;
        });
    }


    public MutableLiveData<Date> getStartDate() {
        return startDate;
    }

    public void setStartDate(Date value) {
        ThreadUtils.onUI(() -> startDate.setValue(value));
    }

    public MutableLiveData<Date> getEndDate() {
        return endDate;
    }

    public void setEndDate(Date value) {
        ThreadUtils.onUI(() -> endDate.setValue(value));
    }

    public LiveData<String> getStartDateString() {
        return startDateString;
    }

    public LiveData<String> getEndDateString() {
        return endDateString;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean getStart() {
        return start;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void makeReport(FragmentActivity activity, Repository repository, ReportState reportState) {
        ReportUtils.createReport(activity, repository, reportState)
                .subscribeWith(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Toast.makeText(activity, "در حال ایجاد گزارش...", Toast.LENGTH_SHORT).show();
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Logger.i("made pdf:" + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                        Toast.makeText(activity, "عملیات با خطا مواجه شد", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("onComplete");
                    }
                });

    }

    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        super.onCleared();
    }
}
