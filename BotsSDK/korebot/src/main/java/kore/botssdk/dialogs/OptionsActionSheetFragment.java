package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import kore.botssdk.R;
import kore.botssdk.adapter.BottomOptionsCycleAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class OptionsActionSheetFragment extends BottomSheetDialogFragment
{
    final String LOG_TAG = OptionsActionSheetFragment.class.getSimpleName();
    private View view;
    private boolean isFromFullView;
    private BotOptionsModel model;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean isFromListMenu = false;
    private ListView lvMoreData;
    private int dp1;
    private TextView tvTab1, tvTab2;
    private TextView tvOptionsTitle;
    private LinearLayout llCloseBottomSheet, llBottomLayout;
    public String getSkillName() {
        return skillName;
    }
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout llTabHeader;
    private RecyclerView rvViewMore;
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
        tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        llBottomLayout = view.findViewById(R.id.llBottomLayout);
        llTabHeader = view.findViewById(R.id.llTabHeader);
        llTabHeader.setVisibility(View.GONE);
        tvOptionsTitle.setVisibility(View.VISIBLE);
        rvViewMore = view.findViewById(R.id.rvMoreData);
        sharedPreferences = getActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        rvViewMore.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvViewMore.setVisibility(View.VISIBLE);
        lvMoreData.setVisibility(View.GONE);
        this.dp1 = (int) DimensionUtil.dp1;
        BottomOptionsCycleAdapter bottomOptionsCycleAdapter;

        if (rvViewMore.getAdapter() == null) {
            bottomOptionsCycleAdapter = new BottomOptionsCycleAdapter(model.getTasks());
            rvViewMore.setAdapter(bottomOptionsCycleAdapter);
            bottomOptionsCycleAdapter.setComposeFooterInterface(composeFooterInterface);
            bottomOptionsCycleAdapter.setContext(getActivity());
        } else {
            bottomOptionsCycleAdapter = (BottomOptionsCycleAdapter) rvViewMore.getAdapter();
        }

        bottomOptionsCycleAdapter.setBotListModelArrayList(bottomSheetDialog, model.getTasks());
        bottomOptionsCycleAdapter.notifyDataSetChanged();

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

    private void sendMessageText(String message, String payLoad) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), payLoad, false);
        } else {
            Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
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

                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight)-(50 * dp1);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight) -(50 * dp1)));
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setisFromFullView(boolean isFromFullView) {
        this.isFromFullView = isFromFullView;
    }

    public void setData(BotOptionsModel taskTemplateModel) {
        model = taskTemplateModel;
    }

    public void setData(BotOptionsModel botOptionsModel, boolean isFromListMenu){
        model = botOptionsModel;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }

}
