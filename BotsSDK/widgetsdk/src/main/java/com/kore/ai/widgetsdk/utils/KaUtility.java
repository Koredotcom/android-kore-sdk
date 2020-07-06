package com.kore.ai.widgetsdk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

//import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.PanelMainActivity;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.PinUnPinEvent;
import com.kore.ai.widgetsdk.events.ShootUtteranceEvent;
import com.kore.ai.widgetsdk.listeners.HelpDataLoadListener;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.ContactsTeamsHolder;
import com.kore.ai.widgetsdk.models.DeviceContactsModel;
import com.kore.ai.widgetsdk.models.DeviceContactsTimeStampResponse;
import com.kore.ai.widgetsdk.models.EmailItem;
import com.kore.ai.widgetsdk.models.FeedbackDataResponse;
import com.kore.ai.widgetsdk.models.HowCanHelpTemplate;
import com.kore.ai.widgetsdk.models.KoraHelpModel;
import com.kore.ai.widgetsdk.models.PhoneItem;
import com.kore.ai.widgetsdk.models.SkillsListResponse;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.net.KoreVersionUpgradeResponse;
import com.kore.ai.widgetsdk.net.SDKConfiguration;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
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
                (ViewGroup) ((Activity) context).findViewById(R.id.root_pin));

        // set a message

        TextView text = (TextView) layout.findViewById(R.id.text);
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
    private static ArrayList<Long> dirIds = new ArrayList<Long>();
    private static Gson gson = new Gson();


    public static String skipped_feedbackId=null;


    public static void setKoraHelpModel(KoraHelpModel koraHelpModel) {
        KaUtility.koraHelpModel = koraHelpModel;
    }

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

    public static int countChar(String str, char c) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }

        return count;
    }


    public static long getCurrentTimestamp() {

        return System.currentTimeMillis();
    }

    public static long addSevenDays(long duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.getDDMMYYYY(duration));
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);


        return calendar.getTimeInMillis();
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
                    KaUtility.launchDialer(context, "tel:" + (String) (data.get("dial")));
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

    public static void handleCustomActionsNew(Map<String, Object> customdata, Context context, int posititon, boolean uttrenceAppend) {
        ArrayList<LinkedTreeMap<String, Object>> maindata=null;
        if(customdata.get("actions")!=null) {
            maindata = (ArrayList<LinkedTreeMap<String, Object>>) customdata.get("actions");
        }
        Map<String, Object> data = maindata!=null ? maindata.get(posititon):null;
        String actionType = data!=null? (String) data.get("type"):null;
        if(StringUtils.isNullOrEmptyWithTrim(actionType)) {
            actionType = (String) customdata.get("mType");
        }
        if (!StringUtils.isNullOrEmptyWithTrim(actionType)) {
            switch (actionType.toLowerCase()) {
                case "url":
                    KaUtility.launchWebUrl(context, (String) (data.get("url")));
                    break;
                case "dial":
                    KaUtility.launchDialer(context, "tel:" + (String) (data.get("dial")));
                    break;
                case "postback":
                    HashMap<String, String> params = new HashMap<>();
                    if (data.get("id") != null && !((String) data.get("id")).equalsIgnoreCase("")) {
                        params.put("eId", (String) data.get("id"));
                        Intent intent = new Intent(context, PanelMainActivity.class);
                        intent.putExtra(BundleConstants.PAYLOAD, gson.toJson(params));
                        intent.putExtra(BundleConstants.MESSAGE, (uttrenceAppend ? Constants.SKILL_UTTERANCE : "") + (String) data.get("payload"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    } else if (customdata.get(BundleUtils.EId) != null && !((String) customdata.get(BundleUtils.EId)).equalsIgnoreCase("")) {
                        try {
                            params.put("eId", (String) customdata.get(BundleUtils.EId));
                            params.put("meetingMessage", (String) data.get("payload"));
                        } catch (Exception E) {

                        }
                        Intent intent = new Intent(context, PanelMainActivity.class);
                        intent.putExtra(BundleConstants.PAYLOAD, gson.toJson(params));
                        intent.putExtra(BundleConstants.MESSAGE, (uttrenceAppend ? Constants.SKILL_UTTERANCE : "") + (String) data.get("payload"));
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
                case "shared":
                    if(customdata!=null && customdata.get("triggers") !=null){
                        String utterance = (String)customdata.get("triggers");
                        ShootUtteranceEvent event = new ShootUtteranceEvent();
                        event.setScrollUpNeeded(true);
                        event.setMessage((uttrenceAppend ? Constants.SKILL_UTTERANCE : "") + utterance);
                        KoreEventCenter.post(event);
//                        if(context!=null && context instanceof NotificationsActivity) {
//                            ((NotificationsActivity) context).finish();
//                        }
                    }
                    break;

                case "open_form":
                    KaUtility.launchTakeNotesFromBell(context, (String) (data.get("meetingId")), (String) (customdata.get("eId")));
                    break;

                default:


            }
        }

    }

    public static void launchTakeNotesFromBell(Context context, String meetingId, String eId) {
//        Intent intent = new Intent(context, MeetingNotesCreationActivity.class);
//        intent.putExtra(BundleConstants.MEETING_ID, meetingId);
//        intent.putExtra(BundleConstants.EVENT_ID, eId);
//        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
    }

    public static int isTitleAndPanelNameMatch(String pname, String panelName) {
        if(pname.equalsIgnoreCase(panelName))
            return View.GONE;
            return View.VISIBLE;
    }

    public enum Features {MEETINGS, KNOWLEDGE, TASKS, SKILLS, UNIVERSAL_SEARCH}

    ;
    public static LinkedHashMap<Features, String> featureDescriptionMap = new LinkedHashMap<>();
    public static LinkedHashMap<Features, String> featureTitleMap = new LinkedHashMap<>();
    public static HashMap<Features, String> featureVideosMap = new HashMap<>();

    //    private String rooturl = "https://qa-app.kora.ai/static/videos/";
    private static String videoLinks[] = new String[]{
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


//        LinkedHashMap<String, String> meetingsVideos = new LinkedHashMap<>();
//        meetingsVideos.put("01   Schedule Meetings", SDKConfiguration.Server.SERVER_URL + "/static/videos/meetings_boxed.mp4");
//        meetingsVideos.put("02   Cancel Meetings", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/meeting_2.mp4");
//        meetingsVideos.put("03   Get your schedule", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/meeting_3.mp4");
        featureVideosMap.put(Features.MEETINGS, videoLinks[0]);

//        LinkedHashMap<String, String> emailsVideos = new LinkedHashMap<>();
//        emailsVideos.put("01   Find email conversations", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/emails_1.mp4");
//        emailsVideos.put("02   Search for files", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/emails_2.mp4");
//        emailsVideos.put("03   Send Emails", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/emails_3.mp4");
        featureVideosMap.put(Features.KNOWLEDGE, videoLinks[1]);

//        LinkedHashMap<String, String> contactsVideos = new LinkedHashMap<>();
//        contactsVideos.put("01   Find someone's mobile number", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/contacts_1.mp4");
//        featureVideosMap.put(Features.CONTACTS, contactsVideos);

//        LinkedHashMap<String, String> infoVideos = new LinkedHashMap<>();
//        infoVideos.put("01   Get info about everything", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/info_1.mp4");
//        infoVideos.put("02   Store Knowledge", SDKConfiguration.Server.SERVER_URL + "/downloads/videos/info_2.mp4");
        featureVideosMap.put(Features.TASKS, videoLinks[2]);
        featureVideosMap.put(Features.SKILLS, videoLinks[3]);
        featureVideosMap.put(Features.UNIVERSAL_SEARCH, videoLinks[4]);
    }

    public static void setFeedbackDataResponse(FeedbackDataResponse feedbackDataResponse) {
        KaUtility.feedbackDataResponse = feedbackDataResponse;
    }

    public static void getFeedbackApi(String accesstoken, String userId, final HelpDataLoadListener helpDataLoadListener) {
        Call<FeedbackDataResponse> call = KaRestBuilder.getKaRestAPI().getFeedbackItemLIst(accesstoken, userId);
        KaRestAPIHelper.enqueueWithRetry(call, new Callback<FeedbackDataResponse>() {
            @Override
            public void onResponse(Call<FeedbackDataResponse> call, Response<FeedbackDataResponse> response) {

                KaUtility.setFeedbackDataResponse(response.body());
                helpDataLoadListener.onDataLoad();

            }

            @Override
            public void onFailure(Call<FeedbackDataResponse> call, Throwable t) {
                helpDataLoadListener.onDataLoad();
            }
        });
    }

    public static void setIsTeachInitiated(boolean isTeachInitiated) {
        KaUtility.isTeachInitiated = isTeachInitiated;
    }

    private static boolean isTeachInitiated = false;


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


//    public static ContactsTeamsHolder convertToCTHModel(KoreContact koreContact) {
//        ContactsTeamsHolder contactsTeamsHolder = new ContactsTeamsHolder();
//        contactsTeamsHolder.setId(koreContact.getId());
//        contactsTeamsHolder.setEmailId(koreContact.getEmailId());
//        contactsTeamsHolder.setDisplayNameInitials(koreContact.getDisplayNameInitials());
//        contactsTeamsHolder.setRole(koreContact.getAccountInfo().getjTitle());
//        contactsTeamsHolder.setPfColor(koreContact.getProfColor());
//        contactsTeamsHolder.setDisplayName(koreContact.getDisplayName());
//        contactsTeamsHolder.setPhoneNo(koreContact.getPhoneNo());
//        contactsTeamsHolder.setLastName(koreContact.getLastName());
//        contactsTeamsHolder.setFirstName(koreContact.getFirstName());
//        contactsTeamsHolder.setIcon(koreContact.getProfileImage());
//        return contactsTeamsHolder;
//    }

    public static HashMap<String, ContactsTeamsHolder> getContactsTeamsHoldersByIds(ArrayList<String> userIds) {
        HashMap<String, ContactsTeamsHolder> contactsTeamsHolders1 = new HashMap<>();
        if (userIds != null && userIds.size() > 0) {
            for (String userId : userIds) {
                ContactsTeamsHolder contactsTeamsHolder = contactsTeamsHolders.get(userId);
                if (contactsTeamsHolder != null) {
                    contactsTeamsHolders1.put(userId, contactsTeamsHolder);
                }
            }
        }
        return contactsTeamsHolders1;
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

    public enum SectionTypes {
        DIVIDEER, ROOT_TITLE_DESCR, SUB_TITLE_DEC, DESC_TITLE, SETTINGS_SEARCH_FORMAT, TOGGLE_FORMAT;
    }

//    private static void updateServerAndLogout() {
//        KoreEventCenter.post(new ResetOnboardingEvent(true));
//    }


    public static void customizeSearchViewForSearch(Context context, SearchView searchView, String hint) {
        // TO Hide the default search Icon
        //  searchView.setIconifiedByDefault(false);
        //  searchView.setIconified(false);
        searchView.setQueryHint(hint);
        searchView.clearFocus();
        searchView.setFocusable(false);
        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(100);
        searchEditText.setFilters(FilterArray);
        searchEditText.setTextColor(context.getResources().getColor(R.color.txtFontBlack));
        //  SearchView.SearchAutoComplete searchAutoComplete = ((SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEditText.setHintTextColor(context.getResources().getColor(R.color.color_a7b0be));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        // TO Hide the default search Icon
        //   ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        //   searchIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        //To hide the bottom bar below search text
        View searchplate = (View) searchView.findViewById(androidx.appcompat.R.id.search_plate);
        searchplate.setBackgroundResource(android.R.color.transparent);

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_black_24dp);
        //  setCursorColorAsTextColor(searchAutoComplete);


    }


    /*public static void loadKoreUserProfileDao(String userId) {
        if (koreUserProfileModel == null) {
            try {
                KoreBaseDao<KoreUserProfileModel, String> userProfileDAO = KoreDBManager.getHelperInstance(userId).getUserProfileDAO();
                koreUserProfileModel = userProfileDAO.queryForId(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

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

    /*private static class UpdateUserInfoWithUserProfileModel extends AsyncTask<KoreUserProfileModel, Void, Void> {

        Context context;
        UserInfo userInfo;

        public UpdateUserInfoWithUserProfileModel(Context context, UserInfo userInfo) {
            this.context = context;
            this.userInfo = userInfo;
        }

        @Override
        protected Void doInBackground(KoreUserProfileModel... koreUserProfileParams) {

            UserInfoDao userInfoDAO = KoreDBManager.getAccountsHelper(context).getUserDAO();

            if (koreUserProfileParams.length > 0) {
                userInfo.setUserProfileData(koreUserProfileParams[0]);

                try {
                    userInfoDAO.createOrUpdate(userInfo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                KoreDBManager.releaseAccountsHelper();
            }
            return null;
        }
    }*/

    public static String getBuildVersion(Context context) {
        //return "v " + getServerInfo(context) + "_" + getVersion(context);
        return "v" + getVersion(context);
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

    public static HashMap sortMapByValues(HashMap<String, Integer> idRoleMap) {
        Set<Map.Entry<String, Integer>> set = idRoleMap.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Map.Entry<String, Integer> aList : list) {
            Map.Entry entry = (Map.Entry) aList;
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }



    public static void showAllEmailOptions(Activity activity, String email) {
/*        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, " ");
        activity.startActivity(Intent.createChooser(emailIntent, "Mail To:"));*/

        Uri uri = Uri.parse(email);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, activity.getPackageName());
        try {
            activity.startActivity(Intent.createChooser(intent, "Mail To:"));
        } catch (ActivityNotFoundException e) {
            Log.d("URLSpan", "Activity was not found for intent, " + intent.toString());
        }
    }

    public static void openMaps(Activity activity, String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.startActivity(mapIntent);
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


    public static Uri getContactId(Context mContext, String name, ArrayList phoneNo, String emailId) {

        Uri uri = null;
        try {

            if ((dirIds == null || dirIds.size() == 0)) {
                CursorQueryWrapper wrapper = new CursorQueryWrapper("utility");

                wrapper.query(mContext, ContactsContract.Directory.CONTENT_URI, new String[]{
                                ContactsContract.Directory._ID, ContactsContract.Directory.ACCOUNT_NAME, ContactsContract.Directory.ACCOUNT_TYPE
                        }, null, null, null,
                        new CursorQueryWrapper.CursorResultIterator() {
                            @Override
                            public void iterate(Cursor cursor) throws Exception {
                                long id = cursor.getLong(0);
                                if (ContactsContract.Directory.DEFAULT != id && ContactsContract.Directory.LOCAL_INVISIBLE != id) {
                                    dirIds.add(id);
                                    Log.d("dirids", "Account: id=" + id + " name=" + cursor.getString(1) + " type=" + cursor.getString(2));
                                }
                            }
                        });
            }

            for (long id : dirIds) {
                final String idStr = String.valueOf(id);
                uri = getDirectoryContactId(mContext, phoneNo, emailId, name, idStr);
                if (uri != null) {
                    break;
                }
            }
        } catch (Exception e) {
            Log.d("Issue", "Contact id:: failed");

        }
        if (uri == null) {
            uri = getDirectoryContactId(mContext, phoneNo, emailId, name, null);
        }
        return uri;
    }

    private static Uri getDirectoryContactId(Context mContext, ArrayList<String> phoneNo, String emailId, String name, String idStr) {
        Uri lookupByMail = null;
        Uri lookupByName = null;
        Cursor mCursor = null;
        Uri contactUri = null;
        try {


            if (!StringUtils.isNullOrEmptyWithTrim(emailId)) {
                if (StringUtils.isNullOrEmptyWithTrim(idStr)) {
                    lookupByMail = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(emailId));
                } else {
                    lookupByMail = ContactsContract.Contacts.CONTENT_FILTER_URI.buildUpon().appendEncodedPath(emailId)
                            .appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY, idStr).build();
                }
            }

            if (!StringUtils.isNullOrEmptyWithTrim(name)) {
                if (StringUtils.isNullOrEmptyWithTrim(idStr)) {
                    lookupByName = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(name));
                } else {
                    lookupByName = ContactsContract.Contacts.CONTENT_FILTER_URI.buildUpon().appendEncodedPath(name)
                            .appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY, idStr).build();
                }
            }


            if (lookupByMail != null) {
                mCursor = mContext.getContentResolver().query(lookupByMail, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
            }
            if (phoneNo != null && (mCursor == null || mCursor.getCount() == 0)) {
                for (String phone : phoneNo) {
                    Uri lookupByNoUri = getPhoneNumberUri(phone, idStr);
                    if ((mCursor == null || mCursor.getCount() == 0) && lookupByNoUri != null) {
                        mCursor = mContext.getContentResolver().query(lookupByNoUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
                    }
                    if (mCursor != null && mCursor.getCount() > 0) {
                        break;
                    }
                }
            }
            if ((mCursor == null || mCursor.getCount() == 0) && lookupByName != null) {
                mCursor = mContext.getContentResolver().query(lookupByName, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
            }


            if (mCursor != null) {
                if (mCursor.moveToFirst()) {
                    contactUri = getContactUri(idStr, mCursor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return contactUri;
    }

    private static Uri getPhoneNumberUri(String phoneNo, String idStr) {
        if (!StringUtils.isNullOrEmptyWithTrim(phoneNo)) {
            if (StringUtils.isNullOrEmptyWithTrim(idStr)) {
                return Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
            } else {
                return ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendEncodedPath(phoneNo)
                        .appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY, idStr).build();
            }

        }
        return null;
    }

    private static Uri getContactUri(String directoryId, Cursor mCursor) {
        long contactId = Long.valueOf(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID)));
        String lookupKey = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
        if (uri != null && Integer.parseInt(directoryId) != ContactsContract.Directory.DEFAULT) {
            uri = uri.buildUpon().appendQueryParameter(
                    ContactsContract.DIRECTORY_PARAM_KEY, directoryId).build();
        }
        return uri;


    }

    public static void launchContact(Context mContext, Uri uri) {
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setData(uri);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "contact not in list",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getDayByPosition(int position) {
        switch (position) {
            case 0:
                return "Sunday";

            case 1:
                return "Monday";

            case 2:
                return "Tuesday";

            case 3:
                return "Wednesday";

            case 4:
                return "Thursday";

            case 5:
                return "Friday";

            case 6:
                return "Saturday";
            default:
                return "";
        }
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + (int) (150 * AppControl.getInstance().getDimensionUtil().dp1);
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public static void updateDeviceContacts(Context context, String authToken, String userId) {
        updateDeviceContactsToServer(context, authToken, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceContactsTimeStampResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeviceContactsTimeStampResponse resp) {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static Observable<DeviceContactsTimeStampResponse> updateDeviceContactsToServer(final Context context, final String authToken, final String userId) {
        final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return Observable.create(new ObservableOnSubscribe<DeviceContactsTimeStampResponse>() {
            @Override
            public void subscribe(ObservableEmitter<DeviceContactsTimeStampResponse> emitter) throws Exception {
                try {
                    Call<DeviceContactsTimeStampResponse> lastTimeStampResponse = KaRestBuilder.getKaRestAPI().getDeviceContactsTimeStamp(Utils.ah(authToken), userId, deviceId);
                    Response<DeviceContactsTimeStampResponse> rBody = lastTimeStampResponse.execute();

                    long lastTimeStamp = rBody.body().getLastUpdatedTimeStamp();
                    ArrayList<DeviceContactsModel> devContacts = readContacts(context, lastTimeStamp);

                    Call<ResponseBody> postdeviceContactsResp = KaRestBuilder.getKaRestAPI().postDeviceContacts(Utils.ah(authToken), userId, deviceId, devContacts);
                    Response<ResponseBody> _rBody = postdeviceContactsResp.execute();

                    emitter.onNext(rBody.body());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    private static ArrayList<DeviceContactsModel> readContacts(Context context, long _timeStamp) throws JSONException {
        final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        @SuppressLint("InlinedApi") final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        try {
            ArrayList<DeviceContactsModel> contactList = new ArrayList<DeviceContactsModel>();

            ContentResolver cr = context.getContentResolver();
            Cursor cursor = null;
            if (_timeStamp > 0) {
                cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " >=?", new String[]{_timeStamp + ""}, null);
            } else {
                cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
            }
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // get the contact's information
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // get the user's email address
                    String email = null;
                    List<EmailItem> emailList = null;
                    Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (ce != null && ce.moveToFirst()) {
                        emailList = new ArrayList<EmailItem>();
                        do {
                            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            int type = ce.getInt(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                            String labelKey = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), type, null).toString();
                            emailList.add(new EmailItem(labelKey, email));
                        } while (ce.moveToNext());
                        ce.close();
                    }
                    // get the user's phone number
                    String phone = null;
                    List<PhoneItem> phoneList = null;
                    if (hasPhone > 0) {
                        Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phoneList = new ArrayList<PhoneItem>();
                            do {
                                phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                int type = cp.getInt(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                String labelKey = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), type, null).toString();
                                phoneList.add(new PhoneItem(labelKey, phone));
                            } while (cp.moveToNext());
                            cp.close();
                        }
                    }

                    // if the user user has an email or phone then add it to contacts
                    if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                            || (!TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches())) {
                        DeviceContactsModel dcm = new DeviceContactsModel();
                        dcm.setName(name);
                        dcm.setEmail(emailList);
                        dcm.setPhone(phoneList);
                        contactList.add(dcm);
                    }
                } while (cursor.moveToNext());
                // clean up cursor
                cursor.close();
            }
            return contactList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Observable<String> getVideosFromServer(final Context mContext) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                try {
                    Call<ResponseBody> mv1 = KaRestBuilder.getKaRestAPI().downloadNewVideo("meetings_boxed.mp4");
                    featureVideosMap.put(Features.MEETINGS, writeResponseBodyToDisk(mContext, mv1.execute(), "meetings_boxed.mp4"));
                    Call<ResponseBody> mv2 = KaRestBuilder.getKaRestAPI().downloadNewVideo("knowledge_boxed.m4v");
                    featureVideosMap.put(Features.KNOWLEDGE, writeResponseBodyToDisk(mContext, mv2.execute(), "knowledge_boxed.m4v"));
                    Call<ResponseBody> mv3 = KaRestBuilder.getKaRestAPI().downloadNewVideo("tasks_boxed.m4v");
                    featureVideosMap.put(Features.TASKS, writeResponseBodyToDisk(mContext, mv3.execute(), "tasks_boxed.m4v"));
                    Call<ResponseBody> mv4 = KaRestBuilder.getKaRestAPI().downloadNewVideo("skills_boxed.m4v");
                    featureVideosMap.put(Features.SKILLS, writeResponseBodyToDisk(mContext, mv4.execute(), "skills_boxed.m4v"));
                    Call<ResponseBody> mv5 = KaRestBuilder.getKaRestAPI().downloadNewVideo("universal_search_boxed2.m4v");
                    featureVideosMap.put(Features.UNIVERSAL_SEARCH, writeResponseBodyToDisk(mContext, mv5.execute(), "universal_search_boxed2.m4v"));
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    private static String writeResponseBodyToDisk(Context mContext, Response<ResponseBody> response, String fileName) throws Exception {
        // todo change the file location/name according to your needs
        new File(mContext.getExternalFilesDir(null) + File.separator + "OnBoarding").mkdir();
        File destinationFile = new File(mContext.getExternalFilesDir(null) + File.separator + "OnBoarding" + File.separator + fileName);


        BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
        bufferedSink.writeAll(response.body().source());
        bufferedSink.close();

//        Log.d("IKIDO", "Done saving file" + destinationFile.getPath());
        return destinationFile.getAbsolutePath();
    }

    public static ArrayList<KaRestResponse.SharedList> convertContactTeamHoldersToSharedList(KaRestResponse.ContactList list) {
        ArrayList<KaRestResponse.SharedList> mList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ContactsTeamsHolder obj = list.get(i);
            KaRestResponse.SharedList rList = new KaRestResponse.SharedList();
            rList.setId(obj.getId());
            rList.setFirstName(obj.getFirstName());
            rList.setLastName(obj.getLastName());
            rList.setDisplayNameInitials(obj.getDisplayNameInitials());
            rList.setEmailId(obj.getEmailId());
            rList.setDisplayName(obj.getDisplayName());
            rList.setAtMention(obj.getAtMention());
            rList.setLabel(obj.getLabel());
            rList.setRole(obj.getRole());
            rList.setPfColor(obj.getPfColor());
            rList.setPhoneNo(obj.getPhoneNo());
            rList.setTeamName(obj.getTeamName());
            mList.add(rList);
        }
        return mList;
    }

    public static void getHelpData(final HelpDataLoadListener listener) {
        Call<KoraHelpModel> featureUtteranceModelCall = KaRestBuilder.getKaRestAPI().getFeatureUtterances(UserDataManager.getUserData().getId(), Utils.ah(UserDataManager.getAuthData().getAccessToken()));
        KaRestAPIHelper.enqueueWithRetry(featureUtteranceModelCall, new Callback<KoraHelpModel>() {
            @Override
            public void onResponse(Call<KoraHelpModel> call, Response<KoraHelpModel> response) {
                if (response.isSuccessful()) {
                    koraHelpModel = response.body();
                    if (listener != null) listener.onDataLoad();
                } else {
                    Log.d("error 1", "error body");
                    if (listener != null) listener.onError();
                }
            }

            @Override
            public void onFailure(Call<KoraHelpModel> call, Throwable t) {
                Log.d("error 1", t.getMessage());
                if (listener != null) listener.onError();
            }
        });

    }


    public static String getHowCanHelpModel(String skillName) {

        HowCanHelpTemplate howCanHelpTemplate = new HowCanHelpTemplate();
        howCanHelpTemplate.setTemplateType("kora_summary_help");
        howCanHelpTemplate.setIsNewVolley(true);
        HowCanHelpTemplate.Element element = new HowCanHelpTemplate.Element();
        element.setText("How can I help you?");

        List<HowCanHelpTemplate.Button> list = new ArrayList<>();


        //

        if (skillsListResponse != null && skillsListResponse.size() > 0) {
            for (SkillsListResponse skill : skillsListResponse) {
                if (skill.getName().equalsIgnoreCase(skillName) && skill.getUtterances() != null && skill.getUtterances().size() > 0) {
                    for (String uttrance : skill.getUtterances()) {
                        HowCanHelpTemplate.Button button = new HowCanHelpTemplate.Button();
                        if (!StringUtils.isNullOrEmptyWithTrim(uttrance)) {
                            button.setType("postback");
                            button.setPayload(uttrance.trim());
                            button.setTitle(uttrance.trim());
                            list.add(button);
                            if (list.size() == 3) {
                                break;
                            }
                        }
                    }
                }
            }


        }

        if (list.size() > 0) {
            element.setButtons(list);
            List<HowCanHelpTemplate.Element> rootList = new ArrayList<>();
            rootList.add(element);
            howCanHelpTemplate.setElements(rootList);
            return new Gson().toJson(howCanHelpTemplate);
        }
        return null;
    }


    public static ArrayList<SkillsListResponse> skillsListResponse;

    public static ArrayList<SkillsListResponse> getskillsListResponse() {
        return skillsListResponse;
    }

    public static void getSkills(final HelpDataLoadListener listener) {
        Call<ArrayList<SkillsListResponse>> featureUtteranceModelCall = KaRestBuilder.getKaRestAPI().getSkills(UserDataManager.getUserData().getId(), Utils.ah(UserDataManager.getAuthData().getAccessToken()));
        KaRestAPIHelper.enqueueWithRetry(featureUtteranceModelCall, new Callback<ArrayList<SkillsListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SkillsListResponse>> call, Response<ArrayList<SkillsListResponse>> response) {
                if (response.isSuccessful()) {
                    skillsListResponse = response.body();
                    if (listener != null) listener.onDataLoad();
                } else {
                    Log.d("error 1", "error body");
                    if (listener != null) listener.onError();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SkillsListResponse>> call, Throwable t) {
                Log.d("error 1", t.getMessage());
                if (listener != null) listener.onError();
            }
        });

    }

    public static void showErrorAlert(String errmsg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errmsg);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void launchWebViewActivity(Activity activity, String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
                url = "http://" + url.toLowerCase();
            }
            if (NetworkUtility.isNetworkConnectionAvailable(activity)) {
//                Intent intent = new Intent(activity, KaGenericWebViewActivity.class);
//                intent.putExtra("url", url);
//                intent.putExtra("header", activity.getResources().getString(R.string.app_name));
//
//                activity.startActivity(intent);
            } else {
                ToastUtils.showToast(activity, "Check your internet connection and please try again");
            }
        }
    }


}