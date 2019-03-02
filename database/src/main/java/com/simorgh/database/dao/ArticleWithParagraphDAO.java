package com.simorgh.database.dao;

import com.simorgh.database.model.ArticleWithParagraph;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import io.reactivex.Single;

@Dao
@Keep
public interface ArticleWithParagraphDAO {

    @Transaction
    @Query("select * from articles  where id=:id")
    Single<ArticleWithParagraph> getArticle(int id);

    @Transaction
    @Query("select * from articles  where id in (select embryo_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    Single<ArticleWithParagraph> getEmbryoWeekArticle(int weekNumber);

    @Transaction
    @Query("select * from articles  where id in (select mother_article_id from weeks where week_number=:weekNumber limit 1) limit 1")
    Single<ArticleWithParagraph> getMotherWeekArticle(int weekNumber);

    @Transaction
    @Query("select * from articles order by id ASC")
    Single<List<ArticleWithParagraph>> getArticles();

    @Transaction
    @Query("select * from articles where type=:type order by id ASC")
    Single<List<ArticleWithParagraph>> getArticlesByType(int type);

}
