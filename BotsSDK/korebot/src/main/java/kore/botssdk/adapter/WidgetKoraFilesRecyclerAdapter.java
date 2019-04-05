package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.databinding.KoraFileLookupViewBinding;
import kore.botssdk.databinding.WidgetKoraFileLookupViewBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.models.WFileLookUpModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.FileUtils;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class WidgetKoraFilesRecyclerAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<WFileLookUpModel> kaFileLookupModels;
    private boolean isExpanded;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean from_widget = false;
    private int NO_DATA = 0;
    private int DATA_FOUND = 1;
    public int getPreviewlength() {
        return previewlength;
    }

    public void setPreviewlength(int previewlength) {
        this.previewlength = previewlength;
    }

    int previewlength;

    public boolean isFrom_widget() {
        return from_widget;
    }

    public WidgetKoraFilesRecyclerAdapter(ArrayList<WFileLookUpModel> fileLookupModels, Context context) {
        this.kaFileLookupModels = fileLookupModels;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        if (kaFileLookupModels != null && kaFileLookupModels.size() > 0) {
            return DATA_FOUND;
        }
        return NO_DATA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NO_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        }
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_kora_file_lookup_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holdermodel, int position) {
        if (holdermodel.getItemViewType() == NO_DATA) {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) holdermodel;
            emptyHolder.tv_disrcription.setText("No Files");
            emptyHolder.img_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_meeting));
        } else {
            ViewHolder holder = (ViewHolder) holdermodel;
            holder.koraFileLookupViewBinding.setFileModel(kaFileLookupModels.get(position));
            String type = kaFileLookupModels.get(position).getData().getExt();
            holder.koraFileLookupViewBinding.image.setImageResource(FileUtils.getDrawableByExt(!StringUtils.isNullOrEmptyWithTrim(type) ? type.toLowerCase() : ""));
            holder.koraFileLookupViewBinding.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WFileLookUpModel kaFileLookupModel = kaFileLookupModels.get(position);
                  /*  if (kaFileLookupModel.getButtons() != null && kaFileLookupModel.getButtons().size() > 0) {
                        verticalListViewActionHelper.driveItemClicked(kaFileLookupModel.getButtons().get(0));
                    }else */if(kaFileLookupModel.getDefault_action().getUrl() != null){
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(kaFileLookupModel.getDefault_action().getUrl()));
                        context.startActivity(browserIntent);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return kaFileLookupModels != null && kaFileLookupModels.size() > 0 ? (!isExpanded && kaFileLookupModels.size() > previewlength ? previewlength : kaFileLookupModels.size()) : 1;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }


    @Override
    public ArrayList getData() {
        return kaFileLookupModels;
    }

    @Override
    public void setData(ArrayList data) {
        kaFileLookupModels = data;

    }

    public void setFrom_widget(boolean b) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WidgetKoraFileLookupViewBinding koraFileLookupViewBinding;

        public ViewHolder(@NonNull WidgetKoraFileLookupViewBinding itemView) {
            super(itemView.getRoot());
            this.koraFileLookupViewBinding = itemView;
        }
    }

}