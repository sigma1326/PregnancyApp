package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Drug;

import java.util.List;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
@Keep
public interface DrugDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Drug drug);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Drug> drugs);

    @Update
    void updateList(List<Drug> drugs);

    @Query("select * from drugs where date=:date order by id ASC")
    List<Drug> getDrugs(Date date);

    @Delete
    void removeList(List<Drug> drugs);

    @Delete
    void remove(Drug drug);
}