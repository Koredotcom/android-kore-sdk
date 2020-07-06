package com.kore.ai.widgetsdk.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kore.ai.widgetsdk.room.models.AuthData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ramachandra Pradeep on 20-Jul-18.
 */

@Dao
public interface AuthDataDao {

    @Query("SELECT * FROM "+ AuthData.TABLE_NAME)
    LiveData<List<AuthData>> getAuthData();

    @Query("SELECT * FROM "+ AuthData.TABLE_NAME)
    List<AuthData> getAuthDataSynchronus();

    @Insert(onConflict = REPLACE)
    void addAuthData(AuthData authData);

    @Delete
    void deleteAuthData(AuthData authData);

    @Update(onConflict = REPLACE)
    void updateAuthData(AuthData authData);

    @Query("DELETE FROM "+ AuthData.TABLE_NAME)
    void clearAuthData();
}
