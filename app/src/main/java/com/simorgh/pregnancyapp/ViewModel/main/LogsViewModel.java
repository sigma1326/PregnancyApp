package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.adapter.LogItem;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogsViewModel extends ViewModel {
    private final MutableLiveData<List<LogItem>> logList = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<List<LogItem>> getLogList() {
        return logList;
    }

    public void loadLogs(Repository repository, Date pregnancyStartDate) {
        if (repository != null) {
            Disposable d = repository
                    .getLoggedDates()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .flatMapSingle(dates -> Observable.fromIterable(dates)
                            .map(date -> new LogItem(date, pregnancyStartDate))
                            .toList()
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(logItems -> logList.setValue(logItems), Logger::printStackTrace);
            disposable.add(d);
        }
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
