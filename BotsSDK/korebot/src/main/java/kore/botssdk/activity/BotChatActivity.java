package kore.botssdk.activity;

import static android.view.View.VISIBLE;
import static kore.botssdk.FCM.FCMWrapper.GROUP_KEY_NOTIFICATIONS;
import static kore.botssdk.activity.KaCaptureImageActivity.rotateIfNecessary;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kore.ai.widgetsdk.fragments.BottomPanelFragment;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.kore.ai.widgetsdk.models.PanelBaseModel;
import com.kore.ai.widgetsdk.models.PanelResponseData;
import com.kore.ai.widgetsdk.views.widgetviews.CustomBottomSheetBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.bot.BotClient;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fragment.BotContentFragment;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.fragment.QuickReplyFragment;
import kore.botssdk.listener.BaseSocketConnectionManager;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.SocketChatListener;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.listener.ThemeChangeListener;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.BotResponsePayLoadText;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.BrandingNewModel;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.ComponentModelPayloadText;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.models.PayloadHeaderModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.models.WebHookRequestModel;
import kore.botssdk.models.WebHookResponseDataModel;
import kore.botssdk.models.limits.Attachment;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.RestBuilder;
import kore.botssdk.net.RestResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.net.WebHookRestBuilder;
import kore.botssdk.pushnotification.PushNotificationRegister;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.SharedPreferenceUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.TTSSynthesizer;
import kore.botssdk.websocket.SocketWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotChatActivity extends BotAppCompactActivity implements ComposeFooterInterface,
                                        QuickReplyFragment.QuickReplyInterface,
                                        TTSUpdate, InvokeGenericWebViewInterface, WidgetComposeFooterInterface, ThemeChangeListener
{
    String LOG_TAG = BotChatActivity.class.getSimpleName();
    FrameLayout chatLayoutFooterContainer;
    FrameLayout chatLayoutContentContainer;
    FrameLayout chatLayoutPanelContainer;
    ProgressBar taskProgressBar;
    FragmentTransaction fragmentTransaction;
    final Handler handler = new Handler();
    String chatBot, taskBotId, jwt;
    Handler actionBarTitleUpdateHandler;
    BotClient botClient;
    BotContentFragment botContentFragment;
    ComposeFooterFragment composeFooterFragment;
    TTSSynthesizer ttsSynthesizer;
    QuickReplyFragment quickReplyFragment;
    BotContentFragmentUpdate botContentFragmentUpdate;
    ComposeFooterUpdate composeFooterUpdate;
    boolean isItFirstConnect = true;
    private final Gson gson = new Gson();
    //For Bottom Panel
    private final String packageName = "com.kore.koreapp";
    private final String appName = "Kore";
    //Fragment Approch
    private FrameLayout composerView;
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private BottomPanelFragment composerFragment;
    private SharedPreferences sharedPreferences;
    private ImageView ivChaseBackground, ivChaseLogo;
    protected int compressQualityInt = 100;
    protected Attachment attachment;
    Handler messageHandler = new Handler();
    protected static long totalFileSize;
    private String fileUrl;
    private ArrayList<BrandingNewModel> arrBrandingNewDos;
    private WebHookResponseDataModel webHookResponseDataModel;
    private BotMetaModel botMetaModel;
    private Runnable runnable;
    private final int poll_delay = 2000;
    private String lastMsgId = "";
    private String strResp = "{\n" +
            "   \"type\":\"bot_response\",\n" +
            "   \"from\":\"bot\",\n" +
            "   \"message\":[\n" +
            "      {\n" +
            "         \"type\":\"text\",\n" +
            "         \"component\":{\n" +
            "            \"type\":\"template\",\n" +
            "            \"payload\":{\n" +
            "               \"type\":\"template\",\n" +
            "               \"payload\":{\n" +
            "                  \"template_type\":\"advancedListTemplate\",\n" +
            "                  \"title\":\"Main Title\",\n" +
            "                  \"isSortEnabled\":true,\n" +
            "                  \"isSearchEnabled\":false,\n" +
            "                  \"isButtonAvailable\":false,\n" +
            "                  \"description\":\"Main title description\",\n" +
            "                  \"listViewType\":\"default\",\n" +
            "                  \"listItems\":[\n" +
            "                     {\n" +
            "                        \"title\":\"Title\",\n" +
            "                        \"description\":\"Description\",\n" +
            "                        \"descriptionIcon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFzSURBVHgBjVJBTgJBEKzu2cS9KT/gB+4T9AXCzYPKiiHCCXiB+ANukhBx1g/ID/AH7g/kCdzcAzttz27WIEKkk8n09HTNdHcVYcPieHDiOOuTuiDUyyilgKSU49HaybLK5cq5ueu1xHx9KqBGjFvKw1qxSIaiaDH0cd3uDap8KkH3fRIakMub1k7TItbujvz+OpuMymq6dWEsmGj08vyUsA8oekQO5xVol/kyyeRNB4x9SywGD/rxfLP+veDpNBUnic5hoD1KRIQEBxobzDW/FehvEdbhzhKJ6fhSW6nOIcIV1qsUJqzz3peBJZw0jnQg1YKWWN0HEE0Iskj991/9zCbWb9sPXsXdM6O8snKUuBwNHGjGoOVFweyyMbG04k4n+g/kqYNIw6uIrbUrJX8ozrzFG4P4C+pESt2CmAvp0c+FyknIFZzmOZIA5aTXyCIT0AWci8nQ0E6L3kHbpTijKoKcFjR50+GpdhOlbGzteFXlfgNFTZhUpiJhVAAAAABJRU5ErkJggg==\",\n" +
            "                        \"descriptionIconAlignment\":\"right\",\n" +
            "                        \"descriptionStyles\":{\n" +
            "                           \"font-size\":\"10px\",\n" +
            "                           \"color\":\"#10f4f4\"\n" +
            "                        },\n" +
            "                        \"titleStyles\":{\n" +
            "                           \"font-size\":\"14px\",\n" +
            "                           \"color\":\"#9bf410\"\n" +
            "                        },\n" +
            "                        \"elementStyles\":{\n" +
            "                           \"background\":\"#fef100\"\n" +
            "                        }\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text[title and description,icon]\",\n" +
            "                        \"description\":\"Description\",\n" +
            "                        \"icon\":\"https://kore.ai/wp-content/uploads/2021/09/kore.ai_logo.svg\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text\",\n" +
            "                        \"description\":\"List item with button\",\n" +
            "                        \"iconShape\":\"circle-img\",\n" +
            "                        \"icon\":\"https://kore.ai/wp-content/uploads/2021/09/kore.ai_logo.svg\",\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"contenttype\":\"button\",\n" +
            "                              \"title\":\"Button\",\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\",\n" +
            "                              \"buttonStyles\":{\n" +
            "                                 \"color\":\"#10f4f4\"\n" +
            "                              }\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text [with dropdown]\",\n" +
            "                        \"description\":\"title 1 description\",\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"type\":\"dropdown\",\n" +
            "                              \"dropdownOptions\":[\n" +
            "                                 {\n" +
            "                                    \"title\":\"option1\",\n" +
            "                                    \"type\":\"postback\",\n" +
            "                                    \"payload\":\"USER_DEFINED_payload\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"option2\",\n" +
            "                                    \"type\":\"postback\",\n" +
            "                                    \"payload\":\"USER_DEFINED_payload\"\n" +
            "                                 }\n" +
            "                              ]\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text [with default view]\",\n" +
            "                        \"description\":\"See more action dropdown and display limit 3\",\n" +
            "                        \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\",\n" +
            "                        \"isCollapsed\":true,\n" +
            "                        \"imageSize\":\"small\",\n" +
            "                        \"iconShape\":\"circle-img\",\n" +
            "                        \"view\":\"default\",\n" +
            "                        \"iconSize\":\"small\",\n" +
            "                        \"isAccordian\":true,\n" +
            "                        \"textInformation\":[\n" +
            "                           {\n" +
            "                              \"title\":\"Oct 9, 9:00am - 9:30am (Day 1/2)\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFzSURBVHgBjVJBTgJBEKzu2cS9KT/gB+4T9AXCzYPKiiHCCXiB+ANukhBx1g/ID/AH7g/kCdzcAzttz27WIEKkk8n09HTNdHcVYcPieHDiOOuTuiDUyyilgKSU49HaybLK5cq5ueu1xHx9KqBGjFvKw1qxSIaiaDH0cd3uDap8KkH3fRIakMub1k7TItbujvz+OpuMymq6dWEsmGj08vyUsA8oekQO5xVol/kyyeRNB4x9SywGD/rxfLP+veDpNBUnic5hoD1KRIQEBxobzDW/FehvEdbhzhKJ6fhSW6nOIcIV1qsUJqzz3peBJZw0jnQg1YKWWN0HEE0Iskj991/9zCbWb9sPXsXdM6O8snKUuBwNHGjGoOVFweyyMbG04k4n+g/kqYNIw6uIrbUrJX8ozrzFG4P4C+pESt2CmAvp0c+FyknIFZzmOZIA5aTXyCIT0AWci8nQ0E6L3kHbpTijKoKcFjR50+GpdhOlbGzteFXlfgNFTZhUpiJhVAAAAABJRU5ErkJggg==\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"title\":\"WebEx\",\n" +
            "                              \"type\":\"web_url\",\n" +
            "                              \"url\":\"https://petersapparel.parseapp.com\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFHSURBVHgBdZC/TgJBEMZnZk+PxOZ4Ax4Bow+ApSWdsYBFJGInb8BV2omVpyQw+gI8gpQWJpydJW8gjQGTux13+aOixyaT7M58M/ubD2HD0bpZEAV9ey3ZGIsAP/aiELPFjaIoekKkEBKfYeujIKkaGMAbL3M8eSUkaXH3lpeZWJ82ayQw+NdwZFHA+My9zmStkMxiULnAW8dQAxAJBKZQrZ8NMcUWczR2daP8quUf0hozQfjQu8u7QMRXINCuXqmfV1GgjSnUcOGGjJDstG7ElZNm22rEOfIjNh005oC5G3vOOufGakEywKLw2iK9z1lFJiuxe3pznxO/vNplyVzW+iKA3CzgaLHDt4F2xAS8WfF30mGk9Kn/iuc/oFAoIP1j3Shvw87YqKljbpPFgIyj4tHL8+7efp6ALoGSKwTMocFD5vu3rIYvr/CSUC3Azu4AAAAASUVORK5CYII=\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"title\":\"Text Info Title\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAGbSURBVHgBdVJNTgJRDG77iBpXHAFPICTuxRu4cydPNHFc6dIdegLZMQt+Bj2AcAJgbwKeALgBbogY5tV2zEMY9CWTee18/fr16yCkjrV32SUsikSc19g5HGVgtx9F1dk6zqwH55fXt0DLDhHkJJxqTu5nkrs/LBxl34dvA49FfymVg5ZEOYzhIorCyaaKIMcGWnKdtBvhxarwvBxUEKEoyRMvN6a5TSQ56nii0mXQc4zdl2atStbarBRZ7fTL/jkkpENEykunnhIlXQRDCBWNaWl2iirBs8bkTuXVV0nPjdAywCCmRdL9B8MjMPNihlhY5SP8c2QW5rVYggED5TNpoHFfEZvdsczjS0+N48c0jhy6kcx47E1hs/cq4JF0+dBHWszY8NNqTsGS1qg52gFjLIgR6u40qocPG+u4Ch5E4rGawwQ9dHuFZB22fHPH6CrAOGs3wwP448iexzqwELeVmBK3ZC8M2JXsTNeRLkpymBg18GpwS5KDkmQn6p6fSebMI9FjVK9V19xOs4sJsicna0rcEyMg3t/6yb8BlCK9m52XgX8AAAAASUVORK5CYII=\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"seeMoreAction\":\"dropdown\",\n" +
            "                        \"buttonsLayout\":{\n" +
            "                           \"displayLimit\":{\n" +
            "                              \"count\":\"3\"\n" +
            "                           },\n" +
            "                           \"buttonAligment\":\"fullwidth\"\n" +
            "                        },\n" +
            "                        \"buttons\":[\n" +
            "                           {\n" +
            "                              \"type\":\"url\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFFSURBVHgBnZGxTsMwEIZ9tlCDuoS5QuQRGAsSonmEPkH7BvQBkOIgdsrIVDIx8ghNGVCBoRnpFiS6w1IFkvj4LQGqqpRI/JId3ymf/7uzEpDTDgPVOo6xRLmYTESNlAWYTZ+Iuog1QLcOlBYwhrrZVMcAfSztHJ7v1UDCbTZFagNmPsEnye5PX/6EiESyXJqBDeDyzoBEjchpa88YnjGTn+cibTR4RiR1Ng2ijZDdAPZRWmB7KkvhSslj9OnnT7rSVdmteI2TrV1/B+CFlHQFeE7EN6p1NC8Xd89uR7uO13GyNM5+nX4ERw2wZx0RejiP0GOMn/YxMA+jGn4+noW0br0GCmOMllKmOF4jP0bmkqpqXgXxfulKvoP8bSVktX0QDDCUwJYEp6goCk8pNQIUbYS+b/ZseXDsoac3vGn48RAOxX/0BeH9oUngtmx1AAAAAElFTkSuQmCC\",\n" +
            "                              \"title\":\"Button 1 url\",\n" +
            "                              \"url\":\"https://bankingassistant-qa-bots.kore.ai/botbuilder\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFFSURBVHgBnZGxTsMwEIZ9tlCDuoS5QuQRGAsSonmEPkH7BvQBkOIgdsrIVDIx8ghNGVCBoRnpFiS6w1IFkvj4LQGqqpRI/JId3ymf/7uzEpDTDgPVOo6xRLmYTESNlAWYTZ+Iuog1QLcOlBYwhrrZVMcAfSztHJ7v1UDCbTZFagNmPsEnye5PX/6EiESyXJqBDeDyzoBEjchpa88YnjGTn+cibTR4RiR1Ng2ijZDdAPZRWmB7KkvhSslj9OnnT7rSVdmteI2TrV1/B+CFlHQFeE7EN6p1NC8Xd89uR7uO13GyNM5+nX4ERw2wZx0RejiP0GOMn/YxMA+jGn4+noW0br0GCmOMllKmOF4jP0bmkqpqXgXxfulKvoP8bSVktX0QDDCUwJYEp6goCk8pNQIUbYS+b/ZseXDsoac3vGn48RAOxX/0BeH9oUngtmx1AAAAAElFTkSuQmCC\",\n" +
            "                              \"title\":\"Button 1\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFFSURBVHgBnZGxTsMwEIZ9tlCDuoS5QuQRGAsSonmEPkH7BvQBkOIgdsrIVDIx8ghNGVCBoRnpFiS6w1IFkvj4LQGqqpRI/JId3ymf/7uzEpDTDgPVOo6xRLmYTESNlAWYTZ+Iuog1QLcOlBYwhrrZVMcAfSztHJ7v1UDCbTZFagNmPsEnye5PX/6EiESyXJqBDeDyzoBEjchpa88YnjGTn+cibTR4RiR1Ng2ijZDdAPZRWmB7KkvhSslj9OnnT7rSVdmteI2TrV1/B+CFlHQFeE7EN6p1NC8Xd89uR7uO13GyNM5+nX4ERw2wZx0RejiP0GOMn/YxMA+jGn4+noW0br0GCmOMllKmOF4jP0bmkqpqXgXxfulKvoP8bSVktX0QDDCUwJYEp6goCk8pNQIUbYS+b/ZseXDsoac3vGn48RAOxX/0BeH9oUngtmx1AAAAAElFTkSuQmCC\",\n" +
            "                              \"title\":\"Button 2\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFFSURBVHgBnZGxTsMwEIZ9tlCDuoS5QuQRGAsSonmEPkH7BvQBkOIgdsrIVDIx8ghNGVCBoRnpFiS6w1IFkvj4LQGqqpRI/JId3ymf/7uzEpDTDgPVOo6xRLmYTESNlAWYTZ+Iuog1QLcOlBYwhrrZVMcAfSztHJ7v1UDCbTZFagNmPsEnye5PX/6EiESyXJqBDeDyzoBEjchpa88YnjGTn+cibTR4RiR1Ng2ijZDdAPZRWmB7KkvhSslj9OnnT7rSVdmteI2TrV1/B+CFlHQFeE7EN6p1NC8Xd89uR7uO13GyNM5+nX4ERw2wZx0RejiP0GOMn/YxMA+jGn4+noW0br0GCmOMllKmOF4jP0bmkqpqXgXxfulKvoP8bSVktX0QDDCUwJYEp6goCk8pNQIUbYS+b/ZseXDsoac3vGn48RAOxX/0BeH9oUngtmx1AAAAAElFTkSuQmCC\",\n" +
            "                              \"title\":\"Button 2\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAAOCAYAAAD0f5bSAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFFSURBVHgBnZGxTsMwEIZ9tlCDuoS5QuQRGAsSonmEPkH7BvQBkOIgdsrIVDIx8ghNGVCBoRnpFiS6w1IFkvj4LQGqqpRI/JId3ymf/7uzEpDTDgPVOo6xRLmYTESNlAWYTZ+Iuog1QLcOlBYwhrrZVMcAfSztHJ7v1UDCbTZFagNmPsEnye5PX/6EiESyXJqBDeDyzoBEjchpa88YnjGTn+cibTR4RiR1Ng2ijZDdAPZRWmB7KkvhSslj9OnnT7rSVdmteI2TrV1/B+CFlHQFeE7EN6p1NC8Xd89uR7uO13GyNM5+nX4ERw2wZx0RejiP0GOMn/YxMA+jGn4+noW0br0GCmOMllKmOF4jP0bmkqpqXgXxfulKvoP8bSVktX0QDDCUwJYEp6goCk8pNQIUbYS+b/ZseXDsoac3vGn48RAOxX/0BeH9oUngtmx1AAAAAElFTkSuQmCC\",\n" +
            "                              \"title\":\"Button 2\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"type\":\"icon\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAHCAYAAAA8sqwkAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABdSURBVHgBjctNDYAwDIbhNkUAoKAZCOCIHBwhASzgCAfDQelhh2Xrfr5Tkz4vgDF2y8VuPa0fWRgEDz33cZ748/4pBhEOwy2NqIztiOo4j7CN407uQTGDyNsVqP0BaHUk0IS2sYcAAAAASUVORK5CYII=\"\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text [view options] \",\n" +
            "                        \"description\":\"List item with check box options\",\n" +
            "                        \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\",\n" +
            "                        \"isCollapsed\":true,\n" +
            "                        \"iconSize\":\"small\",\n" +
            "                        \"imageSize\":\"large\",\n" +
            "                        \"isAccordian\":true,\n" +
            "                        \"view\":\"options\",\n" +
            "                        \"optionsData\":[\n" +
            "                           {\n" +
            "                              \"id\":\"1\",\n" +
            "                              \"type\":\"checkbox\",\n" +
            "                              \"label\":\"1 Lorem ipsum doller ammen\",\n" +
            "                              \"value\":\"1\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"id\":\"2\",\n" +
            "                              \"type\":\"checkbox\",\n" +
            "                              \"label\":\"2 Lorem ipsum doller ammen\",\n" +
            "                              \"value\":\"2\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"buttons\":[\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"btnType\":\"confirm\",\n" +
            "                              \"title\":\"Confirm\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"btnType\":\"cancel\",\n" +
            "                              \"title\":\"Cancel\",\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"buttonAligment\":\"right\",\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"type\":\"text\",\n" +
            "                              \"value\":\"20$\",\n" +
            "                              \"styles\":{\n" +
            "                                 \"color\":\"#105bf4\",\n" +
            "                                 \"font-size\":\"10px\"\n" +
            "                              }\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"Title text [view options]\",\n" +
            "                        \"description\":\"List item with radio options\",\n" +
            "                        \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\",\n" +
            "                        \"isCollapsed\":false,\n" +
            "                        \"iconSize\":\"small\",\n" +
            "                        \"imageSize\":\"medium\",\n" +
            "                        \"iconShape\":\"circle-img\",\n" +
            "                        \"isAccordian\":true,\n" +
            "                        \"view\":\"options\",\n" +
            "                        \"optionsData\":[\n" +
            "                           {\n" +
            "                              \"id\":\"1\",\n" +
            "                              \"type\":\"radio\",\n" +
            "                              \"label\":\"1 Lorem ipsum doller ammen\",\n" +
            "                              \"value\":\"1\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"id\":\"2\",\n" +
            "                              \"type\":\"radio\",\n" +
            "                              \"label\":\"2 Lorem ipsum doller ammen\",\n" +
            "                              \"value\":\"2\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"buttons\":[\n" +
            "                           {\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"btnType\":\"confirm\",\n" +
            "                              \"title\":\"Confirm\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"btnType\":\"cancel\",\n" +
            "                              \"title\":\"Cancel\",\n" +
            "                              \"type\":\"postback\",\n" +
            "                              \"payload\":\"USER_DEFINED_PAYLOAD\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"type\":\"text\",\n" +
            "                              \"value\":\"20$\",\n" +
            "                              \"styles\":{\n" +
            "                                 \"color\":\"#105bf4\",\n" +
            "                                 \"font-size\":\"10px\"\n" +
            "                              }\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"icon\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAMCAYAAACulacQAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAB7SURBVHgBpY7BDUZAEIVnfr+zEQ2M4K4ESlABpehAK3SgEbEVsA3IGhIOa+PiS+ZdvjfJA3iDOS04TmYWbOdpvSiiKATEjigYtHDLI6Qwugre1XIV0LGhBTQ1mK38PSdiL8cAPllfWc5xujInzWchuITwP9NApdQ02nIHzFIyUM1lTqwAAAAASUVORK5CYII=\"\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"title\":\"list item [view = \\\"table\\\"]\",\n" +
            "                        \"description\":\"list item [view = \\\"table\\\"]\",\n" +
            "                        \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\",\n" +
            "                        \"isCollapsed\":false,\n" +
            "                        \"iconSize\":\"small\",\n" +
            "                        \"isAccordian\":true,\n" +
            "                        \"view\":\"table\",\n" +
            "                        \"headerOptions\":[\n" +
            "                           {\n" +
            "                              \"contenttype\":\"button\",\n" +
            "                              \"title\":\"confirm\",\n" +
            "                              \"isStatus\":true,\n" +
            "                              \"buttonStyles\":{\n" +
            "                                 \"background\":\"#00f3fe\",\n" +
            "                                 \"color\":\"#fe0000\",\n" +
            "                                 \"border\":\"1px solid #fe0000\"\n" +
            "                              }\n" +
            "                           },\n" +
            "                           {\n" +
            "                              \"type\":\"icon\",\n" +
            "                              \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAMCAYAAACulacQAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAB7SURBVHgBpY7BDUZAEIVnfr+zEQ2M4K4ESlABpehAK3SgEbEVsA3IGhIOa+PiS+ZdvjfJA3iDOS04TmYWbOdpvSiiKATEjigYtHDLI6Qwugre1XIV0LGhBTQ1mK38PSdiL8cAPllfWc5xujInzWchuITwP9NApdQ02nIHzFIyUM1lTqwAAAAASUVORK5CYII=\"\n" +
            "                           }\n" +
            "                        ],\n" +
            "                        \"type\":\"column\",\n" +
            "                        \"tableListData\":[\n" +
            "                           {\n" +
            "                              \"rowData\":[\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text 1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"iconSize\":\"small\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"iconSize\":\"large\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 },\n" +
            "                                 {\n" +
            "                                    \"title\":\"Text1\",\n" +
            "                                    \"description\":\"Value\",\n" +
            "                                    \"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAApCAYAAABHomvIAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAL5SURBVHgB7ZhPSBRRHMe/762iJmt6SFii2oEOBR6Mog5dTDoFkXWySyodOihSl8DoUCB6LMJTILlC5Kk1hE6ueemwVrTQQtIKs2WxaAdLE8WYfb3fbDvsumPse03uHPrAMvPe22G/8/v33v4YXDDNlUarOtDBGAtjFxCWSASqrYQRakpvX2OFAzOzEgZqHslH2lAZxoCtu4VCHYEfMxtdWWbdh2CNqCQM37jg1w+F6iK5oSS1uNZWVcVfwFdkzxih4Kwt0Mysm/IShr9IY3PrWGAh86Obg3XDfzRmA3yJB4Au+BTO+AVpPNYKv8JEK4dAZbP2T0htHD7H9wKr4BFflr8jOpO070+2HJCfg/ACzwS+N79iZOKlfX+xvcUzgdouJmutrW8641j8g3M/HU85a3TNW1YHJncRAUUGHjy3f3R/8150nT8hrbdUIoLWyNXT8QVbJFl1uP8cVFEWSLHWcSNSZL1yiT28ZgtXQdnFOasdhyoDV9uVxRFaMUiucyNYXyNFNLiuHTWaoYNWFkem3pTMkYUoHgkKgyu3n8jrqrNOiaOT2Uox2Dv8FHPJzyXx55YAc8lPUuRE0VywvlZach/GBy+jXJQsGJMZ6YabZdzm6MXmkotQwZOtjlxazpwOSgLnJ28ieq8bR7YFfGTqdYmgodGZojE9Mz7YiVeP+6GCVqGeN5dlLRwrmssX5gYZZ3EZf/SdQiZdXqwcPD4s7OzWVY3CTmjF4NBoDKrkDxKqaAks3BHIbTsVZyrcbs+ooOViqnlU086eOuyUk97hqCxDqd9iGjAycMnePaIz7+yj2C1ZyHXQShI36NTSJws5oXtyccOzJKEM7us87dx7hWcW/Ff8/1f3t3Bqd8GvUCsOQiTgVwRLUOvjGfyKlY0w6kejtuatlBuGr2BpI7TH4IbRJGPQ6oHvyGmys5hardKCPb5IGFuD6MlpKunyb4QFrDsMrEJNTTELBKS4unR+hrl9jYQi+7M1i91pbnIuZD86OGkYrMSDvwDabB9xyb55JAAAAABJRU5ErkJggg==\"\n" +
            "                                 }\n" +
            "                              ]\n" +
            "                           }\n" +
            "                        ]\n" +
            "                     }\n" +
            "                  ],\n" +
            "                  \"seeMoreTitle\":\"test\",\n" +
            "                  \"previewModalTitle\":\"Preview title\",\n" +
            "                  \"seeMoreIcon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFzSURBVHgBjVJBTgJBEKzu2cS9KT/gB+4T9AXCzYPKiiHCCXiB+ANukhBx1g/ID/AH7g/kCdzcAzttz27WIEKkk8n09HTNdHcVYcPieHDiOOuTuiDUyyilgKSU49HaybLK5cq5ueu1xHx9KqBGjFvKw1qxSIaiaDH0cd3uDap8KkH3fRIakMub1k7TItbujvz+OpuMymq6dWEsmGj08vyUsA8oekQO5xVol/kyyeRNB4x9SywGD/rxfLP+veDpNBUnic5hoD1KRIQEBxobzDW/FehvEdbhzhKJ6fhSW6nOIcIV1qsUJqzz3peBJZw0jnQg1YKWWN0HEE0Iskj991/9zCbWb9sPXsXdM6O8snKUuBwNHGjGoOVFweyyMbG04k4n+g/kqYNIw6uIrbUrJX8ozrzFG4P4C+pESt2CmAvp0c+FyknIFZzmOZIA5aTXyCIT0AWci8nQ0E6L3kHbpTijKoKcFjR50+GpdhOlbGzteFXlfgNFTZhUpiJhVAAAAABJRU5ErkJggg==\"\n" +
            "               }\n" +
            "            }\n" +
            "         },\n" +
            "         \"cInfo\":{\n" +
            "            \"body\":\"{\\\"type\\\":\\\"link\\\",\\\"payload\\\":{\\\"download\\\":true,\\\"fileName\\\":\\\"sample.pdf\\\",\\\"url\\\":\\\"\\\"}}\"\n" +
            "         }\n" +
            "      }\n" +
            "   ],\n" +
            "   \"messageId\":\"ms-d788923c-8376-5042-8c29-ad5feffe7d56\",\n" +
            "   \"botInfo\":{\n" +
            "      \"channelClient\":\"Android\",\n" +
            "      \"chatBot\":\"SDKBot\",\n" +
            "      \"taskBotId\":\"st-b9889c46-218c-58f7-838f-73ae9203488c\"\n" +
            "   },\n" +
            "   \"createdOn\":\"2023-06-29T10:11:11.740Z\",\n" +
            "   \"xTraceId\":111323490,\n" +
            "   \"icon\":\"https://dlnwzkim0wron.cloudfront.net/f-5e050d52-6c4a-5442-b7e6-2ddbc93e4df7.png\",\n" +
            "   \"traceId\":\"780da63c10cc2b16\"\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_chat_layout);

        findViews();
        getBundleInfo();
        getDataFromTxt();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Add Bot Content Fragment
        botContentFragment = new BotContentFragment();
        botContentFragment.setArguments(getIntent().getExtras());
        botContentFragment.setComposeFooterInterface(this);
        botContentFragment.setInvokeGenericWebViewInterface(this);
        botContentFragment.setThemeChangeInterface(this);
        fragmentTransaction.add(R.id.chatLayoutContentContainer, botContentFragment).commit();
        setBotContentFragmentUpdate(botContentFragment);

        //Add Suggestion Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        quickReplyFragment = new QuickReplyFragment();
        quickReplyFragment.setArguments(getIntent().getExtras());
        quickReplyFragment.setListener(BotChatActivity.this);
        fragmentTransaction.add(R.id.quickReplyLayoutFooterContainer, quickReplyFragment).commit();

        //Add Bot Compose Footer Fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        composeFooterFragment = new ComposeFooterFragment();
        composeFooterFragment.setArguments(getIntent().getExtras());
        composeFooterFragment.setComposeFooterInterface(this);
        composeFooterFragment.setBottomOptionData(getDataFromTxt());
        fragmentTransaction.add(R.id.chatLayoutFooterContainer, composeFooterFragment).commit();
        setComposeFooterUpdate(composeFooterFragment);

        updateTitleBar();

        botClient = new BotClient(this);
        ttsSynthesizer = new TTSSynthesizer(this);
        setupTextToSpeech();
        KoreEventCenter.register(this);
        attachFragments();

        if(!SDKConfiguration.Client.isWebHook)
        {
            BotSocketConnectionManager.getInstance().setChatListener(sListener);
        }
        else
            BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(getApplicationContext(),null);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isOnline()) {
                    BotSocketConnectionManager.killInstance();
                }
                finish();
            }
        });
    }

    SocketChatListener sListener = new SocketChatListener() {
        @Override
        public void onMessage(BotResponse botResponse) {
            processPayload("", botResponse);
        }

        @Override
        public void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection) {
            if(state == BaseSocketConnectionManager.CONNECTION_STATE.CONNECTED){
                getBrandingDetails();
            }

            new PushNotificationRegister().registerPushNotification(BotChatActivity.this, botClient.getUserId(), botClient.getAccessToken(), sharedPreferences.getString("FCMToken", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)));

            updateTitleBar(state);
        }

        @Override
        public void onMessage(SocketDataTransferModel data) {
            if (data == null) return;
            if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
                processPayload(strResp, null);

            } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
                if (botContentFragment != null) {
                    botContentFragment.updateContentListOnSend(data.getBotRequest());
                }
            }
        }
    };

    public void postNotification(String title, String pushMessage) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = null;
        if(Build.VERSION.SDK_INT >= 26){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("Kore_Push_Service","Kore_Android",importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            nBuilder = new NotificationCompat.Builder(this,notificationChannel.getId());
        }else {
            nBuilder = new NotificationCompat.Builder(this);
        }

        nBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(Color.parseColor("#009dab"))
                .setContentText(pushMessage)
                .setGroup(GROUP_KEY_NOTIFICATIONS)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        if (alarmSound != null) {
            nBuilder.setSound(alarmSound);
        }

        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.PICK_TYPE, "Notification");
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, SDKConfiguration.Client.bot_name.charAt(0)+"");
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        nBuilder.setContentIntent(pendingIntent);

        Notification notification = nBuilder.build();
        notification.ledARGB = 0xff0000FF;

        mNotificationManager.notify("YUIYUYIU", 237891, notification);
    }

    @Override
    protected void onDestroy() {
        botClient.disconnect();
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jwt = bundle.getString(BundleUtils.JWT_TKN, "");
        }
        chatBot = SDKConfiguration.Client.bot_name;
        taskBotId = SDKConfiguration.Client.bot_id;
    }

    private void findViews() {
        chatLayoutFooterContainer = (FrameLayout) findViewById(R.id.chatLayoutFooterContainer);
        chatLayoutContentContainer = (FrameLayout) findViewById(R.id.chatLayoutContentContainer);
        chatLayoutPanelContainer   = (FrameLayout) findViewById(R.id.chatLayoutPanelContainer);
        taskProgressBar = (ProgressBar) findViewById(R.id.taskProgressBar);
        ivChaseBackground = (ImageView) findViewById(R.id.ivChaseBackground);
        ivChaseLogo = (ImageView) findViewById(R.id.ivChaseLogo);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        RestBuilder.setContext(BotChatActivity.this);
        WebHookRestBuilder.setContext(BotChatActivity.this);
        BrandingRestBuilder.setContext(BotChatActivity.this);
    }

    private void updateTitleBar() {
//        String botName = (chatBot != null && !chatBot.isEmpty()) ? chatBot : ((SDKConfiguration.Server.IS_ANONYMOUS_USER) ? chatBot + " - anonymous" : chatBot);
//        getSupportActionBar().setSubtitle(botName);
    }

    private void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE socketConnectionEvents) {

        String titleMsg = "";
        switch (socketConnectionEvents) {
            case CONNECTING:
                titleMsg = getString(R.string.socket_connecting);
                taskProgressBar.setVisibility(View.VISIBLE);
                updateActionBar();
                break;
            case CONNECTED:
                /*if(isItFirstConnect)
                    botClient.sendMessage("welcomedialog");*/
                titleMsg = getString(R.string.socket_connected);
                taskProgressBar.setVisibility(View.GONE);
                composeFooterFragment.enableSendButton();
                updateActionBar();

                break;
            case DISCONNECTED:
            case CONNECTED_BUT_DISCONNECTED:
                titleMsg = getString(R.string.socket_disconnected);
                taskProgressBar.setVisibility(View.VISIBLE);
                composeFooterFragment.setDisabled(true);
                composeFooterFragment.updateUI();
                updateActionBar();
                break;

            default:
                titleMsg = getString(R.string.socket_connecting);
                taskProgressBar.setVisibility(View.GONE);
                updateActionBar();

        }
    }


    private void setupTextToSpeech() {
        composeFooterFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
        botContentFragment.setTtsUpdate(BotSocketConnectionManager.getInstance());
    }

    public void onEvent(String jwt)
    {
        this.jwt = jwt;
        if(botContentFragment != null)
            botContentFragment.setJwtTokenForWebHook(jwt);

        if(composeFooterFragment != null)
            composeFooterFragment.setJwtToken(jwt);

        getWebHookMeta();
    }

    public void onEvent(SocketDataTransferModel data) {
        if (data == null) return;
        if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE)) {
            processPayload(data.getPayLoad(), null);
        } else if (data.getEvent_type().equals(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE)) {
            if (botContentFragment != null) {
                botContentFragment.updateContentListOnSend(data.getBotRequest());
            }
        }
    }

    public void onEvent(BaseSocketConnectionManager.CONNECTION_STATE states) {
        updateTitleBar(states);
    }

    public void onEvent(BrandingModel brandingModel)
    {
        SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBotchatBgColor());
        editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBotchatTextColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getUserchatBgColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getUserchatTextColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingModel.getButtonActiveBgColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingModel.getButtonActiveTextColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingModel.getButtonInactiveBgColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingModel.getButtonInactiveTextColor());
        editor.putString(BotResponse.WIDGET_BG_COLOR, brandingModel.getWidgetBgColor());
        editor.putString(BotResponse.WIDGET_TXT_COLOR, brandingModel.getWidgetTextColor());
        editor.putString(BotResponse.WIDGET_BORDER_COLOR, brandingModel.getWidgetBorderColor());
        editor.putString(BotResponse.WIDGET_DIVIDER_COLOR, brandingModel.getWidgetDividerColor());
        editor.apply();

        SDKConfiguration.BubbleColors.quickReplyColor = brandingModel.getButtonActiveBgColor();
        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingModel.getButtonActiveTextColor();

        if(botContentFragment != null)
            botContentFragment.changeThemeBackGround(brandingModel.getWidgetBgColor(), brandingModel.getWidgetTextColor());
    }


    public void onEvent(BotResponse botResponse) {
        processPayload("", botResponse);
    }

    public void updateActionbar(boolean isSelected,String type,ArrayList<BotButtonModel> buttonModels) {

    }

    @Override
    public void lauchMeetingNotesAction(Context context, String mid, String eid) {

    }

    @Override
    public void showAfterOnboard(boolean isdiscard) {

    }

    @Override
    public void onPanelClicked(Object pModel, boolean isFirstLaunch) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }

    @Override
    public void externalReadWritePermission(String fileUrl)
    {
        this.fileUrl = fileUrl;
        if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        else
        {
            KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onDeepLinkClicked(String url) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*,Manifest.permission.RECORD_AUDIO*/)) {

                if (!StringUtils.isNullOrEmpty(fileUrl))
                    KaMediaUtils.saveFileFromUrlToKorePath(BotChatActivity.this, fileUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateActionBar() {
        if (actionBarTitleUpdateHandler == null) {
            actionBarTitleUpdateHandler = new Handler();
        }

        actionBarTitleUpdateHandler.removeCallbacks(actionBarUpdateRunnable);
        actionBarTitleUpdateHandler.postDelayed(actionBarUpdateRunnable, 4000);

    }

    Runnable actionBarUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTitleBar();
        }
    };

    @Override
    protected void onPause() {
        BotApplication.activityPaused();
        ttsSynthesizer.stopTextToSpeech();
        super.onPause();
    }




    @Override
    public void onSendClick(String message,boolean isFromUtterance) {
        if(!StringUtils.isNullOrEmpty(message))
        {
            if(!SDKConfiguration.Client.isWebHook)
                BotSocketConnectionManager.getInstance().sendMessage(message, null);
            else
            {
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, null);
                BotSocketConnectionManager.getInstance().stopTextToSpeech();
            }
        }
    }


    @Override
    public void onSendClick(String message, String payload, boolean isFromUtterance)
    {
        if(!SDKConfiguration.Client.isWebHook)
        {
            if(payload != null){
                BotSocketConnectionManager.getInstance().sendPayload(message, payload);
            }else{
                BotSocketConnectionManager.getInstance().sendMessage(message, "");
            }
        }
        else
        {
            BotSocketConnectionManager.getInstance().stopTextToSpeech();
            if(payload != null)
            {
                addSentMessageToChat(message);
                sendWebHookMessage(false, payload, null);
            }
            else
            {
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, null);
            }
        }

        toggleQuickRepliesVisiblity();
    }

    @Override
    public void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance) {
        if(attachments != null && attachments.size() > 0)
        {
            if(!SDKConfiguration.Client.isWebHook)
                BotSocketConnectionManager.getInstance().sendAttachmentMessage(message, attachments);
            else
            {
//                BotSocketConnectionManager.getInstance().stopTextToSpeech();
                addSentMessageToChat(message);
                sendWebHookMessage(false, message, attachments);
            }
        }
    }

    @Override
    public void onFormActionButtonClicked(FormActionTemplate fTemplate) {

    }

    @Override
    public void launchActivityWithBundle(String type, Bundle payload) {

    }

    @Override
    public void sendWithSomeDelay(String message, String payload,long time,boolean isScrollupNeeded) {
        LogUtils.e("Message", message);
    }

    @Override
    public void copyMessageToComposer(String text, boolean isForOnboard) {
        composeFooterFragment.setComposeText(text);
    }

    @Override
    public void showMentionNarratorContainer(boolean show, String natxt,String cotext, String res, boolean isEnd, boolean showOverlay,String templateType) {

    }

    @Override
    public void openFullView(String templateType, String data, CalEventsTemplateModel.Duration duration, int position) {

    }



    public void setBotContentFragmentUpdate(BotContentFragmentUpdate botContentFragmentUpdate) {
        this.botContentFragmentUpdate = botContentFragmentUpdate;
    }

    public void setComposeFooterUpdate(ComposeFooterUpdate composeFooterUpdate) {
        this.composeFooterUpdate = composeFooterUpdate;
    }


    @Override
    public void onQuickReplyItemClicked(String text) {
        onSendClick(text,false);
    }

    /**
     * payload processing
     */

    private void processPayload(String payload, BotResponse botLocalResponse) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer();

        try {
            final BotResponse botResponse = botLocalResponse != null ? botLocalResponse : gson.fromJson(payload, BotResponse.class);
            if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                return;
            }

            LogUtils.d(LOG_TAG, payload);

            PayloadOuter payOuter = null;
            if (!botResponse.getMessage().isEmpty()) {
                ComponentModel compModel = botResponse.getMessage().get(0).getComponent();
                if (compModel != null) {
                    payOuter = compModel.getPayload();
                    if (payOuter != null) {
                        if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                            Gson gson = new Gson();
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }
                        else if(payOuter.getText() != null && payOuter.getText().contains("*"))
                        {
                            String requiredString = payOuter.getText().substring(payOuter.getText().indexOf("(") + 1, payOuter.getText().indexOf(")"));
                            Gson gson = new Gson();
                            payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                        }
                    }
                }
            }
            final PayloadInner payloadInner = payOuter == null ? null : payOuter.getPayload();
            if (payloadInner != null && payloadInner.getTemplate_type() != null && "start_timer".equalsIgnoreCase(payloadInner.getTemplate_type())) {
                BotSocketConnectionManager.getInstance().startDelayMsgTimer();
            }
            botContentFragment.showTypingStatus(botResponse);
            if (payloadInner != null) {
                payloadInner.convertElementToAppropriate();
            }

            if(!BotApplication.isActivityVisible())
            {
                postNotification("Kore Message","Received new message.");
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(botResponse.getMessageId() != null)
                        lastMsgId = botResponse.getMessageId();

                    botContentFragment.addMessageToBotChatAdapter(botResponse);
                    textToSpeech(botResponse);
                    botContentFragment.setQuickRepliesIntoFooter(botResponse);
                    botContentFragment.showCalendarIntoFooter(botResponse);
                }
            }, BundleConstants.TYPING_STATUS_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    if (botContentFragment != null) {
                        BotRequest botRequest = gson.fromJson(payload, BotRequest.class);
                        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
                        botContentFragment.updateContentListOnSend(botRequest);
                    }
                }
                catch (Exception e1)
                {
                    try
                    {
                        final BotResponsePayLoadText botResponse = gson.fromJson(payload, BotResponsePayLoadText.class);
                        if (botResponse == null || botResponse.getMessage() == null || botResponse.getMessage().isEmpty()) {
                            return;
                        }
                        LogUtils.d(LOG_TAG, payload);
                        boolean resolved = true;
                        if (!botResponse.getMessage().isEmpty()) {
                            ComponentModelPayloadText compModel = botResponse.getMessage().get(0).getComponent();
                            if (compModel != null && !StringUtils.isNullOrEmpty(compModel.getPayload()))
                            {
                                displayMessage(compModel.getPayload(), BotResponse.COMPONENT_TYPE_TEXT, botResponse.getMessageId());
                            }
                        }
                    }
                    catch (Exception e2)
                    {
                        e2.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void invokeGenericWebView(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), GenericWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("header", getResources().getString(R.string.app_name));
            startActivity(intent);
        }
    }

    @Override
    public void handleUserActions(String action, HashMap<String,Object > payload) {


    }
    @Override
    public void onStop() {
        BotSocketConnectionManager.getInstance().unSubscribe();
        super.onStop();
    }

    public BotOptionsModel getDataFromTxt()
    {
        BotOptionsModel botOptionsModel = null;

        try
        {
            InputStream is = getResources().openRawResource(R.raw.option);
            Reader reader = new InputStreamReader(is);
            botOptionsModel = gson.fromJson(reader, BotOptionsModel.class);
            LogUtils.e("Options Size", botOptionsModel.getTasks().size() + "" );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return botOptionsModel;
    }

    public void displayMessage(String text, String type, String messageId)
    {
        if(!lastMsgId.equalsIgnoreCase(messageId))
        {
            try
            {
                PayloadOuter payloadOuter = gson.fromJson(text, PayloadOuter.class);

                if(StringUtils.isNullOrEmpty(payloadOuter.getType()))
                    payloadOuter.setType(type);

                ComponentModel componentModel = new ComponentModel();
                componentModel.setType(payloadOuter.getType());
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType(componentModel.getType());
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType(componentModel.getType());
                botResponse.setMessage(arrBotResponseMessages);
                botResponse.setMessageId(messageId);

                if(botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon()))
                    botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            }
            catch (Exception e)
            {
                PayloadInner payloadInner = new PayloadInner();
                payloadInner.setTemplate_type("text");

                PayloadOuter payloadOuter = new PayloadOuter();
                payloadOuter.setText(text);
                payloadOuter.setType("text");
                payloadOuter.setPayload(payloadInner);

                ComponentModel componentModel = new ComponentModel();
                componentModel.setType("text");
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType("text");
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType("text");
                botResponse.setMessage(arrBotResponseMessages);
                botResponse.setMessageId(messageId);

                if(botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon()))
                    botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            }
        }

    }

    public void displayMessage(PayloadOuter payloadOuter)
    {
        try
        {
            if(payloadOuter != null && payloadOuter.getPayload() != null)
            {
                ComponentModel componentModel = new ComponentModel();
                componentModel.setType(payloadOuter.getType());
                componentModel.setPayload(payloadOuter);

                BotResponseMessage botResponseMessage = new BotResponseMessage();
                botResponseMessage.setType(componentModel.getType());
                botResponseMessage.setComponent(componentModel);

                ArrayList<BotResponseMessage> arrBotResponseMessages = new ArrayList<>();
                arrBotResponseMessages.add(botResponseMessage);

                BotResponse botResponse = new BotResponse();
                botResponse.setType(componentModel.getType());
                botResponse.setMessage(arrBotResponseMessages);

                if(botMetaModel != null && !StringUtils.isNullOrEmpty(botMetaModel.getIcon()))
                    botResponse.setIcon(botMetaModel.getIcon());

                processPayload("", botResponse);
            }
            else if(payloadOuter != null && !StringUtils.isNullOrEmpty(payloadOuter.getText()))
            {
                displayMessage(payloadOuter.getText(), BotResponse.COMPONENT_TYPE_TEXT, "");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BotSocketConnectionManager.getInstance().subscribe();
            }
        });
        super.onStart();
    }

    @Override
    protected void onResume() {
        BotApplication.activityResumed();
        if(!SDKConfiguration.Client.isWebHook)
        {
            BotSocketConnectionManager.getInstance().checkConnectionAndRetry(getApplicationContext(), false);
            updateTitleBar(BotSocketConnectionManager.getInstance().getConnection_state());
        }
        super.onResume();
    }

    @Override
    public void ttsUpdateListener(boolean isTTSEnabled) {
        stopTextToSpeech();
    }

    @Override
    public void ttsOnStop() {
        stopTextToSpeech();
    }

    public boolean isTTSEnabled() {
        if (composeFooterFragment != null) {
            return composeFooterFragment.isTTSEnabled();
        } else {
            LogUtils.e(BotChatActivity.class.getSimpleName(), "ComposeFooterFragment not found");
            return false;
        }
    }

    private void stopTextToSpeech() {
        try {
            ttsSynthesizer.stopTextToSpeech();
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
        }
    }

    private void textToSpeech(BotResponse botResponse) {
        if (isTTSEnabled() && botResponse.getMessage() != null && !botResponse.getMessage().isEmpty()) {
            String botResponseTextualFormat = "";
            BotResponseMessage msg = ((BotResponse) botResponse).getTempMessage();
            ComponentModel componentModel = botResponse.getMessage().get(0).getComponent();
            if (componentModel != null) {
                String compType = componentModel.getType();
                PayloadOuter payOuter = componentModel.getPayload();
                if (BotResponse.COMPONENT_TYPE_TEXT.equalsIgnoreCase(compType) || payOuter.getType() == null) {
                    botResponseTextualFormat = payOuter.getText();
                } else if (BotResponse.COMPONENT_TYPE_ERROR.equalsIgnoreCase(payOuter.getType())) {
                    botResponseTextualFormat = payOuter.getPayload().getText();
                } else if (BotResponse.COMPONENT_TYPE_TEMPLATE.equalsIgnoreCase(payOuter.getType()) || BotResponse.COMPONENT_TYPE_MESSAGE.equalsIgnoreCase(payOuter.getType())) {
                    PayloadInner payInner;
                    if (payOuter.getText() != null && payOuter.getText().contains("&quot")) {
                        Gson gson = new Gson();
                        payOuter = gson.fromJson(payOuter.getText().replace("&quot;", "\""), PayloadOuter.class);
                    }
                    payInner = payOuter.getPayload();

                    if (payInner.getSpeech_hint() != null) {
                        botResponseTextualFormat = payInner.getSpeech_hint();
//                        ttsSynthesizer.speak(botResponseTextualFormat);
                        } else if (BotResponse.TEMPLATE_TYPE_BUTTON.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL_ADV.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        }
                        else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(payInner.getTemplate_type())) {
                            botResponseTextualFormat = payInner.getText();
                        }
                    }
                }
            if (BotSocketConnectionManager.getInstance().isTTSEnabled()) {
                BotSocketConnectionManager.getInstance().startSpeak(botResponseTextualFormat);
            }
            }
        }


    private void toggleQuickRepliesVisiblity(){
        quickReplyFragment.toggleQuickReplyContainer(View.GONE);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void attachFragments() {

        if(SDKConfiguration.Client.enablePanel)
        {
            composerView = findViewById(R.id.chatLayoutPanelContainer);
            composerView.setVisibility(VISIBLE);
            composerFragment = new BottomPanelFragment();
            composerFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.chatLayoutPanelContainer, composerFragment).commit();
            composerFragment.setPanelComposeFooterInterface(BotChatActivity.this, SDKConfiguration.Client.identity);
        }
    }

    private PanelBaseModel getHomeModelData(PanelResponseData panelResponseData, String panelName) {
        PanelBaseModel model = null;
        if (panelResponseData != null && panelResponseData.getPanels() != null && panelResponseData.getPanels().size() > 0) {
            for (PanelResponseData.Panel panel : panelResponseData.getPanels()) {
                if (panel != null && panel.getName() != null && panel.getName().equalsIgnoreCase(panelName)) {
                    model = new PanelBaseModel();
                    panel.setItemClicked(true);
                    model.setData(panel);
                    return model;
                }
            }

        }

        return model;
    }

    @Override
    public void onPanelSendClick(String message, boolean isFromUtterance)
    {
        BotSocketConnectionManager.getInstance().sendMessage(message, null);
    }

    @Override
    public void onPanelSendClick(String message, String payload, boolean isFromUtterance)
    {
        if(payload != null){
            BotSocketConnectionManager.getInstance().sendPayload(message, payload);
        }else{
            BotSocketConnectionManager.getInstance().sendMessage(message, payload);
        }

        toggleQuickRepliesVisiblity();
    }

    @Override
    public void onThemeChangeClicked(String message)
    {
        if(message.equalsIgnoreCase(BotResponse.THEME_NAME_1))
        {
            ivChaseLogo.setVisibility(View.VISIBLE);
            ivChaseBackground.setVisibility(View.GONE);
        }
        else
        {
            ivChaseBackground.setVisibility(VISIBLE);
            ivChaseLogo.setVisibility(View.GONE);
        }
    }

    public void sendImage(String fP, String fN, String fPT) {
    /*    String filePath = data.getStringExtra("filePath");
        String fileName = data.getStringExtra("fileName");
        String filePathThumbnail = data.getStringExtra(THUMBNAIL_FILE_PATH);*/
        String filePath = fP;
        String fileName = fN;
        String filePathThumbnail = fPT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            new SaveCapturedImageTask(filePath, fileName, filePathThumbnail).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new SaveCapturedImageTask(filePath, fileName, filePathThumbnail).execute();
        }
    }

    protected class SaveCapturedImageTask extends AsyncTask<String, String, String> {

        private final String filePath;
        private final String fileName;
        private final String filePathThumbnail;
        private String orientation;

        public SaveCapturedImageTask(String filePath, String fileName, String filePathThumbnail) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.filePathThumbnail = filePathThumbnail;
        }


        @Override
        protected String doInBackground(String... params) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
            String extn = null;
            OutputStream fOut = null;

            if (filePath != null) {
                extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                Bitmap thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600, false);
