package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.ListWidgetAdapter;
import kore.botssdk.dialogs.ListWidgetActionSheetFragment;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.Widget;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

import static kore.botssdk.adapter.ListWidgetButtonAdapter.showEmailIntent;

public class ListWidgetView extends LinearLayout {
    private float dp1;
    public RecyclerView botCustomListView;
    private ListWidgetAdapter listWidgetAdapter = null;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    Context context;
    private TextView botCustomListViewButton;
    private LinearLayout botCustomListRoot;
    private PayloadInner model;
    private SharedPreferences sharedPreferences;
    private GradientDrawable rightDrawable;
    public ImageView menu_btn,icon_image_load;
    private TextView panel_name_view;
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
        rightDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_rect_feedback);

        // KoreEventCenter.register(this);
        botCustomListView = view.findViewById(R.id.botCustomListView);
        botCustomListView.setLayoutManager(new LinearLayoutManager(getContext()));
        botCustomListRoot = view.findViewById(R.id.botCustomListRoot);
        botCustomListViewButton = view.findViewById(R.id.botCustomListViewButton);
//        panel_name_view=view.findViewById(com.kora.ai.widgetsdk.R.id.panel_name_view);
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
        listWidgetAdapter = new ListWidgetAdapter(getContext(), BotResponse.TEMPLATE_TYPE_CAL_EVENTS_WIDGET, "");
        listWidgetAdapter.setFromWidget(true);
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void buttonAction(String utt, boolean appendUtterance){
        String utterance = null;
        utterance = utt;

        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
//                KaUtility.launchDialer(getContext(),utterance);
            }else if(utterance.startsWith("mailto:")){
//                KaUtility.showEmailIntent((Activity) getContext(),utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

    }

    public void buttonAction(Widget.Button button, boolean appendUtterance){
        String utterance = null;
        if(button != null){
            utterance = button.getUtterance();
        }
        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
//                KaUtility.launchDialer(getContext(),utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) getContext(),utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
//        if(appendUtterance && trigger!= null)
//            msg = msg.append(trigger).append(" ");
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

            if(model.getHeaderOptions() != null && model.getHeaderOptions().getType()!=null ) {
                switch (model.getHeaderOptions().getType()){
                case "button":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    tvButtonParent.setVisibility(VISIBLE);
                    String btnTitle = "";
                    if(model.getHeaderOptions().getButton() != null && model.getHeaderOptions().getButton().getTitle() != null)
                        btnTitle = model.getHeaderOptions().getButton().getTitle();
                    else
                        btnTitle = model.getHeaderOptions().getText();
                    if(!com.kore.ai.widgetsdk.utils.StringUtils.isNullOrEmpty(btnTitle))
                        tvButton.setText(btnTitle);
                    else
                        tvButtonParent.setVisibility(GONE);


                    tvButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model != null && model.getHeaderOptions() != null && model.getHeaderOptions().getButton() != null
                                && model.getHeaderOptions().getButton().getPayload() != null)
                            {
                                buttonAction(model.getHeaderOptions().getButton().getPayload(), true);
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
                            if ( model.getHeaderOptions()!= null &&  model.getHeaderOptions().getMenu()!= null && model.getHeaderOptions().getMenu().size() > 0) {

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
                    tvText.setText(model.getHeaderOptions().getText());
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(GONE);
                    break;
                case "url":
                    icon_image_load.setVisibility(GONE);
                    imgMenu.setVisibility(GONE);
                    tvText.setVisibility(GONE);
                    SpannableString content = new SpannableString(model.getHeaderOptions().getUrl().getTitle()!=null?model.getHeaderOptions().getUrl().getTitle():model.getHeaderOptions().getUrl().getLink());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tvUrl.setText(content);
                    tvButtonParent.setVisibility(GONE);
                    tvUrl.setVisibility(VISIBLE);
                    tvUrl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(model.getHeaderOptions().getUrl().getLink() != null) {
                                Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
                                intent.putExtra("url", model.getHeaderOptions().getUrl().getLink());
                                intent.putExtra("header", getContext().getResources().getString(com.kora.ai.widgetsdk.R.string.app_name));
                                getContext().startActivity(intent);
                            }
                        }
                    });
                    break;

                case "image":
                    icon_image_load.setVisibility(VISIBLE);
                    if(model.getHeaderOptions().getImage()!=null&&model.getHeaderOptions().getImage().getImage_src()!=null) {
                        Picasso.get().load(model.getHeaderOptions().getImage().getImage_src()).into(icon_image_load);
                        icon_image_load.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (model != null && model.getHeaderOptions() != null && model.getHeaderOptions().getImage() != null
                                        && model.getHeaderOptions().getImage().getPayload() != null)
                                {
                                    buttonAction(model.getHeaderOptions().getImage().getPayload(), true);
                                }
                            }
                        });
                    }
                    break;
                }
            }
            if (model != null && model.getWidgetlistElements() != null && model.getWidgetlistElements().size() > 0 && !model.getTemplate_type().equals("loginURL")) {

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

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}