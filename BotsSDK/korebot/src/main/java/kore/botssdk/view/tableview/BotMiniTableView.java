package kore.botssdk.view.tableview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BotMiniTableModel;
import kore.botssdk.view.tableview.adapters.MiniBotTableAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;
import kore.botssdk.view.tableview.model.TableColumnWeightModel;
import kore.botssdk.view.tableview.toolkit.SimpleTableHeaderAdapter;
import kore.botssdk.view.tableview.toolkit.TableDataRowBackgroundProviders;
import kore.botssdk.view.viewUtils.LayoutUtils;

/**
 * Extension of the {@link TableView} that gives the possibility to sort the table by every single
 * column. For this purpose implementations of {@link Comparator} are used. If there is a comparator
 * set for a column the  will automatically display an ImageView at the start
 * of the header indicating to the user, that this column is sortable.
 * If the user clicks this header the given comparator will used to sort the table ascending by the
 * content of this column. If the user clicks this header again, the table is sorted descending
 * by the content of this column.
 *
 * @author ISchwarz
 */
public class BotMiniTableView extends TableView<MiniTableModel> {


    private Context context;
    private int dp1;
    public BotMiniTableView(final Context context) {
        this(context, null);
        this.context = context;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
//        setBackgroundColor(0xffff0000);

    }

    public BotMiniTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
//        setBackgroundColor(0xffff0000);

    }

    public BotMiniTableView(final Context context, final AttributeSet attributes, final int styleAttributes){
        super(context,attributes,styleAttributes);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
//        setBackgroundColor(0xffff0000);

    }
    public String[] addHeaderAdapter( List<List<String>> primary){
        String[]  headers = new String[primary.size()];
        String[] alignment = new String[primary.size()];
        String defaultAlign = "left";
        for(int i=0; i<primary.size();i++){
            headers[i] = primary.get(i).get(0);
            if(primary.get(i).size() > 1)
                alignment[i] = primary.get(i).get(1);
            else
                alignment[i] = defaultAlign;
        }
        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, headers,alignment);
        simpleTableHeaderAdapter.setTextColor(context.getResources().getColor(R.color.primaryDark));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven =context.getResources().getColor(R.color.table_data_row_even);
        //final int rowColorOdd = context.getResources().getColor(R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorEven));
//        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(headers.length);
        for(int index=0;index<headers.length;index++) {
            tableColumnWeightModel.setColumnWeight(index, 3);
        }
        setColumnCount(headers.length);
        setColumnModel(tableColumnWeightModel);
        return alignment;
    }

    /*public void addDataAdapter(String template_type, List<List<Object>> additional, String[] alignment){
        if(BotResponse.TEMPLATE_TYPE_MINITABLE.equals(template_type) || BotResponse.TEMPLATE_TYPE_TABLE.equals(template_type)) {
            List<MiniTableModel> lists = new ArrayList<>();
            for(int j=0; j<additional.size();j++) {
                MiniTableModel model = new MiniTableModel();
                model.setElements(additional.get(j));
                lists.add(model);
            }
            BotTableAdapter tableAdapter = new BotTableAdapter(context,lists, alignment);
            setDataAdapter(tableAdapter);
        }

    }*/



    public void setData(BotMiniTableModel model){
        if(model != null) {
            addHeaderAdapter(model.getPrimary());
            addDataAdapterForTable(model.getAdditional());
        }else{
            setDataAdapter(null);
        }
    }

    public void addDataAdapterForTable(List<List<Object>> additional) {

        List<MiniTableModel> lists = new ArrayList<>();
        for(int j=0; j<additional.size();j++) {
            MiniTableModel model = new MiniTableModel();
            model.setElements(additional.get(j));
            lists.add(model);
        }
        MiniBotTableAdapter tableAdapter = new MiniBotTableAdapter(context,lists);
        setDataAdapter(tableAdapter);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            height = getListViewHeightBasedOnChildren(tableDataView);
            height += tableHeaderView.getMeasuredHeight();

            /*if (height != 0 ) {
                height = height + (int) (25 * dp1);
            }*/
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height+getPaddingTop(), MeasureSpec.EXACTLY);
//            Log.d("IKIDO","On measure called for botminitab , The total height is "+ height);
    /*        for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.getLayoutParams().height = height;
                child.requestLayout();
            }*/
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return  0;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, dp1*40);

            int measuredHeight = dp1*40;
//            Log.d("IKIDO","On measure called for listview , The total height of individual view is "+ measuredHeight);
            totalHeight += measuredHeight;
        }
        return totalHeight;

    }

}
