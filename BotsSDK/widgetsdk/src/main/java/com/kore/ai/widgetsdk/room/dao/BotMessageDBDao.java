package com.kore.ai.widgetsdk.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kore.ai.widgetsdk.room.models.BotDBMessage;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ramachandra Pradeep on 26-Oct-18.
 */

@Dao
public interface BotMessageDBDao {

    @Query("SELECT * FROM "+ BotDBMessage.TABLE_NAME+" ORDER BY id DESC"+" LIMIT :limit OFFSET :offset")
    List<BotDBMessage> getLimitedBotMessages(int limit, int offset);

    @Query("SELECT * FROM "+BotDBMessage.TABLE_NAME+ " WHERE isSentMessage = :notSentMsg ORDER BY id DESC LIMIT :limit")
    List<BotDBMessage> getLastBotMessage(boolean notSentMsg, int limit);

    @Query("SELECT * FROM "+ BotDBMessage.TABLE_NAME+" ORDER BY id DESC")
    List<BotDBMessage> getAllBotMessages();

    @Insert(onConflict = REPLACE)
    void insertBotMessage(BotDBMessage botDBMessage);

    @Delete
    void deleteBotMessage(BotDBMessage botDBMessage);

    @Update(onConflict = REPLACE)
    void updateBotMessage(BotDBMessage botDBMessage);

    @Query("DELETE FROM "+BotDBMessage.TABLE_NAME)
    void clearBotData();
}
