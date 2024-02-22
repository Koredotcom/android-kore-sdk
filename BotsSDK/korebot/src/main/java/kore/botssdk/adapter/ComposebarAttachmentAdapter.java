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
    public ComposebarAttachmentAdapter(@NonNull Context context, @NonNull AttachmentListner attachmentListner)
    {
        this.context=context;
        this.attachmentListner=attachmentListner;
    }

    @NonNull
    @Override
    public ImageAttachView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attachment_view_composebar,parent,false);
        return new ImageAttachView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAttachView holder, int position) {
        String fileExtension=dataList.get(position).get("fileExtn");
        if(FileUtils.ImageTypes().contains(fileExtension)|| FileUtils.VideoTypes().contains(fileExtension))
        {
            Glide.with(context).load(dataList.get(position).get("localFilePath")).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(holder.attach_view));

           }else {
            holder.attach_view.setImageResource(FileUtils.getDrawableByExt(!StringUtils.isNullOrEmptyWithTrim(fileExtension) ? fileExtension.toLowerCase() : ""));
        }
        holder.close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(holder.getBindingAdapterPosition());
                notifyItemRangeInserted(0, dataList.size() - 1);
                attachmentListner.onRemoveAttachment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    final ArrayList<HashMap<String, String>> dataList=new ArrayList<>();
    public void addAttachment(@NonNull HashMap<String, String> attachmentKey) {
        dataList.add(attachmentKey);
        notifyItemRangeInserted(0, dataList.size() - 1);
    }

    public void clearAll()
    {
        dataList.clear();
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData() {
        return dataList;
    }


    static class ImageAttachView extends RecyclerView.ViewHolder
    {
        final View close_icon;
        final ImageView attach_view;
        public ImageAttachView(@NonNull View itemView) {
            super(itemView);
            close_icon=itemView.findViewById(R.id.close_icon);

            attach_view=itemView.findViewById(R.id.attach_view);
        }
    }
}
