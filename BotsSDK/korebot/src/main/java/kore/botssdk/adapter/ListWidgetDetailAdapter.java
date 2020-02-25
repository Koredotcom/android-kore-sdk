package kore.botssdk.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.ContentModel;
import kore.botssdk.models.LoginModel;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.Widget;
import kore.botssdk.models.WidgetListElementModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.Constants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class ListWidgetDetailAdapter extends RecyclerView.Adapter<ListWidgetDetailAdapter.DetailsViewHolder> {

    Context context;
    ArrayList<ContentModel> details;

    public ListWidgetDetailAdapter(Context context, ArrayList<ContentModel> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_widget_detail_item, parent, false);
        return new DetailsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {

        holder.tv_detail.setText(details.get(position).getDescription());

        if (details.get(position).getImage() != null) {

            try {
                holder.image_view.setVisibility(VISIBLE);
                String imageData;
                imageData = details.get(position).getImage().getImage_src();
                if (imageData.contains(",")) {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.image_view.setImageBitmap(decodedByte);
                } else {
                    Picasso.get().load(imageData).into(holder.image_view);
                }
            } catch (Exception e) {
                holder.image_view.setVisibility(GONE);

            }
        } else {
            holder.image_view.setVisibility(GONE);
        }


    }

    @Override
    public int getItemCount() {
        return details != null && details.size() > 0 ? (!showMore && details.size() > 3 ? 3 : details.size()) : 0;
    }

    boolean showMore;

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
        notifyDataSetChanged();

    }

    public boolean getShowMore() {
        return showMore;

    }

    class DetailsViewHolder extends ViewHolder {
        ImageView image_view;
        TextView tv_detail;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            tv_detail = itemView.findViewById(R.id.tv_detail);
        }
    }
}
