package com.simorgh.pregnancyapp.di.module;

import android.app.Application;

import com.simorgh.database.PregnancyDataBase;
import com.simorgh.database.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    @Provides
    public Repository getRepository(Application application, PregnancyDataBase dataBase) {
        return new Repository(application, dataBase);
    }

    @Provides
    public PregnancyDataBase getDataBase(Application application) {
        return PregnancyDataBase.getDatabase(application);
    }
}
