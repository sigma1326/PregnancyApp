package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Alcohol;
import com.simorgh.database.model.Fever;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface FeverDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Fever fever);

    @Query("select * from fevers where date=:date")
    Fever getFever(Date date);

    @Delete
    void remove(Fever fever);
}
