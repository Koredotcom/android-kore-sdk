package com.kore.ai.widgetsdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.SkillsAdapter;
import com.kore.ai.widgetsdk.models.PayloadInner;
import com.kore.ai.widgetsdk.utils.BundleConstants;

import java.util.ArrayList;

public class SkillSwitchDialog extends Dialog {

    private final LayoutInflater layoutInflater;
    private Context _context;
    private Bundle extras;
    private String userId, accessToken;
    ArrayList<PayloadInner.Skill> data = null;

    public SkillSwitchDialog(@NonNull Context context, Bundle extra, ArrayList<PayloadInner.Skill> _ssm) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        _context = context;
        this.extras = extra;
        userId = extras.getString(BundleConstants.USER_ID);
        accessToken = extras.getString(BundleConstants.ACCESS_TKN);
        data = _ssm;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View layoutView = layoutInflater.inflate(R.layout.skill_switch_list, null);
        setContentView(layoutView);
        findViews(layoutView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void dismissDialog(){
        dismiss();
    }
    private void findViews(View layoutView) {
        RecyclerView recyclerView = layoutView.findViewById(R.id.skillListView);
        SkillsAdapter adapter = new SkillsAdapter(data,_context, SkillSwitchDialog.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(_context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
