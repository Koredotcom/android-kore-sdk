package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotOptionModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BottomOptionsAdapter extends BaseAdapter {

    String LOG_TAG = BottomOptionsAdapter.class.getSimpleName();
    List<BotOptionModel> botOptionModels = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    LayoutInflater ownLayoutInflator;
    Context context;
    RoundedCornersTransform roundedCornersTransform;
    ListView parentListView;

    public BottomOptionsAdapter(Context context, ListView parentListView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        if (botOptionModels != null) {
            return botOptionModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public BotOptionModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botOptionModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ownLayoutInflator.inflate(R.layout.bottom_options_item, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotOptionModel botListModel = getItem(position);
        holder.bottom_option_image.setVisibility(View.GONE);

        if(!StringUtils.isNullOrEmpty(botListModel.getIcon()))
        {
            try
            {
                holder.bottom_option_image.setVisibility(VISIBLE);
                String imageData;
                imageData = botListModel.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.bottom_option_image.setImageBitmap(decodedByte);
                }
            } catch (Exception e) {
                holder.bottom_option_image.setVisibility(GONE);
            }

//            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.bottom_option_name.setText(botListModel.getTitle());
        holder.bottom_option_name.setTypeface(null, Typeface.BOLD);
    }

    public void setBotListModelArrayList(List<BotOptionModel> botOptionModels) {
        this.botOptionModels = botOptionModels;
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
        holder.bottom_option_image = (ImageView) view.findViewById(R.id.bottom_option_image);
        holder.bottom_option_name = (TextView) view.findViewById(R.id.bottom_option_name);
        holder.bot_list_item_root = (LinearLayout) view.findViewById(R.id.bot_list_item_root);
        view.setTag(holder);
    }



    private static class ViewHolder {
        ImageView bottom_option_image;
        TextView bottom_option_name;
        LinearLayout bot_list_item_root;
    }
}
