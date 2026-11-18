package kore.botssdk.fragment.header;

import android.content.res.Configuration;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.net.SDKConfig;

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

    /** Shows agent-close confirmation when enabled and connected; otherwise runs the regular close flow. */
    public final void handleCloseClick(@NonNull Runnable regularCloseAction) {
        if (SDKConfig.isAgentCloseConfirmationEnabled() && isHumanAgentConnected()) {
            new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.agent_close_confirmation)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, which) -> sendAgentCloseEvent())
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }

        regularCloseAction.run();
    }

    public abstract void setBrandingDetails(BrandingModel brandingModel);

    public abstract ImageView getMinimize();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public abstract void onAgentConnected(boolean isConnected);

}
