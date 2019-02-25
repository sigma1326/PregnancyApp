package com.simorgh.database.dao;

import com.simorgh.database.model.Paragraph;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
@Keep
public interface ParagraphDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Paragraph paragraph);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Paragraph> paragraphs);

    @Query("select * from paragraphs  where id=:id")
    Single<Paragraph> getParagraph(int id);

    @Query("select * from paragraphs order by id ASC")
    Single<List<Paragraph>> getParagraphs();

    @Query("select * from paragraphs where article_id=:articleID order by id ASC")
    Single<List<Paragraph>> getParagraphs(long articleID);

}
