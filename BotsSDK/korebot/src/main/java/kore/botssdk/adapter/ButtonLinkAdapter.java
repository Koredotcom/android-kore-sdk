package kore.botssdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RadioListListner;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

@SuppressLint("UnknownNullness")
public class ButtonLinkAdapter extends RecyclerView.Adapter<ButtonLinkAdapter.DeepLinkViewHolder>
{
    private final Context context;
    private final ArrayList<BotButtonModel> arrPdfDownloadModels;
    final InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final int dp1;
    int checkedPosition = -1;
    final RadioListListner radioListListner;
    final ComposeFooterInterface composeFooterInterface;
    private String quickWidgetColor;
    private String fillColor;

    public ButtonLinkAdapter(Context context, ArrayList<BotButtonModel> arrPdfDownloadModels, InvokeGenericWebViewInterface invokeGenericWebViewInterface, int checkedPosition, ComposeFooterInterface composeFooterInterface, RadioListListner radioListListner)
    {
        this.context = context;
        this.arrPdfDownloadModels = arrPdfDownloadModels;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        this.checkedPosition = checkedPosition;
        this.radioListListner = radioListListner;
        this.composeFooterInterface = composeFooterInterface;
        this.dp1= (int) DimensionUtil.dp1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        quickWidgetColor = SDKConfiguration.BubbleColors.quickReplyTextColor;
        fillColor = SDKConfiguration.BubbleColors.quickReplyColor;

        fillColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, fillColor);
        quickWidgetColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, quickWidgetColor);
    }

    @Override
    public int getItemCount() {

        if (arrPdfDownloadModels == null) {
            return 0;
        } else {
            return arrPdfDownloadModels.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public DeepLinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.button_link_template_cell, null);
        DeepLinkViewHolder viewHolder = new DeepLinkViewHolder(convertView);

        GradientDrawable gradientDrawable = (GradientDrawable)convertView.findViewById(R.id.quick_reply_view).getBackground();
        gradientDrawable.setStroke(2 * dp1, Color.parseColor(fillColor));
        gradientDrawable.setColor(Color.parseColor(fillColor));
        gradientDrawable.setSize(ViewGroup.LayoutParams.WRAP_CONTENT, (dp1 > 2 ? 40 : 53) * dp1);

        viewHolder.tvButtonTitle.setTextColor(Color.parseColor(quickWidgetColor));
        DrawableCompat.setTint(viewHolder.ivLinkForward.getDrawable(), Color.parseColor(quickWidgetColor));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeepLinkViewHolder holder, int position)
    {
        populateVIew(holder, position);
    }

    private void populateVIew(DeepLinkViewHolder holder, int position)
    {
        BotButtonModel pdfDownloadModel = arrPdfDownloadModels.get(position);
        holder.tvButtonTitle.setText(pdfDownloadModel.getTitle());

        holder.ivLinkForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StringUtils.isNullOrEmpty(pdfDownloadModel.getUrl()) && checkedPosition == -1)
                {
                    if(radioListListner != null)
                        radioListListner.radioItemClicked(position);

                    if(pdfDownloadModel.isSamePageNavigation())
                        composeFooterInterface.onDeepLinkClicked(pdfDownloadModel.getUrl());
                    else
                        invokeGenericWebViewInterface.invokeGenericWebView(pdfDownloadModel.getUrl());
                }
            }
        });

        holder.tvButtonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StringUtils.isNullOrEmpty(pdfDownloadModel.getUrl()) && checkedPosition == -1)
                {
                    if(radioListListner != null)
                        radioListListner.radioItemClicked(position);

                    if(pdfDownloadModel.isSamePageNavigation())
                        composeFooterInterface.onDeepLinkClicked(pdfDownloadModel.getUrl());
                    else
                        invokeGenericWebViewInterface.invokeGenericWebView(pdfDownloadModel.getUrl());
                }
            }
        });

        holder.llButtonDeepLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StringUtils.isNullOrEmpty(pdfDownloadModel.getUrl()) && checkedPosition == -1)
                {
                    if(radioListListner != null)
                        radioListListner.radioItemClicked(position);

                    if(pdfDownloadModel.isSamePageNavigation())
                        composeFooterInterface.onDeepLinkClicked(pdfDownloadModel.getUrl());
                    else
                        invokeGenericWebViewInterface.invokeGenericWebView(pdfDownloadModel.getUrl());
                }
            }
        });
    }

    static class DeepLinkViewHolder extends RecyclerView.ViewHolder{
        final ImageView ivLinkForward;
        final TextView tvButtonTitle;
        final LinearLayout llButtonDeepLink;

        public DeepLinkViewHolder(View view) {
            super(view);
            ivLinkForward = view.findViewById(R.id.ivLinkForward);
            tvButtonTitle = view.findViewById(R.id.tvButtonTitle);
            llButtonDeepLink = view.findViewById(R.id.llButtonDeepLink);
        }
    }
}
