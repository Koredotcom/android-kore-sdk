package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.KoraFilesCarousalAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Ramachandra Pradeep on 09-Aug-18.
 */

public class KoraFilesCarousalView extends ViewGroup{
    private Context mContext;
    private float dp1;
    private HeightAdjustableViewPager carousalView;
    private KoraFilesCarousalAdapter koraCarousalAdapter;
    private Activity activityContext;

    public Activity getActivityContext() {
        return activityContext;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }

    public KoraFilesCarousalView(Context mContext){

        super(mContext);
        this.mContext = mContext;
        init();
    }
    public KoraFilesCarousalView(Context mContext, AttributeSet attributes){
        super(mContext,attributes);
        this.mContext = mContext;
        init();
    }
    public KoraFilesCarousalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.kora_files_carousel_view, this, true);
        carousalView = (HeightAdjustableViewPager) view.findViewById(R.id.fcarouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);

        carousalView.setPageMargin(pageMargin);

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

        totalHeight += carousalView.getMeasuredHeight()+getPaddingBottom();
        /*if(carousalView.getMeasuredHeight() !=0 ){
            totalHeight+=10*dp1;
        }*/
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

    public void prepareDatasetAndPopulate(ArrayList<KaFileLookupModel> koraFilesModel) {

            carousalView.setOffscreenPageLimit(4);
            //            if (carouselViewpager.getAdapter() == null) {
            koraCarousalAdapter = new KoraFilesCarousalAdapter(koraFilesModel, activityContext);
            carousalView.setAdapter(koraCarousalAdapter);
            koraCarousalAdapter.notifyDataSetChanged();
            if(koraFilesModel!=null)
                carousalView.setSwipeLocked(koraFilesModel.size() == 1);

        }

}
