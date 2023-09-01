package kore.botssdk.view.tableview;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.tableview.colorizers.TableDataRowColorizer;
import kore.botssdk.view.tableview.listeners.OnScrollListener;
import kore.botssdk.view.tableview.listeners.SwipeToRefreshListener;
import kore.botssdk.view.tableview.listeners.TableDataClickListener;
import kore.botssdk.view.tableview.listeners.TableDataLongClickListener;
import kore.botssdk.view.tableview.listeners.TableHeaderClickListener;
import kore.botssdk.view.tableview.model.TableColumnModel;
import kore.botssdk.view.tableview.model.TableColumnWeightModel;
import kore.botssdk.view.tableview.providers.TableDataRowBackgroundProvider;
import kore.botssdk.view.tableview.toolkit.TableDataRowBackgroundProviders;

public class TableExpandView<T> extends LinearLayout {
    private final int DEFAULT_COLUMN_COUNT = 4;
    private final int DEFAULT_HEADER_ELEVATION = 1;
    private final LayoutTransition layoutTransition;
    private TableDataRowBackgroundProvider<? super T> dataRowBackgroundProvider =
            TableDataRowBackgroundProviders.similarRowColor(0x00000000);
    private TableColumnModel columnModel;
    protected TableHeaderView tableHeaderView;
    protected ExpandableListView tableDataView;
    private TableRespExpandDataAdapter<T> tableDataAdapter;
    private TableHeaderAdapter tableHeaderAdapter;
    private int lastPosition = -1;
    private int headerElevation;
    private final String propertyName = "y";

    /**
     * Creates a new TableView with the given context.\n
     * (Has same effect like calling {@code new TableView(context, null, android.R.attr.listViewStyle})
     *
     * @param context The context that shall be used.
     */
    public TableExpandView(final Context context) {
        this(context, null);
    }

