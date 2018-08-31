package kore.botssdk.view.tableview;

import android.content.Context;
import android.util.AttributeSet;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.tableview.adapters.BotTableAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;
import kore.botssdk.view.tableview.model.TableColumnWeightModel;
import kore.botssdk.view.tableview.toolkit.SimpleTableHeaderAdapter;
import kore.botssdk.view.tableview.toolkit.TableDataRowBackgroundProviders;

/**
 * Extension of the {@link TableView} that gives the possibility to sort the table by every single
 * column. For this purpose implementations of {@link Comparator} are used. If there is a comparator
 * set for a column the {@link BotTableView} will automatically display an ImageView at the start
 * of the header indicating to the user, that this column is sortable.
 * If the user clicks this header the given comparator will used to sort the table ascending by the
 * content of this column. If the user clicks this header again, the table is sorted descending
 * by the content of this column.
 *
 * @author ISchwarz
 */
public class BotTableView extends TableView<MiniTableModel> {

    private static final String LOG_TAG = BotTableView.class.getName();

    private Context context;
    public BotTableView(final Context context) {
        this(context, null);
        this.context = context;
//        setBackgroundColor(0xffff0000);

    }

    public BotTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
//        setBackgroundColor(0xffff0000);

    }

    public BotTableView(final Context context, final AttributeSet attributes, final int styleAttributes){
        super(context,attributes,styleAttributes);
//        setBackgroundColor(0xffff0000);

    }
    public String[] addHeaderAdapter( List<List<String>> primary){
        String[]  headers = new String[primary.size()];
        String[] alignment = new String[primary.size()];
        String defaultAlign = "left";
        for(int i=0; i<primary.size();i++){
            headers[i] = new String(primary.get(i).get(0));
            if(primary.get(i).size() > 1)
                alignment[i] = new String(primary.get(i).get(1));
            else
                alignment[i] = new String(defaultAlign);
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

    public void addDataAdapter(String template_type, List<List<String>> additional, String[] alignment){
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

    }

    public void addDataAdapterForTable(PayloadInner data, String[] alignment) {

        List<MiniTableModel> lists = new ArrayList<>();
        int size = ((ArrayList) data.getElements()).size();
        for (int j = 0; j < size; j++) {
            MiniTableModel model = new MiniTableModel();
            model.setElements(((ArrayList)(((LinkedTreeMap)((ArrayList) data.getElements()).get(j))).get("Values")));
            lists.add(model);
        }
        BotTableAdapter tableAdapter = new BotTableAdapter(context, lists,alignment);
        setDataAdapter(tableAdapter);

    }
}
