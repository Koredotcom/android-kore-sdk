package kore.botssdk.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class FreemiumView extends ViewGroup {

    float dp1;
    private TextView message_tv;
    private View rootView;
    private TextView leftBtn, rightBtn;

    public FreemiumView(Context context) {
        super(context);
        init();
    }

    public FreemiumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FreemiumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init() {
        //freemiumViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.freemium_view, this, true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.freemium_view, this, true);
        rootView = view.findViewById(R.id.freemium_root);

        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor()) + BundleConstants.TRANSPERANCY_50_PERCENT);
        rootView.setBackground(shape);


        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        message_tv = view.findViewById(R.id.message_tv);
        leftBtn = view.findViewById(kore.botssdk.R.id.tv_left);
        rightBtn = view.findViewById(kore.botssdk.R.id.tv_right);
    }

    public void onEvent(ProfileColorUpdateEvent event) {
        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor()) + BundleConstants.TRANSPERANCY_50_PERCENT);
        rootView.setBackground(shape);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        // View rootView = freemiumViewBinding.getRoot();
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    public void populateMessage(final String msg) {
        if (message_tv != null) {
            message_tv.setText(msg);
        }
        if (TextUtils.isEmpty(msg)) {
            setVisibility(GONE);
            rootView.setVisibility(GONE);
        } else {
            rootView.setVisibility(VISIBLE);
        }
    }

    public void populateData(ArrayList<BotButtonModel> buttons) {
        rootView.setVisibility(GONE);
        if (buttons != null) {
            rootView.setVisibility(VISIBLE);

            for (int i = 0; i < buttons.size(); i++) {
                int finalI = i;
                switch (finalI) {
                    case 0:
                        leftBtn.setText(buttons.get(finalI).getTitle());
                        leftBtn.setOnClickListener(v -> buttonAction(buttons.get(finalI)));
                        break;
                    case 1:
                        rightBtn.setText(buttons.get(finalI).getTitle());
                        rightBtn.setOnClickListener(v -> buttonAction(buttons.get(finalI)));
                        break;
                }
            }
        }
    }

    private void buttonAction(BotButtonModel model) {
        if (model == null) return;
        if (!TextUtils.isEmpty(model.getType()) && model.getType().equalsIgnoreCase("url"))
            if (model.getUrl().startsWith("https:") || model.getUrl().startsWith("http:")) {
                openBrowser(model.getUrl());
            } else if (model.getUrl().startsWith("mailto:")) {
                sendMail(model.getUrl());
            }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        //int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    private void openBrowser(String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
                url = "http://" + url.toLowerCase();
            }
            if (NetworkUtility.isNetworkConnectionAvailable(getContext())) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            } else {
                Toast.makeText(getContext(), "Check your internet connection and please try again",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendMail(String recepientEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        recepientEmail= recepientEmail.replace("mailto://","");
        emailIntent.setData(Uri.parse("mailto:" + recepientEmail));
        try {
            getContext().startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error while launching email intent!", Toast.LENGTH_SHORT).show();
        }
    }

}
