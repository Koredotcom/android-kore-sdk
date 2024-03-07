package kore.botssdk.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.adapter.ListWidgetAdapter;
import kore.botssdk.dialogs.ListWidgetActionSheetFragment;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.HeaderOptionsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

@SuppressLint("UnknownNullness")
public class ListWidgetView extends LinearLayout {
    private float dp1;
    public RecyclerView botCustomListView;
    private ListWidgetAdapter listWidgetAdapter = null;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    ComposeFooterInterface composeFooterInterface;
    final Context context;
    private TextView botCustomListViewButton;
    private LinearLayout botCustomListRoot;
    PayloadInner model;
    private SharedPreferences sharedPreferences;
    private GradientDrawable rightDrawable;
    public ImageView icon_image_load;
    public TextView tvText;
    public TextView tvUrl;
    public TextView tvButton;
    public LinearLayout tvButtonParent;
    public ImageView imgMenu;
    public TextView  widget_header, meeting_desc;

    public ListWidgetView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bot_list_widget_template_view, this, true);
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_rect_feedback, context.getTheme());

        botCustomListView = view.findViewById(R.id.botCustomListView);
        botCustomListView.setLayoutManager(new LinearLayoutManager(getContext()));
        botCustomListRoot = view.findViewById(R.id.botCustomListRoot);
        botCustomListViewButton = view.findViewById(R.id.botCustomListViewButton);
        icon_image_load=view.findViewById(R.id.icon_image_load);
        tvButton = view.findViewById(R.id.tv_button);
        tvText = view.findViewById(R.id.tv_text);
        tvUrl = view.findViewById(R.id.tv_url);
        tvButtonParent = view.findViewById(R.id.tv_values_layout);
        imgMenu = view.findViewById(R.id.icon_image);
        widget_header = view.findViewById(R.id.meeting_header);
        meeting_desc = view.findViewById(R.id.meeting_desc);

        if(sharedPreferences != null)
        {
            rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#FFFFFF")));
        }

        botCustomListViewButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ListWidgetActionSheetFragment bottomSheetDialog = new ListWidgetActionSheetFragment();
                bottomSheetDialog.setisFromFullView(false);
                bottomSheetDialog.setSkillName("skillName","trigger");

                if(!StringUtils.isNullOrEmpty(model.getTitle()))
                    bottomSheetDialog.setData(model.getTitle(), model.getWidgetlistElements());
                else
                    bottomSheetDialog.setData(model.getWidgetlistElements(), true);

                bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
            }
        });

        dp1 = (int) Utility.convertDpToPixel(context, 1);
        listWidgetAdapter = new ListWidgetAdapter(getContext(), "");
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void buttonAction(String utt){
        String utterance = null;
        utterance = utt;

        if(utterance == null)return;
        if(utterance.startsWith("tel:") || utterance.startsWith("mailto:")){
            if(utterance.startsWith("tel:")){
                launchDialer(getContext(),utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) getContext(),utterance.split(":")[1]);
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

    public void populateListWidgetData(final PayloadInner model)
    {
        String themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);
        this.model = model;
        if(model != null)
        {
            if(!StringUtils.isNullOrEmpty(model.getTitle()))
            {
                widget_header.setVisibility(VISIBLE);
                widget_header.setText(model.getTitle());

                if(sharedPreferences != null)
                    widget_header.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
            }

            if(!StringUtils.isNullOrEmpty(model.getDescription()))
            {
                meeting_desc.setVisibility(VISIBLE);
                meeting_desc.setText(model.getDescription());

                if(sharedPreferences != null)
                    meeting_desc.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")));
            }

            if(model.getHeaderOptions() != null && model.getHeaderOptions() instanceof HeaderOptionsModel && ((HeaderOptionsModel)model.getHeaderOptions()).getType()!=null ) {
                HeaderOptionsModel headerOptionsModel = ((HeaderOptionsModel)model.getHeaderOptions());
                switch (headerOptionsModel.getType())
                {
                case "button":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    tvButtonParent.setVisibility(VISIBLE);
                    String btnTitle = "";
                    if(headerOptionsModel.getButton() != null && headerOptionsModel.getButton().getTitle() != null)
                        btnTitle = headerOptionsModel.getButton().getTitle();
                    else
                        btnTitle = headerOptionsModel.getText();
                    if(!StringUtils.isNullOrEmpty(btnTitle))
                        tvButton.setText(btnTitle);
                    else
                        tvButtonParent.setVisibility(GONE);


                    tvButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.getHeaderOptions() != null && headerOptionsModel.getButton() != null && headerOptionsModel.getButton().getPayload() != null)
                            {
                                buttonAction(headerOptionsModel.getButton().getPayload());
                            }
                        }
                    });

                    break;
                case "menu":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(VISIBLE);
                    tvText.setVisibility(GONE);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);

                    imgMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( model.getHeaderOptions()!= null &&  headerOptionsModel.getMenu()!= null && headerOptionsModel.getMenu().size() > 0) {

                                WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                                bottomSheetDialog.setisFromFullView(false);
                                bottomSheetDialog.setSkillName("skillName","trigger");
                                bottomSheetDialog.setData(model,true);

                                bottomSheetDialog.setVerticalListViewActionHelper(null);
                                bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");

                            }
                        }
                    });


                    break;
                case "text":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(VISIBLE);
                    tvText.setText(headerOptionsModel.getText());
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    break;
                case "url":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    SpannableString content = new SpannableString(headerOptionsModel.getUrl().getTitle()!=null?headerOptionsModel.getUrl().getTitle():headerOptionsModel.getUrl().getLink());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tvUrl.setText(content);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(VISIBLE);
                    tvUrl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(headerOptionsModel.getUrl().getLink() != null) {
                                Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
                                intent.putExtra("url", headerOptionsModel.getUrl().getLink());
                                intent.putExtra("header", getContext().getResources().getString(R.string.app_name));
                                getContext().startActivity(intent);
                            }
                        }
                    });
                    break;

                case "image":
                    icon_image_load.setVisibility(VISIBLE);
                    if(headerOptionsModel.getImage()!=null&&headerOptionsModel.getImage().getImage_src()!=null) {
                        Picasso.get().load(headerOptionsModel.getImage().getImage_src()).into(icon_image_load);
                        icon_image_load.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (model.getHeaderOptions() != null && headerOptionsModel.getImage() != null && headerOptionsModel.getImage().getPayload() != null)
                                {
                                    buttonAction(headerOptionsModel.getImage().getPayload());
                                }
                            }
                        });
                    }
                    break;
                }
            }
            if (model.getWidgetlistElements() != null && model.getWidgetlistElements().size() > 0 && !model.getTemplate_type().equals("loginURL")) {

                if (model.getWidgetlistElements() != null && model.getWidgetlistElements().size() > 3) {
                    botCustomListViewButton.setVisibility(View.VISIBLE);
                }

                listWidgetAdapter.setWidgetData(model.getWidgetlistElements());
                botCustomListView.setAdapter(listWidgetAdapter);
                listWidgetAdapter.setComposeFooterInterface(composeFooterInterface);
                listWidgetAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                listWidgetAdapter.setPreviewLength(3);
                listWidgetAdapter.notifyDataSetChanged();
            }
            else {
                listWidgetAdapter.setData(null);
                botCustomListView.setAdapter(listWidgetAdapter);
                listWidgetAdapter.notifyDataSetChanged();
            }

            if(themeName.equalsIgnoreCase(BotResponse.THEME_NAME_1))
            {
                rightDrawable.setStroke((int) (1*dp1), Color.parseColor("#ffffff"));
                botCustomListRoot.setBackground(rightDrawable);
            }
            else
            {
                rightDrawable.setStroke((int) (2*dp1), Color.parseColor("#d3d3d3"));
                botCustomListRoot.setBackground(rightDrawable);
            }
        }
    }

    public static void showEmailIntent(Activity activity, String recepientEmail) {
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
    public static void launchDialer(Context context, String url) {
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

    public static boolean hasPermission(Context context,String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        for (String s : permission) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                    ActivityCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED;
        }
        return shouldShowRequestPermissionRationale;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}