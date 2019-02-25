package com.simorgh.database.dao;

import com.simorgh.database.model.Week;

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
public interface WeekDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Week week);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Week> weeks);

    @Query("select * from weeks  where week_number=:number")
    Single<Week> getWeek(int number);

    @Query("select * from weeks  where week_number=:number")
    Week getWeekOld(int number);

    @Query("select * from weeks  where week_number=:number")
    LiveData<Week> getWeekLiveData(int number);

    @Query("select * from weeks order by week_number ASC")
    Single<List<Week>> getWeeks();
}
