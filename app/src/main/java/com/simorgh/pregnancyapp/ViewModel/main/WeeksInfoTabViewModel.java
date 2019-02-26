package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Week;
import com.simorgh.threadutils.ThreadUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeeksInfoTabViewModel extends ViewModel {
    private MutableLiveData<Integer> weekNumber = new MutableLiveData<>();
    private MediatorLiveData<Week> weekLiveData = new MediatorLiveData<>();
    private Repository repository;

    public MutableLiveData<Integer> getWeekNumber() {
        return weekNumber;
    }

    public void syncWeekNumber(Integer weekNumber) {
        ThreadUtils.runOnUIThread(() -> {
            this.weekNumber.setValue(weekNumber);
            loadWeekData(weekNumber);
        });
    }

    public void setWeekNumber(Integer weekNumber) {
        ThreadUtils.runOnUIThread(() -> {
            this.weekNumber.setValue(weekNumber);
        });
    }


    public LiveData<Week> getWeekLiveData() {
        return weekLiveData;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public synchronized void loadWeekData(int weekNumber) {
        if (repository != null) {
             repository.getWeekLiveData(weekNumber, new WeekCallBack() {
                @Override
                public void onSuccess(Week week) {
                    ThreadUtils.runOnUIThread(() -> {
                        weekLiveData.setValue(week);
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }
//
//    public synchronized void loadWeekData() {
//        if (repository != null) {
//            repository.getWeekLiveData(weekNumber.getValue(), new WeekCallBack() {
//                @Override
//                public void onSuccess(Week week) {
//                    ThreadUtils.runOnUIThread(() -> {
//                        weekLiveData.setValue(week);
//                    });
//                }
//
//                @Override
//                public void onFailed(String error) {
//
//                }
//            });
//        }
//    }
}
