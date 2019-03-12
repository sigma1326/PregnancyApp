package com.simorgh.pregnancyapp.Model;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.simorgh.database.Repository;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.BuildConfig;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.di.component.DaggerApplicationComponent;
import com.simorgh.pregnancyapp.di.module.ApplicationModule;
import com.simorgh.pregnancyapp.di.module.DataBaseModule;
import com.simorgh.threadutils.ThreadUtils;
import com.squareup.leakcanary.LeakCanary;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.reactivex.Completable;
import io.reactivex.plugins.RxJavaPlugins;

@SuppressWarnings("ResultOfMethodCallIgnored")
@SuppressLint("CheckResult")
public class AppManager extends MultiDexApplication {
    private static DaggerApplicationComponent daggerApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RxJavaPlugins.setErrorHandler(throwable -> Logger.e("Received undelivered error: " + throwable.getMessage()));

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        daggerApplicationComponent = (DaggerApplicationComponent) DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .dataBaseModule(new DataBaseModule())
                .build();


        Completable.fromRunnable(() -> {
            if (BuildConfig.DEBUG) {
//                Stetho.initializeWithDefaults(this);
            }

//            initLeakCanary();

            initTypeFace();
        }).subscribeWith(Repository.completableObserver);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    private void initTypeFace() {
        Completable.fromRunnable(() -> ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/iransans_medium.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())).subscribeWith(Repository.completableObserver);
    }


    public static DaggerApplicationComponent getDaggerApplicationComponent() {
        return daggerApplicationComponent;
    }

    @Override
    public void onTerminate() {
        ThreadUtils.shutDownTasks();
        Repository.finish();
        super.onTerminate();
    }
}
