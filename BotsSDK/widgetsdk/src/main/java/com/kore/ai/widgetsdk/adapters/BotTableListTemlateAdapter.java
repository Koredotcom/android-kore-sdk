package com.kore.ai.widgetsdk.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
import com.kore.ai.widgetsdk.listeners.InvokeGenericWebViewInterface;
import com.kore.ai.widgetsdk.models.BotTableListDefaultActionsModel;
import com.kore.ai.widgetsdk.models.BotTableListElementsItemsModel;
import com.kore.ai.widgetsdk.models.BotTableListElementsModel;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;

import java.util.ArrayList;

public class BotTableListTemlateAdapter extends BaseAdapter {

    String LOG_TAG = BotTableListTemlateAdapter.class.getSimpleName();
    ArrayList<BotTableListElementsModel> botTableListModels = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    LayoutInflater ownLayoutInflator;
    Context context;
    RoundedCornersTransform roundedCornersTransform;
    int count = 0;

    public BotTableListTemlateAdapter(Context context, int count) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.count = count;
    }

    @Override
    public int getCount() {
        if (botTableListModels != null) {
            return botTableListModels.size() > count ? count : botTableListModels.size();
        } else {
            return 0;
        }
    }

    @Override
    public BotTableListElementsModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botTableListModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ownLayoutInflator.inflate(R.layout.bot_table_list_view, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotTableListElementsModel botListModel = getItem(position);

        if(!StringUtils.isNullOrEmpty(botListModel.getSectionHeader())) {
            holder.botListItemTitle.setTag(botListModel);
            holder.botListItemTitle.setVisibility(View.VISIBLE);
            holder.botListItemTitle.setText(botListModel.getSectionHeader());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        }

        if(!StringUtils.isNullOrEmpty(botListModel.getSectionHeaderDesc())) {
            holder.bot_list_item_desc.setTag(botListModel);
            holder.bot_list_item_desc.setVisibility(View.VISIBLE);
            holder.bot_list_item_desc.setText(botListModel.getSectionHeaderDesc());
            holder.bot_list_item_desc.setTypeface(null, Typeface.BOLD);
        }

        if(botListModel.getElements() != null && botListModel.getElements().size() > 0)
        {
            BotTableListInnerAdapter botTableListInnerAdapter = new BotTableListInnerAdapter(context, botListModel.getElements());
            botTableListInnerAdapter.setComposeFooterInterface(composeFooterInterface);
            botTableListInnerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            holder.botTableListView.setAdapter(botTableListInnerAdapter);

            holder.botTableListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    BotTableListElementsItemsModel botTableListRowItemsModel = botListModel.getElements().get(position);

                    if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("postback"))
                    {
                        if(composeFooterInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getPayload())) {
                            composeFooterInterface.onSendClick(botTableListRowItemsModel.getDefault_action().getTitle(), botTableListRowItemsModel.getDefault_action().getPayload(),false);
                        }
                    }
                    else if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("url"))
                    {
//                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getUrl())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getDefault_action().getUrl());
//                        }

                        buttonAction(botTableListRowItemsModel.getDefault_action(), false);
                    }
//                    else if(botTableListRowItemsModel.getDefault_action() == null && botTableListRowItemsModel.getTitle() != null
//                        && botTableListRowItemsModel.getTitle().getUrl() != null)
//                    {
//                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getTitle().getUrl().getLink())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getTitle().getUrl().getLink());
//                        }
//                    }
                }
            });
        }
//        else
//            if(botListModel.getElements() != null && botListModel.getElements().size() > 0)
//        {
//            BotTableListInnerAdapter botTableListInnerAdapter = new BotTableListInnerAdapter(context, botListModel.getElements());
//            botTableListInnerAdapter.setComposeFooterInterface(composeFooterInterface);
//            botTableListInnerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            holder.botTableListView.setAdapter(botTableListInnerAdapter);
//
//            holder.botTableListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//            {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                {
//                    BotTableListRowItemsModel botTableListRowItemsModel = botListModel.getElements().get(position);
//
//                    if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
//                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("postback"))
//                    {
//                        if(composeFooterInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getPayload())) {
//                            composeFooterInterface.onSendClick(botTableListRowItemsModel.getDefault_action().getTitle(), botTableListRowItemsModel.getDefault_action().getPayload(),false);
//                        }
//                    }
//                    else if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
//                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("url"))
//                    {
//                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getUrl())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getDefault_action().getUrl());
//                        }
//                    }
//                    else if(botTableListRowItemsModel.getDefault_action() == null && botTableListRowItemsModel.getTitle() != null
//                            && botTableListRowItemsModel.getTitle().getUrl() != null)
//                    {
//                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getTitle().getUrl().getLink())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getTitle().getUrl().getLink());
//                        }
//                    }
//                }
//            });
//        }
    }


    public void setBotListModelArrayList(ArrayList<BotTableListElementsModel> botListModelArrayList) {
        this.botTableListModels = botListModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void buttonAction(BotTableListDefaultActionsModel botTableListDefaultActionsModel, boolean appendUtterance){
        String utterance = null;
        if(botTableListDefaultActionsModel != null){
            utterance = botTableListDefaultActionsModel.getUtterance();
        }
        if(utterance == null)return;
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
                launchDialer(context,utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) context,utterance.split(":")[1]);
            }
            return;
        }

        if(botTableListDefaultActionsModel.getType().equalsIgnoreCase("url"))
        {
            Intent intent = new Intent(context, GenericWebViewActivity.class);
            intent.putExtra("url", botTableListDefaultActionsModel.getUrl());
            intent.putExtra("header", context.getResources().getString(R.string.app_name));
            context.startActivity(intent);
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
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermission(Context context,String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionLength = permission.length;
            for (int i=0;i<permissionLength;i++) {
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                        ActivityCompat.checkSelfPermission(context, permission[i]) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return shouldShowRequestPermissionRationale;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.bot_list_item_desc = (TextView) view.findViewById(R.id.bot_list_item_desc);
        holder.botTableListView = (ListView) view.findViewById(R.id.botTableListView);
        view.setTag(holder);
    }

    private static class ViewHolder {
        TextView botListItemTitle;
        TextView bot_list_item_desc;
        ListView botTableListView;
    }
}
