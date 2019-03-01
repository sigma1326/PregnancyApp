package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Week;
import com.simorgh.threadutils.ThreadUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private volatile int weekNumber = 1;
    private Repository repository;

    private MutableLiveData<Week> weekLiveData = new MutableLiveData<>();

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        if (this.weekNumber != weekNumber) {
            this.weekNumber = weekNumber;
            loadWeekData(weekNumber);
        }
    }

    public LiveData<Week> getWeekLiveData() {
        loadWeekData(weekNumber);
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
                public void onSuccess(Week weekLiveData1) {
                    ThreadUtils.runOnUIThread(() -> {
                        weekLiveData.setValue(weekLiveData1);
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }
}
