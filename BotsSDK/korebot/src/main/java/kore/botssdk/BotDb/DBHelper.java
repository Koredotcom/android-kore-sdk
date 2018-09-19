package kore.botssdk.BotDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    // Fields

    public static final String DB_NAME = "kore_bot.db";
    private static final int DB_VERSION = 1;
    private BotMessageDao botMessageDao;

    // Public methods

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {

            // Create Table with given table name with columnName
            TableUtils.createTable(cs, BotMessageDBModel.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {

    }
    public BotMessageDao getBotMessageDao() {
        if(null == this.botMessageDao) {
            try {
                this.botMessageDao = getDao(BotMessageDBModel.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return this.botMessageDao;
    }

    public void close(){
        this.botMessageDao = null;
    }

}