package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotTableListRowItemsModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class BotTableListInnerAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<BotTableListRowItemsModel> botTableListRowItemsModels;
    String LOG_TAG = BotListTemplateAdapter.class.getSimpleName();
    LayoutInflater ownLayoutInflator;
    ListView parentListView;
    RoundedCornersTransform roundedCornersTransform;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int dp1;

    protected BotTableListInnerAdapter(Context context, ArrayList<BotTableListRowItemsModel> botTableListRowItemsModels)
    {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.botTableListRowItemsModels = botTableListRowItemsModels;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
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
            convertView = ownLayoutInflator.inflate(R.layout.bot_table_list_inner_cell, null);
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

        holder.bot_list_item_cost.setTextColor(context.getResources().getColor(R.color.black));

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

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.botListItemRoot = (LinearLayout) view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = (ImageView) view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = (TextView) view.findViewById(R.id.bot_list_item_subtitle);
        holder.bot_list_item_cost = (TextView) view.findViewById(R.id.bot_list_item_cost);
        view.setTag(holder);
    }

    private static class ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;
    }
}
