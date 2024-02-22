package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.ContactTemplateModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.CircleTransform;

public class BotContactTemplateAdapter extends BaseAdapter {
    ArrayList<ContactTemplateModel> botListModelArrayList = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    final Context context;
    final CircleTransform roundedCornersTransform;
    final ListView parentListView;

    public BotContactTemplateAdapter(@NonNull Context context, @NonNull ListView parentListView) {
        this.context = context;
        this.roundedCornersTransform = new CircleTransform();
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        if (botListModelArrayList != null) {
            return Math.min(botListModelArrayList.size(), 3);
        } else {
            return 0;
        }
    }

    @Override
    public ContactTemplateModel getItem(int position) {
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
            convertView = View.inflate(context, R.layout.contact_card_template_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        ContactTemplateModel botListModel = getItem(position);
        if(!StringUtils.isNullOrEmpty(botListModel.getUserIcon())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load("https://hs.sbcounty.gov/cn/Photo%20Gallery/_w/Sample%20Picture%20-%20Koala_jpg.jpg").transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getUserName());
        if(!StringUtils.isNullOrEmpty(botListModel.getUserContactNumber())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getUserContactNumber());

            holder.botListItemSubtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+botListModel.getUserContactNumber()));
                        context.startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        if(!StringUtils.isNullOrEmpty(botListModel.getUserEmailId()))
        {
            holder.bot_list_item_email.setVisibility(View.VISIBLE);
            holder.bot_list_item_email.setText(botListModel.getUserEmailId());
        }
    }

    public void setBotListModelArrayList(@NonNull ArrayList<ContactTemplateModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
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
        holder.bot_list_item_email = view.findViewById(R.id.bot_list_item_email);
        view.setTag(holder);
    }

    static class ViewHolder {
        RelativeLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_email;
    }
}