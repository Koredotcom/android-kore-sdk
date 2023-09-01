package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.PdfDownloadModel;

public class PdfDownloadAdapter extends BaseAdapter
{
    private final Context context;
    private final ArrayList<PdfDownloadModel> arrPdfDownloadModels;

    public PdfDownloadAdapter(Context context, ArrayList<PdfDownloadModel> arrPdfDownloadModels)
    {
        this.context = context;
        this.arrPdfDownloadModels = arrPdfDownloadModels;
    }

    @Override
    public int getCount() {
        return arrPdfDownloadModels.size();
    }

    @Override
    public Object getItem(int position) {
        return arrPdfDownloadModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pdf_download_cell, null);
        }

        if (convertView.getTag() == null)
        {
            ViewHolder holder = new ViewHolder();

            holder.iv_pdf_image = convertView.findViewById(R.id.iv_pdf_image);
            holder.tv_pdf_item_title = convertView.findViewById(R.id.tv_pdf_item_title);

            convertView.setTag(holder);
        }

        convertView.setClipToOutline(true);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        PdfDownloadModel pdfDownloadModel = (PdfDownloadModel)getItem(position);
        holder.tv_pdf_item_title.setText(pdfDownloadModel.getTitle());
    }

    private static class ViewHolder {
        ImageView iv_pdf_image;
        TextView tv_pdf_item_title;
    }
}
