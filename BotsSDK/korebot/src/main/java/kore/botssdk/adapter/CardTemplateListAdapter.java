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

import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceButtonClickListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AdvanceListTableModel;

public class CardTemplateListAdapter extends RecyclerView.Adapter<CardTemplateListAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons;
    private final Context mContext;
    private AdvanceOptionsAdapter advanceOptionsAdapter;
    private final ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public CardTemplateListAdapter(Context context, ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> buttons, ComposeFooterInterface composeFooterInterface, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.composeFooterInterface = composeFooterInterface;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
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

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
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
        private final TextView tvBtnText;
        private final TextView tvDescriptionTitle;
        private final ImageView ivListBtnIcon;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBtnText = itemView.findViewById(R.id.tvDescription);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
            ivListBtnIcon = itemView.findViewById(R.id.ivListBtnIcon);
        }
    }


    public void setListAdapter(AdvanceOptionsAdapter listAdapter) {
        this.advanceOptionsAdapter = listAdapter;
    }
}
