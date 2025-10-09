package kore.botssdk.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.ChatAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BaseBotMessage;

public class TemplateBottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetDialog bottomSheetDialog;
    private final ChatAdapter chatAdapter = new ChatAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_bottom_sheet, container, false);
        LinearLayout llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(chatAdapter);

        llCloseBottomSheet.setOnClickListener(v -> {
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
        });
        return view;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        chatAdapter.setComposeFooterInterface(composeFooterInterface);
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        chatAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
    }

    public void show(BaseBotMessage botResponse, FragmentManager manager) {
        show(manager, this.getClass().getName());
        new Handler(Looper.getMainLooper()).postDelayed(() -> chatAdapter.addBaseBotMessage(botResponse), 200);
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        chatAdapter.setBottomSheetDialog(bottomSheetDialog);
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundColor(Color.TRANSPARENT);
            }

            if (bottomSheet == null) return;
            bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight * 0.50)));
            bottomSheetBehavior.setMaxHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight * 0.95)));
        });

        return bottomSheetDialog;
    }
}
