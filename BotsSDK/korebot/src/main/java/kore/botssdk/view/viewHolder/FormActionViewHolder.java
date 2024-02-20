package kore.botssdk.view.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;

/**
 * Created by Ramachandra Pradeep on 09-Feb-18.
 */

public class FormActionViewHolder extends RecyclerView.ViewHolder{
    final TextView formActionTitle;
    final LinearLayout formActionRoot;

    public FormActionViewHolder(View view) {
        super(view);
        formActionTitle = view.findViewById(R.id.form_action_item_text);
        formActionRoot = view.findViewById(R.id.form_action_item_root);
    }

    public TextView getFormActionTitle() {
        return formActionTitle;
    }

    public LinearLayout getFormActionRoot() {
        return formActionRoot;
    }
}
