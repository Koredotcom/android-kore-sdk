package kore.botssdk.dialogs;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.AdvancedMultiSelectAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.AdvancedMultiSelectModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;

public class AdvanceMultiSelectSheetFragment extends BottomSheetDialogFragment implements AdvanceMultiSelectListener {
    final String LOG_TAG = AdvanceMultiSelectSheetFragment.class.getSimpleName();
    private ComposeFooterInterface composeFooterInterface;
    private BottomSheetDialog bottomSheetDialog;
    private final ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems = new ArrayList<>();
    private final AdvancedMultiSelectAdapter advancedMultiSelectAdapter = new AdvancedMultiSelectAdapter();
    private PayloadInner payloadInner;
    private RecyclerView recyclerView;
    private TextView tvAdvanceDone;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advance_multi_select_bottom_sheet, container, false);
        TextView tvOptionsTitle = view.findViewById(R.id.advance_multi_select_title);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        RelativeLayout llBottomLayout = view.findViewById(R.id.llBottomLayout);
        tvOptionsTitle.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.category_list);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        TextView tvViewMore = view.findViewById(R.id.viewMore);
        tvAdvanceDone = view.findViewById(R.id.done);
        ArrayList<AdvancedMultiSelectModel> models = payloadInner.getAdvancedMultiSelectModels();

        GradientDrawable gradientDrawable = (GradientDrawable) tvAdvanceDone.getBackground().mutate();
        gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        tvAdvanceDone.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));

        if (models != null && !models.isEmpty()) {
            tvAdvanceDone.setVisibility(allCheckedItems.isEmpty() ? GONE : VISIBLE);
            advancedMultiSelectAdapter.setMultiSelectModels(models);
            advancedMultiSelectAdapter.setEnabled(true);
            advancedMultiSelectAdapter.setAdvanceMultiListener(this);
            advancedMultiSelectAdapter.setCheckedItems(allCheckedItems);
            recyclerView.setAdapter(advancedMultiSelectAdapter);

            if (payloadInner.getAdvancedMultiSelectModels().size() > payloadInner.getLimit()) {
                tvViewMore.setVisibility(VISIBLE);
            } else {
                tvViewMore.setVisibility(GONE);
            }

            tvViewMore.setOnClickListener(v -> {
                tvViewMore.setVisibility(GONE);
                advancedMultiSelectAdapter.refresh();
            });

            tvAdvanceDone.setOnClickListener(v -> {
                StringBuilder values = new StringBuilder();
                StringBuilder titles = new StringBuilder();
                titles.append("Here are the selected items : ");
                values.append("Here are the selected items : ");
                for (int i = 0; i < allCheckedItems.size(); i++) {
                    titles.append(allCheckedItems.get(i).getTitle());
                    values.append(allCheckedItems.get(i).getValue());
                    if (i != allCheckedItems.size() - 1) {
                        titles.append(", ");
                        values.append(", ");
                    }
                }

                composeFooterInterface.onSendClick(titles.toString(), values.toString(), true);
                dismiss();
            });

        } else {
            recyclerView.setAdapter(null);
        }

        if (sharedPreferences != null)
            llBottomLayout.setBackgroundColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BG_COLOR, "#FFFFFF")));

        if (payloadInner.getHeading() != null) {
            tvOptionsTitle.setText(payloadInner.getHeading());

            if (sharedPreferences != null)
                tvOptionsTitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_TXT_COLOR, "#000000")));
        }

        ivClose.setOnClickListener(v -> {
            if (bottomSheetDialog != null)
                bottomSheetDialog.dismiss();
        });

        return view;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            bottomSheet.getLayoutParams().height = (int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight) - (50 * dp1));
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight) - (50 * dp1)));
        });

        return bottomSheetDialog;
    }


    public void setData(PayloadInner payloadInner) {
        this.payloadInner = payloadInner;
    }

    @Override
    public void itemSelected(AdvanceMultiSelectCollectionModel checkedItems) {
        if (!allCheckedItems.contains(checkedItems)) {
            allCheckedItems.add(checkedItems);
        } else {
            allCheckedItems.remove(checkedItems);
        }

        advancedMultiSelectAdapter.setCheckedItems(allCheckedItems);
        advancedMultiSelectAdapter.notifyDataSetChanged();
        tvAdvanceDone.setVisibility(!allCheckedItems.isEmpty() ? VISIBLE : GONE);
    }

    @Override
    public void allItemsSelected(boolean addOrRemove, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        allCheckedItems.removeAll(checkedItems);
        if (addOrRemove) {
            allCheckedItems.addAll(checkedItems);
        }
        advancedMultiSelectAdapter.setCheckedItems(allCheckedItems);
        advancedMultiSelectAdapter.notifyDataSetChanged();
        tvAdvanceDone.setVisibility(!allCheckedItems.isEmpty() ? VISIBLE : GONE);
    }
}
