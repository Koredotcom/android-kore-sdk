package com.kore.korebot.customtemplates;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kore.korebot.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.viewholders.BaseViewHolder;

public class NewTemplateHolder extends BaseViewHolder {
    private final ImageView ivPdfDownload;
    private final TextView tvPdfName;
    private final ProgressBar pbDownload;

    public static NewTemplateHolder getInstance(ViewGroup parent) {
        return new NewTemplateHolder(createView(R.layout.pdf_download_view, parent));
    }

    private NewTemplateHolder(@NonNull View view) {
        super(view, view.getContext());

        tvPdfName = view.findViewById(kore.botssdk.R.id.tv_pdf_item_title);
        ivPdfDownload = view.findViewById(kore.botssdk.R.id.ivPdfDownload);
        pbDownload = view.findViewById(kore.botssdk.R.id.pbDownload);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;

        tvPdfName.setTextColor(Color.RED);
        tvPdfName.setTypeface(Typeface.DEFAULT_BOLD);
        tvPdfName.setText("Custom Pie chart");
        ivPdfDownload.setOnClickListener(v -> {
            File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + payloadInner.getFileName());
            if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                ivPdfDownload.setVisibility(GONE);
                pbDownload.setVisibility(VISIBLE);

                if (writeBase64ToDisk(payloadInner.getUrl(), fileLocation)) {
                    ivPdfDownload.setVisibility(VISIBLE);
                    pbDownload.setVisibility(GONE);

                    Toast.makeText(itemView.getContext(), "Statement downloaded successfully under downloads", Toast.LENGTH_SHORT).show();
                }
            } else {
                ivPdfDownload.setVisibility(VISIBLE);
                pbDownload.setVisibility(GONE);
                Toast.makeText(itemView.getContext(), "Statement can not be downloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeBase64ToDisk(String fileData, File fileLocation) {
        try {
            fileData = fileData.substring(fileData.indexOf(",") + 1);
            byte[] pdfAsBytes = Base64.decode(fileData, 0);
            FileOutputStream os;
            os = new FileOutputStream(fileLocation, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ivPdfDownload.setVisibility(VISIBLE);
            pbDownload.setVisibility(GONE);
            return false;
        }
    }
}
