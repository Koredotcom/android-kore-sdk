package kore.botssdk.adapter;

import android.content.Intent;
import android.net.Uri;
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
import kore.botssdk.models.ContactTemplateModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.CircleTransform;

public class ContactCardItemAdapter extends RecyclerView.Adapter<ContactCardItemAdapter.ViewHolder> {
    private final ArrayList<ContactTemplateModel> models;
    final CircleTransform roundedCornersTransform = new CircleTransform();

    public ContactCardItemAdapter(ArrayList<ContactTemplateModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.contact_card_template_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactTemplateModel botListModel = getItem(position);
        if (botListModel == null) return;
        if (!StringUtils.isNullOrEmpty(botListModel.getUserIcon())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load("https://hs.sbcounty.gov/cn/Photo%20Gallery/_w/Sample%20Picture%20-%20Koala_jpg.jpg").transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getUserName());
        if (!StringUtils.isNullOrEmpty(botListModel.getUserContactNumber())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getUserContactNumber());

            holder.botListItemSubtitle.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + botListModel.getUserContactNumber()));
                    holder.botListItemSubtitle.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (!StringUtils.isNullOrEmpty(botListModel.getUserEmailId())) {
            holder.bot_list_item_email.setVisibility(View.VISIBLE);
            holder.bot_list_item_email.setText(botListModel.getUserEmailId());
        }
    }

    private ContactTemplateModel getItem(int position) {
        return models != null ? models.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            botListItemRoot = itemView.findViewById(R.id.bot_list_item_root);
            botListItemImage = itemView.findViewById(R.id.bot_list_item_image);
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            bot_list_item_email = itemView.findViewById(R.id.bot_list_item_email);
        }
    }
}
