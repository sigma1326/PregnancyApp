package com.simorgh.pregnancyapp.ViewModel.main;

import android.annotation.SuppressLint;
import android.util.Pair;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.User;
import com.simorgh.logger.Logger;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressLint({"DefaultLocale"})
public class UserViewModel extends ViewModel {
    private final MutableLiveData<Date> firstLoggedDate = new MutableLiveData<>();
    private final Calendar nowC = Calendar.getInstance();
    private LiveData<String> pregnancyStartDate;
    private LiveData<String> motherBirthDate = new MutableLiveData<>();
    private LiveData<String> bloodType = new MutableLiveData<>();
    private LiveData<String> fontSize = new MutableLiveData<>();
    private LiveData<User> user = new MutableLiveData<>();

    private LiveData<String> remainingDays;
    private LiveData<String> currentWeek;
    private LiveData<String> daysToDelivery;

    private LiveData<Pair<String, String>> startWeekLabels;
    private LiveData<Pair<String, String>> endWeekLabels;

    private LiveData<Integer> currentWeekNumber;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public void getUserLiveData(Repository repository) {
        if (repository != null) {
            user = repository.getUserLiveData();
            remainingDays = Transformations.map(user, input -> {
                float diffDays = CalendarTool.getDaysFromDiff(nowC, input.getPregnancyStartDate().getCalendar());
                return String.valueOf(40 * 7 - ((int) diffDays)) + " روز" + "\n" + "باقی‌مانده";
            });

            daysToDelivery = Transformations.map(user, input -> {
                Calendar d = input.getPregnancyStartDate().getCalendar();
                d.add(Calendar.DAY_OF_MONTH, 40 * 7);
                PersianCalendar p = CalendarTool.GregorianToPersian(d);

                return String.format("%s %s\nروز زایمان", String.valueOf(p.getPersianDay()), p.getPersianMonthName());
            });
            currentWeek = Transformations.map(user, input -> {
                float diffDays = CalendarTool
                        .getDaysFromDiff(nowC, Objects.requireNonNull(user.getValue())
                                .getPregnancyStartDate()
                                .getCalendar());
                int diffWeeks = (int) (diffDays / 7) + 1;
                return String.format(" هفته %d", diffWeeks);
            });

            pregnancyStartDate = Transformations.map(user, input -> {
                PersianCalendar p = CalendarTool.GregorianToPersian(input.getPregnancyStartDate().getCalendar());
                int year = p.getPersianYear();
                int month = p.getPersianMonth() + 1;
                int day = p.getPersianDay();

                return String.format("%04d/%02d/%02d", year, month, day);
            });

            motherBirthDate = Transformations.map(user, input -> {
                PersianCalendar p = CalendarTool.GregorianToPersian(input.getBirthDate().getCalendar());
                int year = p.getPersianYear();
                int month = p.getPersianMonth() + 1;
                int day = p.getPersianDay();

                return String.format("%04d/%02d/%02d", year, month, day);
            });

            bloodType = Transformations.map(user
                    , input -> input.getBloodType() + (input.isNegative() ? "<sup>-</sup>" : "<sup>+</sup>"));

            fontSize = Transformations.map(user, input -> String.valueOf(input.getFontSize()));

            startWeekLabels = Transformations.map(user, input -> {
                Calendar s = input.getPregnancyStartDate().getCalendar();
                PersianCalendar ps = CalendarTool.GregorianToPersian(s);
                return new Pair<>(String.valueOf(ps.getPersianDay()), ps.getPersianMonthName());
            });

            endWeekLabels = Transformations.map(user, input -> {
                Calendar s = input.getPregnancyStartDate().getCalendar();
                s.add(Calendar.DAY_OF_MONTH, 40 * 7);
                PersianCalendar ps = CalendarTool.GregorianToPersian(s);
                return new Pair<>(String.valueOf(ps.getPersianDay()), ps.getPersianMonthName());
            });

            currentWeekNumber = Transformations.map(user, input -> {
                Calendar nowC = Calendar.getInstance();
                float diffDays = CalendarTool.getDaysFromDiff(nowC, input.getPregnancyStartDate().getCalendar());
                return (int) (diffDays / 7) + 1;
            });

            loadFirstLog(repository);
        }
    }

    public void loadFirstLog(Repository repository) {
        Disposable d = repository.getFirstLoggedDateObservable()
                .subscribe(date -> firstLoggedDate.setValue(date), Logger::printStackTrace);
        disposable.add(d);
    }

    public LiveData<Pair<String, String>> getStartWeekLabels() {
        return startWeekLabels;
    }

    public LiveData<Pair<String, String>> getEndWeekLabels() {
        return endWeekLabels;
    }

    public void updateFontSize(Repository repository, int value) {
        repository.updateFontSize(value);
    }

    public LiveData<Date> getFirstLoggedDate() {
        return firstLoggedDate;
    }

    public void updateBloodType(@NonNull Repository repository, @NonNull String type, boolean isNegative) {
        repository.updateBloodType(type, isNegative);
    }

    public void updatePregnancyStartDate(@NonNull Repository repository, Date date) {
        repository.updatePregnancyStartDate(date);
    }

    public void updateBirthDate(@NonNull Repository repository, Date date) {
        repository.updateBirthDate(date);
    }

    public LiveData<Integer> getCurrentWeekNumber() {
        return currentWeekNumber;
    }

    public LiveData<String> getDaysToDelivery() {
        return daysToDelivery;
    }

    public LiveData<String> getRemainingDays() {
        return remainingDays;
    }

    public LiveData<String> getCurrentWeek() {
        return currentWeek;
    }

    public LiveData<String> getPregnancyStartDate() {
        return pregnancyStartDate;
    }

    public LiveData<String> getMotherBirthDate() {
        return motherBirthDate;
    }

    public LiveData<String> getBloodType() {
        return bloodType;
    }

    public LiveData<String> getFontSize() {
        return fontSize;
    }

    public LiveData<User> getUser() {
        return user;
    }

    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        super.onCleared();
    }
}
