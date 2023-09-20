package kore.botssdk.activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import kore.botssdk.R;
import kore.botssdk.adapter.PromotionsAdapter;
import kore.botssdk.adapter.QuickRepliesAdapter;
import kore.botssdk.adapter.WelcomeStarterButtonsAdapter;
import kore.botssdk.adapter.WelcomeStaticLinksAdapter;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BrandingWelcomeModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AutoExpandListView;
import kore.botssdk.view.HeightAdjustableViewPager;

public class WelcomeScreenActivity extends BotAppCompactActivity {
    LinearLayout llOuterHeader, llStartConversation;
    RelativeLayout llHeaderLayout;
    private final Gson gson = new Gson();
    BotBrandingModel botOptionsModel = null;
    AutoExpandListView lvPromotions;

    @Override
    protected void onCreate(@NonNull Bundle data) {
        super.onCreate(data);
        setContentView(R.layout.welcome_screen);
        llOuterHeader = findViewById(R.id.llOuterHeader);
        llStartConversation = findViewById(R.id.llStartConversation);
        lvPromotions = findViewById(R.id.lvPromotions);

        getBrandingDataFromTxt();

        if(botOptionsModel != null && botOptionsModel.getWelcome_screen() != null)
        {
            BrandingWelcomeModel welcomeModel = botOptionsModel.getWelcome_screen();

            if(!StringUtils.isNullOrEmpty(welcomeModel.getLayout()))
            {
                if(welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_MEDIUM))
                {
                    llHeaderLayout = (RelativeLayout) View.inflate(WelcomeScreenActivity.this, R.layout.welcome_header_2, null);
                }
                else if(welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_LARGE))
                {
                    llHeaderLayout = (RelativeLayout) View.inflate(WelcomeScreenActivity.this, R.layout.welcome_header_3, null);
                }
                else
                    llHeaderLayout = (RelativeLayout) View.inflate(WelcomeScreenActivity.this, R.layout.welcome_header, null);
            }

            RelativeLayout rlHeader = llHeaderLayout.findViewById(R.id.rlHeader);
            TextView tvWelcomeHeader = llHeaderLayout.findViewById(R.id.tvWelcomeHeader);
            TextView tvWelcomeTitle = llHeaderLayout.findViewById(R.id.tvWelcomeTitle);
            TextView tvWelcomeDescription = llHeaderLayout.findViewById(R.id.tvWelcomeDescription);
            RecyclerView rvStarterButtons = findViewById(R.id.rvStarterButtons);
            HeightAdjustableViewPager hvpLinks = findViewById(R.id.hvpLinks);

            if(!StringUtils.isNullOrEmpty(welcomeModel.getTitle().getName()))
                tvWelcomeHeader.setText(welcomeModel.getTitle().getName());

            if(!StringUtils.isNullOrEmpty(welcomeModel.getSub_title().getName()))
                tvWelcomeTitle.setText(welcomeModel.getSub_title().getName());

            if(!StringUtils.isNullOrEmpty(welcomeModel.getNote().getName()))
                tvWelcomeDescription.setText(welcomeModel.getNote().getName());

            if(welcomeModel.getBackground() != null && !StringUtils.isNullOrEmpty(welcomeModel.getBackground().getImg()))
            {
                Glide.with(WelcomeScreenActivity.this)
                        .load(welcomeModel.getBackground().getImg())
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                rlHeader.setBackground(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {}
                        });
            }

            llOuterHeader.addView(llHeaderLayout);

            if(welcomeModel.getStarter_box().getQuick_start_buttons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons().size() > 0)
            {
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(WelcomeScreenActivity.this);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                rvStarterButtons.setLayoutManager(layoutManager);

                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(WelcomeScreenActivity.this, rvStarterButtons);
                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                rvStarterButtons.setAdapter(quickRepliesAdapter);
            }

            if(welcomeModel.getPromotional_content().getPromotions() != null && welcomeModel.getPromotional_content().getPromotions().size() > 0)
            {
                lvPromotions.setAdapter(new PromotionsAdapter(WelcomeScreenActivity.this, welcomeModel.getPromotional_content().getPromotions()));
            }

            if(welcomeModel.getStatic_links().getLinks().size() > 0)
            {
                WelcomeStaticLinksAdapter quickRepliesAdapter = new WelcomeStaticLinksAdapter(WelcomeScreenActivity.this, welcomeModel.getStatic_links().getLinks());
                hvpLinks.setAdapter(quickRepliesAdapter);
            }
        }
    }
    public void getBrandingDataFromTxt()
    {
        try
        {
            InputStream is = getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botOptionsModel = gson.fromJson(reader, BotBrandingModel.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void launchBotChatActivity(@NonNull View view)
    {
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        if((botOptionsModel != null) && (botOptionsModel.getHeader() != null))
            bundle.putSerializable(BundleUtils.BRANDING, botOptionsModel);

        bundle.putString(BundleUtils.BOT_NAME_INITIALS, SDKConfiguration.Client.bot_name.charAt(0)+"");
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
