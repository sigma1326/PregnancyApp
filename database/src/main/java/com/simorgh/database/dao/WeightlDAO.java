package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Alcohol;
import com.simorgh.database.model.Weight;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface WeightlDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weight weight);

    @Query("select * from weights where date=:date")
    Weight getWeight(Date date);

    @Delete
    void remove(Weight weight);
}
