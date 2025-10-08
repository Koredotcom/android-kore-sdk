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

import kore.botssdk.R;
import kore.botssdk.adapter.BottomOptionsCycleAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.viewUtils.DimensionUtil;

public class OptionsActionSheetFragment extends BottomSheetDialogFragment
{
    private BotOptionsModel model;
    private ComposeFooterInterface composeFooterInterface;
    private int dp1;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_bottom_sheet, container, false);
        ListView lvMoreData = view.findViewById(R.id.lvMoreData);
        TextView tvOptionsTitle = view.findViewById(R.id.tvOptionsTitle);
        LinearLayout llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        LinearLayout llBottomLayout = view.findViewById(R.id.llBottomLayout);
        LinearLayout llTabHeader = view.findViewById(R.id.llTabHeader);
        llTabHeader.setVisibility(View.GONE);
        tvOptionsTitle.setVisibility(View.VISIBLE);
        RecyclerView rvViewMore = view.findViewById(R.id.rvMoreData);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        rvViewMore.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        rvViewMore.setVisibility(View.VISIBLE);
        lvMoreData.setVisibility(View.GONE);
        this.dp1 = (int) DimensionUtil.dp1;
        BottomOptionsCycleAdapter bottomOptionsCycleAdapter;

        if (rvViewMore.getAdapter() == null) {
            bottomOptionsCycleAdapter = new BottomOptionsCycleAdapter(model.getTasks());
            rvViewMore.setAdapter(bottomOptionsCycleAdapter);
            bottomOptionsCycleAdapter.setComposeFooterInterface(composeFooterInterface);
            bottomOptionsCycleAdapter.setContext(requireActivity());
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

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
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

    public void setData(BotOptionsModel taskTemplateModel) {
        model = taskTemplateModel;
    }
}
