package kore.botssdk.viewholders;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_TEXT_COLOR;
import static kore.botssdk.models.BotResponse.THEME_NAME;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.PinFieldItemAdapter;
import kore.botssdk.itemdecoration.HorizontalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class OtpValidationTemplateHolder extends BaseViewHolder {
    public static OtpValidationTemplateHolder getInstance(ViewGroup parent) {
        return new OtpValidationTemplateHolder(createView(R.layout.template_otp_validation, parent));
    }

    public OtpValidationTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        AppCompatImageView ivClose = itemView.findViewById(R.id.close);
        TextView tvTitle = itemView.findViewById(R.id.title);
        TextView tvDescription = itemView.findViewById(R.id.description);
        TextView tvPhoneNumber = itemView.findViewById(R.id.phone_number);
        RecyclerView otpRecycler = itemView.findViewById(R.id.otp_recycler);
        TextView tvSubmit = itemView.findViewById(R.id.submit);
        TextView tvResendOtp = itemView.findViewById(R.id.resend_otp);
        otpRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(10));
        otpRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
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

        ivClose.setVisibility(GONE);
        tvTitle.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getTitle()) ? VISIBLE : GONE);
        tvDescription.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getDescription()) ? VISIBLE : GONE);
        tvPhoneNumber.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getMobileNumber()) ? VISIBLE : GONE);
        tvTitle.setText(payloadInner.getTitle());
        tvDescription.setText(payloadInner.getDescription());
        tvPhoneNumber.setText(payloadInner.getMobileNumber());

        if (payloadInner.getOtpButtons() != null && payloadInner.getOtpButtons().size() > 1) {
            tvSubmit.setText(payloadInner.getOtpButtons().get(0).getTitle());
            SharedPreferences sharedPrefs = itemView.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
            String bgColor = sharedPrefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5");
            String txtColor = sharedPrefs.getString(BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF");
            setRoundedCorner(tvSubmit, 6 * dp1);
            tvSubmit.setBackgroundColor(Color.parseColor(bgColor));
            tvSubmit.setTextColor(Color.parseColor(txtColor));
            SpannableString content = new SpannableString(payloadInner.getOtpButtons().get(1).getTitle());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvResendOtp.setText(content);
            tvSubmit.setOnClickListener(v -> {
                if (!isLastItem()) return;
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
                    composeFooterInterface.onSendClick(getDotMessage(otp), otp, false);
                }
            });

            tvResendOtp.setOnClickListener(v -> {
                if (!isLastItem()) return;
                if (composeFooterInterface != null) {
                    String payload = payloadInner.getOtpButtons().get(1).getPayload();
                    composeFooterInterface.onSendClick(payload, payload, false);
                }
            });
        }
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
