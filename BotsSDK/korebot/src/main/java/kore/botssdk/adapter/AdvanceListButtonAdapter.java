package kore.botssdk.adapter;

import static android.view.View.GONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AdvanceListRefreshEvent;
import kore.botssdk.listener.AdvanceButtonClickListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.Widget;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class AdvanceListButtonAdapter extends RecyclerView.Adapter<AdvanceListButtonAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Widget.Button> buttons;
    private final Context mContext;
    private String skillName;
    private final String type;
    private final AdvanceButtonClickListner advanceButtonClickListner;
    private AdvanceOptionsAdapter advanceOptionsAdapter;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private PopupWindow popupWindow;
    private int count;

    public AdvanceListButtonAdapter(Context context, ArrayList<Widget.Button> buttons, String type, AdvanceButtonClickListner advanceButtonClickListner, ComposeFooterInterface composeFooterInterface, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.type = type;
        this.advanceButtonClickListner = advanceButtonClickListner;
        this.composeFooterInterface = composeFooterInterface;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public AdvanceListButtonAdapter.ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase(BundleConstants.FULL_WIDTH)) {
            return new ButtonViewHolder(inflater.inflate(R.layout.advance_button_fullwidth, viewGroup, false));
        }
        return new ButtonViewHolder(inflater.inflate(R.layout.widget_button_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdvanceListButtonAdapter.ButtonViewHolder holder, int i) {

        Widget.Button btn = buttons.get(i);
        holder.tv.setText(btn.getTitle());
        holder.ivBtnImage.setVisibility(GONE);

        if(!StringUtils.isNullOrEmpty(btn.getIcon())) {
            holder.ivBtnImage.setVisibility(View.VISIBLE);
            try {
                String imageData;
                imageData = btn.getIcon();
                if (imageData.contains(",")) {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.ivBtnImage.setImageBitmap(decodedByte);
                } else {
                    Picasso.get().load(btn.getIcon()).transform(new RoundedCornersTransform()).into(holder.ivBtnImage);
                }
            } catch (Exception e) {
                holder.ivBtnImage.setVisibility(GONE);
            }
        }

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!StringUtils.isNullOrEmpty(btn.getBtnType()))
                {
                    if(btn.getBtnType().equalsIgnoreCase("confirm"))
                    {
                        if(advanceOptionsAdapter != null)
                            advanceButtonClickListner.advanceButtonClick(advanceOptionsAdapter.getData());
                    }
                    else
                    {
                        if(advanceOptionsAdapter != null)
                            advanceOptionsAdapter.setChecked(-1);
                    }
                }
                else
                {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        if(BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(btn.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(btn.getUrl());
                        }else{
                            String title = btn.getTitle();
                            String payload = btn.getPayload();
                            composeFooterInterface.onSendClick(title, payload,false);
                        }
                    }
                }

                if(advanceButtonClickListner != null)
                    advanceButtonClickListner.closeWindow();
            }
        });

        holder.layout_deails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!StringUtils.isNullOrEmpty(btn.getBtnType()))
                {
                    if(btn.getBtnType().equalsIgnoreCase("confirm"))
                    {
                        if(advanceOptionsAdapter != null)
                            advanceButtonClickListner.advanceButtonClick(advanceOptionsAdapter.getData());
                    }
                    else
                    {
                        if(advanceOptionsAdapter != null)
                            advanceOptionsAdapter.setChecked(-1);
                    }
                }
                else
                {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        if(BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(btn.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(btn.getUrl());
                        }else{
                            String title = btn.getTitle();
                            String payload = btn.getPayload();
                            composeFooterInterface.onSendClick(title, payload,false);
                        }
                    }
                }

                if(advanceButtonClickListner != null)
                    advanceButtonClickListner.closeWindow();
            }
        });
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public int getItemCount() {
        if(count != 0)
            return count;

        return buttons != null ? buttons.size() : 0;
    }

    public void setDisplayLimit(int count)
    {
        this.count = count;
    }
    boolean isFullView;

    public void setIsFromFullView(boolean isFullView) {
        this.isFullView = isFullView;
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        private final ImageView ivBtnImage;
        private final LinearLayout layout_deails;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.buttonTV);
            ivBtnImage = itemView.findViewById(R.id.ivBtnImage);
            layout_deails = itemView.findViewById(R.id.layout_deails);
        }
    }

    public void setListAdapter(AdvanceOptionsAdapter listAdapter) {
        this.advanceOptionsAdapter = listAdapter;
    }

    public void buttonAction(String utterance, boolean appendUtterance) {
        if (utterance != null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))) {
            if (utterance.startsWith("tel:")) {
                launchDialer(mContext, utterance);
            } else if (utterance.startsWith("mailto:")) {
                showEmailIntent((Activity) mContext, utterance.split(":")[1]);
            }
            return;
        }
        AdvanceListRefreshEvent event = new AdvanceListRefreshEvent();
        event.setPayLoad(utterance);
        KoreEventCenter.post(event);
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void showEmailIntent(Activity activity, String recepientEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recepientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");

        try {
            activity.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Error while launching email intent!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasPermission(Context context, String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        for (String s : permission) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                    ActivityCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED;
        }
        return shouldShowRequestPermissionRationale;
    }
}
