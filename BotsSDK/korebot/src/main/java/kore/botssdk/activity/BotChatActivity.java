package kore.botssdk.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import kore.botssdk.R;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotChatActivity extends BaseSpiceActivity {

    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);
        findViews();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        BotContentFragment botContentFragment = new BotContentFragment();
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ComposeFooterFragment composeFooterFragment = new ComposeFooterFragment();
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();

    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
    }
}
