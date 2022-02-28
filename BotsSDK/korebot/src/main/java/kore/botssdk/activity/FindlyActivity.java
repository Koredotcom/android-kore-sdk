package kore.botssdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.kore.findlysdk.fragments.FindlyFragment;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.fragment.QuickReplyFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KnowledgeCollectionModel;

public class FindlyActivity extends BotAppCompactActivity
{
    FragmentTransaction fragmentTransaction;
    FindlyFragment findlyFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findly);

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        findlyFragment = new FindlyFragment();
        findlyFragment.setArguments(getIntent().getExtras());
        findlyFragment.setActivityContext(FindlyActivity.this);
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, findlyFragment).commit();
    }

}
