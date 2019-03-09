package com.simorgh.pregnancyapp.Model;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
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
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class AppManager extends MultiDexApplication {
    private static DaggerApplicationComponent daggerApplicationComponent;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        RxJavaPlugins.setErrorHandler(throwable -> {
            Logger.e("Received undelivered error: " + throwable.getMessage());
        });

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


        Completable.fromCallable(() -> {
            ThreadUtils.init(new Handler(getMainLooper()));
            if (BuildConfig.DEBUG) {
                Stetho.initializeWithDefaults(this);
            }

//            initLeakCanary();


            initTypeFace();
            return true;
        })
                .subscribeOn(Schedulers.io())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
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
        ThreadUtils.execute(() -> {
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("fonts/iransans_medium.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        });
    }


    public static DaggerApplicationComponent getDaggerApplicationComponent() {
        return daggerApplicationComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ThreadUtils.shutDownTasks();
    }
}
