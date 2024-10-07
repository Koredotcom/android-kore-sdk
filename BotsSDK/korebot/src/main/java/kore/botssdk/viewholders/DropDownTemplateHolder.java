package kore.botssdk.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.DropDownElementsModel;
import kore.botssdk.models.PayloadInner;

public class DropDownTemplateHolder extends BaseViewHolder {
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
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        msgId = ((BotResponse) baseBotMessage).getMessageId();
        tvDropDownTitle.setText(payloadInner.getLabel());
        placeHolder = payloadInner.getPlaceholder();
        List<DropDownElementsModel> elements = payloadInner.getDropDownElementsModels();
        SpinnerAdapter dataAdapter = new SpinnerAdapter(itemView.getContext(), elements, spinner, isLastItem());
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

        public SpinnerAdapter(Context context, List<DropDownElementsModel> elements, Spinner spinner, boolean isLastItem) {
            this.inflater = LayoutInflater.from(context);
            this.elements = elements;
            this.spinner = spinner;
            this.isLastItem = isLastItem;
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

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

        private class ViewHolder {
            TextView item;
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