package kore.botssdk.activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import kore.botssdk.adapter.WelcomeStaticLinkListAdapter;
import kore.botssdk.adapter.WelcomeStaticLinksAdapter;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotResponse;
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
    ConstraintLayout clStarter;

    @Override
    protected void onCreate(@NonNull Bundle data) {
        super.onCreate(data);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_screen);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llOuterHeader = findViewById(R.id.llOuterHeader);
        llStartConversation = findViewById(R.id.llStartConversation);
        lvPromotions = findViewById(R.id.lvPromotions);
        clStarter = findViewById(R.id.clStarter);

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
            RecyclerView rvLinks = findViewById(R.id.rvLinks);
            TextView tvStarterTitle = findViewById(R.id.tvStarterTitle);
            TextView tvStarterDesc = findViewById(R.id.tvStarterDesc);
            TextView tvStartConversation = findViewById(R.id.tvStartConversation);
            RelativeLayout rlLinks = findViewById(R.id.rlLinks);

            rvLinks.setLayoutManager(new LinearLayoutManager(WelcomeScreenActivity.this, LinearLayoutManager.VERTICAL, false));

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

            if(welcomeModel.getStarter_box() != null)
            {
                if(welcomeModel.getStarter_box().isShow())
                {
                    clStarter.setVisibility(View.VISIBLE);

                    if(!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getTitle()))
                    {
                        tvStarterTitle.setVisibility(View.VISIBLE);

                        tvStarterTitle.setText(welcomeModel.getStarter_box().getTitle());
                        tvStartConversation.setText(welcomeModel.getStarter_box().getTitle());
                    }

                    if(!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getSub_text()))
                    {
                        tvStarterDesc.setVisibility(View.VISIBLE);
                        tvStarterDesc.setText(welcomeModel.getStarter_box().getSub_text());
                    }

                    if(welcomeModel.getStarter_box().getStart_conv_button() != null)
                    {
                        if(!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_button().getColor()))
                        {
                            StateListDrawable gradientDrawable = (StateListDrawable)llStartConversation.getBackground();
                            gradientDrawable.setTint(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_button().getColor()));
                            llStartConversation.setBackground(gradientDrawable);
                        }
                    }

                    if(welcomeModel.getStarter_box().getStart_conv_text() != null)
                    {
                        if(!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_text().getColor()))
                        {
                            tvStartConversation.setTextColor(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_text().getColor()));
                        }
                    }

                    if(welcomeModel.getStarter_box().getQuick_start_buttons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons().size() > 0)
                    {
                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(WelcomeScreenActivity.this);

                        if(!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getQuick_start_buttons().getStyle()))
                        {
                            if(welcomeModel.getStarter_box().getQuick_start_buttons().getStyle().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_LIST))
                            {
                                layoutManager.setFlexDirection(FlexDirection.COLUMN);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(WelcomeScreenActivity.this, BotResponse.TEMPLATE_TYPE_LIST,"#a7b0be");
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            }
                            else
                            {
                                layoutManager.setFlexDirection(FlexDirection.ROW);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(WelcomeScreenActivity.this, BotResponse.TEMPLATE_TYPE_CAROUSEL, "#a7b0be");
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            }
                        }
                    }
                }
            }

            if(welcomeModel.getPromotional_content().getPromotions() != null && welcomeModel.getPromotional_content().getPromotions().size() > 0)
            {
                lvPromotions.setAdapter(new PromotionsAdapter(WelcomeScreenActivity.this, welcomeModel.getPromotional_content().getPromotions()));
            }

            if(welcomeModel.getStatic_links() != null)
            {
                if(welcomeModel.getStarter_box().isShow())
                {
                    if(welcomeModel.getStatic_links().getLinks() != null && welcomeModel.getStatic_links().getLinks().size() > 0)
                    {
                        rlLinks.setVisibility(View.VISIBLE);

                        if(StringUtils.isNullOrEmpty(welcomeModel.getStatic_links().getLayout()) && welcomeModel.getStatic_links().getLayout().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_CAROUSEL)) {
                            hvpLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinksAdapter quickRepliesAdapter = new WelcomeStaticLinksAdapter(WelcomeScreenActivity.this, welcomeModel.getStatic_links().getLinks(),"#a7b0be");
                            hvpLinks.setAdapter(quickRepliesAdapter);
                        }
                        else
                        {
                            rvLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinkListAdapter welcomeStaticLinkListAdapter = new WelcomeStaticLinkListAdapter(WelcomeScreenActivity.this, rvLinks);
                            welcomeStaticLinkListAdapter.setWelcomeStaticLinksArrayList(welcomeModel.getStatic_links().getLinks());
                            rvLinks.setAdapter(welcomeStaticLinkListAdapter);
                        }
                    }
                    else
                        rlLinks.setVisibility(View.GONE);
                }
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
        BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(getApplicationContext(), null);

        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putBoolean(BundleUtils.IS_FROM_WELCOME, true);
        if((botOptionsModel != null) && (botOptionsModel.getHeader() != null))
            bundle.putSerializable(BundleUtils.BRANDING, botOptionsModel);

        bundle.putString(BundleUtils.BOT_NAME_INITIALS, SDKConfiguration.Client.bot_name.charAt(0)+"");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
