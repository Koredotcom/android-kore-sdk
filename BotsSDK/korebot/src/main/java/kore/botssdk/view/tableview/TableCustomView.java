package kore.botssdk.view.tableview;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.BotTableView;
import kore.botssdk.view.tableview.colorizers.TableDataRowColorizer;
import kore.botssdk.view.tableview.listeners.TableHeaderClickListener;
import kore.botssdk.view.tableview.model.TableColumnModel;
import kore.botssdk.view.tableview.model.TableColumnWeightModel;
import kore.botssdk.view.tableview.providers.TableDataRowBackgroundProvider;
import kore.botssdk.view.tableview.toolkit.TableDataRowBackgroundProviders;

public class TableCustomView<T> extends LinearLayout {

    private final String LOG_TAG = TableView.class.getName();

    private final int DEFAULT_COLUMN_COUNT = 4;
    private final int DEFAULT_HEADER_ELEVATION = 1;
//    private static final int DEFAULT_HEADER_COLOR = 0xFFCCCCCC;

    /*private final Set<TableDataLongClickListener<T>> dataLongClickListeners = new HashSet<>();
    private final Set<TableDataClickListener<T>> dataClickListeners = new HashSet<>();
    private final Set<OnScrollListener> onScrollListeners = new HashSet<>();*/
    private final LayoutTransition layoutTransition;

    private TableDataRowBackgroundProvider<? super T> dataRowBackgroundProvider =
            TableDataRowBackgroundProviders.similarRowColor(0x00000000);
    private TableColumnModel columnModel;
    protected TableHeaderView tableHeaderView;
    protected ListView tableDataView;
    private TableDataAdapter<T> tableDataAdapter;
    private TableHeaderAdapter tableHeaderAdapter;
    private LinearLayout llTableDataView;
    private TextView botTableShowMoreButton;
    private final float dp1;
    private PayloadInner payloadInner;
    private int headerElevation;
    private Dialog dialog;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
//    private int headerColor;


    /**
     * Creates a new TableView with the given context.\n
     * (Has same effect like calling {@code new TableView(context, null, android.R.attr.listViewStyle})
     *
     * @param context The context that shall be used.
     */
    public TableCustomView(final Context context) {
        this(context, null);
    }

