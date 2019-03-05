package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Cigarette;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface CigaretteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cigarette cigarette);

    @Query("select * from cigarette where date=:date")
    Cigarette getCigarette(Date date);

    @Delete
    void remove(Cigarette cigarette);

    @Query("delete from cigarette where date=:value")
    void remove(Date value);
}
