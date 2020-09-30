package kore.botssdk.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotListViewTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.dialogs.ListActionSheetFragment;
import kore.botssdk.dialogs.ListMoreActionSheetFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotListViewMoreDataModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class BotListViewTemplateView extends ViewGroup {

    private String LOG_TAG = BotListTemplateView.class.getSimpleName();

    private float dp1, layoutItemHeight = 0;
    private AutoExpandListView autoExpandListView;
    private TextView botCustomListViewButton;
    private TextView workBenchListViewButton;
    private LinearLayout botCustomListRoot;
    private float restrictedMaxWidth, restrictedMaxHeight;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private String title;
    private Context mContext;

    public BotListViewTemplateView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BotListViewTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public BotListViewTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_list_view_template, this, true);
        botCustomListRoot = (LinearLayout) findViewById(R.id.botCustomListRoot);
        autoExpandListView = (AutoExpandListView) findViewById(R.id.botCustomListView);
        botCustomListViewButton = (TextView) findViewById(R.id.botCustomListViewButton);
        workBenchListViewButton = (TextView) findViewById(R.id.workBenchListViewButton);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

        GradientDrawable rightDrawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.rounded_rect_feedback);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);
        String themeBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff");
        String themeTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#444444");
        rightDrawable.setColor(Color.parseColor(themeBgColor));
        rightDrawable.setStroke((int) (1*dp1), Color.parseColor(themeBgColor));
        botCustomListViewButton.setBackground(rightDrawable);

    }

    public void populateListTemplateView(String title, BotListViewMoreDataModel botListViewMoreDataModel, ArrayList<BotListModel> botListModelArrayList, final ArrayList<BotButtonModel> botButtonModelArrayList, int moreCount, String seeMore) {

        if(botListViewMoreDataModel != null)
            Log.e("More Data", botListViewMoreDataModel.getTab1().toString());

        if (botListModelArrayList != null && botListModelArrayList.size() > 0)
        {
            BotListViewTemplateAdapter botListTemplateAdapter;
            if (autoExpandListView.getAdapter() == null)
            {
                if(!StringUtils.isNullOrEmpty(seeMore)) {
                    if(moreCount != 0)
                    {
                        botListTemplateAdapter = new BotListViewTemplateAdapter(getContext(), autoExpandListView, moreCount);
                        autoExpandListView.setAdapter(botListTemplateAdapter);
                        botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                        botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
                        botListTemplateAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    botListTemplateAdapter = new BotListViewTemplateAdapter(getContext(), autoExpandListView, botListModelArrayList.size());
                    autoExpandListView.setAdapter(botListTemplateAdapter);
                    botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                    botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
                    botListTemplateAdapter.notifyDataSetChanged();
                }

            } else {
                botListTemplateAdapter = (BotListViewTemplateAdapter) autoExpandListView.getAdapter();
            }

            botCustomListRoot.setVisibility(VISIBLE);
            if(botButtonModelArrayList != null && botButtonModelArrayList.size() > 0)
            {
                botCustomListViewButton.setText(Html.fromHtml("<u>"+botButtonModelArrayList.get(0).getTitle()+"</u>"));
                botCustomListViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListActionSheetFragment bottomSheetDialog = new ListActionSheetFragment();
                        bottomSheetDialog.setisFromFullView(false);
                        bottomSheetDialog.setSkillName("skillName","trigger");
                        bottomSheetDialog.setData(botListViewMoreDataModel);
                        bottomSheetDialog.setHeaderVisible(true);
                        bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                        bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
                    }
                });


                botCustomListViewButton.setVisibility(botListModelArrayList.size() > moreCount ? VISIBLE : GONE);
            }
            else
            {
                if(moreCount != 0)
                {
                    botCustomListViewButton.setVisibility(botListModelArrayList.size() > moreCount ? VISIBLE : GONE);
                    botCustomListViewButton.setText(Html.fromHtml("<u>"+getResources().getString(R.string.show_more)+"</u>"));
                }


                botCustomListViewButton.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ListMoreActionSheetFragment bottomSheetDialog = new ListMoreActionSheetFragment();
                        bottomSheetDialog.setisFromFullView(false);
                        bottomSheetDialog.setSkillName("skillName","trigger");
                        bottomSheetDialog.setData(title, botListModelArrayList);
                        bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                        bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
                    }
                });
            }
        }
        else
        {
            botCustomListRoot.setVisibility(GONE);
            botCustomListViewButton.setVisibility(GONE);
        }
    }


    public void setRestrictedMaxHeight(float restrictedMaxHeight) {
        this.restrictedMaxHeight = restrictedMaxHeight;
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public int getViewHeight() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (int) (layoutItemHeight * count);
        }
        return viewHeight;
    }

    public int getViewWidth() {
        int viewHeight = 0;
        if (autoExpandListView != null) {
            int count = 0;
            if (autoExpandListView.getAdapter() != null) {
                count = autoExpandListView.getAdapter().getCount();
            }
            viewHeight = (count > 0) ? (int) (restrictedMaxWidth -2*dp1) : 0;
        }
        return viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;
        int totalWidth = getPaddingLeft();

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedMaxWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(botCustomListRoot, childWidthSpec, wrapSpec);

        totalHeight += botCustomListRoot.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
        totalWidth += botCustomListRoot.getMeasuredWidth() + getPaddingLeft()+getPaddingRight();
        if(totalHeight != 0){
            totalWidth = totalWidth + (int)(3 * dp1);
        }

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        int itemWidth = (r - l) / getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE)
            {
                LayoutUtils.layoutChild(child, childLeft, childTop);

                if(SDKConfiguration.BubbleColors.showIcon) {
                    LayoutUtils.layoutChild(child,  (int)(13 *dp1), 0);
                }
                childTop += child.getMeasuredHeight();
            }
        }
    }
}

