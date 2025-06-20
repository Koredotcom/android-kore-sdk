package kore.botssdk.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.AdvanceListTableModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class AdvanceTableListAdapter extends RecyclerView.Adapter<AdvanceTableListAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons;

    public AdvanceTableListAdapter(@NonNull Context context, @NonNull ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ButtonViewHolder(inflater.inflate(R.layout.advance_table_list_cell, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int i) {

        AdvanceListTableModel.AdvanceTableRowDataModel btn = buttons.get(i);
        holder.botListItemTitle.setText(btn.getTitle());

        if (!StringUtils.isNullOrEmpty(btn.getIcon()))
        {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
            layoutParams.setMargins(0, 0, 10, 0);

            if(!StringUtils.isNullOrEmpty(btn.getIconSize()))
            {
                if(btn.getIconSize().equalsIgnoreCase("large"))
                {
                    layoutParams.height = 180;
                    layoutParams.width = 180;
                }
                else if(btn.getIconSize().equalsIgnoreCase("small"))
                {
                    layoutParams.height = 60;
                    layoutParams.width = 60;
                }
            }
            holder.botListItemImage.setLayoutParams(layoutParams);

            try {
                String imageData;
                imageData = btn.getIcon();
                if (imageData.contains(",")) {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.botListItemImage.setImageBitmap(decodedByte);
                } else {
                    Picasso.get().load(btn.getIcon()).transform(new RoundedCornersTransform()).into(holder.botListItemImage);
                }
            } catch (Exception e) {
                holder.botListItemImage.setVisibility(GONE);
            }
        }

        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if (!StringUtils.isNullOrEmpty(btn.getDescription())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(btn.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return buttons != null ? buttons.size() : 0;
    }

    boolean isFullView;

    public void setIsFromFullView(boolean isFullView) {
        this.isFullView = isFullView;
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        final TextView botListItemTitle;
        final TextView botListItemSubtitle;
        final ImageView botListItemImage;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            botListItemImage = itemView.findViewById(R.id.bot_list_item_image);
        }
    }
}
