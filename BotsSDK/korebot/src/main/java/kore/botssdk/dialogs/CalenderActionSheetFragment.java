package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class CalenderActionSheetFragment extends BottomSheetDialogFragment
{
    final String LOG_TAG = OptionsActionSheetFragment.class.getSimpleName();
    private View view;
    private boolean isFromFullView;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean isFromListMenu = false;
    private int dp1;
    private TextView tvOptionsTitle, tvDateConfirm;
    private LinearLayout llCloseBottomSheet;
    private DatePicker datePicker;
    private PayloadInner payInner;
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

        view = inflater.inflate(R.layout.calender_bootom_sheet, container,false);
        tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        tvDateConfirm = view.findViewById(R.id.tvDateConfirm);
        datePicker = view.findViewById(R.id.datePicker1);
        tvOptionsTitle.setVisibility(View.VISIBLE);
        this.dp1 = (int) DimensionUtil.dp1;
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        if(payInner != null && payInner.getText() != null && !payInner.getText().isEmpty())
            tvOptionsTitle.setText(payInner.getText());

        llCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();
            }
        });

        tvDateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                sendMessageText(DateUtils.getMonthName(datePicker.getMonth())+" "+datePicker.getDayOfMonth()+DateUtils.getDayOfMonthSuffix(datePicker.getDayOfMonth())+" "+datePicker.getYear());
            }
        });

        return view;
    }

    private void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(),false);
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

                bottomSheet.getLayoutParams().height = ((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight/4)+50) * dp1);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight/4)+50) * dp1));
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setisFromFullView(boolean isFromFullView) {
        this.isFromFullView = isFromFullView;
    }

    public void setData(PayloadInner payInner) {
        this.payInner = payInner;
    }

    public void setData(BotOptionsModel botOptionsModel, boolean isFromListMenu){
//        model = botOptionsModel;
        this.isFromListMenu = isFromListMenu;
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }

}
