package kore.botssdk.viewholders;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.models.BotResponse.BUBBLE_LEFT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_BG_COLOR;
import static kore.botssdk.models.BotResponse.BUBBLE_RIGHT_TEXT_COLOR;
import static kore.botssdk.models.BotResponse.THEME_NAME;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.PinFieldItemAdapter;
import kore.botssdk.itemdecoration.HorizontalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ResetPinTemplateHolder extends BaseViewHolder {
    public static ResetPinTemplateHolder getInstance(ViewGroup parent) {
        return new ResetPinTemplateHolder(createView(R.layout.template_reset_pin, parent));
    }

    public ResetPinTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        LinearLayoutCompat llCloseBottomSheet = itemView.findViewById(R.id.llCloseBottomSheet);
        LinearLayoutCompat layoutResetPin = itemView.findViewById(R.id.layout_reset_pin);
        AppCompatImageView ivClose = itemView.findViewById(R.id.close);
        TextView tvTitle = itemView.findViewById(R.id.title);
        TextView tvEnterNewPin = itemView.findViewById(R.id.enter_new_pin);
        TextView tvRenterNewPin = itemView.findViewById(R.id.reenter_new_pin);
        RecyclerView newPinRecycler = itemView.findViewById(R.id.new_pin_recycler);
        RecyclerView reenterPinRecycler = itemView.findViewById(R.id.reenter_pin_recycler);
        TextView tvReset = itemView.findViewById(R.id.reset);

        if (bottomSheetDialog != null){
            llCloseBottomSheet.setPadding(llCloseBottomSheet.getPaddingLeft(), 0, llCloseBottomSheet.getPaddingRight(), llCloseBottomSheet.getPaddingBottom());
        }
        SharedPreferences sharedPrefs = itemView.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
        String itemBgColor = sharedPrefs.getString(BUBBLE_LEFT_BG_COLOR, "#000000");
        GradientDrawable gradientDrawable = (GradientDrawable) layoutResetPin.getBackground().mutate();
        gradientDrawable.setStroke((int) dp1, bottomSheetDialog == null ? Color.parseColor(itemBgColor) : Color.TRANSPARENT);
        newPinRecycler.addItemDecoration(new HorizontalSpaceItemDecoration(10));
        newPinRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
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
        reenterPinRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
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

        ivClose.setVisibility(GONE);
        tvTitle.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getTitle()) ? VISIBLE : GONE);
        tvTitle.setText(payloadInner.getTitle());
        tvEnterNewPin.setText(payloadInner.getEnterPinTitle());
        tvRenterNewPin.setText(payloadInner.getReEnterPinTitle());

        if (payloadInner.getResetButtons() != null && !payloadInner.getResetButtons().isEmpty()) {
            tvReset.setText(payloadInner.getResetButtons().get(0).getTitle());
            String bgColor = sharedPrefs.getString(BUBBLE_RIGHT_BG_COLOR, "#3F51B5");
            String txtColor = sharedPrefs.getString(BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF");
            setRoundedCorner(tvReset, 6 * dp1);
            tvReset.setBackgroundColor(Color.parseColor(bgColor));
            tvReset.setTextColor(Color.parseColor(txtColor));
            tvReset.setOnClickListener(v -> {
                if (!isLastItem()) return;
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
                    Toast.makeText(itemView.getContext(), payloadInner.getWarningMessage(), Toast.LENGTH_LONG).show();
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