//                    compressImage(filePath);
                if (thePic != null) {
                    try {
                        // compress the image
                        File _file = new File(filePath);

                        LogUtils.d(LOG_TAG, " file.exists() ---------------------------------------- " + _file.exists());
                        fOut = new FileOutputStream(_file);

                        thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
                        thePic = rotateIfNecessary(filePath, thePic);
                        orientation = thePic.getWidth() > thePic.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        LogUtils.e(LOG_TAG, e.toString());
                    }
                    finally {
                        try {
                            assert fOut != null;
                            fOut.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return extn;
        }


        @Override
        protected void onPostExecute(String extn) {
            if (extn != null) {
                //Common place for addition to composeBar
                long fileLimit = getFileMaxSize();

                if(!SDKConfiguration.Client.isWebHook)
                {
                    KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                            filePath, "bearer " + SocketWrapper.getInstance(BotChatActivity.this).getAccessToken(),
                            SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn,
                            KoreMedia.BUFFER_SIZE_IMAGE,
                            new Messenger(messagesMediaUploadAcknowledgeHandler),
                            filePathThumbnail, "AT_" + System.currentTimeMillis(),
                            BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.koreAPIUrl), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
                }
                else
                {
                    KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                            filePath, "bearer " + jwt,
                            SocketWrapper.getInstance(BotChatActivity.this).getBotUserId(), "workflows", extn,
                            KoreMedia.BUFFER_SIZE_IMAGE,
                            new Messenger(messagesMediaUploadAcknowledgeHandler),
                            filePathThumbnail, "AT_" + System.currentTimeMillis(),
                            BotChatActivity.this, BitmapUtils.obtainMediaTypeOfExtn(extn), (!SDKConfiguration.Client.isWebHook ? SDKConfiguration.Server.SERVER_URL : SDKConfiguration.Server.koreAPIUrl), orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.webHook_bot_id));
                }
            } else {
                showToast("Unable to attach!");
            }
        }
    }

    private long getFileMaxSize() {
        long FILE_MAX_SIZE = getFileLimit();
        if (FILE_MAX_SIZE != -1) {
            FILE_MAX_SIZE = FILE_MAX_SIZE * 1024 * 1024;
        }

        return FILE_MAX_SIZE;
    }

    Handler messagesMediaUploadAcknowledgeHandler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            LogUtils.e("shri", reply + "------------------------------");
          /*  if (reply.getBoolean(UploadBulkFile.isFileSizeMore_key, false)) {
                showFreemiumDialog();
                return;
            }
*/
            if (reply.getBoolean("success", true)) {
               /* long fileSizeBytes = reply.getLong(UploadBulkFile.fileSizeBytes_key);
                totalFileSize= totalFileSize+fileSizeBytes;*/
//                String messageId = reply.getString(Constants.MESSAGE_ID);
                String mediaFilePath = reply.getString("filePath");
                String MEDIA_TYPE = reply.getString("fileExtn");
                String mediaFileId = reply.getString("fileId");
                String mediaFileName = reply.getString("fileName");
                String componentType = reply.getString("componentType");
                String thumbnailURL = reply.getString("thumbnailURL");
                String orientation = reply.getString(BundleConstants.ORIENTATION);
                String COMPONENT_DESCRIPTION = reply.getString("componentDescription") != null ? reply.getString("componentDescription") : null;
                HashMap<String, Object> COMPONENT_DATA = reply.getSerializable("componentData") != null ? ((HashMap<String, Object>) reply.getSerializable("componentData")) : null;
                String fileSize = reply.getString("fileSize");
                KoreComponentModel koreMedia = new KoreComponentModel();
                koreMedia.setMediaType(BitmapUtils.getAttachmentType(componentType));
                HashMap<String, Object> cmpData = new HashMap<>(1);
                cmpData.put("fileName", mediaFileName);

                koreMedia.setComponentData(cmpData);
                koreMedia.setMediaFileName(getComponentId(componentType));
                koreMedia.setMediaFilePath(mediaFilePath);
                koreMedia.setFileSize(fileSize);

                koreMedia.setMediafileId(mediaFileId);
                koreMedia.setMediaThumbnail(thumbnailURL);


//                hideBottomSheet();
                composeFooterFragment.setSectionSelected(/*KoraMainComposeFragment.SECTION_TYPE.SECTION_COMPOSE_WITH_COMPOSE_BAR*/);
                messageHandler.postDelayed(new Runnable() {
                    public void run() {
                        HashMap<String, String> attachmentKey = new HashMap<>();
                        attachmentKey.put("fileName", mediaFileName + "." + MEDIA_TYPE);
                        attachmentKey.put("fileType", componentType);
                        attachmentKey.put("fileId", mediaFileId);
                        attachmentKey.put("localFilePath", mediaFilePath);
                        attachmentKey.put("fileExtn", MEDIA_TYPE);
                        attachmentKey.put("thumbnailURL", thumbnailURL);
                        composeFooterFragment.addAttachmentToAdapter(attachmentKey);
                       /* KoraSocketConnectionManager.getInstance().sendMessageWithCustomDataAttchment(mediaFileName+"."+MEDIA_TYPE, attachmentKey, false);
                        KoraSocketConnectionManager.getInstance().stopDelayMsgTimer();
                        toggleVisibilities(false, true);*/
                    }
                }, 400);


                // kaComponentModels.add(koreMedia);
                // insertTags(koreMedia, componentType, orientation, mediaFileName);

            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    LogUtils.i("File upload error", errorMsg);
                    showToast(errorMsg);
                }
            }
        }
    };

    public void mediaAttachment(HashMap<String, String> attachmentKey)
    {
//        hideBottomSheet();
        composeFooterFragment.setSectionSelected(/*KoraMainComposeFragment.SECTION_TYPE.SECTION_COMPOSE_WITH_COMPOSE_BAR*/);
        messageHandler.postDelayed(new Runnable() {
            public void run() {

                composeFooterFragment.addAttachmentToAdapter(attachmentKey);
                       /* KoraSocketConnectionManager.getInstance().sendMessageWithCustomDataAttchment(mediaFileName+"."+MEDIA_TYPE, attachmentKey, false);
                        KoraSocketConnectionManager.getInstance().stopDelayMsgTimer();
                        toggleVisibilities(false, true);*/
            }
        }, 400);
    }

