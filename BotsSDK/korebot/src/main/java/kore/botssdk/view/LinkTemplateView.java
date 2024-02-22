package kore.botssdk.view;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kore.botssdk.R;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;

public class LinkTemplateView extends LinearLayout {
    private ImageView ivPdfDownload;
    private Context context;
    private ProgressBar pbDownload;
    private TextView tvTitle;

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
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_download_view, this, true);
        ivPdfDownload = view.findViewById(R.id.ivPdfDownload);
        pbDownload = view.findViewById(R.id.pbDownload);
        tvTitle = view.findViewById(R.id.tvTitle);
        KaFontUtils.applyCustomFont(getContext(), view);
    }

    public void populatePdfView(PayloadInner payloadInner) {
        if (payloadInner != null)
        {

            if(!StringUtils.isNullOrEmpty(payloadInner.getFileName()))
                tvTitle.setText(payloadInner.getFileName());

            ivPdfDownload.setOnClickListener(v -> {
                if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    ivPdfDownload.setVisibility(GONE);
                    pbDownload.setVisibility(VISIBLE);

                    if(writeBase64ToDisk(payloadInner.getUrl(), payloadInner.getFileName()))
                    {
                        ivPdfDownload.setVisibility(VISIBLE);
                        pbDownload.setVisibility(GONE);

                        Toast.makeText(context, "Statement downloaded successfully under documents", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    ivPdfDownload.setVisibility(VISIBLE);
                    pbDownload.setVisibility(GONE);
                    Toast.makeText(context, "Statement download unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    private Uri savePDFFile(@NonNull final Context context, @NonNull InputStream in, String fileName) throws IOException {
        String relativeLocation = Environment.DIRECTORY_DOCUMENTS;

        if (!TextUtils.isEmpty("PdfFolder")) {
            relativeLocation += File.separator + "PdfFolder";
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
        final ContentResolver resolver = context.getContentResolver();
        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Files.getContentUri("external");
            uri = resolver.insert(contentUri, contentValues);
            ParcelFileDescriptor pfd;
            try {
                assert uri != null;
                pfd = context.getContentResolver().openFileDescriptor(uri, "w");
                assert pfd != null;
                FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());

                byte[] buf = new byte[4 * 1024];
                int len;
                while ((len = in.read(buf)) > 0) {

                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
                pfd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentValues.clear();
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
            context.getContentResolver().update(uri, contentValues, null, null);
            stream = resolver.openOutputStream(uri);
            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }
            return uri;
        } catch (IOException e) {
            // Don't leave an orphan entry in the MediaStore
            resolver.delete(uri, null, null);
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private boolean writeBase64ToDisk(String fileData, String fileName) {
        FileOutputStream os = null;
        try {
            fileData = fileData.substring(fileData.indexOf(",") + 1);
            byte[] pdfAsBytes = Base64.decode(fileData, 0);
            InputStream isActual = new ByteArrayInputStream(pdfAsBytes);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                savePDFFile(context, isActual, fileName);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
