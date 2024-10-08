package kore.botssdk.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.AdvanceListTableModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewUtils.RoundedCornersTransform;

public class CardTemplateListAdapter extends RecyclerView.Adapter<CardTemplateListAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons;

    public CardTemplateListAdapter(@NonNull Context context, @NonNull ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardTemplateListAdapter.ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CardTemplateListAdapter.ButtonViewHolder(inflater.inflate(R.layout.card_desc_list_cell, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardTemplateListAdapter.ButtonViewHolder holder, int i) {
        AdvanceListTableModel.AdvanceTableRowDataModel btn = buttons.get(i);
        holder.tvBtnText.setText(btn.getTitle());

        holder.tvDescriptionTitle.setVisibility(GONE);
        holder.ivListBtnIcon.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(btn.getDescription())) {
            holder.tvDescriptionTitle.setVisibility(View.VISIBLE);
            holder.tvDescriptionTitle.setText(btn.getTitle());
            holder.tvBtnText.setText(btn.getDescription());
        }

        if(!StringUtils.isNullOrEmpty(btn.getIcon()))
        {
            holder.ivListBtnIcon.setVisibility(View.VISIBLE);
            try {
                String imageData;
                imageData = btn.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.ivListBtnIcon.setImageBitmap(decodedByte);
                }
                else
                {
                    Picasso.get().load(btn.getIcon()).transform(new RoundedCornersTransform()).into(holder.ivListBtnIcon);
                }
            }
            catch (Exception ex)
            {
                holder.ivListBtnIcon.setVisibility(GONE);
            }
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
        final TextView tvBtnText;
        final TextView tvDescriptionTitle;
        final ImageView ivListBtnIcon;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBtnText = itemView.findViewById(R.id.tvDescription);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
            ivListBtnIcon = itemView.findViewById(R.id.ivListBtnIcon);
        }
    }
}
