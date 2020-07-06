package com.kore.ai.widgetsdk.views.viewutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.utils.KaFontUtils;
import com.kore.ai.widgetsdk.view.CircularProfileView;

import java.util.HashMap;
import java.util.Locale;

public class KaViewPagerSlidingTabStrip extends HorizontalScrollView {

    private boolean canShowUnderLine = true;
    private int customTabPosition = -1;
    private TabClickListener tabClickListener;
    public void showUnderLine(boolean show) {
        canShowUnderLine = show;
    }
    public void setTabClickListener(TabClickListener tabClickListener){
        this.tabClickListener = tabClickListener;
    }

    public int getCustomTabPosition() {
        return customTabPosition;
    }

    public void setCustomTabPosition(int customTabPosition) {
        this.customTabPosition = customTabPosition;
    }

    public interface PageTabProviderTextIcon {
        int getPageIconResId(int position);
        int getCurrentSelectedPosition();

    }


    public interface SearchResultPageTabProvider {
        int SHOW_COUNT_AT_BOTTOM = 1;
        int SHOW_PROGRESS_AT_BOTTOM = 2;
        int SHOW_NOTHING_AT_BOTTOM = 3;
        int whatToShowAtBottom(int position);
        int getBottomCountForPosition(int position);
        int getCurrentSelectedPosition();
    }

    public interface PageTabProviderWithUnreadIndicator {
        int getUnreadCountForPosition(int position);
        int getCurrentSelectedPosition();
    }

    public interface TabClickListener{
        void onTabSelected(int position);
    }
    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    // tags
    /*private final String TAG_PAGE_TITLE_TEXTVIEW = "tagPageTitleTextView";
    private final String TAG_PAGE_COUNT_TEXTVIEW = "tagPageCountTextView";
    private final String TAG_PAGE_UNREAD_INDICATOR_IMAGEVIEW = "tagUnreadIndicatorImageView";
    private final String TAG_PAGE_PROGRESS_VIEW = "tagPageProgressView";
    private final String TAG_PAGE_CPV = "tagPageCPV";*/

    private final int TAG_PAGE_TITLE_TEXTVIEW_ID = 1000;
    private final int TAG_PAGE_COUNT_TEXTVIEW_ID = 1001;
    private final int TAG_PAGE_UNREAD_INDICATOR_IMAGEVIEW_ID = 1002;
    private final int TAG_PAGE_PROGRESS_VIEW_ID = 1003;
    private final int TAG_PAGE_CPV_ID = 1004;

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;
    public DelegatoryOnPageChangeListener delegatoryOnPageChangeListener;
    public DelegatoryOnClickListenersMisc delegatoryOnClickListenersMisc;

    private HashMap<Integer, View> tabViewCache;

    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();


    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;
    private int tabTextHighlightedColor = 0xFF000000;
    private int tabTextDefaultColor = 0xFF000000;
    private Typeface tabTextFontStyle;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 4;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 12, tabTopPadding, tabBottomPadding;
    private int dividerWidth = 1;
    private float dp1 = 0;

    private int tabTextSize = 13;
    private int tabTextColor = 0xFF666666;
    private int tabTypefaceStyle = Typeface.BOLD;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private Locale locale;

