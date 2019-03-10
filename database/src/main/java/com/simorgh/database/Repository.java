package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.callback.ArticleCallBack;
import com.simorgh.database.callback.ArticlesForTypeCallBack;
import com.simorgh.database.callback.ParagraphsCallBack;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Alcohol;
import com.simorgh.database.model.Article;
import com.simorgh.database.model.BloodPressure;
import com.simorgh.database.model.Cigarette;
import com.simorgh.database.model.Drug;
import com.simorgh.database.model.ExerciseTime;
import com.simorgh.database.model.Fever;
import com.simorgh.database.model.Paragraph;
import com.simorgh.database.model.SleepTime;
import com.simorgh.database.model.User;
import com.simorgh.database.model.Week;
import com.simorgh.database.model.Weight;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import org.jetbrains.annotations.Contract;

import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Keep
@SuppressLint("CheckResult")
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class Repository {
    private final PregnancyDataBase dataBase;
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final CompletableObserver completableObserver = new CompletableObserver() {
        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.printStackTrace(e);
        }
    };

    public static void finish() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    @NonNull
    @Contract(pure = true)
    public static <T> SingleTransformer<T, T> applySingle() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> SingleTransformer<T, T> applyIOSingle() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> apply() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> MaybeTransformer<T, T> applyMaybe() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> applyIO() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> MaybeTransformer<T, T> applyMaybeIO() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static CompletableTransformer applyCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static CompletableTransformer applyIOCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    private Completable getCompletable(Runnable runnable) {
        return Completable.create(emitter -> {
            runnable.run();
            emitter.onComplete();
        });
    }

    public Repository(@NonNull final Application application, PregnancyDataBase pregnancyDataBase) {
        dataBase = pregnancyDataBase;

        init(application);
    }

    private void init(@NonNull final Application application) {
        getCompletable(() -> {
            PregnancyDataBase importDataBase = RoomAsset.databaseBuilder(application, PregnancyDataBase.class, "pregnancy-db").build();
            importDataBase.close();
        }).compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertUser(@NonNull final User user) {
        getCompletable(() -> dataBase.userDAO().insert(user))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public LiveData<User> getUserLiveData() {
        return dataBase.userDAO().getUserLiveData();
    }

    public User getUser() {
        return dataBase.userDAO().getUserOld();
    }


    public Single<User> getUserSingle() {
        return dataBase.userDAO().getUser().compose(applySingle());
    }

    public LiveData<Article> getWeekArticle(int weekNumber, boolean isMotherArticle) {
        if (isMotherArticle) {
            return dataBase.articleDAO().getMotherWeekArticleLiveData(weekNumber);
        } else {
            return dataBase.articleDAO().getEmbryoWeekArticleLiveData(weekNumber);
        }
    }


//    @SuppressLint("CheckResult")
//    public LiveData<List<ArticleViewSubItem>> getWeekArticleItems(int weekNumber, boolean isMotherArticle) {
//        Observable
//                .just(isMotherArticle)
//                .flatMap(aBoolean -> {
//                    if (aBoolean) {
//                        return dataBase.articleDAO().getMotherWeekArticle(weekNumber);
//                    } else {
//                        return dataBase.articleDAO().getEmbryoWeekArticle(weekNumber);
//                    }
//                })
//                .flatMap(article -> dataBase.articleDAO().getArticle(article.getId()))
//                .flatMap(article -> {
//                    List<ArticleViewSubItem> arrayList = new ArrayList<>();
//                    ArticleViewSubItem title = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.title, article.getTitle());
//                    ArticleViewSubItem image = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.image, article.getImageName());
//                    arrayList.add(title);
//                    arrayList.add(image);
//                    Observable<List<ArticleViewSubItem>> a = Observable.fromArray(arrayList);
//
//                    Observable<List<Paragraph>> p = dataBase.paragraphDAO().getParagraphs(article.getId());
//                    return Obse ;
//                }).;
//
//
//        Observable<List<ArticleViewSubItem>> d1 = dataBase.articleDAO().getArticle(articleID)
//                .compose(applyIO())
//                .map(article -> {
//                    List<ArticleViewSubItem> arrayList = new ArrayList<>();
//                    ArticleViewSubItem title = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.title, article.getTitle());
//                    ArticleViewSubItem image = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.image, article.getImageName());
//                    arrayList.add(title);
//                    arrayList.add(image);
//                    return arrayList;
//                }).toObservable();
//
//
//        Observable<List<ArticleViewSubItem>> d2 = dataBase.paragraphDAO()
//                .getParagraphs(articleID)
//                .compose(applyIO())
//                .map(paragraphs -> {
//                    List<ArticleViewSubItem> arrayList = new ArrayList<>();
//
//                    for (int i = 0; i < paragraphs.size(); i++) {
//                        ArticleViewSubItem p = new ArticleViewSubItem((int) paragraphs.get(i).getId()
//                                , ArticleSubItemType.paragraph, paragraphs.get(i).getContent());
//                        arrayList.add(p);
//                    }
//                    return arrayList;
//                }).toObservable();
//
//
//        Observable
//                .zip(d1, d2, (a1, a2) -> {
//                    a1.addAll(a2);
//                    return a1;
//                })
//                .flatMap(Observable::fromIterable)
//                .subscribe(o -> {
//                    Logger.i(o.getContent() + "");
//                }, Throwable::printStackTrace);
//
//    }

    @SuppressLint("CheckResult")
    public void getParagraphs(long articleID, ParagraphsCallBack callBack) {
        dataBase.paragraphDAO().getParagraphs(articleID)
                .compose(applySingle())
                .subscribe((paragraphs, throwable) -> {
                    if (throwable != null) {
                        callBack.onFailed("");
                    } else {
                        callBack.onSuccess(paragraphs);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public LiveData<List<Paragraph>> getParagraphsLiveData(long articleID) {
        return dataBase.paragraphDAO().getParagraphsLiveData(articleID);
    }

    @SuppressLint("CheckResult")
    public LiveData<Week> getWeekLiveData(int weekNumber) {
        return dataBase.weekDAO().getWeekLiveData(weekNumber);
    }

    @SuppressLint("CheckResult")
    public Maybe<Week> getWeek(int weekNumber) {
        return dataBase.weekDAO().getWeek(weekNumber).compose(applyMaybe());
    }

    @SuppressLint("CheckResult")
    public void getArticlesForType(int type, ArticlesForTypeCallBack callBack) {
        dataBase.articleWithParagraphDAO().getArticlesByType(type)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((articles, throwable) -> {
                    if (throwable != null) {
                        callBack.onFailed("");
                    } else {
                        callBack.onSuccess(articles);
                    }
                });
    }


    public void updateFontSize(int value) {
        getCompletable(() -> dataBase.userDAO().updateFontSize(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void updateBloodType(String type, boolean isNegative) {
        getCompletable(() -> dataBase.userDAO().updateBloodType(type, isNegative))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void updatePregnancyStartDate(@NonNull Date date) {
        getCompletable(() -> dataBase.userDAO().updatePregnancyStartDate(date))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void updateBirthDate(@NonNull Date date) {
        getCompletable(() -> dataBase.userDAO().updateBirthDate(date))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertDrugs(List<Drug> drugList) {
        getCompletable(() -> dataBase.drugDAO().insertAll(drugList))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertBloodPressure(BloodPressure b) {
        getCompletable(() -> dataBase.bloodPressureDAO().insert(b))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertWeight(Weight weight) {
        getCompletable(() -> dataBase.weightlDAO().insert(weight))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertFever(Fever f) {
        getCompletable(() -> dataBase.feverDAO().insert(f))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertCigarette(Cigarette cigarette) {
        getCompletable(() -> dataBase.cigaretteDAO().insert(cigarette))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertAlcohol(Alcohol alcohol) {
        getCompletable(() -> dataBase.alcoholDAO().insert(alcohol))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertSleepTime(SleepTime st) {
        getCompletable(() -> dataBase.sleepTimeDAO().insert(st))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void insertExerciseTime(ExerciseTime exerciseTime) {
        getCompletable(() -> dataBase.exerciseTimeDAO().insert(exerciseTime))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public List<Drug> getDrugs(Date date) {
        return dataBase.drugDAO().getDrugs(date);
    }

    public BloodPressure getBloodPressure(Date date) {
        return dataBase.bloodPressureDAO().getBloodPressure(date);
    }

    public Weight getWeight(Date date) {
        return dataBase.weightlDAO().getWeight(date);
    }

    public Fever getFever(Date date) {
        return dataBase.feverDAO().getFever(date);
    }

    public Cigarette getCigarette(Date date) {
        return dataBase.cigaretteDAO().getCigarette(date);
    }

    public Alcohol getAlcohol(Date date) {
        return dataBase.alcoholDAO().getAlcohol(date);
    }

    public SleepTime getSleepTime(Date date) {
        return dataBase.sleepTimeDAO().getSleepTime(date);
    }

    public ExerciseTime getExerciseTime(Date date) {
        return dataBase.exerciseTimeDAO().getExerciseTime(date);
    }

    public void removeDrugs(List<Drug> deleteList) {
        getCompletable(() -> dataBase.drugDAO().removeList(deleteList))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public Observable<List<Date>> getLoggedDates() {
        return dataBase.dateDAO().getLoggedDates();
    }

    public Date getFirstLoggedDate() {
        return dataBase.dateDAO().getFirstLoggedDate();
    }

    public Maybe<Date> getFirstLoggedDateObservable() {
        return dataBase.dateDAO().getFirstLoggedDateObservable().compose(applyMaybe());
    }

    public void removeFever(Fever value) {
        getCompletable(() -> dataBase.feverDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeCigarette(Cigarette value) {
        getCompletable(() -> dataBase.cigaretteDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeAlcohol(Alcohol value) {
        getCompletable(() -> dataBase.alcoholDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeDrugs(Date value) {
        getCompletable(() -> dataBase.drugDAO().removeDrugs(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeBloodPressure(Date value) {
        getCompletable(() -> dataBase.bloodPressureDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeWeight(Date value) {
        getCompletable(() -> dataBase.weightlDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeFever(Date value) {
        getCompletable(() -> dataBase.feverDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeCigarette(Date value) {
        getCompletable(() -> dataBase.cigaretteDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeAlcohol(Date value) {
        getCompletable(() -> dataBase.alcoholDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeSleepTime(Date value) {
        getCompletable(() -> dataBase.sleepTimeDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public void removeExerciseTime(Date value) {
        getCompletable(() -> dataBase.exerciseTimeDAO().remove(value))
                .compose(applyIOCompletable())
                .subscribeWith(completableObserver);
    }

    public Integer getLoggedDatesCount() {
        return dataBase.dateDAO().getLoggedDatesCount();
    }

    public List<Date> getLoggedDatesList(Date startDate, Date endDate) {
        return dataBase.dateDAO().getLoggedDatesList(startDate, endDate);
    }
}
