package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BotTableDataModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 30-Oct-17.
 */

public class TableView extends ViewGroup {
    private Context mContext;
    TableLayout mTable;
    int dp1;
    public TableView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init(){
        LayoutInflater.from(mContext).inflate(R.layout.bot_table_view, this, true);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        mTable = (TableLayout) findViewById(R.id.tableView);
//        setBackgroundColor(Color.BLUE);
    }

    public void populateTableView(BotTableDataModel data){
        if(data != null){
            mTable.removeAllViews();
            int size = data.getHeaders().size();
            int rowSize = data.getRows().size();

                TableRow tr = new TableRow(mContext);
//                if(i%2==0)tr.setBackgroundColor(Color.GRAY);
//                tr.setId(100+i);
//                tr.setBackgroundColor(Color.GRAY);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                //Create two columns to add as table data
                // Create a TextView to add date
            for(int i = 0; i < size; i++) {
                TextView txtHeader = new TextView(mContext);
                txtHeader.setId(200 + i);
                txtHeader.setBackgroundResource(R.drawable.tableview_cell_shape);
                txtHeader.setText(data.getHeaders().get(i).getTitle());
                txtHeader.setPadding(2, 0, 5, 0);
                txtHeader.setTextColor(Color.BLACK);
                txtHeader.setTextSize(14);
                tr.addView(txtHeader);
            }
            mTable.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            for(int j=0; j< rowSize; j++){
                TableRow trdata = new TableRow(mContext);
//                if(i%2==0)tr.setBackgroundColor(Color.GRAY);
//                tr.setId(100+i);
//                trdata.setBackgroundColor(Color.GRAY);
                trdata.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                for(int k=0; k<size;k++){
                    if(data.getRows().get(j).size() != size) {
                        continue;
                    }
                    TextView txtData = new TextView(mContext);
                    txtData.setId(200 + k);
                    txtData.setBackgroundResource(R.drawable.tableview_cell_shape);
                    txtData.setText(data.getRows().get(j).get(k) != null?data.getRows().get(j).get(k):"");
                    txtData.setPadding(2, 0, 5, 0);
                    txtData.setTextColor(Color.BLACK);
                    txtData.setTextSize(14);
                    trdata.addView(txtData);
                }
                mTable.addView(trdata, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
                /*TextView labelWEIGHT = new TextView(mContext);
                labelWEIGHT.setId(200+i);
                labelWEIGHT.setText(data.getRows().get(i).get(0)!=null?data.getRows().get(i).get(0):"");
                labelWEIGHT.setTextColor(Color.BLACK);
                tr.addView(labelWEIGHT);*/




                
            }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        //walk through each child, and arrange it from left to right
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

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

        /*
         * For Pie View Layout
         */
        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.EXACTLY);
        childHeightSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.pie_layout_height), MeasureSpec.UNSPECIFIED);

        MeasureUtils.measure(mTable, childWidthSpec, childHeightSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
