package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
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
    private LinearLayout llCloseBottomSheet;
    public String getSkillName() {
        return skillName;
    }
    private BottomSheetDialog bottomSheetDialog;

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

        this.dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        BotListViewTemplateAdapter botListTemplateAdapter;
        if (lvMoreData.getAdapter() == null) {
            botListTemplateAdapter = new BotListViewTemplateAdapter(getContext(), lvMoreData);
            lvMoreData.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        } else {
            botListTemplateAdapter = (BotListViewTemplateAdapter) lvMoreData.getAdapter();
        }
        botListTemplateAdapter.setBotListModelArrayList(model.getTab1());
        botListTemplateAdapter.notifyDataSetChanged();

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
//                bottomSheetBehavior.setHideable(true);
//                bindBottomSheet(bottomSheetBehavior);
//                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                    AppUtils.showHideVirtualKeyboard(getContext(), dialog.getCurrentFocus(), true);
//                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
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

