package kore.botssdk.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.BR;
import kore.botssdk.R;
import kore.botssdk.databinding.ContactCardListItemBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.utils.StringUtils;

public class ContactViewRecyclerAdapter extends RecyclerView.Adapter<ContactViewRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {
    private boolean isExpanded = false;
    private ArrayList<ContactViewListModel> dataModelList;
    private final Context context;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public ContactViewRecyclerAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        ContactCardListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.contact_card_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactViewListModel dataModel = dataModelList.get(position);
        holder.bind(dataModel);

        if(!StringUtils.isNullOrEmpty(dataModel.getImage())) {
            holder.itemRowBinding.contactListItemIV.setTypeface(ResourcesCompat.getFont(context, R.font.icomoon));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
            try {
                ((GradientDrawable) drawable).setColor(context.getResources().getColor(R.color.white));
                ((GradientDrawable) drawable).setStroke(1, context.getResources().getColor(R.color.color_e4e5eb));
            } catch (Exception e) {
            }
            holder.itemRowBinding.contactListItemIV.setBackground(drawable);
        }else{
            holder.itemRowBinding.contactListItemIV.setBackground(null);
        }

        if(dataModel.isPhone() || dataModel.isEmail() || dataModel.isAddress()) {
            holder.itemRowBinding.contactCardRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verticalListViewActionHelper.calendarContactItemClick(dataModel);
                }
            });
        }else{
            holder.itemRowBinding.contactCardRL.setOnClickListener(null);
        }
        if(position == dataModelList.size() - 1 && dataModelList.size() <=4){
            holder.itemRowBinding.contactDivider.setVisibility(View.GONE);
        }else{
            holder.itemRowBinding.contactDivider.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return dataModelList != null && dataModelList.size() > 0 ? (!isExpanded && dataModelList.size() > 4 ? 4 : dataModelList.size()) : 0;
    }

    @Override
    public ArrayList getData() {
        return null;
    }

    @Override
    public void setData(ArrayList data) {
        this.dataModelList = new ArrayList<>(data);
        if(this.dataModelList !=null && this.dataModelList.size()>4){
            if(verticalListViewActionHelper!=null)
                verticalListViewActionHelper.meetingWidgetViewMoreVisibility(true);
        }else{
            if(verticalListViewActionHelper!=null)
                verticalListViewActionHelper.meetingWidgetViewMoreVisibility(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        notifyDataSetChanged();
    }

    public boolean isExpanded(){
        return this.isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ContactCardListItemBinding itemRowBinding;

        public ViewHolder(ContactCardListItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.contactListInfo, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
