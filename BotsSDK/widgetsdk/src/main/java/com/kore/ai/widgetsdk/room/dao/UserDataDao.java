package com.kore.ai.widgetsdk.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kore.ai.widgetsdk.room.models.UserData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ramachandra Pradeep on 20-Jul-18.
 */

@Dao
public interface UserDataDao {

    @Query("SELECT * FROM "+ UserData.TABLE_NAME)
    LiveData<List<UserData>> getUserData();

    @Query("SELECT * FROM "+ UserData.TABLE_NAME)
    List<UserData> getUserDataSynchronus();

    @Insert(onConflict = REPLACE)
    void addUserData(UserData userData);

    @Delete
    void deleteUserData(UserData userData);

    @Update(onConflict = REPLACE)
    void updateUserData(UserData userData);

    @Query("DELETE FROM "+ UserData.TABLE_NAME)
    void clearUserData();
}
