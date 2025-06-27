package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.SourceModel;

public class AnswerSourceAdapter extends RecyclerView.Adapter<AnswerSourceAdapter.ViewHolder> {
    private final ArrayList<SourceModel> sources;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public AnswerSourceAdapter(ArrayList<SourceModel> sources) {
        this.sources = sources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_source_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = String.format(holder.itemView.getContext().getString(R.string.source_item), (position + 1) + "", sources.get(position).getTitle());
        holder.tvTitle.setText(title);
        holder.tvTitle.setOnClickListener(v -> invokeGenericWebViewInterface.invokeGenericWebView(sources.get(position).getUrl()));
    }

    @Override
    public int getItemCount() {
        return sources != null ? sources.size() : 0;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.root);
        }
    }
}
