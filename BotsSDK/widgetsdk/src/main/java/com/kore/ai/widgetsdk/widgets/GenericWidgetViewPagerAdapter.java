package com.kore.ai.widgetsdk.widgets;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.WidgetsModel;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.widgetviews.GenericWidgetView;

/**
 * Created by Ramachandra Pradeep on 28-Mar-19.
 */

public class GenericWidgetViewPagerAdapter extends PagerAdapter {
  //  private String tabTitles[] = {"Shared with you", "Created by you"};
    private VerticalListViewActionHelper listener;
    private Activity mContext;
    private final SparseArray<View> instantiatedFragments = new SparseArray<>();
    private GenericWidgetView itemLayout;
    private WidgetsModel widget;
    private String name;
    boolean isPaaginationRequired;
    WidgetViewMoreEnum widgetViewMoreEnum;
    public GenericWidgetViewPagerAdapter(Activity context,
                                         VerticalListViewActionHelper listener, WidgetsModel widget,
                                         String name, boolean isPaaginationRequired, WidgetViewMoreEnum widgetViewMoreEnum) {
        super();
        this.listener = listener;
        this.mContext =  context;
        this.widget=widget;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        this.name = name;
        this.isPaaginationRequired=isPaaginationRequired;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        itemLayout = new GenericWidgetView(mContext,position,listener,widget,
                name,widget.getAutoRefresh().getInterval()+"",isPaaginationRequired,widgetViewMoreEnum);
        container.addView(itemLayout);
        instantiatedFragments.put(position, itemLayout);
        return itemLayout;
    }


    public GenericWidgetView getGenView()
    {
        return itemLayout;
    }
    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return "";
    }

    public View getRegisteredView(int position) {
        return instantiatedFragments.get(position);
    }

}
