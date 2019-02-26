package com.simorgh.database.dao;

import com.simorgh.database.model.User;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
@Keep
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("select * from users  where id=1")
    Single<User> getUser();

    @Query("select * from users  where id=1")
    User getUserOld();

    @Query("select * from users  where id=1")
    LiveData<User> getUserLiveData();

    @Query("update users set font_size=:value where id=1")
    void updateFontSize(int value);

    @Query("update users set blood_type=:type, is_negative=:isNegative where id=1")
    void updateBloodType(String type, boolean isNegative);
}
