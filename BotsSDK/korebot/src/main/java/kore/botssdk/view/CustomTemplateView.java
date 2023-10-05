package kore.botssdk.view;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;

public abstract class CustomTemplateView extends LinearLayout
{
    public CustomTemplateView(@NonNull Context context) {
        super(context);
    }

    public abstract void populateTemplate(@NonNull String payloadInner, boolean isLast);

    @NonNull
    public abstract CustomTemplateView getNewInstance();

    public abstract void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface);
    public abstract void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface composeFooterInterface);
}
