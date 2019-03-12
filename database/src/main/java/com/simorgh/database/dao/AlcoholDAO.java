package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Alcohol;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface AlcoholDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Alcohol alcohol);

    @Query("select * from alcohol where date=:date")
    Alcohol getAlcohol(Date date);

    @Delete
    void remove(Alcohol alcohol);

    @Query("delete from alcohol where date=:value")
    void remove(Date value);

    @Query("delete from alcohol")
    void clearAll();
}
