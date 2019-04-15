package kore.botssdk.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.adapter.WidgetCancelActionsAdapter;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.WCalEventsTemplateModel;
import kore.botssdk.models.WidgetDialogModel;


public class WidgetDialogActivity extends Dialog {

    private ImageView img_cancel;
    WidgetDialogModel widgetDialogModel;
    TextView txtTitle, tv_time, txtPlace, tv_users;
    View sideBar;

    RecyclerView recycler_actions;
    WCalEventsTemplateModel model;
    Context mContext;
    private boolean isFromFullView;



    /* public WidgetDialogActivity(@NonNull Context context) {
         super(context);
     }
 */



    public WidgetDialogActivity(Context mContext, WidgetDialogModel widgetDialogModel, WCalEventsTemplateModel model, boolean isFromFullView) {
        super(mContext, R.style.WidgetDialog);
        this.widgetDialogModel = widgetDialogModel;
        this.mContext = mContext;
        this.model = model;
        this.isFromFullView = isFromFullView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setCanceledOnTouchOutside(false);

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
        WidgetCancelActionsAdapter adapter = new WidgetCancelActionsAdapter((Activity) mContext,WidgetDialogActivity.this, model,isFromFullView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        recycler_actions.setLayoutManager(layoutManager);
        recycler_actions.setAdapter(adapter);

        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottomup);
        recycler_actions.startAnimation(bottomUp);
        recycler_actions.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }


    public void dissmissanim()
    {
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

    }


}