package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ImageViewCompat;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.DropDownElementsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;

public class DropDownTemplateHolder extends BaseViewHolder {
    private final TextView tvDropDownHeading;
    private final TextView tvDropDownTitle;
    private final TextView tvSubmit;
    private final Spinner spinner;
    private String placeHolder;
    private String msgId;
    private int selectedIndex;

    public static DropDownTemplateHolder getInstance(ViewGroup parent) {
        return new DropDownTemplateHolder(createView(R.layout.template_dropdown, parent));
    }

    private DropDownTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        tvDropDownTitle = itemView.findViewById(R.id.tvDropDownTitle);
        tvSubmit = itemView.findViewById(R.id.submit);
        spinner = itemView.findViewById(R.id.spinner);
        tvDropDownHeading = itemView.findViewById(R.id.tvDropDownHeading);
        LinearLayoutCompat llSpinner = itemView.findViewById(R.id.llSpinner);
        setRoundedCorner(llSpinner, 10);

        GradientDrawable gradientDrawable = (GradientDrawable) tvSubmit.getBackground();
        gradientDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        tvSubmit.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        msgId = ((BotResponse) baseBotMessage).getMessageId();
        tvDropDownTitle.setText(payloadInner.getLabel());
        tvDropDownHeading.setVisibility(payloadInner.getHeading() != null ? VISIBLE : GONE);
        tvDropDownHeading.setText(payloadInner.getHeading());
        placeHolder = payloadInner.getPlaceholder();
        List<DropDownElementsModel> elements = payloadInner.getDropDownElementsModels();
        SpinnerAdapter dataAdapter = new SpinnerAdapter(itemView.getContext(), elements, spinner, isLastItem(), spinner.getSelectedItemPosition());
        spinner.setAdapter(dataAdapter);
        spinner.setPrompt(itemView.getContext().getString(R.string.select));
        spinner.setClickable(isLastItem());
        spinner.setEnabled(isLastItem());
        Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
        selectedIndex = contentState != null && contentState.containsKey(BotResponse.SELECTED_ITEM) ? (int) contentState.get(BotResponse.SELECTED_ITEM) : -1;
        spinner.setSelection(selectedIndex);
        tvSubmit.setOnClickListener(view -> {
            if (!isLastItem()) return;
            if (selectedIndex != -1 && !elements.get(selectedIndex).getTitle().equals(placeHolder)) {
                composeFooterInterface.onSendClick(elements.get(selectedIndex).getTitle(), false);
            }
        });
    }

    public class SpinnerAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private final List<DropDownElementsModel> elements;
        private final Spinner spinner;
        private final boolean isLastItem;
        private final int selectedPosition;
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#FFFFFF");

        public SpinnerAdapter(Context context, List<DropDownElementsModel> elements, Spinner spinner, boolean isLastItem, int selectedPosition) {
            this.inflater = LayoutInflater.from(context);
            this.elements = elements;
            this.spinner = spinner;
            this.isLastItem = isLastItem;
            this.selectedPosition = selectedPosition;
        }

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Object getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.drop_down_item_view, null);
                holder = new ViewHolder();
                holder.item = convertView.findViewById(R.id.item);
                holder.llLayout = convertView.findViewById(R.id.bot_options_more);
                holder.ivTick = convertView.findViewById(R.id.ivTick);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.llLayout.setBackgroundColor(Color.WHITE);
            holder.ivTick.setVisibility(GONE);
            holder.item.setText(elements.get(position).getTitle());
            holder.item.setOnClickListener(v -> {
                hideSpinnerDropDown(spinner);
                if (isLastItem) {
                    spinner.setSelection(position);
                    contentStateListener.onSaveState(msgId, position, BotResponse.SELECTED_ITEM);
                }
            });

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LinearLayout view = (LinearLayout) super.getDropDownView(position, convertView, parent);
            ImageView ivTick = view.findViewById(R.id.ivTick);

            if ((selectedPosition == -1 && position == 0) || selectedPosition == position) {
                view.setBackgroundColor(Color.parseColor(leftBgColor));
                ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                ivTick.setImageTintList(colorStateList);
                ivTick.setVisibility(VISIBLE);
            } else {
                view.setBackgroundColor(Color.WHITE);
                ivTick.setVisibility(GONE);
            }
            return view;
        }

        private class ViewHolder {
            TextView item;
            LinearLayout llLayout;
            ImageView ivTick;
        }
    }

    private void hideSpinnerDropDown(Spinner spinner) {
        if (spinner == null) {
            return;
        }
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            AccessibleObject.setAccessible(new AccessibleObject[]{method}, true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}