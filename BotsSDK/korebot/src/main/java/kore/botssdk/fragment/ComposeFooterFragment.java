package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import kore.botssdk.R;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class ComposeFooterFragment extends BaseSpiceFragment {

    EditText composeFooterEditTxt;
    Button composeFooterSendBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.compose_footer_layout, null);

        findViews(view);

        return view;
    }

    private void findViews(View view) {
        composeFooterEditTxt = (EditText) view.findViewById(R.id.composeFooterEditTxt);
        composeFooterSendBtn = (Button) view.findViewById(R.id.composeFooterSendBtn);
    }

}
