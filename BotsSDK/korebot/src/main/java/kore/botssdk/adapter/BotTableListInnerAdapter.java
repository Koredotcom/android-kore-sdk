package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotTableListRowItemsModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class BotTableListInnerAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<BotTableListRowItemsModel> botTableListRowItemsModels;
    final RoundedCornersTransform roundedCornersTransform;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final int dp1;

    protected BotTableListInnerAdapter(@NonNull Context context, @NonNull ArrayList<BotTableListRowItemsModel> botTableListRowItemsModels)
    {
        this.context = context;
        this.botTableListRowItemsModels = botTableListRowItemsModels;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.dp1 = (int) DimensionUtil.dp1;
    }

    @Override
    public int getCount()
    {
        return botTableListRowItemsModels.size();
    }

    @Override
    public BotTableListRowItemsModel getItem(int position)
    {
        return botTableListRowItemsModels.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bot_table_list_inner_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotTableListRowItemsModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if(botListModel.getTitle() != null && botListModel.getTitle().getImage() != null)
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getImage().getImage_src()))
            {
                holder.botListItemImage.setVisibility(View.VISIBLE);
                Picasso.get().load(botListModel.getTitle().getImage().getImage_src()).transform(roundedCornersTransform).into(holder.botListItemImage);

                if(botListModel.getTitle().getImage().getRadius() > 0)
                {
                    holder.botListItemImage.getLayoutParams().height = botListModel.getTitle().getImage().getRadius() * 2 * dp1;
                    holder.botListItemImage.getLayoutParams().width = botListModel.getTitle().getImage().getRadius() * 2 * dp1;
                }
            }
        }

        holder.botListItemTitle.setTag(botListModel);

        if(botListModel.getTitle().getText() != null)
        {
            holder.botListItemTitle.setText(botListModel.getTitle().getText().getTitle());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);

            if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getText().getSubtitle())) {
                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
                holder.botListItemSubtitle.setText(botListModel.getTitle().getText().getSubtitle());
            }
        }
        else if(botListModel.getTitle().getUrl() != null)
        {
            holder.botListItemTitle.setText(botListModel.getTitle().getUrl().getTitle());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);

            if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getUrl().getSubtitle())) {
                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
                holder.botListItemSubtitle.setText(botListModel.getTitle().getUrl().getSubtitle());
            }
        }

        holder.bot_list_item_cost.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.black, context.getTheme()));

        if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getRowColor()))
        {
            holder.botListItemTitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
            holder.botListItemSubtitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
        }

        if(botListModel.getValue().getType().equalsIgnoreCase("text"))
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getValue().getText())) {
                holder.bot_list_item_cost.setVisibility(View.VISIBLE);
                holder.bot_list_item_cost.setText(botListModel.getValue().getText());

                if(!StringUtils.isNullOrEmpty(botListModel.getValue().getLayout().getColor()))
                {
                    holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getValue().getLayout().getColor()));
                }
            }
        }
        else if (botListModel.getValue().getType().equalsIgnoreCase("url"))
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getValue().getUrl().getTitle())) {
                holder.bot_list_item_cost.setVisibility(View.VISIBLE);
                holder.bot_list_item_cost.setText(botListModel.getValue().getUrl().getTitle());
            }
        }

        holder.botListItemRoot.setBackgroundColor(0);

        if(!StringUtils.isNullOrEmpty(botListModel.getBgcolor())) {
            holder.botListItemRoot.setBackgroundColor(Color.parseColor(botListModel.getBgcolor()));
        }

    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.botListItemRoot = view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
        holder.bot_list_item_cost = view.findViewById(R.id.bot_list_item_cost);
        view.setTag(holder);
    }

    static class ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;
    }
}
