package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.KoraCarousalAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.models.KoraSearchResultsModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Shiva Krishna on 2/5/2018.
 */



public class KoraCarouselView extends ViewGroup {
    Context mContext;
    float dp1;
    TextView headerView;
    ViewPager carousalView;
    KoraCarousalAdapter koraCarousalAdapter;

    public Activity getActivityContext() {
        return activityContext;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }

    Activity activityContext;

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterFragment.ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterFragment.ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;


    public KoraCarouselView(Context mContext){

        super(mContext);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context mContext, AttributeSet attributes){
        super(mContext,attributes);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.kora_carousel_view, this, true);
        carousalView = (ViewPager) view.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);

        carousalView.setPageMargin(pageMargin);
        carousalView.setOffscreenPageLimit(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();

        int childWidthSpec;
        int childHeightSpec;
        int contentWidth = 0;
        int childHeight;

        /*
         * For Carousel ViewPager Layout
         */


        childWidthSpec = MeasureSpec.makeMeasureSpec(maxAllowedWidth, MeasureSpec.AT_MOST);
       // childHeightSpec = MeasureSpec.makeMeasureSpec( childHeight , MeasureSpec.EXACTLY);
        MeasureUtils.measure(carousalView, childWidthSpec, wrapSpec);

        totalHeight += carousalView.getMeasuredHeight()+getPaddingBottom()+getPaddingTop();
        if(carousalView.getMeasuredHeight() !=0 ){
            totalHeight+=10*dp1;
        }
        int parentHeightSpec = MeasureSpec.makeMeasureSpec( totalHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, parentHeightSpec);
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

    public void prepareDatasetAndPopulate(KoraSearchResultsModel koraSearchResultsModel) {

        if (invokeGenericWebViewInterface != null && composeFooterInterface != null) {
            ArrayList<KoraSearchDataSetModel> koraSearchDataSetModels = new ArrayList<>();
            if(koraSearchResultsModel != null) {
                ArrayList<EmailModel> emails = koraSearchResultsModel.getEmails();
                ArrayList<KnowledgeDetailModel> knowledgeDetailModels = koraSearchResultsModel.getKnowledges();
                if (emails != null && emails.size() > 0) {
                    for (EmailModel emailModel : emails) {
                        KoraSearchDataSetModel koraSearchDatasetModel = new KoraSearchDataSetModel();
                        koraSearchDatasetModel.setType("email");
                        koraSearchDatasetModel.setPayload(emailModel);
                        koraSearchDatasetModel.setViewType(KoraSearchDataSetModel.ViewType.EMAIL_VIEW);
                        koraSearchDataSetModels.add(koraSearchDatasetModel);
                    }

                }
                if (knowledgeDetailModels != null && knowledgeDetailModels.size() > 0) {
                    for (KnowledgeDetailModel knowledgeDetailModel : knowledgeDetailModels) {
                        KoraSearchDataSetModel koraSearchDatasetModel = new KoraSearchDataSetModel();
                        koraSearchDatasetModel.setType("knowledge");
                        koraSearchDatasetModel.setPayload(knowledgeDetailModel);
                        koraSearchDatasetModel.setViewType(KoraSearchDataSetModel.ViewType.KNOWLEDGE_VIEW);
                        koraSearchDataSetModels.add(koraSearchDatasetModel);
                    }

                }
            }

            //            if (carouselViewpager.getAdapter() == null) {
            koraCarousalAdapter = new KoraCarousalAdapter(koraSearchDataSetModels, activityContext, invokeGenericWebViewInterface, composeFooterInterface);
            carousalView.setAdapter(koraCarousalAdapter);
            koraCarousalAdapter.notifyDataSetChanged();

        }
    }




}
