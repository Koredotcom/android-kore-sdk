package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
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

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kore.ai.widgetsdk.utils.AppUtils;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.activity.BrandingChangeActivity;
import kore.botssdk.dialogs.OptionsActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotOptionModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BottomOptionsCycleAdapter extends RecyclerView.Adapter<BottomOptionsCycleAdapter.ViewHolder>{
    private String LOG_TAG = OptionsActionSheetFragment.class.getSimpleName();
    private List<BotOptionModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private BottomSheetDialog bottomSheetDialog;
    private Context context;
    private SharedPreferences sharedPreferences;
    private float dp1;

    // RecyclerView recyclerView;
    public BottomOptionsCycleAdapter(List<BotOptionModel> model) {
        this.model = model;
        this.roundedCornersTransform = new RoundedCornersTransform();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bottom_options_item, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        dp1 = Utility.convertDpToPixel(context, 1);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BotOptionModel botListModel = model.get(position);
        holder.bottom_option_image.setVisibility(View.GONE);

        if(sharedPreferences != null)
        {
            GradientDrawable rightDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_rect_feedback);
            rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));
            rightDrawable.setStroke((int)(1 * dp1), Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, SDKConfiguration.BubbleColors.rightBubbleUnSelected)));
            holder.bot_list_item_root.setBackground(rightDrawable);
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

                if(botListModel.getTitle().equalsIgnoreCase("Branding Change"))
                {
                    Intent intent = new Intent(context, BrandingChangeActivity.class);
                    context.startActivity(intent);
                }
                else
                {
                    sendMessageText(model.get(position).getPostback().getTitle(), model.get(position).getPostback().getValue());
                }
            }
        });
    }

    public void setBotListModelArrayList(BottomSheetDialog bottomSheetDialog, List<BotOptionModel> botOptionModels) {
        this.model = botOptionModels;
        this.bottomSheetDialog = bottomSheetDialog;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void sendMessageText(String message, String payLoad) {
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
        ImageView bottom_option_image;
        TextView bottom_option_name;
        LinearLayout bot_list_item_root;

        public ViewHolder(View itemView) {
            super(itemView);
            this.bottom_option_image = (ImageView) itemView.findViewById(R.id.bottom_option_image);
            this.bottom_option_name = (TextView) itemView.findViewById(R.id.bottom_option_name);
            this.bot_list_item_root = (LinearLayout) itemView.findViewById(R.id.bot_list_item_root);
        }
    }
}
