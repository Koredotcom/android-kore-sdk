package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class TaskViewWidget extends ViewGroup {
    private TextView creatorView;
    private TextView titleView;
    private TextView dateView;
    private TextView assigneeView;
    private TextView statusView;
    private View rootView;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.task_view_layout, this, true);
        KaFontUtils.applyCustomFont(getContext(), view);
        creatorView = (TextView) view.findViewById(R.id.creator_view);
        assigneeView = view.findViewById(R.id.assignee_view);
        rootView = view.findViewById(R.id.root_layout);
        titleView = (TextView) view.findViewById(R.id.title_view);
        dateView = (TextView) view.findViewById(R.id.date_view);
        statusView = (TextView) view.findViewById(R.id.status_view);
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


    public void populateData(final TaskTemplateModel taskTemplateModel) {
        if (taskTemplateModel != null) {
            rootView.setVisibility(VISIBLE);
            titleView.setText(taskTemplateModel.getTitle());
            creatorView.setText(taskTemplateModel.getOwner().getNameInFirstNameFormat());
            assigneeView.setText(taskTemplateModel.getAssignee().getNameInFirstNameFormat());
            statusView.setText(taskTemplateModel.getStatus());
            dateView.setText(taskTemplateModel.getDueDate());

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

