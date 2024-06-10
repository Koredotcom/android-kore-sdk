package com.kore.ai.widgetsdk.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.events.EntityEditEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.fragments.WidgetActionSheetFragment;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.utils.Constants;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
@SuppressLint("UnknownNullness")
public class ListWidgetButtonAdapter extends RecyclerView.Adapter<ListWidgetButtonAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    final ArrayList<Widget.Button> buttons;
    final Context mContext;
    String skillName;
    final String trigger;

    public ListWidgetButtonAdapter(Context context, ArrayList<Widget.Button> buttons, String trigger) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.trigger = trigger;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ButtonViewHolder(inflater.inflate(R.layout.list_btn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int i) {

        Widget.Button btn = buttons.get(i);

        holder.tvBtnText.setText(btn.getTitle());

        String utt = null;
        if (!StringUtils.isNullOrEmpty(btn.getPayload())) {
            utt = btn.getPayload();
        }
        if (!StringUtils.isNullOrEmpty(btn.getUtterance()) && utt == null) {
            utt = btn.getUtterance();
        }
        if (!StringUtils.isNullOrEmpty(btn.getUrl())) {
            utt = btn.getUrl();
        }

        if (holder.ivListBtnIcon != null && btn.getImage() != null && !StringUtils.isNullOrEmpty(btn.getImage().getImage_src())) {
            holder.ivListBtnIcon.setVisibility(View.VISIBLE);
            String url = btn.getImage().getImage_src().trim();
            url = url.replace("http://", "https://");
            Picasso.get().load(url).transform(new RoundedCornersTransform()).into(holder.ivListBtnIcon);
        }

        final String utterance = utt;

        holder.tvBtnText.setOnClickListener(v -> {
            if (!holder.tvBtnText.getText().equals("More...")) {
                buttonAction(utterance, TextUtils.isEmpty(Constants.SKILL_SELECTION) || !StringUtils.isNullOrEmpty(skillName) && !skillName.equalsIgnoreCase(Constants.SKILL_SELECTION));
            } else {
                WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                bottomSheetDialog.setisFromFullView(false);
                bottomSheetDialog.setSkillName(skillName, trigger);
                bottomSheetDialog.setData(buttons);
                bottomSheetDialog.setVerticalListViewActionHelper(null);
                bottomSheetDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "add_tags");
            }
        });
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
        public final TextView tvBtnText;
        public final ImageView ivListBtnIcon;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBtnText = itemView.findViewById(R.id.tvBtnText);
            ivListBtnIcon = itemView.findViewById(R.id.ivListBtnIcon);
        }
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
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if (appendUtterance && trigger != null) msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

        try {
            if (isFullView) {
                ((Activity) mContext).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @SuppressLint("MissingPermission")
    public static void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermission(Context context, String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        for (String s : permission) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale && ActivityCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED;
        }
        return shouldShowRequestPermissionRationale;
    }


}

