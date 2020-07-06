package com.kore.ai.widgetsdk.room.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kore.ai.widgetsdk.room.converters.StringListConverter;
import com.kore.ai.widgetsdk.room.dao.AuthDataDao;
import com.kore.ai.widgetsdk.room.dao.BotMessageDBDao;
import com.kore.ai.widgetsdk.room.dao.UserDataDao;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.BotDBMessage;
import com.kore.ai.widgetsdk.room.models.UserData;

/**
 * Created by Ramachandra Pradeep on 20-Jul-18.
 */

@Database(entities = {UserData.class, AuthData.class, BotDBMessage.class}, version = 3)
@TypeConverters({StringListConverter.class})
public abstract class KODataBase extends RoomDatabase {

    /**
     * @return The DAO for the UserData table.
     */
    public abstract UserDataDao getUserData();

    public abstract AuthDataDao getAuthData();

    public abstract BotMessageDBDao getBotData();



    /** The only instance */
    private static KODataBase mInstance;

    private static final String dbName = "Kora_Database";

    private static final Object sLock = new Object();

    /**
     * Gets the singleton instance of Kora database.
     *
     * @param context The context.
     * @return The singleton instance of Kora database.
     */
    public static KODataBase getInstance(Context context) {
        synchronized (sLock) {
            if (mInstance == null) {
                mInstance = Room
                        .databaseBuilder(context.getApplicationContext(), KODataBase.class, dbName)
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
            return mInstance;
        }
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            ALTER TABLE "table_name" RENAME COLUMN "column 1" TO "column 2";
            database.execSQL("ALTER TABLE user_data ADD COLUMN profColour TEXT");
        }
    };

}
