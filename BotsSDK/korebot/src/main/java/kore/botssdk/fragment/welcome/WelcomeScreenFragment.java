package kore.botssdk.fragment.welcome;

import static android.view.View.VISIBLE;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.squareup.picasso.Picasso;

import kore.botssdk.R;
import kore.botssdk.adapter.PromotionsAdapter;
import kore.botssdk.adapter.WelcomeStarterButtonsAdapter;
import kore.botssdk.adapter.WelcomeStaticLinkListAdapter;
import kore.botssdk.adapter.WelcomeStaticLinksAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingWelcomeModel;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AutoExpandListView;
import kore.botssdk.view.HeightAdjustableViewPager;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class WelcomeScreenFragment extends DialogFragment {
    private BotBrandingModel botBrandingModel;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public void setBotBrandingModel(BotBrandingModel botBrandingModel) {
        this.botBrandingModel = botBrandingModel;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_screen, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setGravity(Gravity.CENTER);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        if (getDialog() != null) getDialog().setCanceledOnTouchOutside(false);
        RelativeLayout llHeaderLayout = null;
        LinearLayout llOuterHeader = view.findViewById(R.id.llOuterHeader);
        LinearLayout llStartConversation = view.findViewById(R.id.llStartConversation);
        AutoExpandListView lvPromotions = view.findViewById(R.id.lvPromotions);
        ConstraintLayout clStarter = view.findViewById(R.id.clStarter);
        LinearLayout llBottomPower = view.findViewById(R.id.llBottomPower);
        ScrollView svWelcome = view.findViewById(R.id.svWelcome);
        LinearLayout llStarterLogo = view.findViewById(R.id.llStarterLogo);
        ImageView ivStarterLogo = view.findViewById(R.id.ivStarterLogo);

        llStartConversation.setOnClickListener(v -> dismiss());

        if (botBrandingModel != null && botBrandingModel.getWelcome_screen() != null) {
            BrandingWelcomeModel welcomeModel = botBrandingModel.getWelcome_screen();

            if (!StringUtils.isNullOrEmpty(welcomeModel.getLayout())) {
                if (welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_LARGE)) {
                    llHeaderLayout = (RelativeLayout) View.inflate(requireContext(), R.layout.welcome_header_2, null);
                } else if (welcomeModel.getLayout().equalsIgnoreCase(BundleUtils.LAYOUT_MEDIUM)) {
                    llHeaderLayout = (RelativeLayout) View.inflate(requireContext(), R.layout.welcome_header_3, null);
                } else llHeaderLayout = (RelativeLayout) View.inflate(requireContext(), R.layout.welcome_header, null);
            }

            if (llHeaderLayout != null) {
                RelativeLayout rlHeader = llHeaderLayout.findViewById(R.id.rlHeader);
                TextView tvWelcomeHeader = llHeaderLayout.findViewById(R.id.tvWelcomeHeader);
                TextView tvWelcomeTitle = llHeaderLayout.findViewById(R.id.tvWelcomeTitle);
                TextView tvWelcomeDescription = llHeaderLayout.findViewById(R.id.tvWelcomeDescription);
                ImageView ivWelcomeLogo = llHeaderLayout.findViewById(R.id.ivWelcomeLogo);
                ConstraintLayout llInnerHeader = llHeaderLayout.findViewById(R.id.llInnerHeader);

                if (!StringUtils.isNullOrEmpty(welcomeModel.getTitle().getName())) {
                    tvWelcomeHeader.setText(welcomeModel.getTitle().getName());
                }

                if (!StringUtils.isNullOrEmpty(welcomeModel.getSub_title().getName()))
                    tvWelcomeTitle.setText(welcomeModel.getSub_title().getName());

                if (!StringUtils.isNullOrEmpty(welcomeModel.getNote().getName()))
                    tvWelcomeDescription.setText(welcomeModel.getNote().getName());

                if (welcomeModel.getBackground() != null) {
                    if (!StringUtils.isNullOrEmpty(welcomeModel.getBackground().getType())) {
                        if (welcomeModel.getBackground().getType().equalsIgnoreCase(BundleUtils.COLOR)) {
                            llInnerHeader.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(welcomeModel.getBackground().getColor())));
                        } else if (!StringUtils.isNullOrEmpty(welcomeModel.getBackground().getImg())) {
                            Glide.with(requireContext()).load(welcomeModel.getBackground().getImg()).into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@androidx.annotation.NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    llInnerHeader.setBackground(null);
                                    rlHeader.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                        }
                    }
                }

                if (welcomeModel.getLogo() != null && !StringUtils.isNullOrEmpty(welcomeModel.getLogo().getLogo_url())) {
                    Picasso.get().load(welcomeModel.getLogo().getLogo_url()).transform(new RoundedCornersTransform()).into(ivWelcomeLogo);
                }

                if (botBrandingModel.getHeader() != null && botBrandingModel.getHeader().getIcon() != null) {
                    if (botBrandingModel.getHeader().getIcon().getType().equalsIgnoreCase(BundleUtils.CUSTOM)) {
                        llStarterLogo.setBackgroundResource(0);
                        Picasso.get().load(botBrandingModel.getHeader().getIcon().getIcon_url()).transform(new RoundedCornersTransform()).into(ivStarterLogo);
                        ivStarterLogo.setLayoutParams(new LinearLayout.LayoutParams((int) (40 * dp1), (int) (40 * dp1)));
                    } else {
                        switch (botBrandingModel.getHeader().getIcon().getIcon_url()) {
                            case "icon-1":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_1, requireContext().getTheme()));
                                break;
                            case "icon-2":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_2, requireContext().getTheme()));
                                break;
                            case "icon-3":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_3, requireContext().getTheme()));
                                break;
                            case "icon-4":
                                ivStarterLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_icon_4, requireContext().getTheme()));
                                break;
                        }
                    }
                }

                if (welcomeModel.getTop_fonts() != null) {
                    tvWelcomeHeader.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    tvWelcomeTitle.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    tvWelcomeDescription.setTextColor(Color.parseColor(welcomeModel.getTop_fonts().getColor()));
                    ivStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(welcomeModel.getTop_fonts().getColor())));
                }

                if (botBrandingModel.getGeneral() != null && botBrandingModel.getGeneral().getColors() != null && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                    tvWelcomeHeader.setTextColor(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text()));
                    tvWelcomeTitle.setTextColor(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text()));
                    tvWelcomeDescription.setTextColor(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text()));
                    llStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botBrandingModel.getGeneral().getColors().getPrimary())));
                    ivStarterLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text())));
                    llInnerHeader.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botBrandingModel.getGeneral().getColors().getPrimary())));

                    if (!StringUtils.isNullOrEmpty(botBrandingModel.getGeneral().getColors().getSecondary()))
                        svWelcome.setBackgroundColor(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary()));
                }

                if (welcomeModel.getBottom_background() != null && !StringUtils.isNullOrEmpty(welcomeModel.getBottom_background().getColor())) {
                    llBottomPower.setBackgroundColor(Color.parseColor(welcomeModel.getBottom_background().getColor()));
                }
            }

            RecyclerView rvStarterButtons = view.findViewById(R.id.rvStarterButtons);
            HeightAdjustableViewPager hvpLinks = view.findViewById(R.id.hvpLinks);
            RecyclerView rvLinks = view.findViewById(R.id.rvLinks);
            TextView tvStarterTitle = view.findViewById(R.id.tvStarterTitle);
            TextView tvStarterDesc = view.findViewById(R.id.tvStarterDesc);
            TextView tvStartConversation = view.findViewById(R.id.tvStartConversation);
            RelativeLayout rlLinks = view.findViewById(R.id.rlLinks);

            rvLinks.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

            llOuterHeader.addView(llHeaderLayout);

            if (welcomeModel.getStarter_box() != null) {
                if (welcomeModel.getStarter_box().isShow()) {
                    clStarter.setVisibility(View.VISIBLE);

                    if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getTitle())) {
                        tvStarterTitle.setVisibility(View.VISIBLE);

                        tvStarterTitle.setText(welcomeModel.getStarter_box().getTitle());
                        tvStartConversation.setText(welcomeModel.getStarter_box().getTitle());
                    }

                    if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getSub_text())) {
                        tvStarterDesc.setVisibility(View.VISIBLE);
                        tvStarterDesc.setText(welcomeModel.getStarter_box().getSub_text());
                    }

                    if (welcomeModel.getStarter_box().getStart_conv_button() != null) {
                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_button().getColor())) {
                            StateListDrawable gradientDrawable = (StateListDrawable) llStartConversation.getBackground();
                            gradientDrawable.setTint(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_button().getColor()));
                            if (botBrandingModel.getGeneral() != null && botBrandingModel.getGeneral().getColors() != null && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                                gradientDrawable.setTint(Color.parseColor(botBrandingModel.getGeneral().getColors().getPrimary()));
                            }

                            llStartConversation.setBackground(gradientDrawable);
                        }
                    }

                    if (welcomeModel.getStarter_box().getStart_conv_text() != null) {
                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getStart_conv_text().getColor())) {
                            tvStartConversation.setTextColor(Color.parseColor(welcomeModel.getStarter_box().getStart_conv_text().getColor()));

                            if (botBrandingModel.getGeneral() != null && botBrandingModel.getGeneral().getColors() != null && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly() && !StringUtils.isNullOrEmpty(botBrandingModel.getGeneral().getColors().getSecondary_text())) {
                                tvStartConversation.setTextColor(Color.parseColor(botBrandingModel.getGeneral().getColors().getSecondary_text()));
                            }
                        }
                    }

                    if (welcomeModel.getStarter_box().getQuick_start_buttons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons() != null && welcomeModel.getStarter_box().getQuick_start_buttons().getButtons().size() > 0) {
                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());

                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStarter_box().getQuick_start_buttons().getStyle())) {
                            if (welcomeModel.getStarter_box().getQuick_start_buttons().getStyle().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_LIST)) {
                                layoutManager.setFlexDirection(FlexDirection.COLUMN);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(requireContext(), BotResponse.TEMPLATE_TYPE_LIST, !StringUtils.isNullOrEmpty(botBrandingModel.getGeneral().getColors().getSecondary()) ? botBrandingModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                                quickRepliesAdapter.setDialogFragment(this);
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            } else {
                                layoutManager.setFlexDirection(FlexDirection.ROW);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                                rvStarterButtons.setLayoutManager(layoutManager);

                                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(requireContext(), BotResponse.TEMPLATE_TYPE_CAROUSEL, (!StringUtils.isNullOrEmpty(botBrandingModel.getGeneral().getColors().getSecondary()) && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) ? botBrandingModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                                quickRepliesAdapter.setDialogFragment(this);
                                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(welcomeModel.getStarter_box().getQuick_start_buttons().getButtons());
                                quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
                                quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                                rvStarterButtons.setAdapter(quickRepliesAdapter);
                            }
                        }
                    }
                }
            }

            if (welcomeModel.getPromotional_content().getPromotions() != null && welcomeModel.getPromotional_content().isShow() && welcomeModel.getPromotional_content().getPromotions().size() > 0) {
                lvPromotions.setVisibility(VISIBLE);
                lvPromotions.setAdapter(new PromotionsAdapter(requireContext(), welcomeModel.getPromotional_content().getPromotions()));
            }

            if (welcomeModel.getStatic_links() != null) {
                if (welcomeModel.getStarter_box().isShow()) {
                    if (welcomeModel.getStatic_links().getLinks() != null && !welcomeModel.getStatic_links().getLinks().isEmpty()) {
                        rlLinks.setVisibility(View.VISIBLE);

                        if (!StringUtils.isNullOrEmpty(welcomeModel.getStatic_links().getLayout()) && welcomeModel.getStatic_links().getLayout().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_CAROUSEL)) {
                            hvpLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinksAdapter quickRepliesAdapter = new WelcomeStaticLinksAdapter(requireContext(), welcomeModel.getStatic_links().getLinks(), (!StringUtils.isNullOrEmpty(botBrandingModel.getGeneral().getColors().getSecondary()) && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) ? botBrandingModel.getGeneral().getColors().getSecondary() : "#a7b0be");
                            quickRepliesAdapter.setDialogFragment(this);
                            quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
                            quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                            hvpLinks.setAdapter(quickRepliesAdapter);
                        } else {
                            rvLinks.setVisibility(View.VISIBLE);
                            WelcomeStaticLinkListAdapter welcomeStaticLinkListAdapter = new WelcomeStaticLinkListAdapter(requireContext(), rvLinks);
                            welcomeStaticLinkListAdapter.setDialogFragment(this);
                            welcomeStaticLinkListAdapter.setWelcomeStaticLinksArrayList(welcomeModel.getStatic_links().getLinks());
                            welcomeStaticLinkListAdapter.setComposeFooterInterface(composeFooterInterface);
                            welcomeStaticLinkListAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                            rvLinks.setAdapter(welcomeStaticLinkListAdapter);
                        }
                    } else rlLinks.setVisibility(View.GONE);
                }
            }
        }
    }
}
