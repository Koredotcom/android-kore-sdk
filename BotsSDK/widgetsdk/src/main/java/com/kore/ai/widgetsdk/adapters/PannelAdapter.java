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
import com.kore.ai.widgetsdk.utils.Utility;
import com.squareup.picasso.Picasso;

public final class PannelAdapter extends RecyclerView.Adapter<PannelAdapter.RViewHoldeer> {

    private Context context;
    private PanelResponseData panelResponseData;
    private PanelInterface panelInterface;
    private KaMessengerUpdate _msgUpdate;
    private int dp1;

    public boolean isExpanded() {
        return isExpanded;
    }

    private boolean isExpanded = false;
    private boolean isScrolling=false;

    public PannelAdapter(Context mainActivity, PanelResponseData panelResponseData, PanelInterface panelInterface) {
        context = mainActivity;
        //this.panelResponseData = panelResponseData;
        this.panelResponseData = panelResponseData;
        this.panelInterface = panelInterface;
        dp1 = (int) Utility.convertDpToPixel(context, 1);;
    }

    @NonNull
    @Override
    public RViewHoldeer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.panel_adapter_new, parent, false);
        return new RViewHoldeer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RViewHoldeer holder, int position) {
        final PanelResponseData.Panel data = panelResponseData.getPanels().get(position);

        if(data != null && data.getIcon() != null && data.getIcon().toLowerCase().equals("url")) {
            holder.img_skill.setVisibility(View.GONE);
            holder.img_icon.setVisibility(View.VISIBLE);

            holder.item.setBackgroundColor(Color.parseColor(data.getTheme()));
            holder.item.setSelected(data.isItemClicked() ? true : false);
        }else {
            holder.img_icon.setVisibility(View.GONE);

            if (data.getIcon() != null) {

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

            if(data.isItemClicked())
            {
                holder.img_skill.setPadding(3*dp1,3*dp1,3*dp1,3*dp1);
                holder.item.setBackgroundColor(Color.parseColor(data.getTheme()));
                holder.item.setSelected(data.isItemClicked() ? true : false);

            }
            else {
                holder.item.setBackground(null);
                holder.img_skill.setPadding(0,0,0,0);
            }
        }

        holder.txtTitle.setText(data.getName());


        holder.txtTitle.post(new Runnable() {
            @Override public void run() {
                holder.txtTitle.setVisibility(View.VISIBLE);
            } });
        if(isScrolling){
            holder.item.animate().scaleX(1.12f).scaleY(1.12f).setInterpolator(new AccelerateDecelerateInterpolator());
        }else{
            holder.item.animate().scaleX(1f).scaleY(1f).setInterpolator(new AccelerateDecelerateInterpolator());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.setTooltipText(data.getName());
        }
        holder.img_icon.setTypeface(KaUtility.getTypeFaceObj(context));
        holder.unreadIcon.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempiconID = data.get_id();
                for (int index = 0; index < panelResponseData.getPanels().size();index ++){
                    panelResponseData.getPanels().get(index).setItemClicked((tempiconID.equals(panelResponseData.getPanels().get(index).get_id()) &&
                            !tempiconID.equalsIgnoreCase("")) ? true : false);
                }
                PanelBaseModel panelBaseModel = new PanelBaseModel();
                panelBaseModel.setData(data);
                if(panelInterface != null){
                    panelInterface.onPanelClicked(panelBaseModel);
                }
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (panelResponseData != null && panelResponseData.getPanels() != null) {
            return panelResponseData.getPanels().size();
        }
        return 0;
    }

    class RViewHoldeer extends RecyclerView.ViewHolder {
        TextView img_icon,txtTitle;
        ImageView img_skill;
        ViewGroup item;
        ImageView unreadIcon;

        public RViewHoldeer(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            img_skill = itemView.findViewById(R.id.img_skill);
            item = itemView.findViewById(R.id.panel_root);
            unreadIcon = itemView.findViewById(R.id.unreadImg);
        }
    }
}
