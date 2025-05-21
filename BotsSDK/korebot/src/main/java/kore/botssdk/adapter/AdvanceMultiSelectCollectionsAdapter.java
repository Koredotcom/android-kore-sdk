
package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponsePayLoadText.THEME_NAME;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceMultiSelectListener;
import kore.botssdk.models.AdvanceMultiSelectCollectionModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class AdvanceMultiSelectCollectionsAdapter extends RecyclerView.Adapter<AdvanceMultiSelectCollectionsAdapter.ViewHolder> {
    private static final int MULTI_SELECT_ITEM = 0;
    private static final int MULTI_SELECT_BUTTON = 1;
    private final ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels;
    private ArrayList<AdvanceMultiSelectCollectionModel> checkedItems = new ArrayList<>();
    private AdvanceMultiSelectListener multiSelectListener;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled;

    public AdvanceMultiSelectCollectionsAdapter(ArrayList<AdvanceMultiSelectCollectionModel> multiSelectModels) {
        this.multiSelectModels = multiSelectModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == MULTI_SELECT_ITEM ? R.layout.row_advance_multi_select_sub_item : R.layout.multiselect_button;
        return new AdvanceMultiSelectCollectionsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        populateVIew(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null)
            return MULTI_SELECT_ITEM;
        return MULTI_SELECT_BUTTON;
    }

    private AdvanceMultiSelectCollectionModel getItem(int position) {
        return multiSelectModels != null ? multiSelectModels.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (multiSelectModels != null) {
            return multiSelectModels.size();
        } else {
            return 0;
        }
    }

    private void populateVIew(ViewHolder holder, int position) {
        final AdvanceMultiSelectCollectionModel item = getItem(position);
        if (item == null) return;
        holder.title.setText(item.getTitle());
        holder.description.setVisibility(item.getDescription() != null ? View.VISIBLE : View.GONE);
        holder.description.setText(item.getDescription());

        if (!StringUtils.isNullOrEmpty(item.getImage_url())) {
            holder.ivAdvMultiSelect.setVisibility(View.VISIBLE);

            Picasso.get().load(item.getImage_url())
                    .centerInside()
                    .placeholder(R.drawable.transparant_image)
                    .resize((int) (40 * dp1), (int) (40 * dp1))
                    .into(holder.ivAdvMultiSelect);
        }

        GradientDrawable gradientDrawable = (GradientDrawable) holder.checkBox.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor("#ffffff"));
        gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.leftBubbleSelected));

        if (checkedItems.contains(item)) {
            gradientDrawable.setStroke((int) dp1, Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
            gradientDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        }

        holder.checkBox.setOnClickListener(v -> {
            multiSelectListener.itemSelected(item);
        });
        if (!isEnabled) {
            holder.checkBox.setClickable(false);
            holder.checkBox.setEnabled(false);
            holder.checkBox.setFocusableInTouchMode(false);
        }
    }

    public void setCheckedItems(ArrayList<AdvanceMultiSelectCollectionModel> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public void setAdvanceMultiListener(AdvanceMultiSelectListener checkAllListener) {
        this.multiSelectListener = checkAllListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout checkBox;
        TextView title;
        TextView description;
        ImageView ivAdvMultiSelect;
        RelativeLayout rootLayout;

        public ViewHolder(@NonNull View convertView, int viewType) {
            super(convertView);
            if (viewType == MULTI_SELECT_ITEM) {
                title = convertView.findViewById(R.id.title);
                description = convertView.findViewById(R.id.description);
                checkBox = convertView.findViewById(R.id.check_multi_item);
                ivAdvMultiSelect = convertView.findViewById(R.id.ivAdvMultiSelect);
                rootLayout = convertView.findViewById(R.id.root_layout);
            } else {
                title = convertView.findViewById(R.id.text_view_button);
            }

            if (rootLayout != null && rootLayout.getBackground() != null) {
                SharedPreferences pref = rootLayout.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
                GradientDrawable drawable = (GradientDrawable) rootLayout.getBackground().mutate();
                drawable.setStroke(2, Color.parseColor(pref.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff")));
            }

            KaFontUtils.applyCustomFont(convertView.getContext(), convertView);
        }
    }
}
