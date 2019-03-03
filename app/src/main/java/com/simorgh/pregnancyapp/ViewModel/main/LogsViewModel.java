package com.simorgh.pregnancyapp.ViewModel.main;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogsViewModel extends ViewModel {
    private LiveData<List<Date>> dateList = new MutableLiveData<>();

}