    /**
     * Creates a new TableView with the given context.\n
     * (Has same effect like calling {@code new TableView(context, attrs, android.R.attr.listViewStyle})
     *
     * @param context    The context that shall be used.
     * @param attributes The attributes that shall be set to the view.
     */
    public TableExpandView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    /**
     * Creates a new TableView with the given context.
     *
     * @param context         The context that shall be used.
     * @param attributes      The attributes that shall be set to the view.
     * @param styleAttributes The style attributes that shall be set to the view.
     */
    public TableExpandView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);
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
            addView(tableHeaderView, 0);
        } else if (!visible && isHeaderVisible()) {
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

    /**
     * Gives the view that is shown if no data is available.
     *
     * @return The view that is shown if no data is available.
     */
    public View getEmptyDataIndicatorView() {
        return tableDataView.getEmptyView();
    }

    /**
     * Gives information whether the swipe to refresh feature shall be enabled or not.
     *
     * @return Boolean indication whether the swipe to refresh feature shall be enabled or not.
     */
   /* public boolean isSwipeToRefreshEnabled() {
        return swipeRefreshLayout.isEnabled();
    }*/

    /**
     * Toggles the swipe to refresh feature to the user.
     *
     * @param enabled Whether the swipe to refresh feature shall be enabled or not.
     */
   /* public void setSwipeToRefreshEnabled(final boolean enabled) {
        swipeRefreshLayout.setEnabled(enabled);
    }*/

    /**
     * Sets the {@link SwipeToRefreshListener} for this table view. If there is already a {@link SwipeToRefreshListener}
     * set it will be replaced.
     *
     * @param listener The {@link SwipeToRefreshListener} that is called when the user triggers the refresh action.
     */
    /*public void setSwipeToRefreshListener(final SwipeToRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh(new SwipeToRefreshListener.RefreshIndicator() {
                    @Override
                    public void hide() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void show() {
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public boolean isVisible() {
                        return swipeRefreshLayout.isRefreshing();
                    }
                });
            }
        });
    }*/

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
//        tableDataAdapter.setRowBackgroundProvider(dataRowBackgroundProvider);
    }

    /**
     * Adds a {@link TableDataClickListener} to this table. This listener gets notified every time the user clicks
     * a certain data item.
     *
     * @param listener The listener that should be added as click listener.
     */
    /*public void addDataClickListener(final TableDataClickListener<T> listener) {
        dataClickListeners.add(listener);
    }*/

    /**
     * Adds a {@link TableDataLongClickListener} to this table. This listener gets notified every time the user clicks
     * long on a certain data item.
     *
     * @param listener The listener that should be added as long click listener.
     */
    /*public void addDataLongClickListener(final TableDataLongClickListener<T> listener) {
        dataLongClickListeners.add(listener);
    }*/

    /**
     * Adds a {@link OnScrollListener} to this table view.
     *
     * @param onScrollListener The {@link OnScrollListener} that shall be added.
     */
   /* public void addOnScrollListener(final OnScrollListener onScrollListener) {
        onScrollListeners.add(onScrollListener);
    }
*/
    /**
     * Removes a {@link OnScrollListener} from this table view.
     *
     * @param onScrollListener The {@link OnScrollListener} that shall be removed.
     */
   /* public void removeOnScrollListener(final OnScrollListener onScrollListener) {
        onScrollListeners.remove(onScrollListener);
    }*/

    /**
     * Removes the given {@link TableDataClickListener} from the click listeners of this table.
     *
     * @param listener The listener that should be removed.
     * @deprecated This method has been deprecated in the version 2.2.0 for naming alignment reasons. Use the method
     * {@link TableView#removeDataClickListener(TableDataClickListener)} instead.
     */
    /*@Deprecated
    public void removeTableDataClickListener(final TableDataClickListener<T> listener) {
        dataClickListeners.remove(listener);
    }*/

    /**
     * Removes the given {@link TableDataClickListener} from the click listeners of this table.
     *
     * @param listener The listener that should be removed.
     */
    /*public void removeDataClickListener(final TableDataClickListener<T> listener) {
        dataClickListeners.remove(listener);
    }*/

    /**
     * Removes the given {@link TableDataLongClickListener} from the long click listeners of this table.
     *
     * @param listener The listener that should be removed.
     */
    /*public void removeDataLongClickListener(final TableDataLongClickListener<T> listener) {
        dataLongClickListeners.remove(listener);
    }*/

    /**
     * Adds the given {@link TableHeaderClickListener} to this table.
     *
     * @param listener The listener that shall be added to this table.
     */
    public void addHeaderClickListener(final TableHeaderClickListener listener) {
        tableHeaderView.addHeaderClickListener(listener);
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
     * Removes the given {@link TableHeaderClickListener} from this table.
     *
     * @param listener The listener that shall be removed from this table.
     */
    public void removeHeaderClickListener(final TableHeaderClickListener listener) {
        tableHeaderView.removeHeaderClickListener(listener);
    }

    /**
     * Gives the {@link TableHeaderAdapter} that is used to render the header views for each column.
     *
     * @return The {@link TableHeaderAdapter} that is currently set.
     */
    public TableHeaderAdapter getHeaderAdapter() {
        return tableHeaderAdapter;
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
    public TableRespExpandDataAdapter<T> getDataAdapter() {
        return tableDataAdapter;
    }

    /**
     * Sets the {@link TableDataAdapter} that is used to render the data view for each cell.
     *
     * @param dataAdapter The {@link TableDataAdapter} that should be set.
     */
    public void setDataAdapter(final TableRespExpandDataAdapter<T> dataAdapter) {
        if(dataAdapter != null) {
            tableDataAdapter = dataAdapter;
            tableDataAdapter.setColumnModel(columnModel);
            tableDataView.setChildDivider(ResourcesCompat.getDrawable(getContext().getResources(), R.color.transparent_card, getContext().getTheme()));
            tableDataView.setDivider(ResourcesCompat.getDrawable(getContext().getResources(), R.color.transparent_card, getContext().getTheme()));
            tableDataView.setDividerHeight(2);
            tableDataView.setAdapter(tableDataAdapter);

            tableDataView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastPosition != -1
                            && groupPosition != lastPosition) {
                        tableDataView.collapseGroup(lastPosition);
                    }
                    lastPosition = groupPosition;
                }
            });

            forceRefresh();
        }
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
//        if (isInEditMode()) {
//            tableHeaderAdapter = new EditModeTableHeaderAdapter(getContext());
//        } else {
        tableHeaderAdapter = new DefaultTableHeaderAdapter(getContext());
