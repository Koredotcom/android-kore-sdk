package kore.botssdk.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.BottomOptionsCycleAdapter;
import kore.botssdk.adapter.WelcomeStarterButtonsAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
@SuppressLint("UnknownNullness")
public class OptionsActionSheetFragment extends BottomSheetDialogFragment
{
    final String LOG_TAG = OptionsActionSheetFragment.class.getSimpleName();
    BotOptionsModel model;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    boolean isFromListMenu = false;
    int dp1;

    public String getSkillName() {
        return skillName;
    }
    BottomSheetDialog bottomSheetDialog;
    private BotBrandingModel botBrandingModel;

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    private String skillName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_bottom_sheet, container, false);
        TextView tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        LinearLayout llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        LinearLayout llBottomLayout = view.findViewById(R.id.llBottomLayout);
        LinearLayout llOptionsBottom = view.findViewById(R.id.llOptionsBottom);

        LinearLayout llTabHeader = view.findViewById(R.id.llTabHeader);
        llTabHeader.setVisibility(View.GONE);
        tvOptionsTitle.setVisibility(View.VISIBLE);
        RecyclerView rvViewMore = view.findViewById(R.id.rvMoreData);
        RecyclerView rvQuickData = view.findViewById(R.id.rvQuickData);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rvViewMore.setLayoutManager(layoutManager);

        rvViewMore.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        this.dp1 = (int) DimensionUtil.dp1;
        BottomOptionsCycleAdapter bottomOptionsCycleAdapter;

        if (rvViewMore.getAdapter() == null)
        {
            bottomOptionsCycleAdapter = new BottomOptionsCycleAdapter(model.getTasks());
            rvViewMore.setAdapter(bottomOptionsCycleAdapter);
            bottomOptionsCycleAdapter.setComposeFooterInterface(composeFooterInterface);
            bottomOptionsCycleAdapter.setContext(requireActivity());
            bottomOptionsCycleAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            bottomOptionsCycleAdapter = (BottomOptionsCycleAdapter) rvViewMore.getAdapter();
        }

        bottomOptionsCycleAdapter.setBotListModelArrayList(bottomSheetDialog, model.getTasks());
        bottomOptionsCycleAdapter.notifyItemRangeChanged(0, (model.getTasks().size() - 1));

        if(botBrandingModel != null && botBrandingModel.getWelcome_screen() != null
                && botBrandingModel.getWelcome_screen().getStarter_box() != null
                && botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons() != null
                && botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons().getButtons() != null
                && botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons().getButtons().size() > 0)
        {
            if(botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons().getStyle().equalsIgnoreCase(BotResponse.TEMPLATE_TYPE_LIST))
            {
                layoutManager.setFlexDirection(FlexDirection.COLUMN);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                rvQuickData.setLayoutManager(layoutManager);

                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(requireActivity(), BotResponse.TEMPLATE_TYPE_LIST,"#a7b0be");
                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons().getButtons());
                rvQuickData.setAdapter(quickRepliesAdapter);
            }
            else
            {
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                rvQuickData.setLayoutManager(layoutManager);

                WelcomeStarterButtonsAdapter quickRepliesAdapter = new WelcomeStarterButtonsAdapter(requireActivity(), BotResponse.TEMPLATE_TYPE_CAROUSEL, "#a7b0be");
                quickRepliesAdapter.setWelcomeStarterButtonsArrayList(botBrandingModel.getWelcome_screen().getStarter_box().getQuick_start_buttons().getButtons());
                rvQuickData.setAdapter(quickRepliesAdapter);
            }
        }

        if(model.getHeading() != null)
        {
            tvOptionsTitle.setText(model.getHeading());

            if(sharedPreferences != null)
                tvOptionsTitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_TXT_COLOR, "#000000")));
        }

        llCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();
            }
        });

        return view;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        if (bottomSheetDialog.getWindow() != null)
        {
            bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if(bottomSheet != null) {
                    bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight);
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    bottomSheetBehavior.setPeekHeight((int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight));
                    bottomSheetBehavior.setDraggable(false);
                }
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setisFromFullView(boolean isFromFullView) {
    }

    public void setData(BotOptionsModel taskTemplateModel) {
        model = taskTemplateModel;
    }

    public void setData(BotOptionsModel botOptionsModel, boolean isFromListMenu){
        model = botOptionsModel;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
    }

    public void setBrandingModel(BotBrandingModel botBrandingModel) {
        this.botBrandingModel = botBrandingModel;
    }
}
