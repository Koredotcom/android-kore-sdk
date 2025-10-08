package kore.botssdk.dialogs;

import static android.os.Looper.getMainLooper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.adapter.WidgetCancelActionsAdapter;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.WCalEventsTemplateModel;
import kore.botssdk.models.WidgetDialogModel;


public class WidgetDialogActivity extends Dialog {

    final WidgetDialogModel widgetDialogModel;
    TextView txtTitle, tv_time, txtPlace, tv_users;
    View sideBar;
    final int TIMER_START_MINUTE = 5 * 60;
    RecyclerView recycler_actions;
    final WCalEventsTemplateModel model;
    final Context mContext;
    private final boolean isFromFullView;
    Handler someHandler;
    final VerticalListViewActionHelper verticalListViewActionHelper;
    WidgetCancelActionsAdapter adapter;
    boolean flagMeetingInProgress;
    long startTime, endTimer;

    public WidgetDialogActivity(Context mContext, WidgetDialogModel widgetDialogModel, WCalEventsTemplateModel model, boolean isFromFullView, VerticalListViewActionHelper verticalListViewActionHelper) {
        super(mContext, R.style.WidgetDialog);
        this.widgetDialogModel = widgetDialogModel;
        this.mContext = mContext;
        this.model = model;
        this.isFromFullView = isFromFullView;
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(R.color.transparent_card);

        setContentView(R.layout.item_selection_dialog);
        initViews();

        tv_time.setText(widgetDialogModel.getTime());
        txtTitle.setText(widgetDialogModel.getTitle());

        txtPlace.setText(widgetDialogModel.getLocation());
        tv_users.setText(widgetDialogModel.getAttendies());
        txtPlace.setVisibility(widgetDialogModel.getLocation() != null && !TextUtils.isEmpty(widgetDialogModel.getLocation()) ? View.VISIBLE : View.GONE);
        sideBar.setBackgroundColor(Color.parseColor(widgetDialogModel.getColor()));
        recycler_actions.setVisibility(View.GONE);
        adapter = new WidgetCancelActionsAdapter((Activity) mContext,
                this, model, isFromFullView, verticalListViewActionHelper);
        calculateTime((long) model.getData().getDuration().getStart(), (long) model.getData().getDuration().getEnd());


        recycler_actions.setAdapter(adapter);

        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottomup);
        recycler_actions.startAnimation(bottomUp);
        recycler_actions.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }

    public List<WCalEventsTemplateModel.Action> sortShowingAction(boolean timebase) {
        List<WCalEventsTemplateModel.Action> newListActions = new ArrayList<>();
        for (WCalEventsTemplateModel.Action data : model.getActions()) {
            if (timebase) {
                return model.getActions();
            } else {
                if (data.getType().equalsIgnoreCase("url") && data.getCustom_type().equalsIgnoreCase("url")) {
                    continue;
                }
                if (data.getType().equalsIgnoreCase("dial") && data.getCustom_type().equalsIgnoreCase("dial") && !data.getDial().equalsIgnoreCase("")) {
                    continue;
                }
                newListActions.add(data);
            }
        }
        return newListActions;
    }

    private void initViews() {
        txtTitle = findViewById(R.id.txtTitle);
        tv_time = findViewById(R.id.tv_time);
        txtPlace = findViewById(R.id.txtPlace);
        tv_users = findViewById(R.id.tv_users);
        sideBar = findViewById(R.id.sideBar);
        recycler_actions = findViewById(R.id.recycler_actions);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycler_actions.setLayoutManager(layoutManager);
    }

    private void calculateTime(long start, long end) {
        //  DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy, hh:mm:ss a");
        startTime = start;
        endTimer = end;
        long timeStampNow = System.currentTimeMillis();

        if (timeStampNow >= start && timeStampNow <= end) {
            // meeting started, start time to check meeting end time
            runTimer(start, end);
        } else if (timeStampNow < start) {
            //meeting need to start, start the timer to calculate start time
            runTimer(start, end);
        } else {
            //meeting completed

            stateCheck(0);

        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Current time stamp
            long currentTime = System.currentTimeMillis();
            long milliseconds;
            if (currentTime > endTimer) {
                // meeting completed
                stateCheck(0);
                someHandler.removeCallbacks(this);
            } else if (currentTime >= startTime) {
                //meeting in progress
                stateCheck(1);
            } else {
                //meeting need to start
                milliseconds = startTime - currentTime;
                int seconds = (int) milliseconds / 1000;

                if (seconds <= TIMER_START_MINUTE) {
                    stateCheck(-1);
                } else {
                    stateCheck(4);
                }
            }
            someHandler.postDelayed(this, 1000);
        }
    };

    public void runTimer(long startTime, long endTimer) {
        this.startTime = startTime;
        this.endTimer = endTimer;
        someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(runnable
                , 10);
    }

    public void stateCheck(int state) {
        switch (state) {
            case 0:
                adapter.setActionItems(sortShowingAction(false));
                break;
            case 1:
                //meeting in progress
                if (!flagMeetingInProgress) {

                    flagMeetingInProgress = true;
                    adapter.setActionItems(sortShowingAction(true));
                    adapter.notifyDataSetChanged();
                }
                break;

            case -1:
                //meeting need to start
                if (!flagMeetingInProgress) {
                    flagMeetingInProgress = true;
                    adapter.setActionItems(sortShowingAction(true));
                    adapter.notifyDataSetChanged();
                }
                break;

            case 4:
                adapter.setActionItems(sortShowingAction(false));
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (someHandler != null) {
            someHandler.removeCallbacks(runnable);
        }
    }
}