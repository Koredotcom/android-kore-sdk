package kore.botssdk.view;

import android.content.Context;
import android.widget.LinearLayout;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;

public abstract class CustomTemplateView extends LinearLayout
{
    public CustomTemplateView(Context context) {
        super(context);
    }

    public abstract void populateTemplate(PayloadInner payloadInner, boolean isLast);

    public abstract CustomTemplateView getNewInstance();

    public abstract void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface);
    public abstract void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface composeFooterInterface);
}