//        }

        final TableHeaderView tableHeaderView = new TableHeaderView(getContext());
        tableHeaderView.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.round_rect_table_header, getContext().getTheme()));
        setHeaderView(tableHeaderView);
    }

    private void setupTableDataView(final AttributeSet attributes, final int styleAttributes) {
        final LayoutParams dataViewLayoutParams = new LayoutParams(getWidthAttribute(attributes), LayoutParams.WRAP_CONTENT);

//        if (isInEditMode()) {
//            tableDataAdapter = new EditModeTableDataAdapter(getContext());
//        } else {
        tableDataAdapter = new DefaultTableDataAdapter(getContext());
//        }
//        tableDataAdapter.setRowBackgroundProvider(dataRowBackgroundProvider);

        tableDataView = new ExpandableListView(getContext(), attributes, styleAttributes);
        tableDataView.setVerticalScrollBarEnabled(false);
//        tableDataView.setOnItemClickListener(new InternalDataClickListener());
//        tableDataView.setOnItemLongClickListener(new InternalDataLongClickListener());
        tableDataView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tableDataView.setAdapter(tableDataAdapter);
        tableDataView.setId(R.id.table_data_view);
        forceRefresh();
        addView(tableDataView);
//        tableDataView.setOnScrollListener(new InternalOnScrollListener());

//        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
//        swipeRefreshLayout.setLayoutParams(dataViewLayoutParams);
//        swipeRefreshLayout.addView(tableDataView);
//        swipeRefreshLayout.setColorSchemeColors(headerColor);
//        swipeRefreshLayout.setEnabled(false);
//
//        addView(swipeRefreshLayout);

    }

    private int getWidthAttribute(final AttributeSet attributes) {
        final TypedArray ta = getContext().obtainStyledAttributes(attributes, new int[]{android.R.attr.layout_width});
        final int layoutWidth = ta.getLayoutDimension(0, ViewGroup.LayoutParams.MATCH_PARENT);
        ta.recycle();
        return layoutWidth;
    }

    /**
     * Internal management of clicks on the data view.
     *
     * @author ISchwarz
     */
    /*private class InternalDataClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
            informAllListeners(i);
        }

        private void informAllListeners(final int rowIndex) {
            final T clickedObject = tableDataAdapter.getItem(rowIndex);

            for (final TableDataClickListener<T> listener : dataClickListeners) {
                try {
                    listener.onDataClicked(rowIndex, clickedObject);
                } catch (final Throwable t) {
                    Log.w(LOG_TAG, "Caught Throwable on listener notification: " + t.toString());
                    // continue calling listeners
                }
            }
        }
    }*/

    /**
     * Internal long click management of clicks on the data view.
     *
     * @author ISchwarz
     */
    /*private class InternalDataLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int rowIndex, final long id) {
            return informAllListeners(rowIndex);
        }

        private boolean informAllListeners(final int rowIndex) {
            final T clickedObject = tableDataAdapter.getItem(rowIndex);
            boolean isConsumed = false;

            for (final TableDataLongClickListener<T> listener : dataLongClickListeners) {
                try {
                    isConsumed |= listener.onDataLongClicked(rowIndex, clickedObject);
                } catch (final Throwable t) {
                    Log.w(LOG_TAG, "Caught Throwable on listener notification: " + t.toString());
                    // continue calling listeners
                }
            }
            return isConsumed;
        }
    }*/

    /**
     * Internal on {@link AbsListView.OnScrollListener} that dispatches the callbacks to the registered
     * {@link OnScrollListener}s.
     *
     * @author ISchwarz
     */
    /*private class InternalOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(final AbsListView absListView, final int scrollStateValue) {
            final OnScrollListener.ScrollState scrollState = OnScrollListener.ScrollState.fromValue(scrollStateValue);

            for (final OnScrollListener onScrollListener : onScrollListeners) {
                onScrollListener.onScrollStateChanged(tableDataView, scrollState);
            }
        }

        @Override
        public void onScroll(final AbsListView absListView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

            for (final OnScrollListener onScrollListener : onScrollListeners) {
                onScrollListener.onScroll(tableDataView, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }*/

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
    private class DefaultTableDataAdapter extends TableRespExpandDataAdapter<T> {

        public DefaultTableDataAdapter(final Context context) {
            super(context, columnModel, new ArrayList<T>(), new PayloadInner());
        }

        @Override
        public View getGroupedView(int rowIndex, int columnIndex, ViewGroup parentView) {
            return new TextView(getContext());
        }

        @Override
        public View getCellView(final int rowIndex, final int columnIndex, final ViewGroup parentView, boolean showDivider) {
            return new TextView(getContext());
        }
    }

    /**
     * The {@link TableHeaderAdapter} that is used while the view is in edit mode.
     *
     * @author ISchwarz
     */
    /*private class EditModeTableHeaderAdapter extends TableHeaderAdapter {

        private final float TEXT_SIZE = 18;

        public EditModeTableHeaderAdapter(final Context context) {
            super(context, columnModel);
        }

        @Override
        public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
            final TextView textView = new TextView(getContext());
            textView.setText(getResources().getString(R.string.default_header, columnIndex));
            textView.setPadding(20, 40, 20, 40);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TEXT_SIZE);
            return textView;
        }

    }*/

    /**
     * The {@link TableDataAdapter} that is used while the view is in edit mode.
     *
     * @author ISchwarz
     */
    private class EditModeTableDataAdapter extends TableDataAdapter<T> {

        private final float TEXT_SIZE = 16;

        public EditModeTableDataAdapter(final Context context) {
            super(context, columnModel, new ArrayList<T>());
        }

        @Override
        public View getCellView(final int rowIndex, final int columnIndex, final ViewGroup parent) {
            final TextView textView = new TextView(getContext());
            textView.setText(getResources().getString(R.string.default_cell, columnIndex, rowIndex));
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(TEXT_SIZE);
            return textView;
        }

        @Override
        public int getCount() {
            return 50;
        }
    }

}

