package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Week;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class WeeksInfoTabViewModel extends ViewModel {
    private MutableLiveData<Week> weekLiveData = new MutableLiveData<>();
    private Repository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public void syncWeekNumber(Integer weekNumber) {
        loadWeekData(weekNumber);
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
