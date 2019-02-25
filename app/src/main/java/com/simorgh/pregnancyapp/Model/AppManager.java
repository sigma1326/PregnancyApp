package com.simorgh.pregnancyapp.Model;

import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.simorgh.pregnancyapp.BuildConfig;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.di.component.DaggerApplicationComponent;
import com.simorgh.pregnancyapp.di.module.ApplicationModule;
import com.simorgh.pregnancyapp.di.module.DataBaseModule;
import com.simorgh.threadutils.ThreadUtils;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends MultiDexApplication {
    private static DaggerApplicationComponent daggerApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

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


        ThreadUtils.execute(() -> {
            ThreadUtils.init(new Handler(getMainLooper()));
//            Repository repository = new Repository(this);

            if (BuildConfig.DEBUG) {
                Stetho.initializeWithDefaults(this);
            }

//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
            // Normal app init code...

            initTypeFace();

//            Logger.i(DebugDB.getAddressLog());

//            User user = new User();
//            user.setFontSize(14);
//            user.setBloodType("O");
//            user.setNegative(true);
//            user.setBirthDate(new Date(1990, 5, 26, 14, 25, 0));
//            user.setPregnancyStartDate(new Date(2018, 0, 25, 14, 25, 0));
//            repository.insertUser(user);
        });
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
