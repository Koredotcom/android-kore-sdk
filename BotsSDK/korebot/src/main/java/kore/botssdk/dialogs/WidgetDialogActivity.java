package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.adapter.WidgetCancelActionsAdapter;
import kore.botssdk.models.WidgetDialogModel;


public class WidgetDialogActivity extends Dialog {

    private ImageView img_cancel;
    WidgetDialogModel widgetDialogModel;
    TextView txtTitle, tv_time, txtPlace, tv_users;
    View sideBar;

    RecyclerView recycler_actions;



    Context mContext;



    /* public WidgetDialogActivity(@NonNull Context context) {
         super(context);
     }
 */
    public WidgetDialogActivity(Context mContext, WidgetDialogModel widgetDialogModel) {
        super(mContext, R.style.WidgetDialog);
        this.widgetDialogModel = widgetDialogModel;
        this.mContext = mContext;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawableResource(R.color.trasparent_widget);
        setContentView(R.layout.item_selection_dialog);
        initViews();

         tv_time.setText(widgetDialogModel.getTime());
        txtTitle.setText(widgetDialogModel.getTitle());

        txtPlace.setText(widgetDialogModel.getLocation());
        tv_users.setText(widgetDialogModel.getAttendies());
        txtPlace.setVisibility(widgetDialogModel.getLocation() != null && !TextUtils.isEmpty(widgetDialogModel.getLocation()) ? View.VISIBLE : View.GONE);
        sideBar.setBackgroundColor(Color.parseColor(widgetDialogModel.getColor()));

        ArrayList<String> actionList = new ArrayList<>();
        actionList.add("Reschedule");
        actionList.add("Add another");
        actionList.add("Cancel");
        actionList.add("Add another");
        actionList.add("Cancel");
        WidgetCancelActionsAdapter adapter = new WidgetCancelActionsAdapter(WidgetDialogActivity.this, actionList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycler_actions.setLayoutManager(layoutManager);
        recycler_actions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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