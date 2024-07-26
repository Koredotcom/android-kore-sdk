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
import kore.botssdk.models.DropDownElementsModel;
import kore.botssdk.models.PayloadInner;

public class DropDownTemplateHolder extends BaseViewHolderNew {
    private final TextView tvDropDownTitle;
    private final Spinner spinner;

    public DropDownTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        tvDropDownTitle = itemView.findViewById(R.id.tvDropDownTitle);
        spinner = itemView.findViewById(R.id.spinner);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        tvDropDownTitle.setText(payloadInner.getLabel());
        List<DropDownElementsModel> elements = payloadInner.getDropDownElementsModels();
        SpinnerAdapter dataAdapter = new SpinnerAdapter(itemView.getContext(), elements, spinner, isLastItem());
        spinner.setAdapter(dataAdapter);
        spinner.setPrompt(itemView.getContext().getString(R.string.select));
        spinner.setClickable(isLastItem());
        spinner.setEnabled(isLastItem());
        int selectedIndex = -1;
        for (int index = 0; index < elements.size(); index++) {
            if (elements.get(index).getTitle().equals(elements.get((int) spinner.getSelectedItemPosition()).getTitle())) {
                selectedIndex = index;
            }
        }
//        elements.mapIndexed { index, item -> if (item[KEY_TITLE] == selectedItem) selectedIndex = index }
        spinner.setSelection(selectedIndex);
    }

    public class SpinnerAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private final List<DropDownElementsModel> elements;
        private final Spinner spinner;
        private final boolean isLastItem;

        private static final String KEY_TITLE = "title";

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
                if (isLastItem) {
                    composeFooterInterface.copyMessageToComposer(elements.get(position).getTitle(), false);
//                        actionEvent(new BotChatEvent.OnDropDownItemClicked(elements.get(position).get(KEY_TITLE)));
//                        onDropDownSelection(id, elements.get(position).get(KEY_TITLE), BotResponse.SELECTED_ITEM);
                }
                hideSpinnerDropDown(spinner);
                spinner.setSelection(position);
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
