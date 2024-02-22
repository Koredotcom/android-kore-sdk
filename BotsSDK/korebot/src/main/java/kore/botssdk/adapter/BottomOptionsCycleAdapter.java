package kore.botssdk.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.dialogs.OptionsActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotOptionModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

public class BottomOptionsCycleAdapter extends RecyclerView.Adapter<BottomOptionsCycleAdapter.ViewHolder>{
    private final String LOG_TAG = OptionsActionSheetFragment.class.getSimpleName();
    List<BotOptionModel> model;
    private ComposeFooterInterface composeFooterInterface;
    BottomSheetDialog bottomSheetDialog;
    private Context context;
    private SharedPreferences sharedPreferences;
    private float dp1;
    public BottomOptionsCycleAdapter(@NonNull List<BotOptionModel> model) {
        this.model = model;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bottom_options_item, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        dp1 = Utility.convertDpToPixel(context, 1);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotOptionModel botListModel = model.get(position);
        holder.bottom_option_image.setVisibility(View.GONE);

        if(sharedPreferences != null)
        {
            GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_rect_feedback, context.getTheme());
            if(rightDrawable != null)
            {
                rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));
                rightDrawable.setStroke((int)(1 * dp1), Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, SDKConfiguration.BubbleColors.rightBubbleUnSelected)));
                holder.bot_list_item_root.setBackground(rightDrawable);
            }

            holder.bottom_option_name.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
        }

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
        }

        holder.bottom_option_name.setText(botListModel.getTitle());
        holder.bottom_option_name.setTypeface(null, Typeface.BOLD);

        holder.bot_list_item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                sendMessageText(model.get(holder.getBindingAdapterPosition()).getPostback().getTitle(), model.get(holder.getBindingAdapterPosition()).getPostback().getValue());
            }
        });
    }

    public void setBotListModelArrayList(@NonNull BottomSheetDialog bottomSheetDialog, @NonNull List<BotOptionModel> botOptionModels) {
        this.model = botOptionModels;
        this.bottomSheetDialog = bottomSheetDialog;
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setContext(@NonNull Context context)
    {
        this.context = context;
    }

    void sendMessageText(String message, String payLoad) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), payLoad, false);
        } else {
            Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView bottom_option_image;
        final TextView bottom_option_name;
        final LinearLayout bot_list_item_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.bottom_option_image = itemView.findViewById(R.id.bottom_option_image);
            this.bottom_option_name = itemView.findViewById(R.id.bottom_option_name);
            this.bot_list_item_root = itemView.findViewById(R.id.bot_list_item_root);
        }
    }
}
