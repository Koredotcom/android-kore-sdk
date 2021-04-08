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

    Context context;
    AttachmentListner attachmentListner;
    public ComposebarAttachmentAdapter(Context context, AttachmentListner attachmentListner)
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
//        holder. close_icon.setBackground(KaUtility.changeColorOfDrawable(context, R.color.color_E0F2F1));

     // String thumbnail=dataList.get(position).get("thumbnailURL");
        String fileExtn=dataList.get(position).get("fileExtn");
        if(FileUtils.ImageTypes().contains(fileExtn)|| FileUtils.VideoTypes().contains(fileExtn))
        {
            Glide.with(context).load(dataList.get(position).get("localFilePath")).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(holder.attach_view));

           }else {
            holder.attach_view.setImageResource(FileUtils.getDrawableByExt(!StringUtils.isNullOrEmptyWithTrim(fileExtn) ? fileExtn.toLowerCase() : ""));

        }
        holder.close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(position);
                notifyDataSetChanged();
                attachmentListner.onRemoveAttachment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    ArrayList<HashMap<String, String>> dataList=new ArrayList<>();
    public void addAttachment(HashMap<String, String> attachmentKey) {
        dataList.add(attachmentKey);
        notifyDataSetChanged();

    }

    public void clearAll()
    {
        dataList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<HashMap<String, String>> getData() {
        return dataList;
    }


    class ImageAttachView extends RecyclerView.ViewHolder
    {
        View close_icon;
        ImageView attach_view;
        public ImageAttachView(@NonNull View itemView) {
            super(itemView);
            close_icon=itemView.findViewById(R.id.close_icon);

            attach_view=itemView.findViewById(R.id.attach_view);
        }
    }
}
