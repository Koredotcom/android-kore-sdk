package kore.botssdk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

import kore.botssdk.R;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCarouselModel;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.models.KoraSearchResultsModel;
import kore.botssdk.view.viewUtils.CarouselItemViewHelper;
import kore.botssdk.view.viewUtils.KoraCarousalViewHelper;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */

public class KoraCarousalAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<KoraSearchDataSetModel> data;
    InvokeGenericWebViewInterface genericWebViewInterface;
    ComposeFooterFragment.ComposeFooterInterface composeFooterInterface;
    LayoutInflater ownLayoutInflater;
    float pageWidth = 1.0f;
    ArrayList<Integer> heights = new ArrayList<>();
    ArrayList<View> views = new ArrayList<>();
    public KoraCarousalAdapter(ArrayList<KoraSearchDataSetModel> koraSearchDataSetModels, Context mContext, InvokeGenericWebViewInterface webViewInterface, ComposeFooterFragment.ComposeFooterInterface composeFooterInterface){
        super();
        this.data = koraSearchDataSetModels;
        this.mContext = mContext;
        this.composeFooterInterface = composeFooterInterface;
        this.genericWebViewInterface = webViewInterface;
        ownLayoutInflater = LayoutInflater.from(mContext);
        TypedValue typedValue = new TypedValue();
        mContext.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
    }
    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View carouselItemLayout;
        carouselItemLayout = ownLayoutInflater.inflate(R.layout.kora_carousal_view_item, container, false);


        KoraCarousalViewHelper.initializeViewHolder(carouselItemLayout);
        KoraCarousalViewHelper.populateStuffs((KoraCarousalViewHelper.KoraCarousalViewHolder) carouselItemLayout.getTag(), composeFooterInterface, genericWebViewInterface, data.get(position), mContext);
        container.addView(carouselItemLayout);
        ViewTreeObserver vto = carouselItemLayout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                KoraCarousalViewHelper.KoraCarousalViewHolder holder = (KoraCarousalViewHelper.KoraCarousalViewHolder) carouselItemLayout.getTag();
                int height = 0;
                if(data.get(position).getViewType() == KoraSearchDataSetModel.ViewType.KNOWLEDGE_VIEW){
                    height = holder.knowledgeView.getMeasuredHeight();
        /*            ViewGroup.LayoutParams layoutParams = holder.knowledgeView.getLayoutParams();
                    layoutParams.height = height;
                    holder.knowledgeView.setLayoutParams(layoutParams);*/
                }else if(data.get(position).getViewType() == KoraSearchDataSetModel.ViewType.EMAIL_VIEW){
                    height = holder.emailView.getMeasuredHeight();
         /*           ViewGroup.LayoutParams layoutParams = holder.emailView.getLayoutParams();
                    layoutParams.height = height;
                    holder.emailView.setLayoutParams(layoutParams);*/
                }else{
                    height = holder.showMoreView.getMeasuredHeight();
                }

                heights.add((int)(250 * dp1));
                ViewGroup.LayoutParams layoutParams = holder.viewRoot.getLayoutParams();
                layoutParams.height = (int)(250 * dp1);
                holder.viewRoot.setLayoutParams(layoutParams);

                  views.add(holder.viewRoot);
                return true;
            }
        });
        return carouselItemLayout;

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
         applyParams();
    }

    private void applyParams() {
        int maxHeight = getMaxChildHeight();
        for(View view : views) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = maxHeight;
            view.setLayoutParams(layoutParams);
        }
        Log.d("called","view pager 1");
    }

    public int getMaxChildHeight(){
        if(heights.size()>0) {
            return Collections.max(heights);
        }else{
            return 0;
        }
    }

    public void setBotCarouselModels(ArrayList<KoraSearchDataSetModel> data) {
        this.data = data;
    }

    @Override
    public float getPageWidth(int position) {
        if (getCount() == 0) {
            return super.getPageWidth(position);
        } else {
            return pageWidth;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
