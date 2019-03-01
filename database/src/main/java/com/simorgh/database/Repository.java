package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.callback.ArticleCallBack;
import com.simorgh.database.callback.ArticlesForTypeCallBack;
import com.simorgh.database.callback.ParagraphsCallBack;
import com.simorgh.database.callback.WeekCallBack;
import com.simorgh.database.model.Article;
import com.simorgh.database.model.Paragraph;
import com.simorgh.database.model.User;
import com.simorgh.database.model.Week;
import com.simorgh.database.util.ArticleSubItemType;
import com.simorgh.database.util.ArticleViewSubItem;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Keep
public final class Repository {
    private final PregnancyDataBase dataBase;

    @SuppressLint("CheckResult")
    public Repository(@NonNull final Application application, PregnancyDataBase pregnancyDataBase) {
        dataBase = pregnancyDataBase;

        init(application);
    }

    private void init(@NonNull final Application application) {
        ThreadUtils.execute(() -> {
            PregnancyDataBase importDataBase = RoomAsset.databaseBuilder(application, PregnancyDataBase.class, "pregnancy-db").build();
//            Logger.d(importDataBase.userDAO().getUserOld().getBloodType());
//            dataBase.userDAO().insert((importDataBase.userDAO().getUserOld()));
            importDataBase.close();
        });
    }

    public void insertUser(@NonNull final User user) {
        ThreadUtils.execute(() -> {
            dataBase.userDAO().insert(user);
        });
    }

    public LiveData<User> getUser() {
        return dataBase.userDAO().getUserLiveData();
    }

    public Single<User> getUserSingle() {
        return dataBase.userDAO().getUser().subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void getArticle(int articleID, ArticleCallBack callBack) {
        dataBase.articleDAO().getArticle(articleID)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((article, throwable) -> {
                    if (throwable != null) {
                        callBack.onFailed("");
                    } else {
                        callBack.onSuccess(article);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getWeekArticle(int weekNumber, boolean isMotherArticle, ArticleCallBack callBack) {
        if (isMotherArticle) {
            dataBase.articleDAO().getMotherWeekArticle(weekNumber)
                    .compose(apply())
                    .subscribe((article, throwable) -> {
                        if (throwable != null) {
                            callBack.onFailed("");
                        } else {
                            callBack.onSuccess(article);
                        }
                    });
        } else {
            dataBase.articleDAO().getEmbryoWeekArticle(weekNumber)
                    .compose(apply())
                    .subscribe((article, throwable) -> {
                        if (throwable != null) {
                            callBack.onFailed("");
                        } else {
                            callBack.onSuccess(article);
                        }
                    });
        }
    }

    private <T> SingleTransformer<T, T> apply() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> SingleTransformer<T, T> applyIO() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
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
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
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
    public void getWeekLiveData(int weekNumber, WeekCallBack weekCallBack) {
        ThreadUtils.execute(() -> {
            Week week = dataBase.weekDAO().getWeekOld(weekNumber);
            if (week == null) {
                weekCallBack.onFailed("");
            } else {
                weekCallBack.onSuccess(week);
            }
        });
//        dataBase.weekDAO().getWeek(weekNumber).subscribeOn(Schedulers.single())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe((week, throwable) -> {
//            if (throwable != null) {
//                weekCallBack.onFailed("");
//            } else {
//                weekCallBack.onSuccess(week);
//            }
//        });
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
        ThreadUtils.execute(() -> {
            dataBase.userDAO().updateFontSize(value);
        });
    }

    public void updateBloodType(String type, boolean isNegative) {
        ThreadUtils.execute(() -> {
            dataBase.userDAO().updateBloodType(type, isNegative);
        });
    }

    public void updatePregnancyStartDate(@NonNull Date date) {
        ThreadUtils.execute(() -> {
            dataBase.userDAO().updatePregnancyStartDate(date);
        });
    }

    public void updateBirthDate(@NonNull Date date) {
        ThreadUtils.execute(() -> {
            dataBase.userDAO().updateBirthDate(date);
        });
    }
}
