package com.kore.ai.widgetsdk.overscroll;



import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.kore.ai.widgetsdk.overscroll.adapters.AbsListViewOverScrollDecorAdapter;
import com.kore.ai.widgetsdk.overscroll.adapters.HorizontalScrollViewOverScrollDecorAdapter;
import com.kore.ai.widgetsdk.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import com.kore.ai.widgetsdk.overscroll.adapters.ScrollViewOverScrollDecorAdapter;
import com.kore.ai.widgetsdk.overscroll.adapters.StaticOverScrollDecorAdapter;
import com.kore.ai.widgetsdk.overscroll.adapters.ViewPagerOverScrollDecorAdapter;


/**
 * @author amit
 */
public class OverScrollDecoratorHelper {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;


    public static IOverScrollDecor setUpOverScroll(RecyclerView recyclerView, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            default:
                throw new IllegalArgumentException("orientation");
        }
    }

    public static IOverScrollDecor setUpOverScroll(ListView listView) {
        return new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));
    }

    public static IOverScrollDecor setUpOverScroll(GridView gridView) {
        return new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(gridView));
    }

    public static IOverScrollDecor setUpOverScroll(ScrollView scrollView) {
        return new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scrollView));
    }

    public static IOverScrollDecor setUpOverScroll(HorizontalScrollView scrollView) {
        return new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(scrollView));
    }

    /**
     * Set up the over-scroll over a generic view, assumed to always be over-scroll ready (e.g.
     * a plain text field, image view).
     *
     * @param view The view.
     * @param orientation One of {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     *
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
    public static IOverScrollDecor setUpStaticOverScroll(View view, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            default:
                throw new IllegalArgumentException("orientation");
        }
    }

    public static IOverScrollDecor setUpOverScroll(ViewPager viewPager) {
        return new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
    }

}
