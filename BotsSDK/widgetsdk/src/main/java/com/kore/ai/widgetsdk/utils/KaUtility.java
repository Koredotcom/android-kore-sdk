package com.kore.ai.widgetsdk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.GenericWebViewActivity;
import com.kore.ai.widgetsdk.activities.PanelMainActivity;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.PinUnPinEvent;
import com.kore.ai.widgetsdk.models.ContactsTeamsHolder;
import com.kore.ai.widgetsdk.models.FeedbackDataResponse;
import com.kore.ai.widgetsdk.models.KoraHelpModel;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.net.KoreVersionUpgradeResponse;
import com.kore.ai.widgetsdk.net.SDKConfiguration;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shiva Krishna on 2/26/2018.
 */

public class KaUtility {

    public static String getPanelFormatedName(String pName)
    {
        return "FROM "+pName.toUpperCase();
    }
    public static void showToastMessage(Context context,String message){

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast_pin,
                ((Activity) context).findViewById(R.id.root_pin));

        // set a message

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private static HashMap<String, ContactsTeamsHolder> contactsTeamsHolders = new HashMap<>();
    private static ArrayList<String> hashTags;
    private static final ArrayList<Long> dirIds = new ArrayList<Long>();
    private static final Gson gson = new Gson();


    public static String skipped_feedbackId=null;

    public static FeedbackDataResponse getFeedbackDataResponse() {
        return feedbackDataResponse;
    }

    public static Typeface getTypeFaceObj(Context context) {
        return ResourcesCompat.getFont(context, R.font.icomoon);
    }

    private static KoraHelpModel koraHelpModel;
    public static FeedbackDataResponse feedbackDataResponse;
    public static KoraHelpModel getKoraHelpData() {
        return koraHelpModel;
    }

    public static void fetchAppUpgradeStatus(Context context, String token) {
        String version = Utils.getVersion(context);
        String appId = context.getString(R.string.app_id_for_update);

        Call<KoreVersionUpgradeResponse> _response = KaRestBuilder.getKaRestAPI().fetchVersionUpgradeStatus(token, appId, version);
        KaRestAPIHelper.enqueueWithRetry(_response, new Callback<KoreVersionUpgradeResponse>() {
            @Override
            public void onResponse(Call<KoreVersionUpgradeResponse> call, Response<KoreVersionUpgradeResponse> response) {
                if (response.body() != null && response.body().getVersion() != null) {
                    KoreEventCenter.post(response.body());
                }
            }

            @Override
            public void onFailure(Call<KoreVersionUpgradeResponse> call, Throwable t) {

            }
        });
    }

