package com.simorgh.threadutils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.simorgh.logger.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class ThreadUtils {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static volatile Handler applicationHandler;

    private static volatile ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static void execute(@NonNull Runnable runnable) {
        if (executor != null) {
            try {
                executor.execute(runnable);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        }
    }

    public static void shutDownTasks() {
        if (executor != null) {
            try {
                executor.shutdown();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        }
    }

    public static void init(@NonNull final Handler handler) {
        applicationHandler = handler;
    }

    public static void runOnUIThread(@NonNull Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(@NonNull Runnable runnable, final long delay) {
        if (applicationHandler == null) {
            return;
        }
        try {
            if (delay == 0) {
                applicationHandler.post(runnable);
            } else {
                applicationHandler.postDelayed(runnable, delay);
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    @SuppressLint("CheckResult")
    public static void onUI(Runnable runnable) {
        Completable.create(emitter -> runnable.run())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static void onUI(Runnable runnable, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(aLong -> Observable.create(emitter -> {
                    runnable.run();
                    emitter.onNext(true);
                    emitter.onComplete();
                }))
                .subscribeWith(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
