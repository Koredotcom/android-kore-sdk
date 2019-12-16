package kore.botssdk.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotMiniTableModel;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.tableview.BotMiniTableView;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */

public class KoraMiniTableAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<BotMiniTableModel> data;
    LayoutInflater ownLayoutInflater;
    float pageWidth = 1.0f;
    private String template_type;
    public KoraMiniTableAdapter(ArrayList<BotMiniTableModel> miniTableModels, Context mContext, String template_type){
        super();
        this.data = miniTableModels;
        this.mContext = mContext;
        ownLayoutInflater = LayoutInflater.from(mContext);
        TypedValue typedValue = new TypedValue();
        mContext.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
        this.template_type = template_type;
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
        return view == (object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        BotMiniTableView mTable = new BotMiniTableView(mContext);
        //Set layoutParams
        String[] alignment = mTable.addHeaderAdapter(data.get(position).getPrimary());
        mTable.addDataAdapter(template_type, data.get(position).getAdditional(),alignment);
//                tableContainer.addView(mTable, index);
        container.addView(mTable);
        KaFontUtils.applyCustomFont(mContext,mTable);
        /*final View carouselItemLayout;
        if(data.get(position).getViewType() == KoraSearchDataSetModel.ViewType.EMAIL_VIEW) {
            carouselItemLayout = ownLayoutInflater.inflate(R.layout.kora_email_view, container, false);
        }else {
            carouselItemLayout = ownLayoutInflater.inflate(R.layout.kora_knowledge_view, container, false);
        }
        KaFontUtils.applyCustomFont(mContext,carouselItemLayout);
        KoraCarousalViewHelper.initializeViewHolder(carouselItemLayout,data.get(position).getViewType());
        KoraCarousalViewHelper.populateStuffs((KoraCarousalViewHelper.KoraCarousalViewHolder) carouselItemLayout.getTag(), composeFooterInterface, genericWebViewInterface, data.get(position), mContext);*/

        return mTable;

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
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
        container.removeView((BotMiniTableView) object);
    }
}