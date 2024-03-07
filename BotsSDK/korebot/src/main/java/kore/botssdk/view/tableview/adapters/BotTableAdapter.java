package kore.botssdk.view.tableview.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotTableListDefaultActionsModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.tableview.TableDataAdapter;
import kore.botssdk.view.tableview.model.MiniTableModel;


public class BotTableAdapter extends TableDataAdapter<MiniTableModel> {

    private static final int TEXT_SIZE = 12;
    private final String[] alignment;
    final ComposeFooterInterface composeFooterInterface;
    final InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final Gson gson = new Gson();
    private Dialog dialog;

    public BotTableAdapter(final Context context, final List<MiniTableModel> data, String[] alignment, ComposeFooterInterface composeFooterInterface, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        super(context, data);
        this.alignment = alignment;
        this.composeFooterInterface = composeFooterInterface;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        String str = "";
        View renderedView = null;

        if (getRowData(rowIndex).getElements().get(columnIndex) instanceof Double) {
            str = Double.toString((Double) getRowData(rowIndex).getElements().get(columnIndex));
            renderedView = renderString(columnIndex, str);
        } else if (getRowData(rowIndex).getElements().get(columnIndex) instanceof String) {
            str = (String) getRowData(rowIndex).getElements().get(columnIndex);
            renderedView = renderString(columnIndex, str);
        } else if (getRowData(rowIndex).getElements().get(columnIndex) instanceof ArrayList) {
            renderedView = renderString(columnIndex, ((ArrayList<?>) getRowData(rowIndex).getElements().get(columnIndex)));
        }

        return renderedView;
    }

    private int getGravity(int columnIndex) {
        if (alignment[columnIndex].equals("left") || alignment[columnIndex].equals("default"))
            return Gravity.START | Gravity.CENTER_VERTICAL;
        else if (alignment[columnIndex].equals("right"))
            return Gravity.END | Gravity.CENTER_VERTICAL;
        else return Gravity.CENTER;
    }

    public void setDialogReference(Dialog dialog) {
        this.dialog = dialog;
    }


    private View renderString(int columnIndex, final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(10, 20, 10, 20);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setMaxLines(2);
        textView.setGravity(getGravity(columnIndex));
        return textView;
    }

    private View renderString(int columnIndex, final ArrayList value) {

        final TextView textView = new TextView(getContext());
        textView.setPadding(5, 20, 10, 20);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(getGravity(columnIndex));

        if (value != null && value.size() > 0) {
            textView.setText(format(value.get(0)));

            if (value.size() > 1) {
                if (value.get(1).equals("button")) {
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));

                    try {
                        String elementsAsString = gson.toJson(value.get(2));
                        Type carouselType = new TypeToken<BotTableListDefaultActionsModel>() {
                        }.getType();
                        BotTableListDefaultActionsModel botTableListDefaultActionsModel = gson.fromJson(elementsAsString, carouselType);

                        if (botTableListDefaultActionsModel != null) {
                            if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botTableListDefaultActionsModel.getType()))
                                textView.setText(Html.fromHtml("<u>" + format(value.get(0)) + "<\"\"u>"));

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botTableListDefaultActionsModel.getType())) {
                                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListDefaultActionsModel.getUrl());
                                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botTableListDefaultActionsModel.getType())) {
                                            String listElementButtonPayload = botTableListDefaultActionsModel.getPayload();
                                            String listElementButtonTitle = textView.getText().toString();
                                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload, false);
                                        }

                                        if (dialog != null)
                                            dialog.dismiss();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return textView;
    }

    public static String format(Object d) {
        if (d instanceof Double) {
            NumberFormat format = new DecimalFormat("0.#");
            return format.format(d);
        } else
            return d + "";
    }

}
