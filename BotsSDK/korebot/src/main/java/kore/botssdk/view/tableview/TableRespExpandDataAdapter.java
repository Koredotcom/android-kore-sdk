package kore.botssdk.view.tableview;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.view.BotTableView;
import kore.botssdk.view.tableview.model.MiniTableModel;
import kore.botssdk.view.tableview.model.TableColumnModel;
import kore.botssdk.view.tableview.model.TableColumnWeightModel;
import kore.botssdk.view.viewUtils.DimensionUtil;

public abstract class TableRespExpandDataAdapter<T> extends BaseExpandableListAdapter {

    private static final String LOG_TAG = TableDataAdapter.class.getName();
    private final List<T> data;
    private final PayloadInner payloadInner;
    private TableColumnModel columnModel;
    private final Context mContext;
    final int dp1;
    private static final int TEXT_SIZE = 14;
    private Dialog dialog;

    /**
     * Creates a new TableDataAdapter.
     *
     * @param context The context that shall be used.
     * @param data    The data that shall be displayed.
     */
    public TableRespExpandDataAdapter(final Context context, final T[] data, PayloadInner payloadInner) {
        this(context, 0, new ArrayList<>(Arrays.asList(data)), payloadInner);
    }

    /**
     * Creates a new TableDataAdapter.
     *  @param context The context that shall be used.
     * @param data    The data that shall be displayed.
     */
    public TableRespExpandDataAdapter(final Context context, final List<MiniTableModel> data, PayloadInner payloadInner) {
        this(context, 0, (List<T>) data, payloadInner);
    }

    /**
     * Creates a new TableDataAdapter. (internally used)
     *
     * @param context     The context that shall be used.
     * @param columnCount The number of columns.
     * @param data        The data which shall be displayed in the table.
     */
    protected TableRespExpandDataAdapter(final Context context, final int columnCount, final List<T> data, PayloadInner payloadInner) {
        this(context, new TableColumnWeightModel(columnCount), data, payloadInner);
    }

    /**
     * Creates a new TableDataAdapter. (internally used)
     *
     * @param context     The context that shall be used.
     * @param columnModel The column model to be used.
     * @param data        The data which shall be displayed in the table.
     */
    protected TableRespExpandDataAdapter(final Context context, final TableColumnModel columnModel, final List<T> data, PayloadInner payloadInner) {
        this.columnModel = columnModel;
        this.data = data;
        this.mContext = context;
        this.dp1 = (int) DimensionUtil.dp1;
        this.payloadInner = payloadInner;
    }

    /**
     * Gives the data object that shall be displayed in the row with the given index.
     *
     * @param rowIndex The index of the row to get the data for.
     * @return The data that shall be displayed in the row with the given index.
     */
    public T getRowData(final int rowIndex) {
        return data.get(rowIndex);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        MiniTableModel miniTableModel = (MiniTableModel)data.get(groupPosition);
        LogUtils.e("Child Count", miniTableModel.getElements().size()+"");
        return 1 ;
    }

    @Override
    public int getGroupCount() {
        return 5;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        MiniTableModel miniTableModel = (MiniTableModel)data.get(groupPosition);
        return miniTableModel.getElements().get(childPosititon) ;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Gives the data that is set to this adapter.
     *
     * @return The data this adapter is currently working with.
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Gives the {@link Context} of this adapter. (Hint: use this method in the {@code getHeaderView()}-method
     * to programmatically initialize new views.)
     *
     * @return The {@link Context} of this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Gives the {@link LayoutInflater} of this adapter. (Hint: use this method in the
     * {@code getHeaderView()}-method to inflate xml-layout-files.)
     *
     * @return The {@link LayoutInflater} of the context of this adapter.
     */
    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Gives the {@link Resources} of this adapter. (Hint: use this method in the
     * {@code getCellView()}-method to resolve resources.)
     *
     * @return The {@link Resources} of the context of this adapter.
     */
    public Resources getResources() {
        return getContext().getResources();
    }

    /**
     * Method that gives the cell views for the different table cells.
     *
     * @param rowIndex    The index of the row to return the table cell view.
     * @param columnIndex The index of the column to return the table cell view.
     * @param parentView  The view to which the returned view will be added.
     * @return The created header view for the given column.
     */
    public abstract View getCellView(int rowIndex, int columnIndex, ViewGroup parentView, boolean showDivider);

    /**
     * Method that gives the cell views for the different table cells.
     *
     * @param rowIndex    The index of the row to return the table cell view.
     * @param columnIndex The index of the column to return the table cell view.
     * @param parentView  The view to which the returned view will be added.
     * @return The created header view for the given column.
     */
    public abstract View getGroupedView(int rowIndex, int columnIndex, ViewGroup parentView);

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        final LinearLayout rowView = new LinearLayout(getContext());
        final LinearLayout parentGroupView = new LinearLayout(getContext());
        final LinearLayout parentChildView = new LinearLayout(getContext());

        final AbsListView.LayoutParams rowLayoutParams = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowView.setLayoutParams(rowLayoutParams);
        rowView.setGravity(Gravity.CENTER_VERTICAL);

        final int tableWidth = parent.getWidth();

        if(groupPosition != 4)
        {
            for (int columnIndex = 0; columnIndex < 2; columnIndex++)
            {
                View groupView = getGroupedView(groupPosition, columnIndex, rowView);
                if (groupView == null) {
                    groupView = new TextView(getContext());
                }

                final int cellWidth = columnModel.getColumnWidth(columnIndex, (int)(tableWidth*1.5));
                final LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(cellWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                groupView.setLayoutParams(cellLayoutParams);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;

                View imgView = renderArrowView();
                imgView.setLayoutParams(cellLayoutParams);
                parentGroupView.addView(groupView);

                if(columnIndex == 1)
                {
                    parentGroupView.addView(imgView);
                }
            }

            for (int columnIndex = 0; columnIndex < 2; columnIndex++)
            {
                View cellView = getCellView(groupPosition, columnIndex, rowView, false);
                if (cellView == null) {
                    cellView = new TextView(getContext());
                }

                final int cellWidth = columnModel.getColumnWidth(columnIndex, (int)(tableWidth*1.5));
                final LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(cellWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                cellView.setLayoutParams(cellLayoutParams);

                View imgView = renderChildArrowView(true);
                imgView.setLayoutParams(cellLayoutParams);

                parentChildView.addView(cellView);

                if(columnIndex == 1)
                {
                    parentChildView.addView(imgView);
                }
            }

            rowView.addView(parentGroupView);
            rowView.addView(parentChildView);

            parentGroupView.setVisibility(View.VISIBLE);
            parentChildView.setVisibility(View.GONE);

            if(isExpanded)
            {
                parentGroupView.setVisibility(View.GONE);
                parentChildView.setVisibility(View.VISIBLE);
            }
        }
        else
        {

            final TextView textView = new TextView(getContext());
            textView.setText("Show More");
            textView.setTextSize(TEXT_SIZE);
            textView.setTextColor(Color.BLUE);
            textView.setGravity(Gravity.CENTER);

            final int cellWidth = columnModel.getColumnWidth(0, tableWidth * 4);
            final LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(cellWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            cellLayoutParams.setMargins(0,11 * dp1,0,0);
            textView.setLayoutParams(cellLayoutParams);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTableViewDialog(cellLayoutParams);
                }
            });
            rowView.addView(textView);
        }

        return rowView;
    }

