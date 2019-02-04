package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.model.User;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public final class Repository {
    private final PregnancyDataBase dataBase;

    @SuppressLint("CheckResult")
    public Repository(@NonNull final Application application) {
        dataBase = PregnancyDataBase.getDatabase(application);

        init(application);
    }

    private void init(@NonNull final Application application) {
        ThreadUtils.execute(() -> {
            PregnancyDataBase importDataBase = RoomAsset.databaseBuilder(application, PregnancyDataBase.class, "pregnancy-db").build();
            Logger.d(importDataBase.userDAO().getUserOld().getBloodType());
            dataBase.userDAO().insert((importDataBase.userDAO().getUserOld()));
            importDataBase.close();
        });
    }

    public void insertUser(@NonNull final User user) {
        ThreadUtils.execute(() -> {
            dataBase.userDAO().insert(user);
        });
    }
}
