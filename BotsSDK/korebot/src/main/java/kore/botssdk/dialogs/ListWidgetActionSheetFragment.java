package kore.botssdk.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ListWidgetAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.WidgetListElementModel;
import kore.botssdk.utils.StringUtils;

public class ListWidgetActionSheetFragment extends BottomSheetDialogFragment {

    private ArrayList<WidgetListElementModel> model;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public String getSkillName() {
        return skillName;
    }

    private BottomSheetDialog bottomSheetDialog;
    private String title;

    public void setSkillName(String skillName, String trigger) {
        this.skillName = skillName;
    }

    private String skillName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view_more_sheet, container, false);
        LinearLayout llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        TextView tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        RecyclerView rvViewMore = view.findViewById(R.id.rvMoreData);
        View divider = view.findViewById(R.id.divider);
        rvViewMore.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        tvOptionsTitle.setVisibility(View.VISIBLE);
        rvViewMore.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);

        if (model != null) {
            ListWidgetAdapter botListTemplateAdapter = new ListWidgetAdapter(getActivity(), BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET);
            rvViewMore.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setData(model);
            botListTemplateAdapter.setPreviewLength(model.size());
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            botListTemplateAdapter.setBottomSheet(bottomSheetDialog);
        }


        if (!StringUtils.isNullOrEmpty(title)) {
            tvOptionsTitle.setVisibility(View.VISIBLE);
            tvOptionsTitle.setText(title);
        } else
            tvOptionsTitle.setVisibility(View.GONE);

        llCloseBottomSheet.setOnClickListener(v -> {
            if (bottomSheetDialog != null)
                bottomSheetDialog.dismiss();
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
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null && bottomSheet.getLayoutParams() != null)
                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight)));
        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setIsFromFullView(boolean isFromFullView) {
    }

    public void setData(String title, ArrayList<WidgetListElementModel> botListModelArrayList) {
        this.model = botListModelArrayList;
        this.title = title;
    }

    public void setData(ArrayList<WidgetListElementModel> botListModelArrayList, boolean isFromListMenu) {
        model = botListModelArrayList;
    }
}
