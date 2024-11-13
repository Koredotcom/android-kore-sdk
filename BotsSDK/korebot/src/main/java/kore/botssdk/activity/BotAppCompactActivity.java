package kore.botssdk.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.audiocodes.webrtcclient.db.MySQLiteHelper;

public class BotAppCompactActivity extends AppCompatActivity {

    private MySQLiteHelper dataBase;

    protected final String LOG_TAG = getClass().getSimpleName();

    public void finish() {
        super.finish();
    }

    protected void onCreate(Bundle data) {
        super.onCreate(data);
        initDataBase();
    }

    public void initDataBase() {
        dataBase = new MySQLiteHelper(this);
    }

    public MySQLiteHelper getDataBase() {
        return dataBase;
    }
}
