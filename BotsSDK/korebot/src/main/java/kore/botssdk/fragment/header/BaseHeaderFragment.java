package kore.botssdk.fragment.header;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingModel;

public abstract class BaseHeaderFragment extends Fragment {
    protected BrandingModel brandingModel;
    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface){
        this.composeFooterInterface =composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface){
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public abstract void setBrandingDetails(BrandingModel brandingModel);

    public abstract ImageView getMinimize();

}