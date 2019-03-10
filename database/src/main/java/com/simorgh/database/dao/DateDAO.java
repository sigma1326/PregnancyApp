package com.simorgh.database.dao;

import com.simorgh.database.Date;

import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Maybe;
import io.reactivex.Observable;

@Keep
@Dao
public interface DateDAO {
    @Query("select distinct * from (select date from alcohol union all select date from blood_pressure union all select date from cigarette union all select date from drugs union all select date from exercise_time union all select date from fevers union all select date from sleep_time union all select date from weights order by date DESC)")
    Observable<List<Date>> getLoggedDates();

    @Query("select distinct * from (select date from alcohol union all select date from blood_pressure union all select date from cigarette union all select date from drugs union all select date from exercise_time union all select date from fevers union all select date from sleep_time union all select date from weights order by date ASC) limit 1")
    Date getFirstLoggedDate();

    @Query("select distinct * from (select date from alcohol union all select date from blood_pressure union all select date from cigarette union all select date from drugs union all select date from exercise_time union all select date from fevers union all select date from sleep_time union all select date from weights order by date ASC) limit 1")
    Maybe<Date> getFirstLoggedDateObservable();

    @Query("select count(*) from (select distinct * from (select date from alcohol union all select date from blood_pressure union all select date from cigarette union all select date from drugs union all select date from exercise_time union all select date from fevers union all select date from sleep_time union all select date from weights order by date))")
    Integer getLoggedDatesCount();

    @Query("select distinct * from (select date from alcohol union all select date from blood_pressure union all select date from cigarette union all select date from drugs union all select date from exercise_time union all select date from fevers union all select date from sleep_time union all select date from weights where date>=:start and date<=:end order by date)")
    List<Date> getLoggedDatesList(Date start, Date end);
}
