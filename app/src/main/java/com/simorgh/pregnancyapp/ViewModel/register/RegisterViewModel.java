package com.simorgh.pregnancyapp.ViewModel.register;

import com.simorgh.bloodtypepicker.BloodType;
import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.User;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Objects;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<Date> pregnancyStartDate = new MutableLiveData<>();
    private MutableLiveData<Date> motherBirthDate = new MutableLiveData<>();
    private MutableLiveData<BloodType> bloodType = new MutableLiveData<>();


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

    public void login(Repository repository,onUserUpdatedCallback callback) {
        ThreadUtils.execute(() -> {
            User user = new User();
            user.setPregnancyStartDate(pregnancyStartDate.getValue());
            user.setBirthDate(motherBirthDate.getValue());
            user.setBloodType(Objects.requireNonNull(bloodType.getValue()).getBloodType());
            user.setNegative(Objects.requireNonNull(bloodType.getValue()).isNegative());
            repository.insertUser(user);
            callback.onUserUpdated();
        });
    }

    public interface onUserUpdatedCallback{
        public void onUserUpdated();
    }
}
