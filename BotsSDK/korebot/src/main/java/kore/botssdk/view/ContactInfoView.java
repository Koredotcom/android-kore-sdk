package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.HashMap;

import androidx.databinding.DataBindingUtil;
import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.ContactInfoViewBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.ContactInfoModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class ContactInfoView extends ViewGroup {

    float dp1;
    private int splashColor;
    private ContactInfoViewBinding contactInfoViewBinding;
    public ContactInfoView(Context context) {
        super(context);
        init();
    }

    public ContactInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        contactInfoViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.contact_info_view, this, true);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        splashColor = getContext().getResources().getColor(R.color.splash_color);
        contactInfoViewBinding.setViewBase(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        View rootView = contactInfoViewBinding.getRoot();
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


    public void populateData(final ContactInfoModel contactInfoModel) {

        if (contactInfoModel != null) {
            contactInfoViewBinding.getRoot().setVisibility(VISIBLE);
            contactInfoViewBinding.setContactInfo(contactInfoModel);
            ((GradientDrawable)contactInfoViewBinding.initials.getBackground()).setColor(!StringUtils.isNullOrEmptyWithTrim(contactInfoModel.getColor()) ? Color.parseColor(contactInfoModel.getColor()) : splashColor);
        } else {
            contactInfoViewBinding.getRoot().setVisibility(GONE);
            contactInfoViewBinding.email.setText("");
            contactInfoViewBinding.phone1.setText("");
            contactInfoViewBinding.phone2.setText("");
            contactInfoViewBinding.email.setVisibility(GONE);
            contactInfoViewBinding.phone1.setVisibility(GONE);
            contactInfoViewBinding.phone2.setVisibility(GONE);
        }
    }

    public void launchDialer(String number){
        HashMap<String,Object> map = new HashMap<>();
        map.put("phone",number);
        invokeGenericWebViewInterface.handleUserActions(BundleConstants.OPEN_DIALER,map);
    }
    public void launchEmail(String email){
        HashMap<String,Object> map = new HashMap<>();
        map.put("email",email);
        invokeGenericWebViewInterface.handleUserActions(BundleConstants.OPEN_EMAIL,map);
    }
    public void openDetails(ContactInfoModel contactInfoModel){
        HashMap<String,Object> map = new HashMap<>();
        map.put("email",contactInfoModel.getEmail());
        map.put("phone",contactInfoModel.getPhone());
        map.put("name",contactInfoModel.getName());
        map.put("url",contactInfoModel.getContactUrl());
        invokeGenericWebViewInterface.handleUserActions(BundleConstants.VIEW_CONTACT,map);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

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
}
