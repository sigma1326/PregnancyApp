package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.SleepTime;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface SleepTimeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SleepTime sleepTime);

    @Query("select * from sleep_time where date=:date")
    SleepTime getSleepTime(Date date);

    @Delete
    void remove(SleepTime sleepTime);
}
