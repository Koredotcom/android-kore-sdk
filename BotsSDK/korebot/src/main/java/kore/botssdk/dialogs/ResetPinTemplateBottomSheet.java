package kore.botssdk.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_TEXT_COLOR;
import static kore.botssdk.models.BotResponse.THEME_NAME;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;
import static kore.botssdk.viewholders.BaseViewHolder.getDotMessage;
import static kore.botssdk.viewholders.BaseViewHolder.setRoundedCorner;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.adapter.PinFieldItemAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.itemdecoration.HorizontalSpaceItemDecoration;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ResetPinTemplateBottomSheet extends BottomSheetDialogFragment {
    private ComposeFooterInterface composeFooterInterface;
    private BottomSheetDialog bottomSheetDialog;
    private PayloadInner payloadInner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reset_pin_template_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageView ivClose = view.findViewById(R.id.close);
        TextView tvTitle = view.findViewById(R.id.title);
        TextView tvEnterNewPin = view.findViewById(R.id.enter_new_pin);
        TextView tvRenterNewPin = view.findViewById(R.id.reenter_new_pin);
        RecyclerView newPinRecycler = view.findViewById(R.id.new_pin_recycler);
        RecyclerView reenterPinRecycler = view.findViewById(R.id.reenter_pin_recycler);
        TextView tvReset = view.findViewById(R.id.reset);
        newPinRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(10));
        newPinRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        newPinRecycler.post(() -> {
            newPinRecycler.setAdapter(new PinFieldItemAdapter(payloadInner.getPinLength(), newPinRecycler.getMeasuredWidth()));
            newPinRecycler.post(() -> {
                int childCount = newPinRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = newPinRecycler.getChildAt(index).findViewById(R.id.otp_field);

                    EditText prevChildView = (index - 1 >= 0)
                            ? newPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field)
                            : null;

                    EditText nextChildView = (index + 1 < childCount)
                            ? newPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field)
                            : null;

                    addTextWatcher(prevChildView, childView, nextChildView);
                }
            });
        });

        reenterPinRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(10));
        reenterPinRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        reenterPinRecycler.post(() -> {
            reenterPinRecycler.setAdapter(new PinFieldItemAdapter(payloadInner.getPinLength(), reenterPinRecycler.getMeasuredWidth()));
            reenterPinRecycler.post(() -> {
                int childCount = reenterPinRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field);

                    EditText prevChildView = (index - 1 >= 0)
                            ? reenterPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field)
                            : null;

                    EditText nextChildView = (index + 1 < childCount)
                            ? reenterPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field)
                            : null;

                    addTextWatcher(prevChildView, childView, nextChildView);
                }
            });
        });

        tvTitle.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getTitle()) ? VISIBLE : GONE);
        tvTitle.setText(payloadInner.getTitle());
        tvEnterNewPin.setText(payloadInner.getEnterPinTitle());
        tvRenterNewPin.setText(payloadInner.getReEnterPinTitle());

        if (payloadInner.getResetButtons() != null && !payloadInner.getResetButtons().isEmpty()) {
            tvReset.setText(payloadInner.getResetButtons().get(0).getTitle());
            SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
            String bgColor = sharedPrefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5");
            String txtColor = sharedPrefs.getString(BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF");
            setRoundedCorner(tvReset, 6 * dp1);
            tvReset.setBackgroundColor(Color.parseColor(bgColor));
            tvReset.setTextColor(Color.parseColor(txtColor));
            tvReset.setOnClickListener(v -> {
                StringBuilder enteredPinBuilder = new StringBuilder();
                int childCount = newPinRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = newPinRecycler.getChildAt(index).findViewById(R.id.otp_field);
                    enteredPinBuilder.append(childView.getText().toString());
                }
                String enteredPin = enteredPinBuilder.toString();

                StringBuilder reenteredPinBuilder = new StringBuilder();
                childCount = reenterPinRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field);
                    reenteredPinBuilder.append(childView.getText().toString());
                }
                String reenteredPin = reenteredPinBuilder.toString();

                if (enteredPin.length() == payloadInner.getPinLength() && reenteredPin.length() == payloadInner.getPinLength() && enteredPin.equals(reenteredPin)) {
                    if (composeFooterInterface != null && enteredPin.length() == payloadInner.getPinLength()) {
                        composeFooterInterface.onSendClick(getDotMessage(enteredPin), enteredPin, false);
                        if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
                    }
                } else {
                    Toast.makeText(view.getContext(), payloadInner.getWarningMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        ivClose.setOnClickListener(v -> {
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
        });
    }

    public void setData(PayloadInner payloadInner) {
        this.payloadInner = payloadInner;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                if (bottomSheet.getLayoutParams() != null)
                    bottomSheet.getLayoutParams().height = (int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight) - (50 * dp1));

                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight((int) ((AppControl.getInstance(getContext()).getDimensionUtil().screenHeight) - (50 * dp1)));
            }
        });

        return bottomSheetDialog;
    }

    private void addTextWatcher(EditText previousView, EditText editText, EditText nextView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    if (nextView != null) {
                        nextView.requestFocus();
                    }
                } else {
                    if (previousView != null) {
                        previousView.requestFocus();
                    }
                }
            }
        });
    }
}
