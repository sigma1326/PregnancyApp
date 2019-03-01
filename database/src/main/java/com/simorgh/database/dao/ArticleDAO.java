package com.simorgh.database.dao;

import com.simorgh.database.model.Article;

import java.util.List;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
@Keep
public interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Article> articles);

    @Query("select * from articles  where id=:id")
    Single<Article> getArticle(int id);

    @Query("select * from articles  where id in (select embryo_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    Single<Article> getEmbryoWeekArticle(int weekNumber);

    @Query("select * from articles  where id in (select mother_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    Single<Article> getMotherWeekArticle(int weekNumber);

    @Query("select * from articles  where id in (select mother_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    LiveData<Article> getMotherWeekArticleLiveData(int weekNumber);

    @Query("select * from articles order by id ASC")
    Single<List<Article>> getArticles();

    @Query("select * from articles  where id in (select embryo_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    LiveData<Article> getEmbryoWeekArticleLiveData(int weekNumber);
}
