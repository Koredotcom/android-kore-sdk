/*
package kore.botssdk.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.KoraCarousalViewHelper;

*/
/**
 * Created by Ramachandra Pradeep on 09-Aug-18.
 *//*


public class KoraFilesCarousalAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<KaFileLookupModel> dataList;
    private LayoutInflater ownLayoutInflater;
    private float pageWidth = 1.0f;

    public KoraFilesCarousalAdapter(ArrayList<KaFileLookupModel> dataList, Context mContext){
        super();
        this.dataList = dataList;
        this.mContext = mContext;
        ownLayoutInflater = LayoutInflater.from(mContext);
        TypedValue typedValue = new TypedValue();
        mContext.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        } else {
            return dataList.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View carouselItemLayout;
        carouselItemLayout = ownLayoutInflater.inflate(R.layout.kora_file_lookup_view, container, false);
        KaFontUtils.applyCustomFont(mContext,carouselItemLayout);
        KoraCarousalViewHelper.initializeFileLookupViewHolder(carouselItemLayout);
        KoraCarousalViewHelper.populateFileLookUpStuffs((KoraCarousalViewHelper.KoraFilesCarousalViewHolder) carouselItemLayout.getTag(),dataList.get(position), mContext);
        container.addView(carouselItemLayout);
        return carouselItemLayout;

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        // applyParams();
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
        container.removeView((CardView) object);
    }

}
*/
