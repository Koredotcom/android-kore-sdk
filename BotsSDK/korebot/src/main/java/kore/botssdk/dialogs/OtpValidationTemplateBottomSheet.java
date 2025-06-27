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
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
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

public class OtpValidationTemplateBottomSheet extends BottomSheetDialogFragment {
    private ComposeFooterInterface composeFooterInterface;
    private BottomSheetDialog bottomSheetDialog;
    private PayloadInner payloadInner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.otp_template_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.title);
        AppCompatImageView ivClose = view.findViewById(R.id.close);
        LinearLayoutCompat llBottomLayout = view.findViewById(R.id.llBottomLayout);
        tvTitle.setVisibility(View.VISIBLE);

        TextView tvDescription = view.findViewById(R.id.description);
        TextView tvPhoneNumber = view.findViewById(R.id.phone_number);
        RecyclerView otpRecycler = view.findViewById(R.id.otp_recycler);
        TextView tvSubmit = view.findViewById(R.id.submit);
        TextView tvResendOtp = view.findViewById(R.id.resend_otp);
        otpRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(10));
        otpRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        otpRecycler.post(() -> {
            otpRecycler.setAdapter(new PinFieldItemAdapter(payloadInner.getPinLength(), otpRecycler.getMeasuredWidth()));
            otpRecycler.post(() -> {
                int childCount = otpRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = otpRecycler.getChildAt(index).findViewById(R.id.otp_field);

                    EditText prevChildView = (index - 1 >= 0)
                            ? otpRecycler.getChildAt(index - 1).findViewById(R.id.otp_field)
                            : null;

                    EditText nextChildView = (index + 1 < childCount)
                            ? otpRecycler.getChildAt(index + 1).findViewById(R.id.otp_field)
                            : null;

                    addTextWatcher(prevChildView, childView, nextChildView);
                }
            });
        });

        tvTitle.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getTitle()) ? VISIBLE : GONE);
        tvDescription.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getDescription()) ? VISIBLE : GONE);
        tvPhoneNumber.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getMobileNumber()) ? VISIBLE : GONE);
        tvTitle.setText(payloadInner.getTitle());
        tvDescription.setText(payloadInner.getDescription());
        tvPhoneNumber.setText(payloadInner.getMobileNumber());

        if (payloadInner.getOtpButtons() != null && payloadInner.getOtpButtons().size() > 1) {
            tvSubmit.setText(payloadInner.getOtpButtons().get(0).getTitle());
            SharedPreferences sharedPrefs = view.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
            String bgColor = sharedPrefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5");
            String txtColor = sharedPrefs.getString(BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF");
            setRoundedCorner(tvSubmit, 6 * dp1);
            tvSubmit.setBackgroundColor(Color.parseColor(bgColor));
            tvSubmit.setTextColor(Color.parseColor(txtColor));
            SpannableString content = new SpannableString(payloadInner.getOtpButtons().get(1).getTitle());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvResendOtp.setText(content);
            tvSubmit.setOnClickListener(v -> {
                StringBuilder otpBuilder = new StringBuilder();
                int childCount = otpRecycler.getChildCount();

                for (int index = 0; index < childCount; index++) {
                    EditText childView = otpRecycler
                            .getChildAt(index)
                            .findViewById(R.id.otp_field);
                    otpBuilder.append(childView.getText().toString());
                }

                String otp = otpBuilder.toString();
                if (composeFooterInterface != null && otp.length() == payloadInner.getPinLength()) {
                    if (bottomSheetDialog != null)
                        bottomSheetDialog.dismiss();
                    composeFooterInterface.onSendClick(getDotMessage(otp), otp, false);
                }
            });

            tvResendOtp.setOnClickListener(v -> {
                if (composeFooterInterface != null) {
                    String payload = payloadInner.getOtpButtons().get(1).getPayload();
                    composeFooterInterface.onSendClick(payload, payload, false);
                    if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
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
