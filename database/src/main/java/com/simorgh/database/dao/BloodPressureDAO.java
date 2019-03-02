package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.BloodPressure;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface BloodPressureDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BloodPressure bloodPressure);

    @Query("select * from blood_pressure where date=:date")
    BloodPressure getBloodPressure(Date date);

    @Delete
    void remove(BloodPressure bloodPressure);
}
