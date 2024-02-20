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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.adapter.WidgetCancelActionsAdapter;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.WCalEventsTemplateModel;
import kore.botssdk.models.WidgetDialogModel;


public class WidgetDialogActivity extends Dialog {

    private ImageView img_cancel;
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
    long starttime, endtimer;

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

        getWindow().setBackgroundDrawableResource(R.color.transparent_card);

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
                WidgetDialogActivity.this, model, isFromFullView, verticalListViewActionHelper);
        caculateTime((long) model.getData().getDuration().getStart(), (long) model.getData().getDuration().getEnd());


        recycler_actions.setAdapter(adapter);

        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottomup);
        recycler_actions.startAnimation(bottomUp);
        recycler_actions.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }

    public List<WCalEventsTemplateModel.Action> sortShowingAction(boolean timebased) {
        List<WCalEventsTemplateModel.Action> newlistActions = new ArrayList<>();
        for (WCalEventsTemplateModel.Action data : model.getActions()) {
            if (timebased) {
                return model.getActions();
            } else {
                if (data.getType().equalsIgnoreCase("url") && data.getCustom_type().equalsIgnoreCase("url")) {
                    continue;
                }
                if (data.getType().equalsIgnoreCase("dial") && data.getCustom_type().equalsIgnoreCase("dial") && !data.getDial().equalsIgnoreCase("")) {
                    continue;
                }
                newlistActions.add(data);
            }
        }
        return newlistActions;
    }


    public void dissmissanim() {
        Animation bottomdown = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottomdown);
        recycler_actions.startAnimation(bottomdown);
        recycler_actions.setVisibility(View.INVISIBLE);
    }

    private void initViews() {

        img_cancel = findViewById(R.id.img_cancel);
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

    private void caculateTime(long start, long end) {
        //  DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy, hh:mm:ss a");
        starttime = start;
        endtimer = end;
        long timeStampNow = System.currentTimeMillis();

        if (timeStampNow >= start && timeStampNow <= end) {
            // meeting started, start time to check meeting end time
            runTimer(start, end);
        } else if (timeStampNow < start) {
            //meeting need to start, start the timer to calculate start time
            runTimer(start, end);
        } else {
            //meeting completed

            stateCheck(0, "Meeting Completed");

        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Current time stamp
            long cuurentTime = System.currentTimeMillis();
            long milliseconds = 0;
            if (cuurentTime > endtimer) {
                // meeting completed
                stateCheck(0, "Meeting Completed");
                someHandler.removeCallbacks(this);
            } else if (cuurentTime >= starttime && cuurentTime <= endtimer) {
                //meeting in progress
                stateCheck(1, "Meeting in progress");
            } else if (cuurentTime <= starttime) {
                //meeting need to start
                milliseconds = starttime - cuurentTime;
                int seconds = (int) milliseconds / 1000;

                if (seconds <= TIMER_START_MINUTE) {
                    stateCheck(-1, "Meeting need to start");
                } else {
                    stateCheck(4, "Before our timw");
                }
            }
            someHandler.postDelayed(this, 1000);
        }
    };

    public void runTimer(long starttime, long endtimer) {
        someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(runnable
                , 10);
    }

    public void stateCheck(int state, String message) {
        switch (state) {
            case 0:

                adapter.setActionItems(sortShowingAction(false));

                //meeting completed
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
        if (someHandler != null&&runnable!=null) {
            someHandler.removeCallbacks(runnable);
        }
    }
}