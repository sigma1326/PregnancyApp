package com.simorgh.pregnancyapp.ViewModel.main;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    /**
     * 0 for default
     * 1 for pregnancy start date
     * 2 for birth date
     */
    private int dateType;

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }
}
