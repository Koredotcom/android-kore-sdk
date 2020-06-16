package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.AnnouncementCardLayoutBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.AnnoucementResModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.CircularProfileView;
import kore.botssdk.view.viewUtils.CircleTransform;

import static kore.botssdk.net.RestAPI.URL_VERSION;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class AnnouncementAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private static final int DATA_FOUND = 1;
    private static final int NO_DATA = 0;
    private static final int MESSAGE = 2;
    Context context;
    String msg;
    boolean isFromWidget;
    Drawable errorIcon;
//    public void setData(ArrayList<AnnoucementResModel> data) {
//        this.data = data;
//    }

    ArrayList<AnnoucementResModel> data;
    boolean isViewMore;
    private VerticalListViewActionHelper verticalListViewActionHelper;

    public AnnouncementAdapter(Context context) {
        this.context = context;
    }


    public AnnouncementAdapter(Context activity, ArrayList<AnnoucementResModel> data) {

        context = activity;
        this.data = data;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA_FOUND)
            return new AnnouncementAdapter.AnnouncementViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.announcement_card_layout, parent, false));
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new AnnouncementAdapter.EmptyAnnocementViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && data.size() > 0) {
            return DATA_FOUND;
        }
        if (msg != null && !msg.equalsIgnoreCase("")) {
            return MESSAGE;
        }
        return NO_DATA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AnnouncementViewHolder) {
            AnnouncementViewHolder holder = (AnnouncementViewHolder) viewHolder;
            AnnoucementResModel annoucementResModel = data.get(position);
            holder.binding.setAnnoucement(annoucementResModel);

            holder.binding.tvTime.setText(DateUtils.getFormattedSendDateInTimeFormatCoreFunctionality2(context, annoucementResModel.getLastMod()));
            holder.binding.userProfileName.setCircle(true);

            if (!TextUtils.isEmpty(annoucementResModel.getOwner().getIcon()) && annoucementResModel.getOwner().getIcon().equalsIgnoreCase("profile.png"))
            {
                holder.binding.imgProfile.setVisibility(View.VISIBLE);
                holder.binding.userProfileName.setVisibility(View.GONE);
                String url = SDKConfiguration.Server.SERVER_URL + "/api"+URL_VERSION+"/getMediaStream/profilePictures/" + annoucementResModel.getOwner().getId() + "/profile.jpg";
                Picasso.get().load(url)
                        .placeholder(getProfileDrawable(context, annoucementResModel.getOwner().getFullName(), annoucementResModel.getOwner().getColor(),35))
                        .transform(new CircleTransform())
                        .noFade()
                        .resize((int)dp1*35, (int)dp1*35)
                        .into(holder.binding.imgProfile);

            }else if (annoucementResModel.getOwner() != null && annoucementResModel.getOwner().getFullName() != null) {
                // holder.binding.userProfileName.setText(StringUtils.getInitials(annoucementResModel.getOwner().getFullName()));
                holder.binding.userProfileName.setText(StringUtils.getInitials(annoucementResModel.getOwner().getFullName()));
                holder.binding.imgProfile.setVisibility(View.GONE);
                holder.binding.userProfileName.setVisibility(View.VISIBLE);
                try {
                    holder.binding.userProfileName.setColor(Color.parseColor(annoucementResModel.getOwner().getColor()));
                } catch (Exception e) {
                    holder.binding.userProfileName.setColor(context.getResources().getColor(R.color.splash_background_color));
                }

            }

            if (position == data.size() - 1 && data.size() < 3)
                  holder.binding.divider.setVisibility(View.GONE);

              holder.binding.viewAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    extras.putString(BundleConstants.KNOWLEDGE_ID, annoucementResModel.getId());
                    verticalListViewActionHelper.knowledgeItemClicked(extras, false);
                }
            });

        } else {
//            holder = (EmptyAnnocementViewHolder) holder;
            ((EmptyAnnocementViewHolder) viewHolder).tv_message.setText(viewHolder.getItemViewType() == NO_DATA ? "No Announcements" : msg);
            ((EmptyAnnocementViewHolder) viewHolder).img_icon.setImageDrawable(viewHolder.getItemViewType() == NO_DATA ? ContextCompat.getDrawable(context, R.drawable.no_meeting) : errorIcon);


        }


    }

    private  Drawable getProfileDrawable(Context mContext,String fullName,String profileColor,int size) {

        String DEFAULT_GREEN = "#3942f6";
        int dp35 = (int)dp1*size;

        if (profileColor == null || !profileColor.contains("#")) {
            profileColor = DEFAULT_GREEN;
        }

        CircularProfileView circularProfileViewForActionBar = new CircularProfileView(mContext);
        circularProfileViewForActionBar.setDimens(dp35, dp35);
        circularProfileViewForActionBar.setDrawingCacheEnabled(true);
        circularProfileViewForActionBar.setClickable(true);

//        circularProfileViewForActionBar.setOnClickListener(onCircularProfileViewForActionBarClickListner);

        circularProfileViewForActionBar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        circularProfileViewForActionBar.populateLayout(StringUtils.getInitials(fullName),
                null, null, null, -1, Color.parseColor(profileColor), true, dp35, dp35);//34 * dp1, 34 * dp1);

        Bitmap cb = loadBitmapFromView(circularProfileViewForActionBar, dp35, dp35);
        return new BitmapDrawable(mContext.getResources(), cb);
    }

    private  Bitmap loadBitmapFromView(View v, int height, int width) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        v.draw(c);
        return b;
    }

    public void addItem(ArrayList<AnnoucementResModel> model) {
        if (model != null) {
            this.data.addAll(0, model);
            notifyItemInserted(0);
        }
    }

    @Override
    public long getItemId(int position) {
        if (data != null && data.size() > 0) {
            AnnoucementResModel model = data.get(position);
            if (model != null && model.getSharedOn() != null)
                return model.getSharedOn() + position;
            else return position;
        } else return position;
    }
    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
    @Override
    public int getItemCount() {
        if (isFromWidget&&widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW) {
            return data != null && data.size() > 0 ? data.size() : 1;
        }
        return data != null && data.size() > 0 ? (!isViewMore && data.size() > 3 ? 3 : data.size()) : 1;

    }

    public void setFullView(boolean isViewMore) {
        this.isViewMore = isViewMore;
    }

    @Override
    public ArrayList getData() {
        return data;
    }

    @Override
    public void setData(ArrayList data) {
        this.data = data;
    }

    public void addAllItems(ArrayList _data) {
        data.addAll(_data);
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }

    public void setFromWidget(boolean isFromWidget) {
        this.isFromWidget=isFromWidget;
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        AnnouncementCardLayoutBinding binding;

        public AnnouncementViewHolder(@NonNull AnnouncementCardLayoutBinding binding) {
            super((View) binding.getRoot());
            this.binding = binding;

        }
    }

    class EmptyAnnocementViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;
        RelativeLayout view_action;
        ImageView img_icon;

        public EmptyAnnocementViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            view_action = itemView.findViewById(R.id.view_action);
            img_icon = itemView.findViewById(R.id.img_icon);
        }
    }
}