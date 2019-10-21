package kore.botssdk.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.WidgetSelectActionsAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.WTaskTemplateModel;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class WidgetActionSheetFragment extends BottomSheetDialogFragment {

    View view;
    boolean isFromFullView;
    RecyclerView recycler_actions;
    WTaskTemplateModel model;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.widget_actions_sheet, container,
                false);
        recycler_actions = view.findViewById(R.id.recycler_actions);
        recycler_actions.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_actions.setHasFixedSize(true);

        WidgetSelectActionsAdapter adapter = new WidgetSelectActionsAdapter((Activity) getActivity(),this, model,isFromFullView);

        recycler_actions.setAdapter(adapter);
        return view;

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                // Right here!
               BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);




             /*   FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) (300 * dp1));
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {


                        // Check Logs to see how bottom sheets behaves
                        switch (newState) {
                            case BottomSheetBehavior.STATE_COLLAPSED:

                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
                                break;
                            case BottomSheetBehavior.STATE_HIDDEN:
                                dismiss();
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                break;
                            case BottomSheetBehavior.STATE_HALF_EXPANDED:
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // React to dragging events
                    }
                });

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
            }

        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }


    public void setisFromFullView(boolean isFromFullView) {
        this.isFromFullView=isFromFullView;
    }

    public void setData(WTaskTemplateModel taskTemplateModel) {
        model=taskTemplateModel;
    }
}
