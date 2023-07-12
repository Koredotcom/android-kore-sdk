package kore.botssdk.view;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kore.botssdk.R;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

public class LinkTemplateView extends LinearLayout {
    private ImageView ivPdfDownload;
    private Context context;
    private ProgressBar pbDownload;

    public LinkTemplateView(Context context) {
        super(context);
        init(context);
    }

    public LinkTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pdf_download_view, this, true);
        ivPdfDownload = view.findViewById(R.id.ivPdfDownload);
        pbDownload = view.findViewById(R.id.pbDownload);

        KaFontUtils.applyCustomFont(getContext(), view);
    }

    public void populatePdfView(PayloadInner payloadInner) {
        if (payloadInner != null)
        {
            ivPdfDownload.setOnClickListener(v -> {
                File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + payloadInner.getFileName());
                if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    ivPdfDownload.setVisibility(GONE);
                    pbDownload.setVisibility(VISIBLE);

                    if(writeBase64ToDisk(payloadInner.getUrl(), fileLocation))
                    {
                        ivPdfDownload.setVisibility(VISIBLE);
                        pbDownload.setVisibility(GONE);

                        Toast.makeText(context, "Statement downloaded successfully under downloads", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    ivPdfDownload.setVisibility(VISIBLE);
                    pbDownload.setVisibility(GONE);
                    Toast.makeText(context, "Statement can not be downloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean writeBase64ToDisk(String fileData, File fileLocation) {
        FileOutputStream os = null;
        try {
            fileData = fileData.substring(fileData.indexOf(",") + 1);
            byte[] pdfAsBytes = Base64.decode(String.valueOf(fileData), 0);
            os = new FileOutputStream(fileLocation, false);
            os.write(pdfAsBytes);
            os.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if(os != null)
                    os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