//    public void hideBottomSheet() {
//        if (mBottomSheetBehavior != null && mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        }
//    }

    private int getFileLimit() {
        attachment = SharedPreferenceUtils.getInstance(this).getAttachmentPref("");

        int file_limit = -1;
        if (attachment != null) {
            file_limit = attachment.getSize();
        }

        return file_limit;
    }

    private String getComponentId(String componentType) {
        if (componentType.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE)) {
            return "image_" + System.currentTimeMillis();
        } else if (componentType.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO)) {
            return "video_" + System.currentTimeMillis();
        } else {
            return "doc_" + System.currentTimeMillis();
        }
    }

    private void getBrandingDetails() {
        Call<ArrayList<BrandingNewModel>> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails("bearer " + SocketWrapper.getInstance(BotChatActivity.this).getAccessToken(), SDKConfiguration.Client.tenant_id, "published", "1","en_US", SDKConfiguration.Client.bot_id);
        getBankingConfigService.enqueue(new Callback<ArrayList<BrandingNewModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BrandingNewModel>> call, Response<ArrayList<BrandingNewModel>> response) {
                if (response.isSuccessful())
                {
                    arrBrandingNewDos = response.body();

                    if(arrBrandingNewDos != null && arrBrandingNewDos.size() > 0)
                    {
                        BotOptionsModel botOptionsModel = arrBrandingNewDos.get(0).getHamburgermenu();

                        if(composeFooterFragment != null)
                            composeFooterFragment.setBottomOptionData(botOptionsModel);

                        if(arrBrandingNewDos.size() > 1)
                            onEvent(arrBrandingNewDos.get(1).getBrandingwidgetdesktop());

                        if(isItFirstConnect)
                        {
                            botClient.sendMessage("BotNotifications");
                            isItFirstConnect = false;
                        }
                    }
                }
                else
                {
                    if(isItFirstConnect)
                    {
                        botClient.sendMessage("BotNotifications");
                        isItFirstConnect = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BrandingNewModel>> call, Throwable t)
            {
                LogUtils.e("getBrandingDetails", t.toString());

                if(isItFirstConnect)
                {
                    botClient.sendMessage("BotNotifications");
                    isItFirstConnect = false;
                }
            }
        });
    }

    private void sendWebHookMessage(boolean new_session, String msg, ArrayList<HashMap<String, String>> attachments)
    {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().sendWebHookMessage(SDKConfiguration.Client.webHook_bot_id, "bearer " + jwt, getJsonRequest(new_session, msg, attachments));
        getBankingConfigService.enqueue(new Callback<WebHookResponseDataModel>() {
            @Override
            public void onResponse(Call<WebHookResponseDataModel> call, Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful())
                {
                    webHookResponseDataModel = response.body();
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();
                    updateActionBar();

                    if(webHookResponseDataModel != null && webHookResponseDataModel.getData() != null &&
                            webHookResponseDataModel.getData().size() > 0)
                    {
                        for(int i = 0; i < webHookResponseDataModel.getData().size(); i++)
                        {
                            if(webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
                            else if(webHookResponseDataModel.getData().get(i).getVal() instanceof Object)
                            {
                                try {
                                    String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                    Type carouselType = new TypeToken<PayloadOuter>() {}.getType();
                                    PayloadOuter payloadOuter = gson.fromJson(elementsAsString, carouselType);
                                    displayMessage(payloadOuter);
                                }
                                catch (Exception e)
                                {
                                    try {
                                        String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                        Type carouselType = new TypeToken<PayloadHeaderModel>() {}.getType();
                                        PayloadHeaderModel payloadOuter = gson.fromJson(elementsAsString, carouselType);
                                        if(payloadOuter != null && payloadOuter.getPayload() != null)
                                        {
                                            displayMessage(payloadOuter.getPayload().getTemplate_type(), BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                        }
                                    }
                                    catch (Exception ex)
                                    {
                                        String elementsAsString = gson.toJson(webHookResponseDataModel.getData().get(i).getVal());
                                        displayMessage(elementsAsString, BotResponse.COMPONENT_TYPE_TEXT, webHookResponseDataModel.getData().get(i).getMessageId());
                                    }

                                }
                            }
                        }

                        if(!StringUtils.isNullOrEmpty(webHookResponseDataModel.getPollId()))
                            startSendingPo11(webHookResponseDataModel.getPollId());
                    }
                }
                else
                {
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();
                    updateActionBar();
                }
            }

            @Override
            public void onFailure(Call<WebHookResponseDataModel> call, Throwable t)
            {
            }
        });
    }

    private void getWebHookMeta()
    {
        Call<BotMetaModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getWebHookBotMeta("bearer " + jwt, SDKConfiguration.Client.webHook_bot_id);
        getBankingConfigService.enqueue(new Callback<BotMetaModel>() {
            @Override
            public void onResponse(Call<BotMetaModel> call, Response<BotMetaModel> response) {
                if (response.isSuccessful())
                {
                    botMetaModel = response.body();
                    SDKConfiguration.BubbleColors.setIcon_url(botMetaModel.getIcon());
                    sendWebHookMessage(true, "ON_CONNECT", null);
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<BotMetaModel> call, Throwable t)
            {}
        });
    }

    private void postPollingData(String pollId)
    {
        Call<WebHookResponseDataModel> getBankingConfigService = WebHookRestBuilder.getRestAPI().getPollIdData("bearer " + jwt, SDKConfiguration.Client.webHook_bot_id, pollId);
        getBankingConfigService.enqueue(new Callback<WebHookResponseDataModel>() {
            @Override
            public void onResponse(Call<WebHookResponseDataModel> call, Response<WebHookResponseDataModel> response) {
                if (response.isSuccessful())
                {
                    webHookResponseDataModel = response.body();
                    taskProgressBar.setVisibility(View.GONE);
                    composeFooterFragment.enableSendButton();
                    updateActionBar();
//                    getBrandingDetails();

                    if(webHookResponseDataModel != null && webHookResponseDataModel.getData() != null &&
                            webHookResponseDataModel.getData().size() > 0)
                    {
                        for(int i = 0; i < webHookResponseDataModel.getData().size(); i++)
                        {
                            if(webHookResponseDataModel.getData().get(i).getVal() instanceof String)
                                displayMessage(webHookResponseDataModel.getData().get(i).getVal().toString(), webHookResponseDataModel.getData().get(i).getType(), webHookResponseDataModel.getData().get(i).getMessageId());
                        }

                        stopSendingPolling();
                    }
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<WebHookResponseDataModel> call, Throwable t)
            {}
        });
    }

    private void addSentMessageToChat(String message)
    {
        //Update the bot content list with the send message
        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);
        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);
        BotInfoModel botInfo = new BotInfoModel(SDKConfiguration.Client.bot_name, SDKConfiguration.Client.bot_id, null);
        botPayLoad.setBotInfo(botInfo);
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        sListener.onMessage(new SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE ,message,botRequest,false));
    }

    private HashMap<String, Object> getJsonRequest(boolean new_session, String msg, ArrayList<HashMap<String, String>> attachments)
    {
        String jsonPayload = "";
        HashMap<String, Object> hsh = new HashMap<>();

        try
        {
            WebHookRequestModel webHookRequestModel = new WebHookRequestModel();

            WebHookRequestModel.Session session = new WebHookRequestModel.Session();
            session.setNewSession(new_session);
            webHookRequestModel.setSession(session);
            hsh.put("session", session);

            WebHookRequestModel.Message message = new WebHookRequestModel.Message();
            message.setVal(msg);

            if (new_session)
                message.setType("event");
            else
                message.setType("text");

            webHookRequestModel.setMessage(message);
            hsh.put("message", message);

            WebHookRequestModel.From from = new WebHookRequestModel.From();
            from.setId(SDKConfiguration.Client.webHook_identity);
            WebHookRequestModel.From.WebHookUserInfo userInfo = new WebHookRequestModel.From.WebHookUserInfo();
            userInfo.setFirstName("");
            userInfo.setLastName("");
            userInfo.setEmail("");
            from.setUserInfo(userInfo);
            webHookRequestModel.setFrom(from);
            hsh.put("from", from);

            WebHookRequestModel.To to = new WebHookRequestModel.To();
            to.setId("Kore.ai");
            WebHookRequestModel.To.GroupInfo groupInfo = new WebHookRequestModel.To.GroupInfo();
            groupInfo.setId("");
            groupInfo.setName("");
            to.setGroupInfo(groupInfo);
            webHookRequestModel.setTo(to);
            hsh.put("to", to);

            WebHookRequestModel.Token token = new WebHookRequestModel.Token();
            hsh.put("token", token);

            if(attachments != null && attachments.size() > 0)
                hsh.put("attachments", attachments);

            jsonPayload = gson.toJson(webHookRequestModel);
        }
        catch (Exception e)
        {

        }

        return hsh;
    }

    private void startSendingPo11(String pollId)
    {
        handler.postDelayed(runnable = new Runnable() {
            public void run()
            {
                handler.postDelayed(runnable, poll_delay);
                postPollingData(pollId);
            }
        }, poll_delay);
    }

    private void stopSendingPolling()
    {
        handler.removeCallbacks(runnable);
    }
}
