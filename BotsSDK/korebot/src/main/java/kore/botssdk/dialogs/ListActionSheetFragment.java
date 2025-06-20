package kore.botssdk.dialogs;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListViewTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotListViewMoreDataModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;

public class ListActionSheetFragment extends BottomSheetDialogFragment {

    private BotListViewMoreDataModel model;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private TextView tvTab1;
    private TextView tvTab2;
    private TextView tvOptionsTitle;
    private boolean isEnabled;
    private String title;
    private BottomSheetDialog bottomSheetDialog;
    private boolean showHeader = false;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName, String trigger) {
        this.skillName = skillName;
    }

    private String skillName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_bottom_sheet, container, false);
        ListView lvMoreData = view.findViewById(R.id.lvMoreData);
        tvTab1 = view.findViewById(R.id.tvTab1);
        tvTab2 = view.findViewById(R.id.tvTab2);
        tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        LinearLayout llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        LinearLayout llTabHeader = view.findViewById(R.id.llTabHeader);
        LinearLayout llBottomLayout = view.findViewById(R.id.llBottomLayout);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        BotListViewTemplateAdapter botListTemplateAdapter;
        if (lvMoreData.getAdapter() == null) {
            botListTemplateAdapter = new BotListViewTemplateAdapter(requireActivity(), lvMoreData, 0, bottomSheetDialog);
            lvMoreData.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (BotListViewTemplateAdapter) lvMoreData.getAdapter();
        }

        tvTab1.setText(ContextCompat.getString(requireActivity(), R.string.tab1));
        tvTab2.setText(ContextCompat.getString(requireActivity(), R.string.tab2));

        botListTemplateAdapter.setBotListModelArrayList(model.getTab1());
        botListTemplateAdapter.notifyDataSetChanged();
        tvTab1.setSelected(true);
        tvOptionsTitle.setVisibility(!StringUtils.isNullOrEmpty(title) ? View.VISIBLE : View.GONE);
        tvOptionsTitle.setText(title);

        llTabHeader.setVisibility(View.VISIBLE);
        if (!showHeader)
            llTabHeader.setVisibility(View.GONE);

        tvTab1.setOnClickListener(v -> {
            tvTab1.setSelected(true);
            tvTab2.setSelected(false);
            botListTemplateAdapter.setBotListModelArrayList(model.getTab1());
        });

        tvTab2.setOnClickListener(v -> {
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
            botListTemplateAdapter.setBotListModelArrayList(model.getTab2());
        });

        llCloseBottomSheet.setOnClickListener(v -> {
            if (bottomSheetDialog != null)
                bottomSheetDialog.dismiss();
        });
        return view;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setHeaderVisible(boolean visible) {
        this.showHeader = visible;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) (500 * dp1));
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setIsFromFullView(boolean isFromFullView) {
    }

    public void setData(String title, BotListViewMoreDataModel taskTemplateModel) {
        this.title = title;
        model = taskTemplateModel;
    }

    public void setData(String title, BotListViewMoreDataModel taskTemplateModel, boolean isFromListMenu) {
        this.title = title;
        model = taskTemplateModel;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
    }
}

