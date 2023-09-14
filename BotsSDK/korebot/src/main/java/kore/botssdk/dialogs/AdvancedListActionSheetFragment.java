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

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedListAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.AdvancedListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class AdvancedListActionSheetFragment extends BottomSheetDialogFragment {
    private TextView tvOptionsTitle;
    private View view;
    private boolean isFromFullView;
    private ArrayList<AdvancedListModel> model;
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
    private SharedPreferences sharedPreferences;
    private String title;

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

        view = inflater.inflate(R.layout.advances_action_sheet, container,false);
        lvMoreData = view.findViewById(R.id.lvMoreData);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        llBottomLayout = view.findViewById(R.id.llBottomLayout);
        tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        sharedPreferences = getActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        this.dp1 = (int) DimensionUtil.dp1;

        if(!StringUtils.isNullOrEmpty(title))
        {
            tvOptionsTitle.setVisibility(View.VISIBLE);
            tvOptionsTitle.setText(title);
        }

        AdvancedListAdapter botListTemplateAdapter;
        if (lvMoreData.getAdapter() == null) {
            botListTemplateAdapter = new AdvancedListAdapter(getContext(), lvMoreData);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (AdvancedListAdapter) lvMoreData.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(model);
        lvMoreData.setAdapter(botListTemplateAdapter);
        botListTemplateAdapter.notifyDataSetChanged();

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

                assert bottomSheet != null;
                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(bottomSheet.getLayoutParams().height);
                bottomSheetBehavior.setDraggable(false);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setData(ArrayList<AdvancedListModel> taskTemplateModel) {
        model = taskTemplateModel;
    }

    public void setData(ArrayList<AdvancedListModel> taskTemplateModel, boolean isFromListMenu){
        model = taskTemplateModel;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }
}
