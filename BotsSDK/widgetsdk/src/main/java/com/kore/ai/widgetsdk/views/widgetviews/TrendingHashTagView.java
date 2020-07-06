package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.adapters.HashTagWidgetAdapter;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.cache.PanelDataLRUCache;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.NewHashTagEvent;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.models.TrendingHahTagPanelNewResponse;
import com.kore.ai.widgetsdk.models.TrendingHashTagModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.net.KaRestAPIHelper;
import com.kore.ai.widgetsdk.net.KaRestBuilder;
import com.kore.ai.widgetsdk.utils.NetworkUtility;
import com.kore.ai.widgetsdk.utils.Utility;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.views.viewutils.LayoutUtils;
import com.kore.ai.widgetsdk.views.viewutils.MeasureUtils;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kore.ai.widgetsdk.utils.AppUtils.getMapObject;

public class TrendingHashTagView extends ViewGroup {

    private UpdateRefreshItem listener;
    private View rootView;
    private RecyclerView recycler_hash_tag;
    public TextView view_more,tv_hashtag_title;
    private HashTagWidgetAdapter hAdapter = null;
    private float dp1;

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
        getHashSuggestions();
    }

    private Widget widget;
    private String name;
    WidgetViewMoreEnum widgetViewMoreEnum;
    public TrendingHashTagView(Context context, UpdateRefreshItem listener, String name,WidgetViewMoreEnum widgetViewMoreEnum) {
        super(context);
        this.listener = listener;
        this.name = name;
        this.widgetViewMoreEnum=widgetViewMoreEnum;
        init();
    }

    public TrendingHashTagView(Context context, AttributeSet attrs, UpdateRefreshItem listener, String name) {
        super(context, attrs);
        this.listener = listener;
        this.name = name;

        init();
    }

    public TrendingHashTagView(Context context, AttributeSet attrs, int defStyleAttr, UpdateRefreshItem listener, String name) {
        super(context, attrs, defStyleAttr);
        this.listener = listener;
        this.name = name;
        init();
    }



    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hashtag_layout, this, true);
        rootView = view.findViewById(R.id.hashtag_root);
        tv_hashtag_title=view.findViewById(R.id.tv_hashtag_title);
        tv_hashtag_title.setText(widget.getTitle());
        recycler_hash_tag = view.findViewById(R.id.recycler_hashtag);
        recycler_hash_tag.setLayoutManager(new LinearLayoutManager(getContext()));
        view_more = view.findViewById(R.id.view_more);
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.inset_10_divider));
//        recycler_hash_tag.addItemDecoration(itemDecorator);
        hAdapter = new HashTagWidgetAdapter((FragmentActivity) getContext(), null);
        hAdapter.setFullView(false);
        hAdapter.setViewMoreEnum(widgetViewMoreEnum);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }

    private void getHashSuggestions() {
     /*   if (!NetworkUtility.isNetworkConnectionAvailable(getContext())) {
            return;
        }*/
        // showProgress("Please Wait", false);
      /*  if(PanelDataLRUCache.getInstance().getEntry(name) !=null){
            afterDataLoad((TrendingHahTagPanelNewResponse) PanelDataLRUCache.getInstance().getEntry(name));
            return;
        }*/
        Map<String,Object> result = getMapObject(widget.getHook().getParams());
        Call<TrendingHahTagPanelNewResponse> hashReq = KaRestBuilder.getKaRestAPI()
                .getTrendingHahTagPanel(Utils.ah(""), widget.getHook().getApi(),result,widget.getHook().getBody());
        KaRestAPIHelper.enqueueWithRetry(hashReq, new Callback<TrendingHahTagPanelNewResponse>() {
            @Override
            public void onResponse(Call<TrendingHahTagPanelNewResponse> call, Response<TrendingHahTagPanelNewResponse> response) {
                if (response.isSuccessful()) {
                    TrendingHahTagPanelNewResponse resp = response.body();
                    PanelDataLRUCache.getInstance().putEntry(name,resp);
                    afterDataLoad(resp);
                } else {
                    // holder.hashtag_root.setVisibility(View.GONE);
                    // showErrorToast(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TrendingHahTagPanelNewResponse> call, Throwable t) {
                //  dismissProgress();
//                rootView.setVisibility(View.GONE);

                String msg;
                Drawable drawable=null;
                if (!NetworkUtility.isNetworkConnectionAvailable(TrendingHashTagView.this.getContext())) {
                    //No Internet Connect
                    msg=getResources().getString(R.string.no_internet_connection);
                    drawable=getResources().getDrawable(R.drawable.no_internet);
                } else {
                    //Oops some thing went wrong
                    msg=getResources().getString(R.string.oops);
                    drawable=getResources().getDrawable(R.drawable.oops_icon);
                }
                hAdapter.setHashTagList(null);
                hAdapter.setMessage(msg,drawable);
                recycler_hash_tag.setAdapter(hAdapter);
                view_more.setVisibility(GONE);
                hAdapter.notifyDataSetChanged();
            }
        });

    }

    private void afterDataLoad(TrendingHahTagPanelNewResponse models){
        ArrayList<TrendingHashTagModel> tags = (ArrayList<TrendingHashTagModel>) models.getElements();
        if (tags != null && tags.size() > 0) {
            rootView.setVisibility(View.VISIBLE);
            hAdapter.setHashTagList(tags);
            view_more.setVisibility(tags.size() > 3&& Utility.isViewMoreVisible(widgetViewMoreEnum) ? View.VISIBLE : View.GONE);
            recycler_hash_tag.setAdapter(hAdapter);
            hAdapter.notifyDataSetChanged();
            view_more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
//                    Intent fullviewIntent = new Intent(getContext(), HashtagFullView.class);
//                    fullviewIntent.putExtra(BundleConstants.HASHATAG_DATA, gson.toJson(tags));
//                    ((Activity)getContext()).startActivityForResult(fullviewIntent,BundleConstants.REQ_CODE_VIEW_HASH_TAGS_FROM_WIDGET);
                    if(listener != null)
                        listener.updateItemToRefresh(-1);

                }
            });
        } else {
            //    holder.hashtag_root.setVisibility(View.GONE);
            hAdapter.setHashTagList(null);
            view_more.setVisibility(View.GONE);
            recycler_hash_tag.setAdapter(hAdapter);
            hAdapter.notifyDataSetChanged();
        }
    }
    public void onEventMainThread(NewHashTagEvent event){
        getHashSuggestions();
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - (2 * dp1)), MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootView, childWidthSpec, wrapSpec);

        totalHeight += rootView.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
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
}
