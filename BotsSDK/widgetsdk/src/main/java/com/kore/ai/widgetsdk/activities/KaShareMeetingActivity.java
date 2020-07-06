package com.kore.ai.widgetsdk.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.ShareMeetingAdapter;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.models.AttendiesBase;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.net.ShareInfoRequest;
import com.kore.ai.widgetsdk.utils.BundleConstants;
import com.kore.ai.widgetsdk.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaShareMeetingActivity extends KaAppCompatActivity {

    private RecyclerView recycler_share;
    private List<CalEventsTemplateModel.Attendee> attendeesList;

    private SwitchCompat attendee_switch;
    private ShareMeetingAdapter adapter;
    private View layout_edit;
    private EditText edt_search;
    private String navigationfrom;
    private View available_switch_layout;
    private List<CalEventsTemplateModel.Attendee> serachList = new ArrayList<>();
    private boolean isCheckBoxRequired = true;
    private ActionBar actionBar;
    private TextView label_attendee;
    private MenuItem menuDone;
    private String userId;
    private String accessToken;
    private String mNId;
    private boolean isDirectSwitchCompatAction = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_meeting_layout);
        setupActionBar();
        findViews();
        AttendiesBase attendiesBase = (AttendiesBase) getIntent().getSerializableExtra(BundleConstants.MEETING_DATA_ATTENDEE);
        if (attendiesBase != null && attendiesBase.getAttendeeList() != null) {
            attendeesList = attendiesBase.getAttendeeList();
        } else {
            finish();
        }
        navigationfrom = getIntent().getStringExtra(BundleConstants.NAVIGATION_FROM);
        mNId = getIntent().getStringExtra(BundleConstants.MEETING_NOTES_ID);
        switchUIVisblity(navigationfrom != null ? navigationfrom : "");
        if (attendeesList != null) {

            layout_edit.setVisibility(attendeesList != null && attendeesList.size() > 0 ? View.VISIBLE : View.GONE);
            adapter = new ShareMeetingAdapter(KaShareMeetingActivity.this, attendeesList, isCheckBoxRequired);
            recycler_share.setAdapter(adapter);
        }

        getUserInfo();
        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                AlterAdapter();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        attendee_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (adapter != null && isDirectSwitchCompatAction) {
                    adapter.setAllSelected(isChecked);
                    adapter.notifyDataSetChanged();
                }
                isDirectSwitchCompatAction = true;

            }
        });
        getSharedList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(menuDone != null)
            menuDone.setEnabled(true);
    }

    public void doAttendeeSwitchAction(boolean _checked){
        isDirectSwitchCompatAction = false;
        attendee_switch.setChecked(_checked);
        isDirectSwitchCompatAction = true;
    }

    private void getUserInfo() {
        userId = UserDataManager.getUserData().getId();
        accessToken = UserDataManager.getAuthData().getAccessToken();
    }

    private void getSharedList() {
        if (mNId == null) return;
        showProgress("", false);

        new ShareInfoRequest(accessToken, userId, mNId, false, 4, null, new Callback<KaRestResponse.ShareResponse>() {
            @Override
            public void onResponse(Call<KaRestResponse.ShareResponse> call, Response<KaRestResponse.ShareResponse> response) {
                dismissProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if (adapter != null)
                        adapter.setSelectedUsers(response.body().getSharedList());
                } else {
//                    showErrorToast(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<KaRestResponse.ShareResponse> call, Throwable t) {
//                showErrorToast(t);
                dismissProgress();
            }
        }).loadDataFromNetwork();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.meeting_task_menu, menu);
        menuDone = menu.findItem(R.id.done);

        if (navigationfrom.equals("Share")) {
            menuDone.setVisible(true);
        } else {
            menuDone.setVisible(false);
        }
        return true;
    }

    public void menuDoneVisibility(boolean visible){
        if(menuDone!=null)
        menuDone.setEnabled(visible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
//            showMissingValuesDialog();
            if (navigationfrom.equals("Share"))
                shareMeetingNotes();
            else onBackPressed();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    private void switchUIVisblity(String navigationfrom) {

        switch (navigationfrom) {
            case "Attendee Search":
                actionBar.setTitle("Invitees");
                available_switch_layout.setVisibility(View.GONE);
                isCheckBoxRequired = false;
                label_attendee.setVisibility(View.GONE);
                break;

            default:
                //share cuurently
                //pass the navigation from and change visiblity accordingly
                actionBar.setTitle("Share");
                available_switch_layout.setVisibility(View.VISIBLE);
                isCheckBoxRequired = true;
                removeMyEmail();
                label_attendee.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void removeMyEmail() {
        if (attendeesList != null) {
            for (int i = 0; i < attendeesList.size(); i++)
                if (attendeesList.get(i).getEmail().equals(UserDataManager.getUserData().getEmailId())) {
                    attendeesList.remove(i);
                    break;
                }
        }
    }

    private void findViews() {
        layout_edit = findViewById(R.id.layout_edit);
        recycler_share = findViewById(R.id.recycler_share);
        recycler_share.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        attendee_switch = findViewById(R.id.attendee_switch);
        edt_search = findViewById(R.id.edt_search);
        available_switch_layout = findViewById(R.id.available_switch_layout);
        label_attendee = findViewById(R.id.label_attendee);
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        actionBar = getSupportActionBar();
    }

    private void shareMeetingNotes() {
        HashMap<String, ArrayList> allInvitees = adapter.getAllInvitees();
        final ArrayList<KaRestResponse.SharedList> _selected = allInvitees.get("selected");
        final ArrayList<KaRestResponse.SharedList> _unselected = allInvitees.get("unselected");



        if (_selected != null && _selected.size() == 0 && _unselected != null && _unselected.size() == 0) {
            showToast("Saved successfully", Toast.LENGTH_SHORT);
            setResult(RESULT_OK);
            finish();
            return;
        }


        showProgress("",false);
        shareOrUnShareMeetingnotesToInvitees().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KaRestResponse.ShareResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KaRestResponse.ShareResponse resp) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgress();
                        showToast("Something went wrong", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onComplete() {
                        dismissProgress();
                        String msg = "Shared successfully";
                        if(_selected != null && _selected.size() == 0 && _unselected != null && _unselected.size() > 0){
                            msg = "Saved successfully";
                        }
                        showToast(msg, Toast.LENGTH_SHORT);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    public Observable<KaRestResponse.ShareResponse> shareOrUnShareMeetingnotesToInvitees() {
        return Observable.create(new ObservableOnSubscribe<KaRestResponse.ShareResponse>() {
            @Override
            public void subscribe(ObservableEmitter<KaRestResponse.ShareResponse> emitter) throws Exception {
                try {
                    HashMap<String, ArrayList<KaRestResponse.SharedList>> payLoad = new HashMap<>();
                    HashMap<String, ArrayList> allInvitees = adapter.getAllInvitees();

                    ArrayList<KaRestResponse.SharedList> _selected = allInvitees.get("selected");
                    ArrayList<KaRestResponse.SharedList> _unselected = allInvitees.get("unselected");
                    Response<KaRestResponse.ShareResponse> rBody = null;

                    if (_selected != null && _selected.size() > 0) {
                        payLoad.clear();
                        payLoad.put("users", _selected);

                        Call<KaRestResponse.ShareResponse> shareRequest = KaRestBuilder.getKaRestAPI().shareInfo(Utils.ah(accessToken), userId, mNId, true, payLoad);
                        rBody = shareRequest.execute();
                    }
                    if (_unselected != null && _unselected.size() > 0) {
                        payLoad.clear();
                        payLoad.put("users", _unselected);
                        Call<KaRestResponse.ShareResponse> unShareRequest = KaRestBuilder.getKaRestAPI().updateSharePrivilege(Utils.ah(accessToken), userId, mNId, payLoad);
                        rBody = unShareRequest.execute();
                    }

                    if (rBody != null && (rBody.isSuccessful())) {
                        emitter.onNext(rBody.body());
                        emitter.onComplete();
                    }else{
                        dismissProgress();
                        showErrorToast(rBody.errorBody());
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }


    public void AlterAdapter() {
        if (attendeesList != null) {
            if (edt_search.getText().toString().isEmpty()) {
                serachList.clear();
                adapter.setAttendeesList(attendeesList);
                adapter.notifyDataSetChanged();
            } else {
                serachList.clear();
                for (int i = 0; i < attendeesList.size(); i++) {
                    String name = attendeesList.get(i).getName() != null ? attendeesList.get(i).getName() : attendeesList.get(i).getEmail();
                    if (name.toLowerCase().startsWith(edt_search.getText().toString().trim().toLowerCase())) {
                        serachList.add(attendeesList.get(i));
                        adapter.setAttendeesList(serachList);
                        adapter.notifyDataSetChanged();
                    }
                }
                if (serachList.size() == 0) {
                    adapter.setAttendeesList(serachList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}

