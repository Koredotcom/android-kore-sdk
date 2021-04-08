package kore.botssdk.dialogs;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListViewTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotListViewMoreDataModel;
import kore.botssdk.models.BotResponse;

public class ListActionSheetFragment extends BottomSheetDialogFragment {

    private View view;
    private boolean isFromFullView;
    private BotListViewMoreDataModel model;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean isFromListMenu = false;
    private ListView lvMoreData;
    private int dp1;
    private TextView tvTab1, tvTab2;
    private LinearLayout llCloseBottomSheet, llBottomLayout;
    public String getSkillName() {
        return skillName;
    }
    private BottomSheetDialog bottomSheetDialog;
    private boolean showHeader = false;
    private LinearLayout llTabHeader;
    private SharedPreferences sharedPreferences;

    public void setSkillName(String skillName, String trigger) {
        this.skillName = skillName;
        this.trigger = trigger;
    }

    private String skillName;
    private String trigger;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_bottom_sheet, container,false);
        lvMoreData = view.findViewById(R.id.lvMoreData);
        tvTab1 = view.findViewById(R.id.tvTab1);
        tvTab2 = view.findViewById(R.id.tvTab2);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        llTabHeader = view.findViewById(R.id.llTabHeader);
        llBottomLayout = view.findViewById(R.id.llBottomLayout);
        sharedPreferences = getActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        this.dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        BotListViewTemplateAdapter botListTemplateAdapter;
        if (lvMoreData.getAdapter() == null) {
            botListTemplateAdapter = new BotListViewTemplateAdapter(getContext(), lvMoreData, 0);
            lvMoreData.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (BotListViewTemplateAdapter) lvMoreData.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(model.getTab1());
        botListTemplateAdapter.notifyDataSetChanged();

        llTabHeader.setVisibility(View.VISIBLE);
        if(!showHeader)
            llTabHeader.setVisibility(View.GONE);

        tvTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTab1.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_bg));
                tvTab1.setTextColor(getResources().getColor(R.color.white));

                tvTab2.setBackground(getResources().getDrawable(R.drawable.calender_view_background));
                tvTab2.setTextColor(getResources().getColor(R.color.footer_color_dark_grey));

                botListTemplateAdapter.setBotListModelArrayList(model.getTab1());
            }
        });

        tvTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTab2.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_bg));
                tvTab2.setTextColor(getResources().getColor(R.color.white));

                tvTab1.setBackground(getResources().getDrawable(R.drawable.calender_view_background));
                tvTab1.setTextColor(getResources().getColor(R.color.footer_color_dark_grey));

                botListTemplateAdapter.setBotListModelArrayList(model.getTab2());
            }
        });

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

    public void setHeaderVisible(boolean visible)
    {
        this.showHeader = visible;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) (500 * dp1));
            }

        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setisFromFullView(boolean isFromFullView) {
        this.isFromFullView = isFromFullView;
    }

    public void setData(BotListViewMoreDataModel taskTemplateModel) {
        model = taskTemplateModel;
    }

    public void setData(BotListViewMoreDataModel taskTemplateModel, boolean isFromListMenu){
        model = taskTemplateModel;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }
}

