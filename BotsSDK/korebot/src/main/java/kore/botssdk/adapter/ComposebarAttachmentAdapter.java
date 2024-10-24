package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.listener.AttachmentListner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.FileUtils;

public class ComposebarAttachmentAdapter extends RecyclerView.Adapter<ComposebarAttachmentAdapter.ImageAttachView> {

    final Context context;
    final AttachmentListner attachmentListner;

    public ComposebarAttachmentAdapter(Context context, AttachmentListner attachmentListner) {
        this.context = context;
        this.attachmentListner = attachmentListner;
    }

    @NonNull
    @Override
    public ImageAttachView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attachment_view_composebar, parent, false);
        return new ImageAttachView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAttachView holder, int position) {
        String fileExtn = dataList.get(position).get("fileExtn");
        if (FileUtils.ImageTypes().contains(fileExtn)) {
            Glide.with(context).load(dataList.get(position).get("localFilePath")).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(holder.attachView));
        } else {
            holder.attachView.setImageResource(FileUtils.getDrawableByExt(!StringUtils.isNullOrEmptyWithTrim(fileExtn) ? fileExtn.toLowerCase() : ""));
        }
        holder.closeIcon.setOnClickListener(view -> {
            if (dataList.isEmpty()) return;
            dataList.remove(holder.getBindingAdapterPosition());
            notifyItemRangeInserted(0, dataList.size() - 1);
            attachmentListner.onRemoveAttachment();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    public void addAttachment(HashMap<String, String> attachmentKey) {
        dataList.add(attachmentKey);
        notifyDataSetChanged();
    }

    public void clearAll() {
        dataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<HashMap<String, String>> getData() {
        return dataList;
    }


    static class ImageAttachView extends RecyclerView.ViewHolder {
        final View closeIcon;
        final ImageView attachView;

        public ImageAttachView(@NonNull View itemView) {
            super(itemView);
            closeIcon = itemView.findViewById(R.id.close_icon);
            attachView = itemView.findViewById(R.id.attach_view);
        }
    }
}
