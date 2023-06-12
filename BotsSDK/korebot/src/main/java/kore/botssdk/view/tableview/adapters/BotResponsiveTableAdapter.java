package kore.botssdk.view.tableview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.tableview.TableResponsiveDataAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;

public class BotResponsiveTableAdapter extends TableResponsiveDataAdapter<MiniTableModel> {

    private static final int TEXT_SIZE = 14;
    private final String[] alignment;
    private final String[]  headers;
    int dp1;
    private final PayloadInner payloadInner;


    public BotResponsiveTableAdapter(final Context context, final List<MiniTableModel> data, String[] alignment, String[]  headers,  PayloadInner payloadInner) {
        super(context, data);
        this.alignment = alignment;
        this.headers = headers;
        this.payloadInner = payloadInner;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        String  str;
        if(getRowData(rowIndex).getElements().get(columnIndex) instanceof Double){
            str = Double.toString((Double) getRowData(rowIndex).getElements().get(columnIndex));
        }else if(getRowData(rowIndex).getElements().get(columnIndex) instanceof String){
            str = (String) getRowData(rowIndex).getElements().get(columnIndex);
        }else{
            str = "";
        }

        View renderedView = null;
        renderedView = renderString(columnIndex,str, headers[columnIndex]);

        return renderedView;
    }

    @Override
    public View getGroupView(int rowIndex, int columnIndex, ViewGroup parentView)
    {
        String  str;
        if(getRowData(rowIndex).getElements().get(columnIndex) instanceof Double){
            str = Double.toString((Double) getRowData(rowIndex).getElements().get(columnIndex));
        }else if(getRowData(rowIndex).getElements().get(columnIndex) instanceof String){
            str = (String) getRowData(rowIndex).getElements().get(columnIndex);
        }else{
            str = "";
        }

        View renderedView = null;
        renderedView = renderGroupString(columnIndex,str);

        return renderedView;
    }

    private int getGravity(int columnIndex){
        if(alignment[columnIndex].equals("left") || alignment[columnIndex].equals("default"))
            return Gravity.LEFT;
        else if(alignment[columnIndex].equals("right"))
            return Gravity.RIGHT;
        else return Gravity.CENTER;
    }

    private View renderString(int columnIndex, final String value, String header) {
        LinearLayout renderView = new LinearLayout(getContext());
        renderView.setOrientation(LinearLayout.VERTICAL);

        final TextView headerTextView = new TextView(getContext());
        headerTextView.setText(header);
//        headerTextView.setPadding(20, 10, 20, 10);
        headerTextView.setTextSize(TEXT_SIZE);
        headerTextView.setTextColor(Color.BLACK);
        headerTextView.setGravity(getGravity(columnIndex));
        renderView.addView(headerTextView);

        final TextView textView = new TextView(getContext());
        textView.setText(value);
//        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(getGravity(columnIndex));
        renderView.addView(textView);
        return renderView;
    }

    private View renderGroupString(int columnIndex, final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
//        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(getGravity(columnIndex));
        return textView;
    }
}
