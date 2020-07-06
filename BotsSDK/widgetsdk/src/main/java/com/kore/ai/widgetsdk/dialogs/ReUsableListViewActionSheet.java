package com.kore.ai.widgetsdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.utils.KaFontUtils;

/**
 * Created by Shiva Krishna on 1/22/2018.
 */

public class ReUsableListViewActionSheet extends Dialog {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ListAdapter adapter;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View layoutView = layoutInflater.inflate(R.layout.reusable_listview_actionsheet, null);
        setContentView(layoutView);
        findViews(layoutView);
        KaFontUtils.applyCustomFont(mContext, layoutView);
    }

    public void findViews(View layoutView) {
        optionsListView = (ListView) layoutView.findViewById(R.id.template_selection_tasksListView);
        TextView _view = (TextView) findViewById(R.id.template_empty_text);
        _view.setVisibility(View.VISIBLE);
        optionsListView.setEmptyView(_view);
    }

    public void setAdapter(ListAdapter adapter){
        this.adapter =adapter;
        optionsListView.setAdapter(adapter);

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