    /**
     * Creates a new TableView with the given context.\n
     * (Has same effect like calling {@code new TableView(context, attrs, android.R.attr.listViewStyle})
     *
     * @param context    The context that shall be used.
     * @param attributes The attributes that shall be set to the view.
     */
    public TableCustomView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    /**
     * Creates a new TableView with the given context.
     *
     * @param context         The context that shall be used.
     * @param attributes      The attributes that shall be set to the view.
     * @param styleAttributes The style attributes that shall be set to the view.
     */
    public TableCustomView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);
        this.dp1 = AppControl.getInstance().getDimensionUtil().density;
        setOrientation(LinearLayout.VERTICAL);
        setAttributes(attributes);
        setupTableHeaderView(attributes);
        setupTableDataView(attributes, styleAttributes);
        setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.round_rect, context.getTheme()));
        layoutTransition = new LayoutTransition();
    }

    /**
     * Replaces the default {@link TableHeaderView} with the given one.
     *
     * @param headerView The new {@link TableHeaderView} that should be set.
     */
    protected void setHeaderView(final TableHeaderView headerView) {
        this.tableHeaderView = headerView;

        tableHeaderView.setAdapter(tableHeaderAdapter);
        tableHeaderView.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.round_rect_table_header, getContext().getTheme()));
        tableHeaderView.setId(R.id.table_header_view);

        if (getChildCount() == 2) {
            removeViewAt(0);
        }

        addView(tableHeaderView, 0);
        setHeaderElevation(headerElevation);

        forceRefresh();
    }

    /**
     * Sets the {@link TableView} header visible or hides it.
     *
     * @param visible Whether the {@link TableView} header shall be visible or not.
     */
    public void setHeaderVisible(boolean visible) {
        setHeaderVisible(visible, 0);
    }

    /**
     * Sets the {@link TableView} header visible or hides it.
     *
     * @param visible Whether the {@link TableView} header shall be visible or not.
     */
    public void setHeaderVisible(boolean visible, int animationDuration) {
        if (visible && !isHeaderVisible()) {
            setLayoutTransition(null);
            addView(tableHeaderView, 0);
        } else if (!visible && isHeaderVisible()) {
            setLayoutTransition(null);
            removeView(tableHeaderView);
        }
    }

    /**
     * Gives a boolean that indicates if the {@link TableView} header is visible or not.
     *
     * @return A boolean that indicates if the {@link TableView} header is visible or not.
     */
    public boolean isHeaderVisible() {
        return getChildCount() == 2;//tableHeaderView.getVisibility() == VISIBLE;
    }

    /**
     * Sets the view that shall be shown if no data is available.
     *
     * @param emptyDataView The reference to the view that shall be shown if no data is available.
     */
    public void setEmptyDataIndicatorView(final View emptyDataView) {
        tableDataView.setEmptyView(emptyDataView);
    }

    public void setPayloadInner(PayloadInner payloadInner)
    {
        this.payloadInner = payloadInner;
    }

    public void setTableComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setTableInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    /**
     * Sets the given resource as background of the table header.
     *
     * @param resId The if of the resource tht shall be set as background of the table header.
     */
    public void setHeaderBackground(@DrawableRes final int resId) {
        tableHeaderView.setBackgroundResource(resId);
    }

    /**
     * Sets the given color as background of the table header.
     *
     * @param color The color that shall be set as background of the table header.
     */
    public void setHeaderBackgroundColor(@ColorInt final int color) {
        tableHeaderView.setBackgroundColor(color);
//        swipeRefreshLayout.setColorSchemeColors(color);
    }

    /**
     * Sets the elevation level of the header view. If you are not able to see the elevation shadow
     * you should set a background(-color) to the header.
     *
     * @param elevation The elevation that shall be set to the table header.
     */
    public void setHeaderElevation(final int elevation) {
        ViewCompat.setElevation(tableHeaderView, elevation);
    }

    /**
     * Sets the given {@link TableDataRowColorizer} that will be used to define the background color for
     * every table data row.
     *
     * @param colorizer The {@link TableDataRowColorizer} that shall be used.
     * @deprecated This method is deprecated. Use {@link TableView#setDataRowBackgroundProvider} instead.
     */
    @Deprecated
    public void setDataRowColorizer(final TableDataRowColorizer<? super T> colorizer) {
        setDataRowBackgroundProvider(new TableDataRowBackgroundColorProvider<>(colorizer));
    }

    /**
     * Sets the given {@link TableDataRowBackgroundProvider} that will be used to define the background color for
     * every table data row.
     *
     * @param backgroundProvider The {@link TableDataRowBackgroundProvider} that shall be used.
     */
    public void setDataRowBackgroundProvider(final TableDataRowBackgroundProvider<? super T> backgroundProvider) {
        dataRowBackgroundProvider = backgroundProvider;
        tableDataAdapter.setRowBackgroundProvider(dataRowBackgroundProvider);
    }

    /**
     * Removes the given {@link TableHeaderClickListener} from this table.
     *
     * @param listener The listener that shall be removed from this table.
     * @deprecated This method has been deprecated in the version 2.2.0 for naming alignment reasons. Use the method
     * {@link TableView#removeHeaderClickListener(TableHeaderClickListener)} instead.
     */
    @Deprecated
    public void removeHeaderListener(final TableHeaderClickListener listener) {
        tableHeaderView.removeHeaderClickListener(listener);
    }

    /**
     * Sets the {@link TableHeaderAdapter} that is used to render the header views for each column.
     *
     * @param headerAdapter The {@link TableHeaderAdapter} that should be set.
     */
    public void setHeaderAdapter(final TableHeaderAdapter headerAdapter) {
        tableHeaderAdapter = headerAdapter;
        tableHeaderAdapter.setColumnModel(columnModel);
        tableHeaderView.setAdapter(tableHeaderAdapter);
        forceRefresh();
    }

    /**
     * Gives the {@link TableDataAdapter} that is used to render the data view for each cell.
     *
     * @return The {@link TableDataAdapter} that is currently set.
     */
    public TableDataAdapter<T> getDataAdapter() {
        return tableDataAdapter;
    }

    /**
     * Sets the {@link TableDataAdapter} that is used to render the data view for each cell.
     *
     * @param dataAdapter The {@link TableDataAdapter} that should be set.
     */
    public void setDataAdapter(final TableDataAdapter<T> dataAdapter, int count, boolean showiewMore) {
        if(dataAdapter != null) {
            tableDataAdapter = dataAdapter;
            tableDataAdapter.setCount(count);
            tableDataAdapter.setColumnModel(columnModel);
            tableDataAdapter.setRowBackgroundProvider(dataRowBackgroundProvider);
            tableDataView.setAdapter(tableDataAdapter);

            if(showiewMore)
                botTableShowMoreButton.setVisibility(VISIBLE);

            botTableShowMoreButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTableViewDialog();
                }
            });

            forceRefresh();
        }else{
            tableDataView.setAdapter(null);
            tableHeaderView.setAdapter(null);
        }
    }

    private void showTableViewDialog()
    {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.tableview_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        LinearLayout llTableView = dialog.findViewById(R.id.llTableView);
        ImageView ivDialogClose = dialog.findViewById(R.id.ivDialogClose);
        BotTableView tableView = new BotTableView(getContext());
        tableView.setComposeFooterInterface(composeFooterInterface);
        tableView.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        tableView.setDialogReference(dialog);
        tableView.setData(payloadInner);
        llTableView.addView(tableView);

        ivDialogClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Gives the {@link TableColumnModel} which is currently set to this {@link TableView}.
     *
     * @return The current {@link TableColumnModel}
     */
    public TableColumnModel getColumnModel() {
        return columnModel;
    }

    /**
     * Sets the given {@link TableColumnModel} to this {@link TableView}.
     *
     * @param columnModel The {@link TableColumnModel} that shall be used.
     */
    public void setColumnModel(final TableColumnModel columnModel) {
        this.columnModel = columnModel;
        this.tableHeaderAdapter.setColumnModel(this.columnModel);
        this.tableDataAdapter.setColumnModel(this.columnModel);
        forceRefresh();
    }

    /**
     * Gives the number of columns of this table.
     *
     * @return The current number of columns.
     */
    public int getColumnCount() {
        return columnModel.getColumnCount();
    }

    public int getRowCount(){
        return tableDataView.getCount()+1;
    }

    /**
     * Sets the number of columns of this table.
     *
     * @param columnCount The number of columns.
     */
    public void setColumnCount(final int columnCount) {
        columnModel.setColumnCount(columnCount);
        forceRefresh();
    }

    /**
     * Sets the column weight (the relative width of the column) of the given column.
     *
     * @param columnIndex  The index of the column the weight should be set to.
     * @param columnWeight The weight that should be set to the column.
     * @deprecated This method has been deprecated in the version 2.4.0. Use the method
     * {@link #setColumnModel(TableColumnModel)} instead.
     */
    @Deprecated
    public void setColumnWeight(final int columnIndex, final int columnWeight) {
        if (columnModel instanceof TableColumnWeightModel) {
            TableColumnWeightModel columnWeightModel = (TableColumnWeightModel) columnModel;
            columnWeightModel.setColumnWeight(columnIndex, columnWeight);
            forceRefresh();
        }
    }

    /**
     * Gives the column weight (the relative width of the column) of the given column.
     *
     * @param columnIndex The index of the column the weight should be returned.
     * @return The weight of the given column index.
     * @deprecated This method has been deprecated in the version 2.4.0. Use the method
     * {@link #getColumnModel()} instead.
     */
    @Deprecated
    public int getColumnWeight(final int columnIndex) {
        if (columnModel instanceof TableColumnWeightModel) {
            TableColumnWeightModel columnWeightModel = (TableColumnWeightModel) columnModel;
            return columnWeightModel.getColumnWeight(columnIndex);
        }
        return -1;
    }

    @Override
    public void setSaveEnabled(final boolean enabled) {
        super.setSaveEnabled(enabled);
        tableHeaderView.setSaveEnabled(enabled);
        tableDataView.setSaveEnabled(enabled);
    }

    private void forceRefresh() {
        if (tableHeaderView != null) {
            tableHeaderView.invalidate();
            tableHeaderAdapter.notifyDataSetChanged();
        }
        if (tableDataView != null) {
            tableDataView.invalidate();
            tableDataAdapter.notifyDataSetChanged();
        }
    }
    @SuppressLint("CustomViewStyleable")
    private void setAttributes(final AttributeSet attributes) {
        final TypedArray styledAttributes = getContext().obtainStyledAttributes(attributes, R.styleable.TableView);
        headerElevation = styledAttributes.getInt(R.styleable.TableView_tableView_headerElevation, DEFAULT_HEADER_ELEVATION);
        final int columnCount = styledAttributes.getInt(R.styleable.TableView_tableView_columnCount, DEFAULT_COLUMN_COUNT);
        columnModel = new TableColumnWeightModel(columnCount);
        styledAttributes.recycle();
    }

    private void setupTableHeaderView(final AttributeSet attributes) {
        tableHeaderAdapter = new DefaultTableHeaderAdapter(getContext());
        final TableHeaderView tableHeaderView = new TableHeaderView(getContext());
        tableHeaderView.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.round_rect_table_header, getContext().getTheme()));
        setHeaderView(tableHeaderView);
    }

    private void setupTableDataView(final AttributeSet attributes, final int styleAttributes)
    {
        llTableDataView = (LinearLayout) View.inflate(getContext(), R.layout.table_show_more, null);
        tableDataAdapter = new DefaultTableDataAdapter(getContext());
        tableDataAdapter.setRowBackgroundProvider(dataRowBackgroundProvider);

        tableDataView = llTableDataView.findViewById(R.id.lvTableDataView);
        botTableShowMoreButton = llTableDataView.findViewById(R.id.botTableShowMoreButton);
        tableDataView.setVerticalScrollBarEnabled(false);
        tableDataView.setDivider(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.divider_new, getContext().getTheme()));
        tableDataView.setDividerHeight((int)(10 * dp1));
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        llTableDataView.setLayoutParams(layoutParams);
        tableDataView.setAdapter(tableDataAdapter);
        tableDataView.setId(R.id.table_data_view);
        addView(llTableDataView);
    }

    /**
     * The {@link TableHeaderAdapter} that is used by default. It contains the column model of the
     * table but no headers.
     *
     * @author ISchwarz
     */
    private class DefaultTableHeaderAdapter extends TableHeaderAdapter {

        public DefaultTableHeaderAdapter(final Context context) {
            super(context, columnModel);
        }

        @Override
        public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
            final TextView view = new TextView(getContext());
            view.setText(" ");
            view.setPadding(20, 40, 20, 40);
            return view;
        }
    }

    /**
     * The {@link TableDataAdapter} that is used by default. It contains the column model of the
     * table but no data.
     *
     * @author ISchwarz
     */
    private class DefaultTableDataAdapter extends TableDataAdapter<T> {

        public DefaultTableDataAdapter(final Context context) {
            super(context, columnModel, new ArrayList<T>());
        }

        @Override
        public View getCellView(final int rowIndex, final int columnIndex, final ViewGroup parentView) {
            return new TextView(getContext());
        }
    }
}
