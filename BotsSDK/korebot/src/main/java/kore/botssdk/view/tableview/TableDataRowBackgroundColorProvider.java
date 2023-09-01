package kore.botssdk.view.tableview;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import kore.botssdk.view.tableview.colorizers.TableDataRowColorizer;
import kore.botssdk.view.tableview.providers.TableDataRowBackgroundProvider;

class TableDataRowBackgroundColorProvider<T> implements TableDataRowBackgroundProvider<T> {

    private final TableDataRowColorizer<T> colorizer;

    /**
     * Creates a new {@link TableDataRowBackgroundColorProvider} using the given {@link TableDataRowColorizer}.
     *
     * @param colorizer The {@link TableDataRowColorizer} that shall be wrapped.
     */
    public TableDataRowBackgroundColorProvider(final TableDataRowColorizer<T> colorizer) {
        this.colorizer = colorizer;
    }

    @Override
    public Drawable getRowBackground(final int rowIndex, final T rowData) {
        return new ColorDrawable(colorizer.getRowColor(rowIndex, rowData));
    }

}
