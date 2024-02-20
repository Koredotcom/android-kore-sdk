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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListViewTemplateAdapter;
import kore.botssdk.adapter.ListViewMoreAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class ListMoreActionSheetFragment extends BottomSheetDialogFragment {

    private View view;
    private boolean isFromFullView;
    private ArrayList<BotListModel> model;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean isFromListMenu = false;
    private ListView lvMoreData;
    private int dp1;
    private LinearLayout llCloseBottomSheet, llBottomLayout;
    public String getSkillName() {
        return skillName;
    }
    private BottomSheetDialog bottomSheetDialog;
    private final boolean showHeader = true;
    private int count;
    private RecyclerView rvViewMore;
    private TextView tvOptionsTitle;
    private String title;
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

        view = inflater.inflate(R.layout.list_view_more_sheet, container,false);
        lvMoreData = view.findViewById(R.id.lvMoreData);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        rvViewMore = view.findViewById(R.id.rvMoreData);
        llBottomLayout = view.findViewById(R.id.llBottomLayout);
        sharedPreferences = getActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        rvViewMore.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        tvOptionsTitle.setVisibility(View.VISIBLE);
        rvViewMore.setVisibility(View.VISIBLE);
        this.dp1 = (int) DimensionUtil.dp1;
        BotListViewTemplateAdapter botListTemplateAdapter;
        if (lvMoreData.getAdapter() == null) {
            botListTemplateAdapter = new BotListViewTemplateAdapter(getContext(), lvMoreData, model.size());
            lvMoreData.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (BotListViewTemplateAdapter) lvMoreData.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(model);
        botListTemplateAdapter.notifyDataSetChanged();

        if(!StringUtils.isNullOrEmpty(title)) {
            tvOptionsTitle.setText(title);

            if(sharedPreferences != null)
                tvOptionsTitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_TXT_COLOR, "#000000")));
        }

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        ListViewMoreAdapter listViewMoreAdapter = new ListViewMoreAdapter(model);
        rvViewMore.setAdapter(listViewMoreAdapter);

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight)));
            }

        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setisFromFullView(boolean isFromFullView) {
        this.isFromFullView = isFromFullView;
    }

    public void setData(String title, ArrayList<BotListModel> botListModelArrayList) {
        this.model = botListModelArrayList;
        this.title = title;
    }

    public void setData(ArrayList<BotListModel> botListModelArrayList, boolean isFromListMenu){
        model = botListModelArrayList;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }
}
