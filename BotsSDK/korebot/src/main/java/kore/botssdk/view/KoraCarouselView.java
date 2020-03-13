package kore.botssdk.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import kore.botssdk.R;
import kore.botssdk.adapter.KoraMiniTableNewAdapter;
import kore.botssdk.adapter.PreloadLinearLayoutManager;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Shiva Krishna on 2/5/2018.
 */



public class KoraCarouselView extends ViewGroup {
    private Context mContext;
    private float dp1;
    private RecyclerView carousalView;
    // private KoraCarousalAdapter koraCarousalAdapter;
    private KoraMiniTableNewAdapter koraMiniTableAdapter;
//    private String template_type;


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

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


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
        dp1 = DimensionUtil.dp1;
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.kora_carousel_view_new, this, true);


        carousalView = view.findViewById(R.id.carouselRecylerview);

        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(carousalView.getLayoutParams());
        marginLayoutParams.setMargins(0, 10, 0, 10);
        carousalView.setLayoutParams(marginLayoutParams);

        SnapHelper snapHelper = new PagerSnapHelper();
        carousalView.addItemDecoration(new MarginItemDecoration((int) DimensionUtil.dp1 * 8));

        PreloadLinearLayoutManager layoutManager = new PreloadLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) {

            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = (int) (getWidth() * (0.85f));
               // lp.setMargins(0,0,0,0);

                return true;
            }

            @Override
            public boolean canScrollHorizontally() {
                return true;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        carousalView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(carousalView);

//        carousalView.setAddExtraHeight(true);
        // int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);
       // HeightAdjustableViewPager
        //carousalView.setPageMargin(pageMargin);

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxAllowedWidth = parentWidth;
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = 0;//getPaddingTop();
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

        totalHeight += carousalView.getMeasuredHeight();
        /*if(carousalView.getMeasuredHeight() !=0 ){
            totalHeight+=1*dp1;
        }*/
        int parentHeightSpec = MeasureSpec.makeMeasureSpec( totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(childWidthSpec, MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);

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

    /*  public void prepareDataSetAndPopulate(KoraSearchResultsModel koraSearchResultsModel) {

          if (invokeGenericWebViewInterface != null && composeFooterInterface != null) {
              ArrayList<KoraSearchDataSetModel> koraSearchDataSetModels = new ArrayList<>();
              if(koraSearchResultsModel != null) {
                  ArrayList<EmailModel> emails = koraSearchResultsModel.getEmails();
                  ArrayList<KnowledgeDetailModel> knowledgeDetailModels = koraSearchResultsModel.getKnowledge();
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

              carousalView.setOffscreenPageLimit(4);
              //            if (carouselViewpager.getAdapter() == null) {
              koraCarousalAdapter = new KoraCarousalAdapter(koraSearchDataSetModels, activityContext, invokeGenericWebViewInterface, composeFooterInterface);
              carousalView.setAdapter(koraCarousalAdapter);
              koraCarousalAdapter.notifyDataSetChanged();
              carousalView.setSwipeLocked(koraSearchDataSetModels.size() == 1);

          }
      }
  */
    public void populateMiniTable(PayloadInner payloadInner) {
//        this.template_type = template_type;
        if (payloadInner != null && payloadInner.getMiniTableDataModels() != null) {
            koraMiniTableAdapter = new KoraMiniTableNewAdapter(payloadInner.getMiniTableDataModels(), mContext);
            carousalView.setAdapter(koraMiniTableAdapter);
            koraMiniTableAdapter.notifyDataSetChanged();
           invalidate();
        } else {
            carousalView.setAdapter(null);
            koraMiniTableAdapter = null;
        }

        if(false) {
            if (payloadInner != null && payloadInner.getMiniTableDataModels() != null) {
                // koraMiniTableAdapter = new KoraMiniTableAdapter(payloadInner.getMiniTableDataModels(), mContext);
                // carousalView.setAdapter(koraMiniTableAdapter);
                //  koraMiniTableAdapter.notifyDataSetChanged();
                // carousalView.setSwipeLocked(payloadInner.getMiniTableDataModels().size() == 1);
            } else {
                //carousalView.setAdapter(null);
                //                //koraMiniTableAdapter = null;
            }
        }

    }

   private static class MarginItemDecoration extends RecyclerView.ItemDecoration{
        int spaceHeight;
        public MarginItemDecoration(int spaceHeight) {
            this.spaceHeight=spaceHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            final int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == RecyclerView.NO_POSITION) {
                return;
            }
            outRect.bottom=0;
            outRect.left=0;
            outRect.right=spaceHeight;
            outRect.top=0;
        }
    }

}
