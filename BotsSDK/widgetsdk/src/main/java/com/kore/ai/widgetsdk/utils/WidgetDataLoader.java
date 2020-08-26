package com.kore.ai.widgetsdk.utils;

import android.location.Location;

import com.kore.ai.widgetsdk.models.BotTableListTemplateModel;
import com.kore.ai.widgetsdk.models.Header;
import com.kore.ai.widgetsdk.models.SummaryViewResponseModel;
import com.kore.ai.widgetsdk.models.WUpcomingFilesModel;
import com.kore.ai.widgetsdk.models.WUpcomingMeetingModel;
import com.kore.ai.widgetsdk.models.WUpcomingTasksModel;
import com.kore.ai.widgetsdk.models.WeatherWidgetModel;
import com.kore.ai.widgetsdk.models.WidgetListDataModel;
import com.kore.ai.widgetsdk.models.WidgetTableListDataModel;
import com.kore.ai.widgetsdk.models.WidgetsDataModel;
import com.kore.ai.widgetsdk.models.WidgetsWidgetModel;
import com.kore.ai.widgetsdk.net.KaRestBuilder;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ramachandra Pradeep on 08-Mar-19.
 */

public class WidgetDataLoader {

    public enum SERVICE_TYPE{
        TYPE_HEADER,
        TYPE_UPCOMING_MEETING,
        TYPE_TASKS_ASSIGNED_TO_YOU,
        TYPE_TASKS_ASSIGNED_BY_YOU,
        TYPE_OVERDUE_ASSIGNED_TO_YOU,
        TYPE_OVERDUE_ASSIGNED_BY_YOU,
        TYPE_GET_FILES_BY_YOU,
        TYPE_GET_FILES_SHARED_WITH_YOU

    }
    public static Observable<Header> loadHeaderData(final String url, final String accessToken, final Location loc) {
        return Observable.create(new ObservableOnSubscribe<Header>() {
            @Override
            public void subscribe(ObservableEmitter<Header> observableEmitter) throws Exception {
                try {
                    Call<Header> widgetResp;
                    if(loc != null)
                        widgetResp = KaRestBuilder.getKaRestAPI().getHeaderDataWithLoc(url,accessToken,loc.getLatitude(),loc.getLongitude());
                    else
                        widgetResp = KaRestBuilder.getKaRestAPI().getHeaderData(url,accessToken);
                    observableEmitter.onNext(widgetResp.execute().body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }
    public static Observable<WUpcomingMeetingModel> loadUpcomingMeetingData(final String url, final String accessToken) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingMeetingModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingMeetingModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingMeetingModel> widgetResp = KaRestBuilder.getKaRestAPI().getUpcomingMeetingsData(url,accessToken, null , "");
                    Response<WUpcomingMeetingModel> model=widgetResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }


    public static Observable<SummaryViewResponseModel> loadSummaryViewData(final String url, final String accessToken, Map<String,Object> hm, final Object body) {
        return Observable.create(new ObservableOnSubscribe<SummaryViewResponseModel>() {
            @Override
            public void subscribe(ObservableEmitter<SummaryViewResponseModel> observableEmitter) throws Exception {
                try {
                    Call<SummaryViewResponseModel> widgetResp = KaRestBuilder.getKaRestAPI().getSummaryView(url,accessToken,body);
                    Response<SummaryViewResponseModel> model=widgetResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WidgetsWidgetModel> loadForDefaultServiceData(final String url, final String accessToken, Map<String,Object> hm) {
        return Observable.create(new ObservableOnSubscribe<WidgetsWidgetModel>() {
            @Override
            public void subscribe(ObservableEmitter<WidgetsWidgetModel> observableEmitter) throws Exception {
                try {
                    Call<WidgetsWidgetModel> widgetResp = KaRestBuilder.getKaRestAPI().getDefaultWidgetServiceData(url,accessToken, hm);
                    Response<WidgetsWidgetModel> model=widgetResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WidgetListDataModel> loadListWidgetServiceData(final String url, final String accessToken, final Map<String,Object> hm) {
        return Observable.create(new ObservableOnSubscribe<WidgetListDataModel>() {
            @Override
            public void subscribe(ObservableEmitter<WidgetListDataModel> observableEmitter) throws Exception {
                try {
                    Call<WidgetListDataModel> resp = KaRestBuilder.getKaRestAPI().getListWidgetData(url,accessToken, hm);
                    Response<WidgetListDataModel> model = resp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WidgetTableListDataModel> loadTableListWidgetServiceData(final String url, final String accessToken, final Map<String,Object> hm) {
        return Observable.create(new ObservableOnSubscribe<WidgetTableListDataModel>() {
            @Override
            public void subscribe(ObservableEmitter<WidgetTableListDataModel> observableEmitter) throws Exception {
                try {
                    Call<WidgetTableListDataModel> resp = KaRestBuilder.getKaRestAPI().getTableListWidgetData(url,accessToken, hm);
                    Response<WidgetTableListDataModel> model = resp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WeatherWidgetModel> loadForWeatherServiceData(final String url, final String accessToken, final Map<String,Object> hm, final Object body) {
        return Observable.create(new ObservableOnSubscribe<WeatherWidgetModel>() {
            @Override
            public void subscribe(ObservableEmitter<WeatherWidgetModel> observableEmitter) throws Exception {
                try {
                    Call<WeatherWidgetModel> widgetResp = KaRestBuilder.getKaRestAPI().getWeatherServiceData(url,accessToken, hm,body);
                    Response<WeatherWidgetModel> model=widgetResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WidgetsDataModel> loadChartData(final String url, final String accessToken, final Map<String,Object> hm) {
        return Observable.create(new ObservableOnSubscribe<WidgetsDataModel>() {
            @Override
            public void subscribe(ObservableEmitter<WidgetsDataModel> observableEmitter) throws Exception {
                try {
                    Call<WidgetsDataModel> chartResp = KaRestBuilder.getKaRestAPI().getChartData(url,accessToken,hm);
                    Response<WidgetsDataModel> model = chartResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WidgetsDataModel> loadWidgetChartData(final String url, final String accessToken, final Map<String,Object> hm) {
        return Observable.create(new ObservableOnSubscribe<WidgetsDataModel>() {
            @Override
            public void subscribe(ObservableEmitter<WidgetsDataModel> observableEmitter) throws Exception {
                try {
                    Call<WidgetsDataModel> chartResp = KaRestBuilder.getKaRestAPI().getWidgetChartData(url,accessToken,hm);
                    Response<WidgetsDataModel> model = chartResp.execute();
                    if(model.isSuccessful()) {

                        observableEmitter.onNext(model.body());
                        observableEmitter.onComplete();
                    }
                    else
                    {
                        observableEmitter.onError(new Exception(model.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }


    public static Observable<WUpcomingTasksModel> loadTasksAssignedToYou(final String url, final String accessToken, final Map<String,Object> hm, final Object body) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingTasksModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingTasksModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingTasksModel> widgetResp =  KaRestBuilder.getKaRestAPI().getTasksAssignedToYou(url,accessToken,hm,body);
                    Response<WUpcomingTasksModel> response = widgetResp.execute();
                    observableEmitter.onNext(response.body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }
    public static Observable<WUpcomingTasksModel> loadTasksAssignedByYou(final String url, final String accessToken) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingTasksModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingTasksModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingTasksModel> widgetResp =  KaRestBuilder.getKaRestAPI().getTasksAssignedByYou(url,accessToken);
                    observableEmitter.onNext(widgetResp.execute().body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WUpcomingTasksModel> loadOverDueTasksAssignedToYou(final String url, final String accessToken) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingTasksModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingTasksModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingTasksModel> widgetResp =  KaRestBuilder.getKaRestAPI().getOverDueTasksAssignedToYou(url,accessToken);
                    observableEmitter.onNext(widgetResp.execute().body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WUpcomingTasksModel> loadOverDueTasksAssignedByYou(final String url, final String accessToken) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingTasksModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingTasksModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingTasksModel> widgetResp =  KaRestBuilder.getKaRestAPI().getOverDueTasksAssignedByYou(url,accessToken);
                    observableEmitter.onNext(widgetResp.execute().body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<WUpcomingFilesModel> loadFilesCreatedByYou(final String url, final String accessToken, final Map<String,Object> hm, final Object body) {
        return Observable.create(new ObservableOnSubscribe<WUpcomingFilesModel>() {
            @Override
            public void subscribe(ObservableEmitter<WUpcomingFilesModel> observableEmitter) throws Exception {
                try {
                    Call<WUpcomingFilesModel> widgetResp =  KaRestBuilder.getKaRestAPI().getFilesCreatedByYou(url,accessToken,hm,body);
                    Response<WUpcomingFilesModel> response=widgetResp.execute();
                    if(response.isSuccessful()) {
                        observableEmitter.onNext(response.body());
                        observableEmitter.onComplete();
                    }
                    else {
                        observableEmitter.onError(new Exception(response.code()+""));
                    }
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public static Observable<ResponseBody> loadFilesSharedWithYou(final String url, final String accessToken) {
        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBody> observableEmitter) throws Exception {
                try {
                    Call<ResponseBody> widgetResp =  KaRestBuilder.getKaRestAPI().getFilesSharedWithYou(url,accessToken);
                    observableEmitter.onNext(widgetResp.execute().body());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }
}
