package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.ExerciseTime;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface ExerciseTimeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseTime exerciseTime);

    @Query("select * from exercise_time where date=:date")
    ExerciseTime getExerciseTime(Date date);

    @Delete
    void remove(ExerciseTime exerciseTime);
}
