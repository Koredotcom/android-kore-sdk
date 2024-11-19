package kore.botssdk.fragment.header;

import androidx.fragment.app.Fragment;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingHeaderModel;

public abstract class BaseHeaderFragment extends Fragment {
    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface){
        this.composeFooterInterface =composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface){
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public abstract void setBrandingDetails(BrandingHeaderModel brandingModel);
}