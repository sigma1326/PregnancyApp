package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.bloodtypepicker.BloodType;
import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Date> pregnancyStartDate = new MutableLiveData<>();
    private MutableLiveData<Date> motherBirthDate = new MutableLiveData<>();
    private MutableLiveData<BloodType> bloodType = new MutableLiveData<>();
    private LiveData<User> user = new MutableLiveData<>();

    public MutableLiveData<Date> getPregnancyStartDate() {
        return pregnancyStartDate;
    }

    public MutableLiveData<Date> getMotherBirthDate() {
        return motherBirthDate;
    }

    public MutableLiveData<BloodType> getBloodType() {
        return bloodType;
    }

    public void setPregnancyStartDate(Date pregnancyStartDate) {
        this.pregnancyStartDate.setValue(pregnancyStartDate);
    }

    public void setMotherBirthDate(Date motherBirthDate) {
        this.motherBirthDate.setValue(motherBirthDate);
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType.setValue(bloodType);
    }


    public LiveData<User> getUser() {
        return user;
    }

    public void getUserLiveData(Repository repository) {
        if (repository != null) {
            user = repository.getUser();
        }
    }

    public void updateFontSize(Repository repository, int value) {
        repository.updateFontSize(value);
    }

    public void updateBloodType(@NonNull Repository repository,@NonNull String type, boolean isNegative) {
        repository.updateBloodType(type, isNegative);
    }
}
