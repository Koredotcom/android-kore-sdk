package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewHolder.FormActionViewHolder;

public class FormActionsAdapter extends RecyclerView.Adapter<FormActionViewHolder> {
    ArrayList<FormActionTemplate> formActionTemplateArrayList;
    final Context context;
    final RecyclerView parentRecyclerView;
    ComposeFooterInterface composeFooterInterface;

    public FormActionsAdapter(@NonNull Context context, @NonNull RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
    }

    @NonNull
    @Override
    public FormActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.form_action_item_layout, null);
        KaFontUtils.applyCustomFont(context,convertView);
        return new FormActionViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull FormActionViewHolder holder, int position) {
        final FormActionTemplate formActionTemplate = formActionTemplateArrayList.get(position);

        holder.getFormActionTitle().setText(formActionTemplate.getTitle());

        holder.getFormActionRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  parentRecyclerView.getChildAdapterPosition(v);
                FormActionTemplate formTemplate = formActionTemplateArrayList.get(position);
                if(formTemplate != null){
                    composeFooterInterface.onFormActionButtonClicked(formTemplate);
                }
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (formActionTemplateArrayList == null) {
            return 0;
        } else {
            return formActionTemplateArrayList.size();
        }
    }
    public void setQuickReplyTemplateArrayList(@NonNull ArrayList<FormActionTemplate> formActionTemplateArrayList) {
        this.formActionTemplateArrayList = formActionTemplateArrayList;
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

}
