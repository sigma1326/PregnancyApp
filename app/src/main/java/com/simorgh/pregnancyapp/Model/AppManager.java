package com.simorgh.pregnancyapp.Model;

import android.os.Handler;

import com.simorgh.database.Repository;
import com.simorgh.threadutils.ThreadUtils;

public class AppManager extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ThreadUtils.execute(() -> {
            ThreadUtils.init(new Handler(getMainLooper()));
            Repository repository = new Repository(this);


//            User user = new User();
//            user.setFontSize(14);
//            user.setBloodType("O");
//            user.setNegative(true);
//            user.setBirthDate(new Date(1990, 5, 26, 14, 25, 0));
//            user.setPregnancyStartDate(new Date(2018, 0, 25, 14, 25, 0));
//            repository.insertUser(user);
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ThreadUtils.shutDownTasks();
    }
}
