package kore.botssdk.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.KoraFileLookupViewBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.view.viewUtils.FileUtils;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KoraFilesRecyclerAdapter extends RecyclerView.Adapter<KoraFilesRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KaFileLookupModel> kaFileLookupModels;
    private boolean isExpanded;

    public KoraFilesRecyclerAdapter(ArrayList<KaFileLookupModel> fileLookupModels, Context context) {
        this.kaFileLookupModels = fileLookupModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.kora_file_lookup_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.koraFileLookupViewBinding.setFileModel(kaFileLookupModels.get(position));
        holder.koraFileLookupViewBinding.image.setImageResource(FileUtils.getDrawableByExt(kaFileLookupModels.get(position).getFileType()));
        holder.koraFileLookupViewBinding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaFileLookupModel kaFileLookupModel = kaFileLookupModels.get(position);
                if (kaFileLookupModel.getButtons() != null && kaFileLookupModel.getButtons().size() > 0) {
                    launchDetails(kaFileLookupModel.getButtons().get(0));
                }
            }
        });
    }

    private void launchDetails(BotCaourselButtonModel botCaourselButtonModel) {
        LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) botCaourselButtonModel.getCustomData().get("redirectUrl");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map.get("mob")));
        context.startActivity(browserIntent);
    }

    @Override
    public int getItemCount() {
        return kaFileLookupModels != null ? (!isExpanded && kaFileLookupModels.size() > 3 ? 3 : kaFileLookupModels.size()) : 0;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {

    }

    @Override
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {

    }

    @Override
    public ArrayList getData() {
        return kaFileLookupModels;
    }

    @Override
    public void setData(ArrayList data) {
        kaFileLookupModels = data;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        KoraFileLookupViewBinding koraFileLookupViewBinding;

        public ViewHolder(@NonNull KoraFileLookupViewBinding itemView) {
            super(itemView.getRoot());
            this.koraFileLookupViewBinding = itemView;
        }
    }

}
