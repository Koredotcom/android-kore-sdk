package kore.botssdk.fragment.header;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingHeaderModel;
import kore.botssdk.R;
import kore.botssdk.net.SDKConfig;

public abstract class BaseHeaderFragment extends Fragment {
    protected ComposeFooterInterface composeFooterInterface;
    protected InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean agentConnected;
    private String agentName;
    private String agentProfileImage;
    private TextView headerTitle;
    private TextView headerSubtitle;
    private ImageView headerAvatar;
    private LinearLayout avatarContainer;
    private String defaultTitle;
    private String defaultSubtitle;
    private Drawable defaultAvatar;
    private ColorStateList defaultAvatarTint;
    private ViewGroup.LayoutParams defaultAvatarLayoutParams;
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface){
        this.composeFooterInterface =composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface){
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public final boolean isHumanAgentConnected() {
        return composeFooterInterface != null && composeFooterInterface.isHumanAgentConnected();
    }

    public final void sendAgentCloseEvent() {
        if (composeFooterInterface != null) composeFooterInterface.sendAgentCloseEvent();
    }

    protected final void handleHeaderClose() {
        // Prefer the connection state delivered to this header. The footer callback can
        // lag behind (or be unavailable in custom headers) during an agent handoff.
        if (SDKConfig.isAgentCloseConfirmationEnabled() && (agentConnected || isHumanAgentConnected())) {
            new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.agent_close_confirmation)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, which) -> sendAgentCloseEvent())
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }
    }

    /** Called whenever the connected agent state changes. */
    public void onAgentConnected(boolean isConnected) {
        agentConnected = isConnected;
        updateAgentHeader();
    }

    public void setAgentDetails(String name, String profileImage) {
        agentName = name;
        agentProfileImage = profileImage;
        updateAgentHeader();
    }

    protected final void bindAgentHeader(TextView title, TextView subtitle, ImageView avatar, LinearLayout avatarBackground) {
        headerTitle = title;
        headerSubtitle = subtitle;
        headerAvatar = avatar;
        avatarContainer = avatarBackground;
        defaultTitle = title.getText().toString();
        defaultSubtitle = subtitle.getText().toString();
        defaultAvatar = avatar.getDrawable();
        defaultAvatarLayoutParams = avatar.getLayoutParams();
        defaultAvatarTint = avatarBackground.getBackgroundTintList();
        updateAgentHeader();
    }

    private void updateAgentHeader() {
        if (headerTitle == null || headerSubtitle == null || headerAvatar == null || avatarContainer == null) return;
        if (!agentConnected) {
            headerTitle.setText(defaultTitle);
            headerSubtitle.setText(defaultSubtitle);
            headerAvatar.setImageDrawable(defaultAvatar);
            headerAvatar.setLayoutParams(defaultAvatarLayoutParams);
            avatarContainer.setBackgroundTintList(defaultAvatarTint);
            return;
        }

        headerTitle.setText(!TextUtils.isEmpty(agentName) ? agentName : "Agent");
        headerSubtitle.setText(R.string.live_chat);
        if (!TextUtils.isEmpty(agentProfileImage)) {
            Glide.with(this).load(agentProfileImage)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .error(R.drawable.agent_icon)
                    .into(headerAvatar);
        } else {
            headerAvatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.agent_icon, requireContext().getTheme()));
        }
    }

    public abstract void setBrandingDetails(BrandingHeaderModel brandingModel);
}
