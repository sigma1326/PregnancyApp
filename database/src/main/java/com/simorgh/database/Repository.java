package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.callback.ArticleCallBack;
import com.simorgh.database.callback.ParagraphsCallBack;
import com.simorgh.database.callback.WeekCallBack;
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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
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
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((article, throwable) -> {
                        if (throwable != null) {
                            callBack.onFailed("");
                        } else {
                            callBack.onSuccess(article);
                        }
                    });
        } else {
            dataBase.articleDAO().getEmbryoWeekArticle(weekNumber)
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
    }

    @SuppressLint("CheckResult")
    public void getArticleDetailItems(int articleID) {
        Observable<List<ArticleViewSubItem>> d1 = dataBase.articleDAO().getArticle(articleID)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map(article -> {
                    List<ArticleViewSubItem> arrayList = new ArrayList<>();
                    ArticleViewSubItem title = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.title, article.getTitle());
                    ArticleViewSubItem image = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.image, article.getImageName());
                    arrayList.add(title);
                    arrayList.add(image);
                    return arrayList;
                }).toObservable();


        Observable<List<ArticleViewSubItem>> d2 = dataBase.paragraphDAO()
                .getParagraphs(articleID)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread()).map(paragraphs -> {
                    List<ArticleViewSubItem> arrayList = new ArrayList<>();

                    for (int i = 0; i < paragraphs.size(); i++) {
                        ArticleViewSubItem p = new ArticleViewSubItem((int) paragraphs.get(i).getId()
                                , ArticleSubItemType.paragraph, paragraphs.get(i).getContent());
                        arrayList.add(p);
                    }
                    return arrayList;
                }).toObservable();


        Observable.concat(d1, d2)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleViewSubItems -> {
                    Logger.i(articleViewSubItems.size() + "::");
                }, throwable -> {
                    throwable.printStackTrace();
                });

//        Observable.merge(d1, d2).reduce((o, o2) -> {
//            o.
//        });

//                .concatWith(dataBase.paragraphDAO().getParagraphs(articleID).map(paragraphs -> {
//                    ArrayList<ArticleViewSubItem> arrayList = new ArrayList<>();
//
//                    for (int i = 0; i < paragraphs.size(); i++) {
//                        ArticleViewSubItem p = new ArticleViewSubItem((int) paragraphs.get(i).getId()
//                                , ArticleSubItemType.paragraph, paragraphs.get(i).getContent());
//                        arrayList.add(p);
//                    }
//                    return arrayList;
//                }))
//                .subscribe(articleViewSubItems -> {
//                    Logger.i(articleViewSubItems.size() + "");
//                },throwable -> {},() -> {});
//                .subscribe((article, throwable) -> {
//                    if (throwable != null) {
//                        callBack.onFailed("");
//                    } else {
//                        callBack.onSuccess(article);
//                    }
//                });
    }

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
}
