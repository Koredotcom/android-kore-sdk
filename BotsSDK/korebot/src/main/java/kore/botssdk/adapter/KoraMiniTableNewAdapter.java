package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.WactionItemLayoutBinding;
import kore.botssdk.models.BotMiniTableModel;
import kore.botssdk.models.MultiAction;
import kore.botssdk.view.tableview.BotMiniTableView;

/**
 * Created by Sathish Kumar Challa on 13-03-2020.
 **/


public class KoraMiniTableNewAdapter extends RecyclerView.Adapter<KoraMiniTableNewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BotMiniTableModel> data;

    public KoraMiniTableNewAdapter(ArrayList<BotMiniTableModel> data, Context mContext) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public KoraMiniTableNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new BotMiniTableView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull KoraMiniTableNewAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private BotMiniTableView view;
        public ViewHolder(BotMiniTableView view) {
            super(view);
            this.view=view;
         }

        public void bindData(BotMiniTableModel botMiniTableModel) {
            view.setData(botMiniTableModel);
        }
    }


}
