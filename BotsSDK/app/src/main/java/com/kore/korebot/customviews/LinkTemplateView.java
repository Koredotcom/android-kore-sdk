package com.kore.korebot.customviews;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kore.korebot.R;
import com.kore.korebot.model.ResponsePayload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.CustomTemplateView;

public class LinkTemplateView extends CustomTemplateView {
    private ImageView ivPdfDownload;
    private TextView tvPdfName;
    private Context context;
    private ProgressBar pbDownload;
    private final Gson gson = new Gson();
    public LinkTemplateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    @Override
    public void populateTemplate(@NonNull String botResponse, boolean isLast) {

        Type botResp = new TypeToken<ResponsePayload>() {}.getType();
        ResponsePayload responsePayload = gson.fromJson(botResponse, botResp);

        if(responsePayload != null)
        {
            tvPdfName.setText(responsePayload.getFileName());
            ivPdfDownload.setOnClickListener(v -> {
                if (!StringUtils.isNullOrEmpty(responsePayload.getUrl())) {
                    ivPdfDownload.setVisibility(GONE);
                    pbDownload.setVisibility(VISIBLE);

                    if(writeBase64ToDisk(responsePayload.getUrl(), responsePayload.getFileName()))
                    {
                        ivPdfDownload.setVisibility(VISIBLE);
                        pbDownload.setVisibility(GONE);

                        Toast.makeText(context, "Statement downloaded successfully under Documents folder", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void savePDFFile(@NonNull final Context context, @NonNull InputStream in, String fileName) throws IOException {
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

    @NonNull
    @Override
    public CustomTemplateView getNewInstance() {
        return new LinkTemplateView(context);
    }

    @Override
    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {

    }

    @Override
    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface composeFooterInterface) {

    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pdf_download_view, this, true);
        tvPdfName = view.findViewById(R.id.tv_pdf_item_title);
        ivPdfDownload = view.findViewById(R.id.ivPdfDownload);
        pbDownload = view.findViewById(R.id.pbDownload);

        KaFontUtils.applyCustomFont(getContext(), view);
    }

    private boolean writeBase64ToDisk(String fileData, String fileName) {
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
