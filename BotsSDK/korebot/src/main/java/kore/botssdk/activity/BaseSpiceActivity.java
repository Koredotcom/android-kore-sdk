package kore.botssdk.activity;

import android.support.v7.app.AppCompatActivity;
import com.octo.android.robospice.SpiceManager;
import kore.botssdk.net.KoreBotRestService;

/**
 * Created by Pradeep Mahato on 30-May-16.
 */
public class BaseSpiceActivity extends AppCompatActivity {

    SpiceManager spiceManager = new SpiceManager(KoreBotRestService.class);

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
