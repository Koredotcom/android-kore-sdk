package kore.botssdk.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.ListClickableListner;
import kore.botssdk.models.BotBeneficiaryModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class BotBeneficiaryTemplateAdapter extends BaseAdapter {
    private ArrayList<BotBeneficiaryModel> botListModelArrayList = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ListClickableListner listClickableListner;
    private final Context context;
    private final RoundedCornersTransform roundedCornersTransform;
    private final ListView parentListView;
    private int count = 0;
    private final SharedPreferences sharedPreferences;
    private boolean isClickable = true;

    public BotBeneficiaryTemplateAdapter(Context context, ListView parentListView, int count) {
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        this.count = count;
        this.sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
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
    public BotBeneficiaryModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else
        {
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
            convertView = View.inflate( context, R.layout.beneficiary_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotBeneficiaryModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if(sharedPreferences != null)
        {
            GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_rect_feedback, context.getTheme());
            if(rightDrawable != null)
                rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));

            holder.botListItemTitle.setTextColor(Color.parseColor("#000000"));
        }

        if(!StringUtils.isNullOrEmpty(botListModel.getIcon()))
        {
            try
            {
                holder.botListItemImage.setVisibility(View.VISIBLE);
                String imageData;
                imageData = botListModel.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.botListItemImage.setImageBitmap(decodedByte);
                }
            } catch (Exception e) {
                holder.botListItemImage.setVisibility(GONE);
            }
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getDescription());
        holder.botListItemSubtitle.setText(botListModel.getTitle());
    }

    public void setBotListModelArrayList(ArrayList<BotBeneficiaryModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setListClickableInterface(ListClickableListner listClickableInterface) {
        this.listClickableListner = listClickableInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemImage = view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);

        view.setTag(holder);
    }

    public void setListClickable(boolean b)
    {
        this.isClickable = b;
    }

    private static class ViewHolder {
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle;
    }
}
