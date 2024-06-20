package com.kore.ai.widgetsdk.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.events.KaMessengerUpdate;
import com.kore.ai.widgetsdk.interfaces.PanelInterface;
import com.kore.ai.widgetsdk.models.PanelBaseModel;
import com.kore.ai.widgetsdk.models.PanelResponseData;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utility;
import com.squareup.picasso.Picasso;

public final class PannelAdapter extends RecyclerView.Adapter<PannelAdapter.RViewHolder> {

    private final Context context;
    final PanelResponseData panelResponseData;
    final PanelInterface panelInterface;
    private KaMessengerUpdate _msgUpdate;
    private final int dp1;

    public boolean isExpanded() {
        return isExpanded;
    }

    private final boolean isExpanded = false;
    private final boolean isScrolling = false;
    String bgColor;

    public PannelAdapter(Context mainActivity, PanelResponseData panelResponseData, PanelInterface panelInterface) {
        context = mainActivity;
        //this.panelResponseData = panelResponseData;
        this.panelResponseData = panelResponseData;
        this.panelInterface = panelInterface;
        dp1 = (int) Utility.convertDpToPixel(context, 1);
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.panel_adapter_new, parent, false);
        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RViewHolder holder, int position) {
        final PanelResponseData.Panel data = panelResponseData.getPanels().get(position);

        if (data != null && data.getIcon() != null && data.getIcon().equalsIgnoreCase("url")) {
            holder.img_skill.setVisibility(View.GONE);
            holder.img_icon.setVisibility(View.VISIBLE);

            holder.item.setBackgroundColor(Color.parseColor(data.getTheme()));
            holder.item.setSelected(data.isItemClicked());
        } else {
            holder.img_icon.setVisibility(View.GONE);

            if (data != null && data.getIcon() != null) {

                try {
                    holder.img_skill.setVisibility(VISIBLE);
                    String imageData;
                    imageData = data.getIcon();
                    if (imageData.contains(",")) {
                        imageData = imageData.substring(imageData.indexOf(",") + 1);
                        byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.img_skill.setImageBitmap(decodedByte);
                    } else {
                        Picasso.get().load(imageData).into(holder.img_skill);
                    }
                } catch (Exception e) {
                    holder.img_skill.setVisibility(GONE);

                }
            } else {
                holder.img_skill.setVisibility(GONE);
            }

            if (data != null && data.isItemClicked()) {
                holder.img_skill.setPadding(3 * dp1, 3 * dp1, 3 * dp1, 3 * dp1);
                holder.item.setBackgroundColor(Color.parseColor(data.getTheme()));

                if (!StringUtils.isNullOrEmpty(bgColor))
                    holder.item.setBackgroundColor(Color.parseColor(bgColor));

                holder.item.setSelected(data.isItemClicked());

            } else {
                holder.item.setBackground(null);
                holder.img_skill.setPadding(0, 0, 0, 0);
            }
        }

        if (data != null) holder.txtTitle.setText(data.getName());


        holder.txtTitle.post(new Runnable() {
            @Override
            public void run() {
                holder.txtTitle.setVisibility(View.VISIBLE);
            }
        });
        if (isScrolling) {
            holder.item.animate().scaleX(1.12f).scaleY(1.12f).setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            holder.item.animate().scaleX(1f).scaleY(1f).setInterpolator(new AccelerateDecelerateInterpolator());
        }
        if (data != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.setTooltipText(data.getName());
        }
        holder.img_icon.setTypeface(KaUtility.getTypeFaceObj(context));
        holder.unreadIcon.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    String tempIconId = data.get_id();
                    for (int index = 0; index < panelResponseData.getPanels().size(); index++) {
                        panelResponseData.getPanels().get(index).setItemClicked(tempIconId.equals(panelResponseData.getPanels().get(index).get_id()) && !tempIconId.equalsIgnoreCase(""));
                    }
                    PanelBaseModel panelBaseModel = new PanelBaseModel();
                    panelBaseModel.setData(data);
                    if (panelInterface != null) {
                        panelInterface.onPanelClicked(panelBaseModel);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    public int getItemCount() {
        if (panelResponseData != null && panelResponseData.getPanels() != null) {
            return panelResponseData.getPanels().size();
        }
        return 0;
    }

    static class RViewHolder extends RecyclerView.ViewHolder {
        final TextView img_icon;
        final TextView txtTitle;
        final ImageView img_skill;
        final ViewGroup item;
        final ImageView unreadIcon;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            img_skill = itemView.findViewById(R.id.img_skill);
            item = itemView.findViewById(R.id.panel_root);
            unreadIcon = itemView.findViewById(R.id.unreadImg);
        }
    }
}
