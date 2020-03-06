package kore.botssdk.view.tableview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kore.botssdk.view.tableview.TableDataAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;


public class MiniBotTableAdapter extends TableDataAdapter<MiniTableModel> {

    private static final int TEXT_SIZE = 14;


    public MiniBotTableAdapter(final Context context, final List<MiniTableModel> data) {
        super(context, data);
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
        renderedView = renderString(str);

        return renderedView;
    }

    private int getGravity(){
            return Gravity.LEFT;
    }



    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(getGravity());
        return textView;
    }




}
