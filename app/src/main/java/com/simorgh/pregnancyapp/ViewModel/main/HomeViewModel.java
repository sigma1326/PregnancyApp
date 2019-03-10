package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Week;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class HomeViewModel extends ViewModel {
    private volatile int weekNumber = 1;
    private Repository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

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

    public void loadWeekData(int weekNumber) {
        if (repository != null) {
            Disposable d = repository.getWeek(weekNumber)
                    .subscribe(week -> weekLiveData.setValue(week), Logger::printStackTrace);
            disposable.add(d);
        }
    }

    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        repository = null;
        super.onCleared();
    }

}
