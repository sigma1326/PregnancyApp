package com.simorgh.pregnancyapp.ViewModel.register;

import com.simorgh.bloodtypepicker.BloodType;
import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.User;
import com.simorgh.threadutils.ThreadUtils;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MediatorLiveData<Date> pregnancyStartDate = new MediatorLiveData<>();
    private MediatorLiveData<Date> motherBirthDate = new MediatorLiveData<>();
    private MediatorLiveData<BloodType> bloodType = new MediatorLiveData<>();


    public MediatorLiveData<Date> getPregnancyStartDate() {
        return pregnancyStartDate;
    }

    public MediatorLiveData<Date> getMotherBirthDate() {
        return motherBirthDate;
    }

    public MediatorLiveData<BloodType> getBloodType() {
        return bloodType;
    }

    public void setPregnancyStartDate(Date pregnancyStartDate) {
        ThreadUtils.runOnUIThread(() -> {
            this.pregnancyStartDate.setValue(pregnancyStartDate);
        });
    }

    public void setMotherBirthDate(Date motherBirthDate) {
        ThreadUtils.runOnUIThread(() -> {
            this.motherBirthDate.setValue(motherBirthDate);
        });
    }

    public void setBloodType(BloodType bloodType) {
        ThreadUtils.runOnUIThread(() -> {
            this.bloodType.setValue(bloodType);
        });
    }

    public void login(Repository repository, onUserUpdatedCallback callback) {
        ThreadUtils.runOnUIThread(() -> {
            User user = new User();
            BloodType b = bloodType.getValue();
            ThreadUtils.execute(() -> {
                if (b != null) {
                    user.setPregnancyStartDate(pregnancyStartDate.getValue());
                    user.setBirthDate(motherBirthDate.getValue());
                    user.setBloodType(b.getBloodType());
                    user.setNegative(b.isNegative());
                    repository.insertUser(user);
                    callback.onUserUpdated();
                } else {
                    callback.onFailed();
                }
            });
        });
    }

    public interface onUserUpdatedCallback {
        public void onUserUpdated();

        public void onFailed();
    }
}
