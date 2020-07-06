package com.kore.ai.widgetsdk.managers;

import android.content.Context;

import com.kore.ai.widgetsdk.koranet.KORestBuilder;
import com.kore.ai.widgetsdk.models.TimeZoneModel;
import com.kore.ai.widgetsdk.room.db.KODataBase;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.KORestResponse;
import com.kore.ai.widgetsdk.room.models.UserData;
import com.kore.ai.widgetsdk.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Ramachandra Pradeep on 20-Jul-18.
 */

public class UserDataManager {

    public static UserData getUserData() {
        if (userData == null && KORestBuilder.mContext != null) {
            readUserDataInMainThread(KORestBuilder.mContext);
        }
        return userData;
    }

    public static void setUserData(UserData userData) {
        UserDataManager.userData = userData;
    }

    public static AuthData getAuthData() {
        if (authData == null && KORestBuilder.mContext != null) {
            readUserDataInMainThread(KORestBuilder.mContext);
        }
        return authData;
    }

    public static void clearData(){
        authData = null;
        userData = null;
    }

    public static void setAuthData(AuthData authData) {
        UserDataManager.authData = authData;
    }

    private static UserData userData;
    private static AuthData authData;

    private static AppExecutors executor = new AppExecutors();

    public static void persistUserData(final Context mContext, final KORestResponse.KOLoginResponse response, final String type, final KOTask callback) {
        executor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                KODataBase dbInstance = KODataBase.getInstance(mContext);
                response.getUserData().setSsoType(type);
                response.getUserData().processForPersistance();
                dbInstance.getUserData().addUserData(response.getUserData());
                dbInstance.getAuthData().addAuthData(response.getAuthData());

                setUserData(response.getUserData());
                setAuthData(response.getAuthData());
                executor.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onTaskCompleted();
                    }
                });
            }
        });
    }

    public static void updateAuthData(final Context mContext, final AuthData authData, final KOTask callback) {
        executor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                KODataBase dbInstance = KODataBase.getInstance(mContext);
                dbInstance.getAuthData().updateAuthData(authData);

                setAuthData(authData);

                executor.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onTaskCompleted();
                    }
                });
            }
        });
    }

    public static void updateUserData(final Context mContext, final UserData userData, final KOTask callback) {
        executor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                KODataBase dbInstance = KODataBase.getInstance(mContext);
                dbInstance.getUserData().updateUserData(userData);

                setUserData(userData);

                executor.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onTaskCompleted();
                    }
                });
            }
        });
    }

    public static void readUserDataAsynchronus(final Context mContext, final KOTask callback) {
        executor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                KODataBase dbInstance = KODataBase.getInstance(mContext);
                KORestResponse resp = new KORestResponse();
                final KORestResponse.KOLoginResponse lResp = resp.new KOLoginResponse();
                List<UserData> userData = dbInstance.getUserData().getUserDataSynchronus();
                if (userData != null && !userData.isEmpty()) {
                    lResp.setUserData(userData.get(0));
                    setUserData(userData.get(0));
                }
                List<AuthData> authData = dbInstance.getAuthData().getAuthDataSynchronus();
                if (authData != null && !authData.isEmpty()) {
                    lResp.setAuthData(authData.get(0));
                    setAuthData(authData.get(0));
                }

                executor.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onTaskCompletedWithData(lResp);
                        }
                    }
                });
            }
        });
    }

    public static KORestResponse.KOLoginResponse readUserDataInMainThread(final Context mContext) {

        KODataBase dbInstance = KODataBase.getInstance(mContext);
        KORestResponse resp = new KORestResponse();
        final KORestResponse.KOLoginResponse lResp = resp.new KOLoginResponse();
        List<UserData> userData = dbInstance.getUserData().getUserDataSynchronus();
        if (userData != null && !userData.isEmpty()) {
            lResp.setUserData(userData.get(0));
            setUserData(userData.get(0));
        }
        List<AuthData> authData = dbInstance.getAuthData().getAuthDataSynchronus();
        if (authData != null && !authData.isEmpty()) {
            lResp.setAuthData(authData.get(0));
            setAuthData(authData.get(0));
        }

        return lResp;
    }

    public static Observable<ArrayList<TimeZoneModel>> getTimeZoneList(Context mContext) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<TimeZoneModel>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<TimeZoneModel>> observableEmitter) throws Exception {
                try {
                    String json = null;
                    InputStream is = mContext.getAssets().open("timezones.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                    ArrayList<TimeZoneModel> arrayList = null;
                    if (!StringUtils.isNullOrEmpty(json)) {
                        arrayList = new ArrayList<>();
                        JSONArray m_jArry = new JSONArray(json);

                        for (int i = 0; i < m_jArry.length(); i++) {
                            JSONObject jo_inside = m_jArry.getJSONObject(i);
                            TimeZoneModel model = new TimeZoneModel();
                            model.setTimeZoneName(jo_inside.getString("timezonename"));
                            model.setName(jo_inside.getString("name"));
                            model.setCountry_name(jo_inside.getString("country_name"));
                            model.setLocale_code(jo_inside.getString("locale_code"));
                            arrayList.add(model);
                        }

                    }
                    observableEmitter.onNext(arrayList);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }

    public interface KOTask {
        void onTaskCompleted();

        void onTaskFailed();

        void onTaskCompletedWithData(Object data);

    }

}
