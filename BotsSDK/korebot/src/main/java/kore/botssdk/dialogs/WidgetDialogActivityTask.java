package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.adapter.WidgetCancelActionsAdapter;
import kore.botssdk.adapter.WidgetSelectActionsAdapter;
import kore.botssdk.databinding.WidgetFilesItemSelectionDialogBinding;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.WTaskTemplateModel;
import kore.botssdk.models.WidgetDialogModel;


public class WidgetDialogActivityTask extends Dialog {

    private ImageView img_cancel;
    WTaskTemplateModel widgetDialogModel;
    ImageView checkbox;

    RecyclerView recycler_actions;
    Context mContext;
    WTaskTemplateModel model;


    /* public WidgetDialogActivity(@NonNull Context context) {
         super(context);
     }
 */
    public WidgetDialogActivityTask(Context mContext, WTaskTemplateModel widgetDialogModel, WTaskTemplateModel model) {
        super(mContext, R.style.WidgetDialog);
        this.widgetDialogModel = widgetDialogModel;
        this.mContext = mContext;
        this.model = model;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawableResource(R.color.transparent_card);
        // ViewDataBinding mBinding = DataBindingUtil.setContentView(this,R.layout.widget_files_item_selection_dialog);

        WidgetFilesItemSelectionDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_files_item_selection_dialog, null, false);
        setContentView(binding.getRoot());

        binding.setTaskData(widgetDialogModel);

        // WidgetFilesItemSelectionDialogBinding binding = DataBindingUtil.setContentView(mContext, R.layout.activity_main);


        // setContentView(R.layout.widget_files_item_selection_dialog);
        initViews();
        recycler_actions.setVisibility(View.GONE);
        WidgetSelectActionsAdapter adapter = new WidgetSelectActionsAdapter(WidgetDialogActivityTask.this, model);
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

    public void dissmissanim() {
        Animation bottomdown = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottomdown);
        recycler_actions.startAnimation(bottomdown);
        recycler_actions.setVisibility(View.INVISIBLE);
    }

    private void initViews() {

        img_cancel = findViewById(R.id.img_cancel);
        checkbox = findViewById(R.id.checkbox);
        checkbox.setVisibility(View.GONE);
        recycler_actions = findViewById(R.id.recycler_actions);

    }

}