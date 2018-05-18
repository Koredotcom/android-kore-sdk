package kore.botssdk.view.tableview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kore.botssdk.view.tableview.TableDataAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;


public class BotTableAdapter extends TableDataAdapter<MiniTableModel> {

    private static final int TEXT_SIZE = 14;
    private String[] alignment;


    public BotTableAdapter(final Context context, final List<MiniTableModel> data, String[] alignment) {
        super(context, data);
        this.alignment = alignment;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final String  str = getRowData(rowIndex).getElements().get(columnIndex);
        View renderedView = null;
//        Log.d("IKIDO","The String is "+str);
        renderedView = renderString(columnIndex,str);

        return renderedView;
    }

    private int getGravity(int columnIndex){
        if(alignment[columnIndex].equals("left") || alignment[columnIndex].equals("default"))
            return Gravity.LEFT;
        else if(alignment[columnIndex].equals("right"))
            return Gravity.RIGHT;
        else return Gravity.CENTER;
    }



    private View renderString(int columnIndex, final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(getGravity(columnIndex));
        return textView;
    }




}
