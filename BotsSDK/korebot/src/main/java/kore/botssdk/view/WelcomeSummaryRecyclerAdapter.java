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
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;

public class WelcomeSummaryRecyclerAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private final Context context;
    private ArrayList<WelcomeChatSummaryModel> summaryList;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isEnabled;
    String msg;
    Drawable errorIcon;
    public WelcomeSummaryRecyclerAdapter(Context context) {
        this.context = context;
    }
    private final int DATA_CARD_FLAG = 1;
    private final int MESSAGE = 2;
    private final int EMPTY_CARD_FLAG = 0;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == DATA_CARD_FLAG) {
            WelcomeSummaryListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.welcome_summary_list_item, parent, false);
            return new ViewHolder(binding);
        } else {
            return new EmptyWidgetViewHolder(LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false));


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holdermain, int position) {
                if(holdermain instanceof ViewHolder )
                {
                    ViewHolder  holder=(ViewHolder)holdermain;
                    WelcomeChatSummaryModel model = summaryList.get(position);
                    holder.bind(model);

                    holder.itemRowBinding.icon.setTypeface(getTypeFaceObj(context));
                    if(!StringUtils.isNullOrEmpty(model.getIconId()))
                        setImage(model, holder);
//                    setIntrensic(model,holder);


                    holder.itemRowBinding.summaryRootLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(verticalListViewActionHelper != null && isEnabled())
                                verticalListViewActionHelper.welcomeSummaryItemClick(model);
                        }
                    });
                }else
                {
                    EmptyWidgetViewHolder holder = (EmptyWidgetViewHolder) holdermain;
                    holder.tv_disrcription.setText(holder.getItemViewType() == EMPTY_CARD_FLAG ? "" : msg);
                    holder.img_icon.setImageDrawable(holder.getItemViewType() == EMPTY_CARD_FLAG ? null : errorIcon);

                }
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
                holder.itemRowBinding.icon.setText(R.string.icon_2d);//
                holder.itemRowBinding.icon.setBackground(changeColorOfDrawable(context, R.color.color_4e74f0));
                break;
            case "notificationForm":
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
        return summaryList != null && summaryList.size() > 0 ? summaryList.size() :1;
    }
    @Override
    public int getItemViewType(int position) {

        if (summaryList != null && summaryList.size() > 0)
            return DATA_CARD_FLAG;


        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return EMPTY_CARD_FLAG;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg=msg;
        this.errorIcon=errorIcon;
        notifyDataSetChanged();
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final WelcomeSummaryListItemBinding itemRowBinding;

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
