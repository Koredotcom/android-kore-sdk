package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.TasksListAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.TaskTemplateResponse;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class TaskViewWidget extends ViewGroup {
    private ListView listView;
    private View rootView;
    private View buttonView;

    private TextView button1;
    private TextView button2;

    float dp1;
    Gson gson = new Gson();
    private float restrictedLayoutWidth;

    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }

    public TaskViewWidget(Context context) {
        super(context);
        init();
    }

    public TaskViewWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterFragment.ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.task_preview_layout, this, true);
        KaFontUtils.applyCustomFont(getContext(), view);
        rootView = view.findViewById(R.id.root_layout);
        listView = view.findViewById(R.id.botCustomButtonList);
        buttonView = view.findViewById(R.id.buttonPanel);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }


    public void populateData(final TaskTemplateResponse taskTemplateModel, int viewPosition, boolean isEnabled) {
        if (taskTemplateModel != null) {
            TasksListAdapter tasksListAdapter = new TasksListAdapter(getContext(), taskTemplateModel.getTaskData(), taskTemplateModel.isShowButton());
            listView.setAdapter(tasksListAdapter);
            if (isEnabled && SelectionUtils.getSelectedTasks() != null) {
                tasksListAdapter.addSelectedTasks(SelectionUtils.getSelectedTasks());
            }
            rootView.setAlpha(isEnabled || !taskTemplateModel.isShowButton() ? 1.0f : 0.5f);
            tasksListAdapter.notifyDataSetChanged();
            rootView.setVisibility(VISIBLE);
            buttonView.setVisibility(GONE);
            buttonView.setAlpha(isEnabled && tasksListAdapter.getSelectedTasks().size() > 0 ? 1.0f : 0.5f);
            ArrayList<BotButtonModel> buttonModels = taskTemplateModel.getButtons();
            if (taskTemplateModel.isShowButton() && buttonModels != null) {
                button1.setVisibility(GONE);
                button2.setVisibility(GONE);
                buttonView.setVisibility(VISIBLE);
                for (int i = 0; i <= buttonModels.size() - 1; i++) {
                    if (i == 0) {
                        button1.setVisibility(VISIBLE);
                        button1.setText(buttonModels.get(i).getTitle());
                    } else if (i == 1) {
                        button2.setVisibility(VISIBLE);
                        button2.setText(buttonModels.get(i).getTitle());
                    } else {
                        break;
                    }
                }
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isEnabled && !"close".equalsIgnoreCase(tasksListAdapter.getItem(position).getStatus())) {
                        tasksListAdapter.addOrRemoveSelectedTask(tasksListAdapter.getItem(position).getId());
                        tasksListAdapter.notifyDataSetChanged();
                        SelectionUtils.setSelectedTasks(tasksListAdapter.getSelectedTasks());
                        buttonView.setAlpha(tasksListAdapter.getSelectedTasks().size() > 0 ? 1.0f : 0.5f);

                    }

                }
            });
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> selectedTasks = tasksListAdapter.getSelectedTasks();
                    if (buttonModels != null && buttonModels.size() > 0 && selectedTasks.size() > 0) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("action", buttonModels.get(0).getAction());
                        hashMap.put("tIds", selectedTasks);
                        composeFooterInterface.sendWithSomeDelay(buttonModels.get(0).getPayload(), gson.toJson(hashMap), 0);
                    }

                }
            });


            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> selectedTasks = tasksListAdapter.getSelectedTasks();
                    if (buttonModels != null && buttonModels.size() > 0 && selectedTasks.size() > 0) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("action", buttonModels.get(1).getAction());
                        hashMap.put("tIds", selectedTasks);
                        composeFooterInterface.sendWithSomeDelay(buttonModels.get(1).getPayload(), gson.toJson(hashMap), 0);
                    }
                }
            });

        } else {
            rootView.setVisibility(GONE);

        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}

