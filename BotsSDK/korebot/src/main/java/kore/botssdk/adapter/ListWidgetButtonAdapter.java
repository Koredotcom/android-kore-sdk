package kore.botssdk.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.Widget;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.viewholders.BaseViewHolder;

public class ListWidgetButtonAdapter extends RecyclerView.Adapter<ListWidgetButtonAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Widget.Button> buttons;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private String skillName;
    private BottomSheetDialog bottomSheetDialog;
    private PopupWindow popupWindow;
    int displayLimit = -1;
    private boolean isEnabled = true;
    private int type;
    private boolean isMenus = false;

    public ListWidgetButtonAdapter(Context context, ArrayList<Widget.Button> buttons, boolean isMenus) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        this.isMenus = isMenus;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (type == 1) {
            return new ButtonViewHolder(inflater.inflate(R.layout.advance_button_fullwidth, viewGroup, false));
        }
        return new ButtonViewHolder(inflater.inflate(R.layout.widget_button_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int i) {
        Widget.Button btn = buttons.get(i);
        holder.tv.setText(btn.getTitle());
        holder.tv.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        Drawable errorDrawable = BaseViewHolder.getTintDrawable(holder.tv.getContext(), SDKConfiguration.BubbleColors.quickReplyColor, R.drawable.check_icon);
        holder.image.setVisibility(isMenus ? GONE : VISIBLE);
        if (!isMenus) {
            if (btn.getImage() != null && btn.getImage().getImage_type().equals(BotResponse.COMPONENT_TYPE_IMAGE) && !btn.getImage().getImage_src().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(btn.getImage().getUrl() != null ? btn.getImage().getUrl() : btn.getImage().getImage_src())
                        .error(errorDrawable)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(holder.image);
            } else {
                holder.image.setImageDrawable(errorDrawable);
            }
        }

        ((ViewGroup) holder.tv.getParent()).setOnClickListener(v -> {
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
            if (popupWindow != null && popupWindow.isShowing()) popupWindow.dismiss();

            if (invokeGenericWebViewInterface != null) {
                if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(btn.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(btn.getUrl());
                    return;
                } else if (BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(btn.getType())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(btn.getUrl());
                    return;
                }
            }
            if (composeFooterInterface != null && isEnabled) {
                composeFooterInterface.onSendClick(btn.getTitle(), btn.getPayload(), false);
            }
        });
    }

    public void setDisplayLimit(int displayLimit) {
        this.displayLimit = displayLimit;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setBottomSheet(BottomSheetDialog bottomSheetDialog) {
        this.bottomSheetDialog = bottomSheetDialog;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    @Override
    public int getItemCount() {
        if (buttons != null) {
            return (displayLimit > -1 && buttons.size() > displayLimit) ? displayLimit : buttons.size();
        }

        return 0;
    }

    boolean isFullView;

    public void setIsFromFullView(boolean isFullView) {
        this.isFullView = isFullView;
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        final TextView tv;
        final ImageView image;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.buttonTV);
            image = itemView.findViewById(R.id.ivBtnImage);
        }
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public static void showEmailIntent(Activity activity, String recipientEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recipientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");

        try {
            activity.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Error while launching email intent!", Toast.LENGTH_SHORT).show();
        }
    }
}

