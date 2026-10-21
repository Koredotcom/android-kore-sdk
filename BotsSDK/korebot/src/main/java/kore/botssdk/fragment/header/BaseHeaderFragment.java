package kore.botssdk.fragment.header;

import android.content.res.Configuration;

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

    /** Returns whether the SDK currently has a human agent connected. */
    public final boolean isHumanAgentConnected() {
        return composeFooterInterface != null && composeFooterInterface.isHumanAgentConnected();
    }

    /** Sends the agent-close event through the SDK without closing the chat window. */
    public final void sendAgentCloseEvent() {
        if (composeFooterInterface != null) composeFooterInterface.sendAgentCloseEvent();
    }

    public abstract void setBrandingDetails(BrandingModel brandingModel);

    public abstract ImageView getMinimize();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