    private void showTableViewDialog(LinearLayout.LayoutParams cellLayoutParams)
    {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.tableview_dialog);

        LinearLayout llTableView = dialog.findViewById(R.id.llTableView);

        BotTableView tableView = new BotTableView(getContext());
        tableView.setData(payloadInner);
        llTableView.addView(tableView);
        llTableView.setLayoutParams(cellLayoutParams);

        dialog.show();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final LinearLayout rowView = new LinearLayout(getContext());

        final AbsListView.LayoutParams rowLayoutParams = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowView.setLayoutParams(rowLayoutParams);
        rowView.setGravity(Gravity.CENTER_VERTICAL);

        final int tableWidth = parent.getWidth();

        for (int columnIndex = 2; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            View cellView = getCellView(groupPosition, columnIndex, rowView, true);
            if (cellView == null) {
                cellView = new TextView(getContext());
            }

            final int cellWidth = columnModel.getColumnWidth(columnIndex, (int)(tableWidth * 1.5));
            final LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(cellWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            cellView.setLayoutParams(cellLayoutParams);

            cellView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Clicked View "+groupPosition, Toast.LENGTH_SHORT).show();
                }
            });

            rowView.addView(cellView);

            View imgView = renderChildArrowView(false);
            imgView.setLayoutParams(cellLayoutParams);

            if(columnIndex == columnModel.getColumnCount() -1)
                rowView.addView(imgView);
        }

        return rowView;
    }

//    /**
//     * Sets the {@link TableDataRowBackgroundProvider} that will be used to define the table data rows background.
//     *
//     * @param rowBackgroundProvider The {@link TableDataRowBackgroundProvider} that shall be used.
//     */
//    protected void setRowBackgroundProvider(final TableDataRowBackgroundProvider<? super T> rowBackgroundProvider) {
//        this.rowBackgroundProvider = rowBackgroundProvider;
//    }

    /**
     * Gives the {@link TableColumnWeightModel} that is currently used to render the table headers.
     *
     * @return The {@link TableColumnModel} which is currently used..
     */
    protected TableColumnModel getColumnModel() {
        return columnModel;
    }

    /**
     * Sets the {@link TableColumnModel} that will be used to render the table cells.
     *
     * @param columnModel The {@link TableColumnModel} that should be set.
     */
    protected void setColumnModel(final TableColumnModel columnModel) {
        this.columnModel = columnModel;
    }

    private View renderArrowView() {
        LinearLayout renderView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.table_responsive_group_layout, null);
        TextView tvViewGroup1 = renderView.findViewById(R.id.tvViewGroup1);
        ImageView ivArrow = renderView.findViewById(R.id.ivArrowView);
        ivArrow.setVisibility(View.VISIBLE);
        return renderView;
    }

    private View renderChildArrowView(boolean showDivider) {
        LinearLayout renderView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.table_resp_child_arrow_view, null);
        ImageView ivArrowView = renderView.findViewById(R.id.ivArrowView);
        ImageView ivChildArrowDivider = renderView.findViewById(R.id.ivChildArrowDivider);
        ivArrowView.setVisibility(View.VISIBLE);
        ivChildArrowDivider.setVisibility(View.GONE);

        if(!showDivider)
        {
            ivArrowView.setVisibility(View.GONE);
            ivChildArrowDivider.setVisibility(View.VISIBLE);
        }


        return renderView;
    }
}


