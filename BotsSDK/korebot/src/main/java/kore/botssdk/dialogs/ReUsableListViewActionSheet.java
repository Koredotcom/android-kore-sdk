package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.Utility;

/**
 * Created by Shiva Krishna on 1/22/2018.
 */

public class ReUsableListViewActionSheet extends Dialog {
    private final Context mContext;
    private final LayoutInflater layoutInflater;
    private ListAdapter adapter;
    private final float dp1;

    public ListView getOptionsListView() {
        return optionsListView;
    }

    public void setOptionsListView(ListView optionsListView) {
        this.optionsListView = optionsListView;
    }

    private ListView optionsListView;
    //private ItemSelectionCallback itemSelectionCallback;

    public ReUsableListViewActionSheet(Context context) {
        super(context);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.dp1 = Utility.convertDpToPixel(context, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View layoutView = layoutInflater.inflate(R.layout.reusable_listview_actionsheet, null);
        setContentView(layoutView);
        findViews(layoutView);
        KaFontUtils.applyCustomFont(mContext, layoutView);
    }

    public void findViews(View layoutView) {
        optionsListView = layoutView.findViewById(R.id.template_selection_tasksListView);
        TextView _view = findViewById(R.id.template_empty_text);
        _view.setVisibility(View.VISIBLE);
        optionsListView.setEmptyView(_view);
    }

    public void setAdapter(ListAdapter adapter){
        this.adapter =adapter;
        optionsListView.setAdapter(adapter);

        ViewGroup.LayoutParams params = optionsListView.getLayoutParams();
        params.height = (int)(70 * dp1) * (adapter.getCount() - 1);
        optionsListView.setLayoutParams(params);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }



}
