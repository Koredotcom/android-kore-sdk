package kore.botssdk.view;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.adapter.PdfDownloadAdapter;
import kore.botssdk.models.PdfDownloadModel;
import kore.botssdk.models.PdfResponseModel;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfDownloadView extends LinearLayout {
    private ImageView ivPdfDownload;
    private AutoExpandListView lvPdfs;
    private Context context;
    private final Gson gson = new Gson();
    private ProgressBar pbDownload;

    public PdfDownloadView(Context context) {
        super(context);
        init(context);
    }

    public PdfDownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PdfDownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_download_view, this, true);
        lvPdfs = view.findViewById(R.id.lvPdfs);
        ivPdfDownload = view.findViewById(R.id.ivPdfDownload);
        pbDownload = view.findViewById(R.id.pbDownload);

        KaFontUtils.applyCustomFont(getContext(), view);
    }

    public void populatePdfView(ArrayList<PdfDownloadModel> arrPdfDownloadModels) {
        if (arrPdfDownloadModels != null && arrPdfDownloadModels.size() > 0) {
            lvPdfs.setAdapter(new PdfDownloadAdapter(getContext(), arrPdfDownloadModels));

            lvPdfs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    PdfDownloadModel pdfDownloadModel = arrPdfDownloadModels.get(position);

                    if (pdfDownloadModel != null && !StringUtils.isNullOrEmpty(pdfDownloadModel.getUrl())
                            && pdfDownloadModel.getBody() != null && pdfDownloadModel.getHeader() != null) {
                        ivPdfDownload.setVisibility(GONE);
                        pbDownload.setVisibility(VISIBLE);

                        if (pdfDownloadModel.getPdfType() > 0)
                            getPdfBase64(pdfDownloadModel, pdfDownloadModel.getUrl(), pdfDownloadModel.getHeader(), pdfDownloadModel.getBody());
                        else
                            getPdfDetails(pdfDownloadModel, pdfDownloadModel.getUrl(), pdfDownloadModel.getHeader(), pdfDownloadModel.getBody());
                    } else
                        Toast.makeText(context, "Statement can not be downloaded", Toast.LENGTH_SHORT).show();
                }
            });

            ivPdfDownload.setOnClickListener(v -> {
                PdfDownloadModel pdfDownloadModel = arrPdfDownloadModels.get(0);

                if (pdfDownloadModel != null && !StringUtils.isNullOrEmpty(pdfDownloadModel.getUrl())
                        && pdfDownloadModel.getBody() != null && pdfDownloadModel.getHeader() != null) {
                    ivPdfDownload.setVisibility(GONE);
                    pbDownload.setVisibility(VISIBLE);

                    if (pdfDownloadModel.getPdfType() > 0)
                        getPdfBase64(pdfDownloadModel, pdfDownloadModel.getUrl(), pdfDownloadModel.getHeader(), pdfDownloadModel.getBody());
                    else
                        getPdfDetails(pdfDownloadModel, pdfDownloadModel.getUrl(), pdfDownloadModel.getHeader(), pdfDownloadModel.getBody());
                } else
                    Toast.makeText(context, "Statement can not be downloaded", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void getPdfDetails(PdfDownloadModel pdfDownloadModel, String url, HashMap<String, String> header, HashMap<String, Object> body) {
        JsonObject jsonObject = gson.toJsonTree(body).getAsJsonObject();
        Call<ResponseBody> getBankingConfigService = BrandingRestBuilder.getPDfAPI().getPdfDetails(url, header, jsonObject);
        getBankingConfigService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + DateUtils.getCurrentDateTime() + "_" + pdfDownloadModel.getTitle().replace(" ", "-") + "." + pdfDownloadModel.getFormat().toLowerCase(Locale.ROOT));

                    if (response.body() != null && writeResponseBodyToDisk(response.body(), fileLocation)) {
                        Toast.makeText(context, "Statement saved successfully under Downloads", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try
                    {
                        if(response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("errorMessage"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Statement download failed", Toast.LENGTH_SHORT).show();
                    }
                }

                ivPdfDownload.setVisibility(VISIBLE);
                pbDownload.setVisibility(GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Statement download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPdfBase64(PdfDownloadModel pdfDownloadModel, String url, HashMap<String, String> header, HashMap<String, Object> body) {
        if (body.containsKey("AccountNumber")) {
            if (body.get("AccountNumber") instanceof Double) {
                try {
                    double aDouble = (Double) body.get("AccountNumber");
                    LogUtils.e("Changed Value", BigDecimal.valueOf(aDouble) + "");
                    body.put("AccountNumber", BigDecimal.valueOf(aDouble));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        if (header.containsKey("X-CORRELATION-ID")) {
            if (!StringUtils.isNullOrEmpty(header.get("X-CORRELATION-ID"))) {

                try {
                    double xcrid = Double.parseDouble(Objects.requireNonNull(header.get("X-CORRELATION-ID")));
                    header.put("X-CORRELATION-ID", ((int) xcrid) + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        JsonObject jsonObject = gson.toJsonTree(body).getAsJsonObject();

        Call<PdfResponseModel> getBankingConfigService = BrandingRestBuilder.getPDfAPI().getPdfBaseDetails(url, header, jsonObject);
        getBankingConfigService.enqueue(new Callback<PdfResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PdfResponseModel> call, @NonNull Response<PdfResponseModel> response) {
                if (response.isSuccessful()) {
                    PdfResponseModel pdfDownloadBaseModel = response.body();
                    if (pdfDownloadBaseModel != null) {
                        File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + pdfDownloadModel.getTitle().replace(" ", "-") + "-" + DateUtils.getCurrentDateTime() + ".pdf");

                        try {
                            if (response.body() != null && writeBase64ToDisk(pdfDownloadBaseModel.getData(), fileLocation)) {
                                Toast.makeText(context, "Statement saved successfully under Downloads", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    try {
                        if(response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("errorMessage"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Statement download failed", Toast.LENGTH_SHORT).show();
                    }
                }

                ivPdfDownload.setVisibility(VISIBLE);
                pbDownload.setVisibility(GONE);
            }

            @Override
            public void onFailure(@NonNull Call<PdfResponseModel> call, @NonNull Throwable t) {
                ivPdfDownload.setVisibility(VISIBLE);
                pbDownload.setVisibility(GONE);
                Toast.makeText(context, "Statement download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeBase64ToDisk(String fileData, File fileLocation) throws IOException {
        FileOutputStream os = null;

        try {
            byte[] pdfAsBytes = Base64.decode(String.valueOf(fileData), 0);
            os = new FileOutputStream(fileLocation, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null)
                os.close();
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, File fileLocation) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(fileLocation);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                LogUtils.d("Downloading file", "file download: " + fileSizeDownloaded + " of " + fileSize);
            }

            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