    public KaViewPagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public KaViewPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KaViewPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);
       // bringToFront();
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
      //  tabsContainer.bringToFront();
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.KaPagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.KaPagerSlidingTabStrip_kaIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.KaPagerSlidingTabStrip_kaUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.KaPagerSlidingTabStrip_kaDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaTabPaddingLeftRight, tabPadding);
        tabTopPadding = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaTabPaddingTop, tabTopPadding);
        tabBottomPadding = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaTabPaddingBottom, tabBottomPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.KaPagerSlidingTabStrip_kaTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.KaPagerSlidingTabStrip_kaShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.KaPagerSlidingTabStrip_kaTextAllCaps, textAllCaps);
        tabTextSize = a.getDimensionPixelSize(R.styleable.KaPagerSlidingTabStrip_kaTabTextSize, tabTextSize);
        tabTextHighlightedColor = a.getColor(R.styleable.KaPagerSlidingTabStrip_kaTabTextHighlightedColor, tabTextHighlightedColor);
        tabTextDefaultColor = a.getColor(R.styleable.KaPagerSlidingTabStrip_kaTabTextDefaultColor, tabTextDefaultColor);
        tabTextFontStyle = KaFontUtils.resolveTypeface(a.getInt(R.styleable.KaPagerSlidingTabStrip_kaTabTextFontStyle, 6), getContext());

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        if (!isInEditMode()) {
            dp1 = AppControl.getInstance().getDimensionUtil().dp1;

            //Getting default selectable background from default theme resources
            TypedValue backgroundResValue = new TypedValue();
            boolean isAttrResolved = getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, backgroundResValue, true);
            if(isAttrResolved) {
                tabBackgroundResId = backgroundResValue.resourceId;
            }
        }

    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
    }

    public void invalidateTabViews(){
        pager.setOnPageChangeListener(pageListener);
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        tabViewCache = new HashMap<>(tabCount);
        for (int i = 0; i < tabCount; i++) {
            if(pager.getAdapter() instanceof SearchResultPageTabProvider) {
                addSearchPageTab(i);
            } else if (pager.getAdapter() instanceof PageTabProviderWithUnreadIndicator) {
                addPageTabWithUnreadIndicator(i);
            } else if(pager.getAdapter() instanceof PageTabProviderTextIcon){
                addPageTabTextIcon(i);
            }else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void addPageTabTextIcon(int position){
        LinearLayout tabLayoutContainer = new LinearLayout(getContext());
        tabLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        tabLayoutContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        //tabLayoutContainer.setPadding(0, (int) (5 * dp1), 0, (int) (7 * dp1));
        CircularProfileView circularProfileView = new CircularProfileView(getContext());
        circularProfileView.setDimens((int) (25 * dp1), (int) (28 * dp1));
        //circularProfileView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        circularProfileView.setPadding((int) (0 * dp1), (int) (0 * dp1), (int) (0 * dp1), (int) (3 * dp1));
        circularProfileView.setId(TAG_PAGE_CPV_ID);
        tabLayoutContainer.addView(circularProfileView);

        TextView tabTitle = new TextView(getContext());
        tabTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tabTitle.setSingleLine();
        tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        tabTitle.setPadding(0, 0, 0, 0);
        tabTitle.setId(TAG_PAGE_TITLE_TEXTVIEW_ID);
        tabTitle.setTag(KaFontUtils.ROBOTO_MEDIUM);
        tabLayoutContainer.addView(tabTitle);

        tabViewCache.put(position, tabLayoutContainer);

        addTab(position, tabLayoutContainer);
    }

//    public void notifyDataSetChanged(){
//        if(CollectionUtils.isNullOrEmpty(tabViewCache)){
//            invalidateTabViews();
//        } else {
//            tabCount = pager.getAdapter().getCount();
//            for (int i=0; i<tabCount; i++){
//                notifyDataSetChanged(i);
//            }
//        }
//    }

    public void notifyDataSetChanged(int position){
        if(pager.getAdapter() instanceof SearchResultPageTabProvider) {
            updateSearchResultPageTab(position);
        } else if (pager.getAdapter() instanceof PageTabProviderWithUnreadIndicator) {
            updatePageTabWithUnreadIndicator(position);
        } else if (pager.getAdapter() instanceof PageTabProviderTextIcon) {
            updatePageTabTextIcon(position);
        } else {
            updateTextTab(position);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    private void updateTextTab(int position){
        TextView tv = (TextView) tabViewCache.get(position);
        tv.setText(pager.getAdapter().getPageTitle(position).toString());
        boolean focused = pager.getCurrentItem() == position;
        tv.setTextColor(focused ? tabTextHighlightedColor : tabTextDefaultColor);
    }

    private void updatePageTabTextIcon(int position){
        PageTabProviderTextIcon tabProvider = (PageTabProviderTextIcon) pager.getAdapter();
        LinearLayout tabContainer = (LinearLayout) tabViewCache.get(position);
        CircularProfileView circularProfileView = (CircularProfileView) tabContainer.findViewById(TAG_PAGE_CPV_ID);
      //  circularProfileView.setDimens((int) (25 * dp1), (int) (25 * dp1));
        TextView titleTV = (TextView) tabContainer.findViewById(TAG_PAGE_TITLE_TEXTVIEW_ID);
        boolean focused = tabProvider.getCurrentSelectedPosition() == position;
         if(focused) {
            titleTV.setTextColor(tabTextHighlightedColor);
        } else {
            titleTV.setTextColor(tabTextDefaultColor);
        }
        titleTV.setText(pager.getAdapter().getPageTitle(position));
        circularProfileView.setImageResource(tabProvider.getPageIconResId(position));
    }

    private void updateSearchResultPageTab(int position){
        SearchResultPageTabProvider tabProvider = (SearchResultPageTabProvider) pager.getAdapter();
        LinearLayout tabContainer = (LinearLayout) tabViewCache.get(position);
        TextView titleTextView = (TextView) tabContainer.findViewById(TAG_PAGE_TITLE_TEXTVIEW_ID);
        TextView countTextView = (TextView) tabContainer.findViewById(TAG_PAGE_COUNT_TEXTVIEW_ID);
        View progressView = tabContainer.findViewById(TAG_PAGE_PROGRESS_VIEW_ID);
        boolean focused = tabProvider.getCurrentSelectedPosition() == position;
        if (focused) {
            titleTextView.setTextColor(tabTextHighlightedColor);
        } else {
            titleTextView.setTextColor(tabTextDefaultColor);
        }
        titleTextView.setText(pager.getAdapter().getPageTitle(position));
        titleTextView.setVisibility(VISIBLE);

        switch (tabProvider.whatToShowAtBottom(position)){
            case SearchResultPageTabProvider.SHOW_COUNT_AT_BOTTOM:
                countTextView.setText("("+tabProvider.getBottomCountForPosition(position)+")");
                countTextView.setVisibility(VISIBLE);
                progressView.setVisibility(GONE);
                break;
            case SearchResultPageTabProvider.SHOW_NOTHING_AT_BOTTOM:
                countTextView.setVisibility(INVISIBLE);
                progressView.setVisibility(GONE);
                break;
            case SearchResultPageTabProvider.SHOW_PROGRESS_AT_BOTTOM:
                countTextView.setVisibility(GONE);
                progressView.setVisibility(VISIBLE);
                break;
        }
    }


    private void updatePageTabWithUnreadIndicator(int position){
        PageTabProviderWithUnreadIndicator tabProvider = (PageTabProviderWithUnreadIndicator) pager.getAdapter();
        LinearLayout tabContainer = (LinearLayout) tabViewCache.get(position);
        TextView titleTextView = (TextView) tabContainer.findViewById(TAG_PAGE_TITLE_TEXTVIEW_ID);
        ImageView unreadIndicator = (ImageView) tabContainer.findViewById(TAG_PAGE_UNREAD_INDICATOR_IMAGEVIEW_ID);
        boolean focused = tabProvider.getCurrentSelectedPosition() == position;
        if (focused) {
            titleTextView.setTextColor(tabTextHighlightedColor);
        } else {
            titleTextView.setTextColor(tabTextDefaultColor);
        }
        int unreadCount = tabProvider.getUnreadCountForPosition(position);
        if(unreadCount>0 && !focused){
            unreadIndicator.setVisibility(VISIBLE);
        } else {
            unreadIndicator.setVisibility(INVISIBLE);
        }
        titleTextView.setText(pager.getAdapter().getPageTitle(position));
        titleTextView.setVisibility(VISIBLE);
    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setTextColor(tabTextDefaultColor);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
        tab.setTypeface(tabTextFontStyle, tabTypefaceStyle);

        tabViewCache.put(position, tab);

        addTab(position, tab);
    }

    public void setTabTextFontStyle(Typeface tabTextFontStyle) {
        this.tabTextFontStyle = tabTextFontStyle;
    }

    private void addSearchPageTab(int position) {

        LinearLayout tabLayoutContainer = new LinearLayout(getContext());
        tabLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        tabLayoutContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        TextView tabTitleTV = new TextView(getContext());
        tabTitleTV.setSingleLine();
        tabTitleTV.setTextColor(tabTextColor);
        tabTitleTV.setId(TAG_PAGE_TITLE_TEXTVIEW_ID);
        tabTitleTV.setTypeface(tabTextFontStyle, tabTypefaceStyle);
        tabTitleTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tabLayoutContainer.addView(tabTitleTV);

        TextView tabCountTV = new TextView(getContext());
        tabCountTV.setSingleLine();
        tabCountTV.setTextColor(tabTextColor);
        tabCountTV.setId(TAG_PAGE_COUNT_TEXTVIEW_ID);
        tabCountTV.setPadding(0, 0, 0, (int) (dp1 * 5));
        tabCountTV.setTextSize(10);

        tabCountTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tabCountTV.setVisibility(GONE);

        tabLayoutContainer.addView(tabCountTV);


        ProgressBar progressView = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
        progressView.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.bubbles_sending_circular_progress_bar));
        progressView.setId(TAG_PAGE_PROGRESS_VIEW_ID);
        progressView.setPadding(0, 0, 0, (int) (dp1 * 5));

        //Set Layout Params
        progressView.setIndeterminate(true);
        progressView.setInterpolator(accelerateDecelerateInterpolator);
        progressView.setLayoutParams(new LinearLayout.LayoutParams((int) (20 * dp1), (int) (20 * dp1)));
        progressView.setVisibility(GONE);

        tabLayoutContainer.addView(progressView);

        tabViewCache.put(position, tabLayoutContainer);

        addTab(position, tabLayoutContainer);
    }



    private void addPageTabWithUnreadIndicator(int position) {
        LinearLayout tabLayoutContainer = new LinearLayout(getContext());
        tabLayoutContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tabLayoutContainer.setGravity(Gravity.CENTER);

        ImageView unreadIndicator = new ImageView(getContext());
        unreadIndicator.setId(TAG_PAGE_UNREAD_INDICATOR_IMAGEVIEW_ID);
        unreadIndicator.setImageResource(R.drawable.inbox_unread_drawable_blue);
        unreadIndicator.setPadding(0, 0, (int) dp1 * 4, 0);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            unreadIndicator.setPaddingRelative(0, 0, (int) dp1 * 4, 0);
        }

        RelativeLayout.LayoutParams  imageViewParams = new RelativeLayout.LayoutParams((int)dp1*10, (int)dp1*6);
        unreadIndicator.setLayoutParams(imageViewParams);


        tabLayoutContainer.addView(unreadIndicator);

        TextView tabTitleTV = new TextView(getContext());
        tabTitleTV.setSingleLine();
        tabTitleTV.setTextColor(tabTextColor);
        tabTitleTV.setId(TAG_PAGE_TITLE_TEXTVIEW_ID);
        tabTitleTV.setTypeface(tabTextFontStyle, tabTypefaceStyle);

        RelativeLayout.LayoutParams  textViewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tabTitleTV.setLayoutParams(textViewParams);

        tabLayoutContainer.addView(tabTitleTV);

        tabViewCache.put(position, tabLayoutContainer);

        addTab(position, tabLayoutContainer);
    }

    private void addTab(final int position, View tab) {
        tab.setVisibility(position == customTabPosition ? INVISIBLE :VISIBLE);
        tab.setFocusable(true);
        tab.setTag(position);
        tab.setOnClickListener(onClickListener);
        tab.setPadding(tabPadding, tabTopPadding, tabPadding, tabBottomPadding);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        KaFontUtils.applyCustomFont(getContext(), tab);
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View tab) {
            if (delegatoryOnClickListenersMisc != null) {
                delegatoryOnClickListenersMisc.doScrollToBottom();
            }

            int position = (int) tab.getTag();
            if(tabClickListener != null && position == customTabPosition){
                tabClickListener.onTabSelected(position);
            }else {
                pager.setCurrentItem(position);
            }
        }
    };

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
                tab.setTypeface(tabTextFontStyle, tabTypefaceStyle);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            } else if (v instanceof LinearLayout) {

                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    View view = ((LinearLayout) v).getChildAt(i);

                    if (v instanceof TextView) {

                        TextView tab = (TextView) v;
                        tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
                        tab.setTypeface(tabTextFontStyle, tabTypefaceStyle);

                        // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                        // pre-ICS-build
                        if (textAllCaps) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                tab.setAllCaps(true);
                            } else {
                                tab.setText(tab.getText().toString().toUpperCase(locale));
                            }
                        }
                    }

                }

            }
        }

    }
    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }
        if(canShowUnderLine)
        {
            final int height = getHeight();

            // draw indicator line

            rectPaint.setColor(indicatorColor);

            // default: line below current tab
            View currentTab = tabsContainer.getChildAt(currentPosition);
            float lineLeft = currentTab.getLeft();
            float lineRight = currentTab.getRight();

            // if there is an offset, start interpolating left and right coordinates between current and next tab
            if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

                View nextTab = tabsContainer.getChildAt(currentPosition + 1);
                final float nextTabLeft = nextTab.getLeft();
                final float nextTabRight = nextTab.getRight();

                lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
                lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
            }

            canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

            // draw underline

            rectPaint.setColor(underlineColor);
            canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

            // draw divider

            dividerPaint.setColor(dividerColor);
            for (int i = 0; i < tabCount - 1; i++) {
                View tab = tabsContainer.getChildAt(i);
                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
            }

        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (delegatoryOnPageChangeListener != null) {
                delegatoryOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }

            if (delegatoryOnPageChangeListener != null) {
                delegatoryOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }

            if (delegatoryOnPageChangeListener != null) {
                delegatoryOnPageChangeListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }


    public void setTypeface(Typeface typeface) {
        this.tabTextFontStyle = typeface;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public DelegatoryOnPageChangeListener getDelegatoryOnPageChangeListener() {
        return delegatoryOnPageChangeListener;
    }

    public void setDelegatoryOnPageChangeListener(DelegatoryOnPageChangeListener delegatoryOnPageChangeListener) {
        this.delegatoryOnPageChangeListener = delegatoryOnPageChangeListener;
    }

    public DelegatoryOnClickListenersMisc getDelegatoryOnClickListenersMisc() {
        return delegatoryOnClickListenersMisc;
    }

    public void setDelegatoryOnClickListenersMisc(DelegatoryOnClickListenersMisc delegatoryOnClickListenersMisc) {
        this.delegatoryOnClickListenersMisc = delegatoryOnClickListenersMisc;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    public interface DelegatoryOnPageChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageScrollStateChanged(int state);

        void onPageSelected(int position);
    }

    public interface DelegatoryOnClickListenersMisc {

        void doScrollToBottom();

    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}