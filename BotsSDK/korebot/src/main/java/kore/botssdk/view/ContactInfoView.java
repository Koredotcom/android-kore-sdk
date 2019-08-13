package kore.botssdk.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.ContactInfoViewBinding;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.ContactInfoModel;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.Email;
import kore.botssdk.models.Phone;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class ContactInfoView extends ViewGroup implements VerticalListViewActionHelper {

    float dp1;
    private int splashColor;
    private ContactInfoViewBinding contactInfoViewBinding;
    private ContactViewRecyclerAdapter myRecyclerViewAdapter;
    private TextView showMore, viewMoreIV;
    private TextView sourceIcon, source;
    private RecyclerView contactListRecyclerView;

//    private ContactViewListBinding contactViewListBinding;

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
        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor()) + BundleConstants.TRANSPERANCY_50_PERCENT);
        contactInfoViewBinding.getRoot().setBackground(shape);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        splashColor = getContext().getResources().getColor(R.color.splash_color);
        contactInfoViewBinding.setViewBase(this);

        showMore = findViewById(R.id.view_more_contact);
        viewMoreIV = findViewById(R.id.viewMoreIV);
        viewMoreIV.setTypeface(ResourcesCompat.getFont(getContext(), R.font.icomoon));
        sourceIcon = findViewById(R.id.sourceIcon);
        source = findViewById(R.id.source);
        sourceIcon.setTypeface(ResourcesCompat.getFont(getContext(), R.font.icomoon));
        contactListRecyclerView = ((RecyclerView)findViewById(R.id.contactListView));
    }

    public void onEvent(ProfileColorUpdateEvent event){
        LayerDrawable shape = (LayerDrawable) getResources().getDrawable(R.drawable.shadow_layer_background);
        GradientDrawable outer = (GradientDrawable) shape.findDrawableByLayerId(R.id.inner);
        outer.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+ BundleConstants.TRANSPERANCY_50_PERCENT);
        contactInfoViewBinding.getRoot().setBackground(shape);
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

            ArrayList<ContactViewListModel> list = new ArrayList<ContactViewListModel>();

            ArrayList<Phone> phones = (ArrayList<Phone>) contactInfoModel.getPhones();
            if(phones != null && phones.size()>0) {
                for (Phone ph : phones) {
                    ContactViewListModel cvlm = new ContactViewListModel();
                    cvlm.setHeader(ph.getType());
                    cvlm.setValue(ph.getValue());
                    cvlm.setImage(getResources().getString(R.string.icon_e91d));
                    cvlm.setPhone(true);
                    list.add(cvlm);
                }
            }


            ArrayList<Email> emails = (ArrayList<Email>) contactInfoModel.getEmails();
            if(emails !=null && emails.size()>0) {
                for (Email email : emails) {
                    ContactViewListModel cvlmE = new ContactViewListModel();
                    cvlmE.setHeader(email.getType());
                    cvlmE.setValue(email.getValue());
                    cvlmE.setImage(getResources().getString(R.string.icon_e915));
                    cvlmE.setEmail(true);
                    list.add(cvlmE);
                }
            }

            if (!StringUtils.isNullOrEmpty(contactInfoModel.getDepartment())) {
                ContactViewListModel cvlmD = new ContactViewListModel();
                cvlmD.setHeader("Department");
                cvlmD.setValue(contactInfoModel.getDepartment());
                list.add(cvlmD);
            }

            if(!StringUtils.isNullOrEmpty(contactInfoModel.getManager())){
                ContactViewListModel cvlmM = new ContactViewListModel();
                cvlmM.setHeader("Manager");
                cvlmM.setValue(contactInfoModel.getManager());
                list.add(cvlmM);
            }

            if(!StringUtils.isNullOrEmpty(contactInfoModel.getEmailId())){
                ContactViewListModel cvlmEMP = new ContactViewListModel();
                cvlmEMP.setHeader("Employee ID");
                cvlmEMP.setValue(contactInfoModel.getEmailId());
                list.add(cvlmEMP);
            }
            if(!StringUtils.isNullOrEmpty(contactInfoModel.getAddress())){
                ContactViewListModel cvlmA = new ContactViewListModel();
                cvlmA.setHeader("Address");
                cvlmA.setValue(contactInfoModel.getAddress());
                cvlmA.setImage(getResources().getString(R.string.icon_e92c));
                cvlmA.setAddress(true);
                list.add(cvlmA);
            }

            sourceIcon.setText(getResources().getText(R.string.icon_e94e));
            source.setText(contactInfoModel.getSource());

            myRecyclerViewAdapter = new ContactViewRecyclerAdapter(getContext());
            myRecyclerViewAdapter.setExpanded(false);
            myRecyclerViewAdapter.setVerticalListViewActionHelper(this);
            myRecyclerViewAdapter.setData(list);

            /*DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
            divider.setDrawable(getResources().getDrawable(R.drawable.contact_list_seperator));
            contactListRecyclerView.addItemDecoration(divider);*/
            contactListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            contactInfoViewBinding.setMyAdapter(myRecyclerViewAdapter);
            ((GradientDrawable)contactInfoViewBinding.initials.getBackground()).setColor(!StringUtils.isNullOrEmptyWithTrim(contactInfoModel.getColor()) ? Color.parseColor(contactInfoModel.getColor()) : splashColor);
        } else {
            contactInfoViewBinding.getRoot().setVisibility(GONE);
            contactInfoViewBinding.setContactInfo(null);
        }
    }

    public void expandOrCollapse(){
        if (myRecyclerViewAdapter != null && !myRecyclerViewAdapter.isExpanded()) {
            myRecyclerViewAdapter.setExpanded(true);
            showMore.setText("View less");
            viewMoreIV.setText(getResources().getText(R.string.icon_e914));
            contactListRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    contactListRecyclerView.scrollToPosition(myRecyclerViewAdapter.getItemCount());
                }
            }, 200);
        }else {
            myRecyclerViewAdapter.setExpanded(false);
            showMore.setText("View more");
            viewMoreIV.setText(getResources().getText(R.string.icon_e961));
        }
    }

    public String getViewMoreLessIcon(){
        if(myRecyclerViewAdapter == null){
            return String.valueOf(getResources().getText(R.string.icon_e961));
        }else {
            if(!myRecyclerViewAdapter.isExpanded()){
                return String.valueOf(getResources().getText(R.string.icon_e961));
            }else{
                return String.valueOf(getResources().getText(R.string.icon_e914));
            }
        }
    }

    public String getSourceIcon(ContactInfoModel model){
        if(model!=null) {
            if (model.getSource() != null && model.getSource().toLowerCase().equals("device")) {
                return String.valueOf(getResources().getText(R.string.icon_e94f));
            } else {
                return String.valueOf(getResources().getText(R.string.icon_e94e));
            }
        }
        return String.valueOf(getResources().getText(R.string.icon_e94e));
    }


    public void launchDialer(String number){
        HashMap<String,Object> map = new HashMap<>();
        map.put("phone",number);
        if(invokeGenericWebViewInterface != null)
            invokeGenericWebViewInterface.handleUserActions(BundleConstants.OPEN_DIALER,map);
    }

    public void launchMap(String address){
        HashMap<String,Object> map = new HashMap<>();
        map.put("address",address);
        if(invokeGenericWebViewInterface != null)
            invokeGenericWebViewInterface.handleUserActions(BundleConstants.VIEW_LOCATION,map);
    }

    public void launchAction(ContactViewListModel model){
        if(model.isPhone()){
            launchDialer(model.getValue());
        }else if(model.isEmail()){
            launchEmail((model.getValue()));
        }else if(model.isAddress()){

        }
    }

    public void launchEmail(String email){
        HashMap<String,Object> map = new HashMap<>();
        map.put("email",email);
        if(invokeGenericWebViewInterface != null)
            invokeGenericWebViewInterface.handleUserActions(BundleConstants.OPEN_EMAIL,map);
    }
    public void openDetails(ContactInfoModel contactInfoModel){
        HashMap<String,Object> map = new HashMap<>();
        map.put("email",contactInfoModel.getEmail());
        map.put("phone",contactInfoModel.getPhone());
        map.put("name",contactInfoModel.getName());
        map.put("url",contactInfoModel.getContactUrl());
        if(invokeGenericWebViewInterface != null)
            invokeGenericWebViewInterface.handleUserActions(BundleConstants.VIEW_CONTACT,map);
    }
    public int getVisibility(ContactInfoModel contactInfoModel){
        if(contactInfoModel != null && !StringUtils.isNullOrEmpty(contactInfoModel.getContactUrl())){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int contactListItemImageVisibillity(ContactViewListModel model){
        if(!StringUtils.isNullOrEmpty(model.getImage())){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
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

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {
        if(showMore!=null)
            showMore.setVisibility(visible? View.VISIBLE: View.GONE);

        if(viewMoreIV!=null)
            viewMoreIV.setVisibility(visible? View.VISIBLE: View.GONE);
    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {
        if(model.isPhone()){
            launchDialer(model.getValue());
        }else if(model.isEmail()){
            launchEmail((model.getValue()));
        }else if(model.isAddress()){
            launchMap(model.getValue());
        }
    }
}
