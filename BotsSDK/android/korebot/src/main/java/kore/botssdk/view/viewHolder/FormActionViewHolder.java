package kore.botssdk.view.viewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import kore.botssdk.R;

/**
 * Created by Ramachandra Pradeep on 09-Feb-18.
 */

public class FormActionViewHolder extends RecyclerView.ViewHolder{
    TextView formActionTitle;
    LinearLayout formActionRoot;

    public FormActionViewHolder(View view) {
        super(view);
        formActionTitle = (TextView) view.findViewById(R.id.form_action_item_text);
        formActionRoot = (LinearLayout) view.findViewById(R.id.form_action_item_root);
    }

    public TextView getFormActionTitle() {
        return formActionTitle;
    }

    public LinearLayout getFormActionRoot() {
        return formActionRoot;
    }
}