    public static void handleCustomActions(Map<String, Object> data, Context context) {
        String actionType = (String) data.get("type");
        if (!StringUtils.isNullOrEmptyWithTrim(actionType)) {
            switch (actionType.toLowerCase()) {
                case "url":
                    KaUtility.launchWebUrl(context, (String) (data.get("url")));
                    break;
                case "dial":
                    KaUtility.launchDialer(context, "tel:" + data.get("dial"));
                    break;
                case "open_form":
                    KaUtility.launchTakeNotes(context, (String) (data.get("meetingId")), (String) (data.get("eId")));
                    break;
                case "postback":
                    HashMap<String, String> params = new HashMap<>();
                    if (data.get("id") != null && !((String) data.get("id")).equalsIgnoreCase("")) {
                        params.put("eId", (String) data.get("id"));
                        Intent intent = new Intent(context, PanelMainActivity.class);
                        intent.putExtra(BundleConstants.PAYLOAD, gson.toJson(params));
                        intent.putExtra(BundleConstants.MESSAGE, (String) data.get("payload"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else if (data.get(BundleUtils.EId) != null && !((String) data.get(BundleUtils.EId)).equalsIgnoreCase("")) {
                        try {
                            params.put("eId", (String) data.get(BundleUtils.EId));
                            params.put("meetingMessage", (String) data.get("payload"));
                        } catch (Exception E) {

                        }
                        Intent intent = new Intent(context, PanelMainActivity.class);
                        intent.putExtra(BundleConstants.PAYLOAD, gson.toJson(params));
                        intent.putExtra(BundleConstants.MESSAGE, (String) data.get("payload"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else if (data.get("utterance") != null) {
                        Intent intent = new Intent(context, PanelMainActivity.class);
//                        intent.putExtra(BundleConstants.PAYLOAD, gson.toJson(params));
                        intent.putExtra(BundleConstants.MESSAGE, (String) data.get("utterance"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }


                    break;
                default:


            }
        }

    }

    public static void launchTakeNotes(Context context, String meetingId, String eId) {
//        Intent intent = new Intent(context, MeetingNotesCreationActivity.class);
//        intent.putExtra(BundleConstants.MEETING_ID, meetingId);
//        intent.putExtra(BundleConstants.EVENT_ID, eId);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
    }

    public static int isTitleAndPanelNameMatch(String pname, String panelName) {
        if(pname.equalsIgnoreCase(panelName))
            return View.GONE;

        return View.VISIBLE;
    }

    public enum Features {MEETINGS, KNOWLEDGE, TASKS, SKILLS, UNIVERSAL_SEARCH}

    public static final LinkedHashMap<Features, String> featureDescriptionMap = new LinkedHashMap<>();
    public static final LinkedHashMap<Features, String> featureTitleMap = new LinkedHashMap<>();
    public static final HashMap<Features, String> featureVideosMap = new HashMap<>();

    //    private String rooturl = "https://qa-app.kora.ai/static/videos/";
    private static final String[] videoLinks = new String[]{
            SDKConfiguration.Server.SERVER_URL + "/static/videos/meetings_boxed.mp4",
            SDKConfiguration.Server.SERVER_URL + "/static/videos/knowledge_boxed.m4v",
            SDKConfiguration.Server.SERVER_URL + "/static/videos/tasks_boxed.m4v",
            SDKConfiguration.Server.SERVER_URL + "/static/videos/skills_boxed.m4v",
            SDKConfiguration.Server.SERVER_URL + "/static/videos/universal_search_boxed2.m4v"
    };

    static {
        featureTitleMap.put(Features.MEETINGS, "Scheduling meetings made easy");
        featureTitleMap.put(Features.KNOWLEDGE, "Organize your mind");
        featureTitleMap.put(Features.TASKS, "Manage your tasks");
        featureTitleMap.put(Features.SKILLS, "Arm Kora with new skills");
        featureTitleMap.put(Features.UNIVERSAL_SEARCH, "Search anything using Kora");
        featureDescriptionMap.put(Features.MEETINGS, "Kora identifies free time slots across all invitees and intelligently schedules meetings");
        featureDescriptionMap.put(Features.KNOWLEDGE, "Kora helps you create and manage your knowledge base from various sources and retrieves when you need it");
        featureDescriptionMap.put(Features.TASKS, "Just ask Kora to create and assign a task to anyone");
        featureDescriptionMap.put(Features.SKILLS, "With Kora you can bring different enterprise skills like Jira and ITSM under one roof");
        featureDescriptionMap.put(Features.UNIVERSAL_SEARCH, "Kora deep searches into your drives, emails, knowledge base and presents when you ask for it");
        featureVideosMap.put(Features.MEETINGS, videoLinks[0]);
        featureVideosMap.put(Features.KNOWLEDGE, videoLinks[1]);
        featureVideosMap.put(Features.TASKS, videoLinks[2]);
        featureVideosMap.put(Features.SKILLS, videoLinks[3]);
        featureVideosMap.put(Features.UNIVERSAL_SEARCH, videoLinks[4]);
    }

    public static void setFeedbackDataResponse(FeedbackDataResponse feedbackDataResponse) {
        KaUtility.feedbackDataResponse = feedbackDataResponse;
    }

    private static final boolean isTeachInitiated = false;


    public static HashMap<String, ContactsTeamsHolder> getKoreContacts() {
        return contactsTeamsHolders;
    }

    public static void setKoreContacts(HashMap<String, ContactsTeamsHolder> koreContacts) {
        KaUtility.contactsTeamsHolders = koreContacts;
    }

    public static void addKoreContacts(Collection<ContactsTeamsHolder> koreContacts) {
        for (ContactsTeamsHolder contactsTeamsHolder : koreContacts) {
            KaUtility.contactsTeamsHolders.put(contactsTeamsHolder.getId(), contactsTeamsHolder);
        }
    }

    public static void addKoreContact(ContactsTeamsHolder koreContact) {
        KaUtility.contactsTeamsHolders.put(koreContact.getId(), koreContact);
    }

    public static void setPinnedViewState(Context context,boolean isPinned, TextView tv,String panelId)
    {

        tv.setText(isPinned?context.getResources().getString(R.string.icon_31):context.getResources().getString(R.string.icon_32));
        showToastMessage(context,isPinned? "This widget is now pinned in the home panel": "This widget is now unpinned from the home panel");
        KoreEventCenter.post(new PinUnPinEvent(panelId));
    }

    public static Drawable changeColorOfDrawable(Context context, int colorCode) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
        try {
            ((GradientDrawable) drawable).setColor(context.getResources().getColor(colorCode));
            return drawable;
        } catch (Exception e) {
            return drawable;
        }

    }

    public static Transformation getRoundTransformation() {
        return new RoundedTransformationBuilder()
                .oval(true)
                .build();
    }

    public static boolean isTeachInitiated() {
        return isTeachInitiated;
    }


    public static ArrayList<String> getHashTags() {
        return hashTags;
    }

    public static void setHashTags(ArrayList<String> hashTags) {
        KaUtility.hashTags = hashTags;
    }

    /**
     * @return App version
     */
    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
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

    public static void launchWebUrl(Context context, String url) {
        if (url == null) {
            return;
        }
        try {
            if (url.toLowerCase().startsWith("www")) {
                url = "http://" + url.toLowerCase(); //Http is needed in standard url, so adding it to avoid unnecessary exceptions
            }

            Uri uri = Uri.parse(url);

            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NO_HISTORY
                        | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, context.getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, context.getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public static void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(KaPermissionsHelper.hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
        }
    }


    public static void launchWebViewActivity(Activity activity, String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
                url = "http://" + url.toLowerCase();
            }
            if (NetworkUtility.isNetworkConnectionAvailable(activity)) {
                Intent intent = new Intent(activity, GenericWebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("header", activity.getResources().getString(R.string.app_name));

                activity.startActivity(intent);
            } else {
                ToastUtils.showToast(activity, "Check your internet connection and please try again");
            }
        }
    }


}