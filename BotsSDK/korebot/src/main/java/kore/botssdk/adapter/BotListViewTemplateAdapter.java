package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponsePayLoadText.THEME_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewUtils.RoundedCornersTransform;

@SuppressLint("UnknownNullness")
public class BotListViewTemplateAdapter extends BaseAdapter {
    private ArrayList<BotListModel> botListModelArrayList = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final Context context;
    private final RoundedCornersTransform roundedCornersTransform;
    final ListView parentListView;
    private final int count;
    private final BottomSheetDialog bottomSheetDialog;

    public BotListViewTemplateAdapter(Context context, ListView parentListView, int count, BottomSheetDialog bottomSheetDialog) {
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        this.count = count;
        this.bottomSheetDialog = bottomSheetDialog;
    }

    @Override
    public int getCount() {
        if (count > 0) {
            return count;
        } else {
            return botListModelArrayList.size();
        }
    }

    @Override
    public BotListModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botListModelArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bot_listview_template_item_layout, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotListModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        holder.bot_list_item_cost.setText(botListModel.getValue());

        if (botListModel.getColor() != null)
            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getColor()));

        holder.bot_list_item_cost.setTypeface(null, Typeface.BOLD);

        if (!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());
        }

        holder.botListItemRoot.setOnClickListener(v -> {
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                int position1 = parentListView.getPositionForView(v);
                BotListModel _botListModel = getItem(position1);
                if (_botListModel != null && _botListModel.getDefault_action() != null) {
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(_botListModel.getDefault_action().getUrl());
                    } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {

                        if (!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getPayload()))
                            composeFooterInterface.onSendClick(_botListModel.getDefault_action().getTitle(), _botListModel.getDefault_action().getPayload(), false);
                        else if (!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getTitle()))
                            composeFooterInterface.onSendClick(_botListModel.getDefault_action().getTitle(), false);
                    }
                }
            }
        });
    }

    public void setBotListModelArrayList(ArrayList<BotListModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
        holder.bot_list_item_cost = view.findViewById(R.id.bot_list_item_cost);

        SharedPreferences pref = holder.botListItemRoot.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
        GradientDrawable drawable = (GradientDrawable) holder.botListItemRoot.getBackground().mutate();
        drawable.setStroke(2, Color.parseColor(pref.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff")));

        view.setTag(holder);
    }

    static class ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;
    }
}