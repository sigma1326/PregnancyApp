package com.simorgh.database.callback;

import com.simorgh.database.model.Week;

import androidx.lifecycle.LiveData;

public interface WeekCallBack {
    void onSuccess(Week week);

    void onFailed(String error);
}
