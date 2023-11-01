package kore.botssdk.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kore.ai.widgetsdk.utils.KaFontUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.Widget;

public class AdvanceListdetailsAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<Widget.Button> contentModels;
    private final LayoutInflater layoutInflater;

    protected AdvanceListdetailsAdapter(Context context, ArrayList<Widget.Button> contentModels)
    {
        this.context = context;
        this.contentModels = contentModels;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount()
    {
        return contentModels.size();
    }

    @Override
    public Object getItem(int i)
    {
        return contentModels.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        DetailsViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.listwidget_details_item, null);
            KaFontUtils.applyCustomFont(context, convertView);
            holder = new DetailsViewHolder();
            holder.tvBtnText = convertView.findViewById(com.kora.ai.widgetsdk.R.id.tvBtnText);
            holder.ivListBtnIcon = convertView.findViewById(com.kora.ai.widgetsdk.R.id.ivListBtnIcon);
            convertView.setTag(holder);
        } else {
            holder = (DetailsViewHolder) convertView.getTag();
        }

        populateData(holder, position);

        return convertView;
    }

    private void populateData(DetailsViewHolder holder, int position) {
        Widget.Button dataObj = (Widget.Button) getItem(position);
        holder.tvBtnText.setText(dataObj.getTitle());

        if(!StringUtils.isNullOrEmpty(dataObj.getIcon()))
        {
            holder.ivListBtnIcon.setVisibility(View.VISIBLE);
            try {
                String imageData;
                imageData = dataObj.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.ivListBtnIcon.setImageBitmap(decodedByte);
                }
                else
                {
                    Picasso.get().load(dataObj.getIcon()).transform(new RoundedCornersTransform()).into(holder.ivListBtnIcon);
                }
            }
            catch (Exception ex)
            {
                holder.ivListBtnIcon.setVisibility(GONE);
            }
        }
    }

    private static class DetailsViewHolder {
        private TextView tvBtnText;
        private ImageView ivListBtnIcon;

    }
}