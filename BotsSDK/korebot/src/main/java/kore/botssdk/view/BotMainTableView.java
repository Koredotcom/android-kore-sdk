package kore.botssdk.view;/*
package kore.botssdk.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.tableview.BotTableView;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

*/
/**
 * Created by Ramachandra Pradeep on 13-Apr-18.
 *//*


public class BotMainTableView extends ViewGroup {

//    private BotTableView mTable;
    private Context mContext;
    private int dp1;
//    LinearLayout tableContainer;
    private int MAX_HEIGHT;


    public BotMainTableView(Context context) {
        super(context);
        mContext = context;
        init();
    }
    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.table_view, this);
//        tableContainer = (LinearLayout) findViewById(R.id.table_view_layout);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        MAX_HEIGHT = (int) AppControl.getInstance().getDimensionUtil().screenHeight;
//        mTable = new BotTableView(mContext);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

// set the marker to the chart
//        addView(mTable);
//        int margin = 5*dp1;
//        setPadding(margin,margin,margin,margin);
//        setBackgroundResource(R.drawable.round_rect);
//        setBackgroundColor(mContext.getResources().getColor(R.color.bgLightBlue));
    }

    public void setData(String template_type, PayloadInner payloadInner){
//        tableContainer.removeAllViews();
        removeAllViews();

        */
/*if(BotResponse.TEMPLATE_TYPE_MINITABLE.equals(template_type)) {
            for(int index=0; index <payloadInner.getMiniTableDataModels().size(); index++){
                BotTableView mTable = new BotTableView(mContext);
                //Set layoutParams
                String[] alignment = mTable.addHeaderAdapter(payloadInner.getMiniTableDataModels().get(index).getPrimary());
                mTable.addDataAdapter(template_type, payloadInner.getMiniTableDataModels().get(index).getAdditional(),alignment);
//                tableContainer.addView(mTable, index);
                addView(mTable);
                *//*
*/
/*if(index > 0){
                    View v = new View(mContext);
                    v.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            5
                    ));
                    v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                    tableContainer.addView(v);
                }*//*
*/
/*
            }
        }else*//*
 if(BotResponse.TEMPLATE_TYPE_TABLE.equals(template_type)){
            BotTableView mTable = new BotTableView(mContext);
            String[] alignment = mTable.addHeaderAdapter(payloadInner.getColumns());
            mTable.addDataAdapterForTable(payloadInner,alignment);
//            tableContainer.addView(mTable);
            addView(mTable);
        }
    }

    protected int getViewHeight(int index){
        int viewHeight = 0;
        if(getChildAt(index) != null && getChildAt(index) instanceof BotTableView) {
            return ((BotTableView) getChildAt(index)).getRowCount() * (48 * dp1);
        } else {
            return 0;
        }

    }

    */
/*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int childWidthSpec,childHeightSpec;
        int viewHeight = 0;

        //Iterate through every child Tables ie. BotTableView
        for(int i=0; i < Math.min(getChildCount(),1); i++) {
            viewHeight = getViewHeight(i);
            View childView = getChildAt(i);
            childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.line_layout_height), MeasureSpec.EXACTLY);
            childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
            MeasureUtils.measure(childView, childWidthSpec, childHeightSpec); // Apply measure height for BotTableView
//            totViewHeight += viewHeight; //Keep adding BotTableView for total height
        }

//        childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
//        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
//        MeasureUtils.measure(tableContainer, childWidthSpec, childHeightSpec);
//        for(int i= 0; i<childs;i++){

//            childHeight += mTable.getMeasuredHeight();
//        }
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        int parentHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.line_layout_height), MeasureSpec.EXACTLY);

       // super.onMeasure(parentWidthSpec, parentHeightSpec);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);

    }*//*


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;

        */
/*
         * For Pie View Layout
         *//*

        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.my_table_height), MeasureSpec.EXACTLY);

        MeasureUtils.measure(getChildAt(0), childWidthSpec, childHeightSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;//this.getPaddingLeft();
        int childTop = 0;//this.getPaddingTop();

        //walk through each child, and arrange it from left to right
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
*/
