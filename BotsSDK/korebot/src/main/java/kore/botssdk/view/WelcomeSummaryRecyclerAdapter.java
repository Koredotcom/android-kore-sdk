package kore.botssdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.BR;
import kore.botssdk.R;
import kore.botssdk.databinding.WelcomeSummaryListItemBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.WelcomeChatSummaryModel;
import kore.botssdk.utils.StringUtils;

public class WelcomeSummaryRecyclerAdapter extends RecyclerView.Adapter<WelcomeSummaryRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<WelcomeChatSummaryModel> summaryList;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public WelcomeSummaryRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WelcomeSummaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WelcomeSummaryListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.welcome_summary_list_item, parent, false);
        return new WelcomeSummaryRecyclerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeSummaryRecyclerAdapter.ViewHolder holder, int position) {
        WelcomeChatSummaryModel model = summaryList.get(position);
        holder.bind(model);

        holder.itemRowBinding.icon.setTypeface(getTypeFaceObj(context));
        if(!StringUtils.isNullOrEmpty(model.getIconId()))
            setImage(model, holder);


        holder.itemRowBinding.summaryRootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalListViewActionHelper.welcomeSummaryItemClick(model);
            }
        });
    }

    private Drawable changeColorOfDrawable(Context context, int colorCode) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
        try {
            ((GradientDrawable) drawable).setColor(context.getResources().getColor(colorCode));
            return drawable;
        } catch (Exception e) {
            return drawable;
        }

    }

    private Typeface getTypeFaceObj(Context context) {
        return ResourcesCompat.getFont(context, R.font.icomoon);
    }

    private void setImage(WelcomeChatSummaryModel mdl, ViewHolder holder){
        switch(mdl.getIconId()){
            case "meeting":
                holder.itemRowBinding.icon.setText(R.string.icon_e979);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_4e74f0));
                break;
            case "form":
                holder.itemRowBinding.icon.setText(R.string.icon_e943);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_ffab18));
                break;
            case "overdue":
                holder.itemRowBinding.icon.setText(R.string.icon_e926);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_ff5b6a));
                break;
            case "email":
                holder.itemRowBinding.icon.setText(R.string.icon_e915);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_2ad082));
                break;
            case "upcoming_tasks":
                holder.itemRowBinding.icon.setText(R.string.icon_e96c);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_ff5b6a));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return summaryList != null && summaryList.size() > 0 ? summaryList.size() : 0;
    }

    @Override
    public ArrayList getData() {
        return null;
    }

    @Override
    public void setData(ArrayList data) {
        this.summaryList= new ArrayList<WelcomeChatSummaryModel>(data);
        notifyDataSetChanged();
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
            this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public WelcomeSummaryListItemBinding itemRowBinding;

        public ViewHolder(WelcomeSummaryListItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.welcomeSummaryList, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
