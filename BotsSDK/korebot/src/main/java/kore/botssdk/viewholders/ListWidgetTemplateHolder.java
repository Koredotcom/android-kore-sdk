package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.ListWidgetAdapter;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.HeaderOptionsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ListWidgetTemplateHolder extends BaseViewHolder {
    private final RecyclerView botCustomListView;
    private ListWidgetAdapter listWidgetAdapter = null;
//    private final TextView botCustomListViewButton;
    private PayloadInner payloadInner;
    private final ImageView iconImageLoad;
    private final TextView tvText;
    private final TextView tvUrl;
    private final TextView tvButton;
    private final LinearLayout tvButtonParent;
    private final ImageView imgMenu;
    private final TextView widgetHeader;
    private final TextView meetingDesc;
    private final SharedPreferences sharedPreferences;

    public static ListWidgetTemplateHolder getInstance(ViewGroup parent) {
        return new ListWidgetTemplateHolder(createView(R.layout.template_list_widget, parent));
    }

    private ListWidgetTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
        sharedPreferences = itemView.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        botCustomListView = itemView.findViewById(R.id.botCustomListView);
        botCustomListView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//        botCustomListViewButton = itemView.findViewById(R.id.botCustomListViewButton);
        iconImageLoad = itemView.findViewById(R.id.icon_image_load);
        tvButton = itemView.findViewById(R.id.tv_button);
        tvText = itemView.findViewById(R.id.tv_text);
        tvUrl = itemView.findViewById(R.id.tv_url);
        tvButtonParent = itemView.findViewById(R.id.tv_values_layout);
        imgMenu = itemView.findViewById(R.id.icon_image);
        widgetHeader = itemView.findViewById(R.id.meeting_header);
        meetingDesc = itemView.findViewById(R.id.meeting_desc);

//        botCustomListViewButton.setOnClickListener(view -> {
//            ListWidgetActionSheetFragment bottomSheetDialog = new ListWidgetActionSheetFragment();
//            bottomSheetDialog.setisFromFullView(false);
//            bottomSheetDialog.setSkillName("skillName", "trigger");
//
//            if (!StringUtils.isNullOrEmpty(payloadInner.getTitle()))
//                bottomSheetDialog.setData(payloadInner.getTitle(), payloadInner.getWidgetlistElements());
//            else
//                bottomSheetDialog.setData(payloadInner.getWidgetlistElements(), true);
//
//            bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
//            bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            bottomSheetDialog.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "add_tags");
//        });

        listWidgetAdapter = new ListWidgetAdapter(itemView.getContext(), "");
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());
        if (!StringUtils.isNullOrEmpty(payloadInner.getTitle())) {
            widgetHeader.setVisibility(VISIBLE);
            widgetHeader.setText(payloadInner.getTitle());

//            if (sharedPreferences != null)
//                widgetHeader.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
        }

        if (!StringUtils.isNullOrEmpty(payloadInner.getDescription())) {
            meetingDesc.setVisibility(VISIBLE);
            meetingDesc.setText(payloadInner.getDescription());

//            if (sharedPreferences != null)
//                meetingDesc.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
        }

        if (payloadInner.getHeaderOptions() != null && payloadInner.getHeaderOptions() instanceof HeaderOptionsModel headerOptionsModel && ((HeaderOptionsModel) payloadInner.getHeaderOptions()).getType() != null) {
            switch (headerOptionsModel.getType()) {
                case "button":
                    iconImageLoad.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    tvButtonParent.setVisibility(VISIBLE);
                    String btnTitle = "";
                    if (headerOptionsModel.getButton() != null && headerOptionsModel.getButton().getTitle() != null)
                        btnTitle = headerOptionsModel.getButton().getTitle();
                    else
                        btnTitle = headerOptionsModel.getText();
                    if (!StringUtils.isNullOrEmpty(btnTitle))
                        tvButton.setText(btnTitle);
                    else
                        tvButtonParent.setVisibility(GONE);

                    tvButton.setOnClickListener(v -> {
                        if (payloadInner.getHeaderOptions() != null && headerOptionsModel.getButton() != null && headerOptionsModel.getButton().getPayload() != null) {
                            buttonAction(headerOptionsModel.getButton().getPayload());
                        }
                    });

                    break;
                case "menu":
                    iconImageLoad.setVisibility(GONE);
                    imgMenu.setVisibility(VISIBLE);
                    tvText.setVisibility(GONE);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);

                    imgMenu.setOnClickListener(v -> {
                        if (payloadInner.getHeaderOptions() != null && headerOptionsModel.getMenu() != null && !headerOptionsModel.getMenu().isEmpty()) {
                            WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                            bottomSheetDialog.setIsFromFullView(false);
                            bottomSheetDialog.setSkillName("skillName", "trigger");
                            bottomSheetDialog.setData(payloadInner, true);
                            bottomSheetDialog.setVerticalListViewActionHelper(null);
                            bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                            bottomSheetDialog.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "add_tags");
                        }
                    });


                    break;
                case "text":
                    iconImageLoad.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(VISIBLE);
                    tvText.setText(headerOptionsModel.getText());
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    break;
                case "url":
                    iconImageLoad.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    SpannableString content = new SpannableString(headerOptionsModel.getUrl().getTitle() != null ? headerOptionsModel.getUrl().getTitle() : headerOptionsModel.getUrl().getLink());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tvUrl.setText(content);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(VISIBLE);
                    tvUrl.setOnClickListener(v -> {
                        if (invokeGenericWebViewInterface != null && headerOptionsModel.getUrl().getLink() != null) {
                            invokeGenericWebViewInterface.invokeGenericWebView(headerOptionsModel.getUrl().getLink());
                        }
                    });
                    break;

                case "image":
                    iconImageLoad.setVisibility(VISIBLE);
                    if (headerOptionsModel.getImage() != null && headerOptionsModel.getImage().getImage_src() != null) {
                        Picasso.get().load(headerOptionsModel.getImage().getImage_src()).into(iconImageLoad);
                        iconImageLoad.setOnClickListener(v -> {
                            if (payloadInner.getHeaderOptions() != null && headerOptionsModel.getImage() != null && headerOptionsModel.getImage().getPayload() != null) {
                                buttonAction(headerOptionsModel.getImage().getPayload());
                            }
                        });
                    }
                    break;
            }
        }
        listWidgetAdapter.setIsEnabled(isLastItem());
        if (payloadInner.getWidgetlistElements() != null && !payloadInner.getWidgetlistElements().isEmpty() && !payloadInner.getTemplate_type().equals("loginURL")) {
//            if (payloadInner.getWidgetlistElements().size() > 3) {
//                botCustomListViewButton.setVisibility(View.VISIBLE);
//            }

            listWidgetAdapter.setWidgetData(payloadInner.getWidgetlistElements());
            listWidgetAdapter.setComposeFooterInterface(composeFooterInterface);
            listWidgetAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            listWidgetAdapter.setPreviewLength(payloadInner.getWidgetlistElements().size());
            botCustomListView.setAdapter(listWidgetAdapter);
        } else {
            listWidgetAdapter.setData(null);
            botCustomListView.setAdapter(listWidgetAdapter);
        }
    }

    public void showEmailIntent(Activity activity, String recipientEmail) {
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

    public void buttonAction(String utt) {
        String utterance = null;
        utterance = utt;

        if (utterance == null) return;
        if (utterance.startsWith("tel:") || utterance.startsWith("mailto:")) {
            if (utterance.startsWith("tel:")) {
                launchDialer(itemView.getContext(), utterance);
            } else if (utterance.startsWith("mailto:")) {
                showEmailIntent((Activity) itemView.getContext(), utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuilder msg = new StringBuilder();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);
    }
}
