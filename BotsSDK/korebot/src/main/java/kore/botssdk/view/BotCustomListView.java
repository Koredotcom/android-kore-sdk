package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.adapter.BotListCustomAdapter;
import kore.botssdk.adapter.BotListCustomAdapterNew;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BotCustomListModel;
import kore.botssdk.models.ButtonTemplate;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/**
 * Created by Anil Kumar on 12/8/2016.
 */
public class BotCustomListView extends ViewGroup {

    String LOG_TAG = BotCustomListView.class.getSimpleName();

    private ListView botList;
    private Button btn;
    private BotListCustomAdapter bla;
    private BotListCustomAdapterNew blaNew;

    private boolean isNewList;
    int dp1;

    public BotCustomListView(Context context) {
        super(context);
        init();
    }

    public BotCustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotCustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_list, this, true);
        botList = (ListView) inflatedView.findViewById(R.id.botCustomListView);
        botList.setOnItemClickListener(botOptionsClickListener);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }

    public void populateBotListView(ArrayList<ButtonTemplate> buttons) {
        setNewList(false);
        bla = new BotListCustomAdapter(getContext());
        bla.setMoreSelectionListener(botMoreOptionsClickListener);
        bla.setOptionsList(new ArrayList<ButtonTemplate>());
        botList.setAdapter(bla);

        bla.setOptionsList(buttons);
        bla.notifyDataSetChanged();
        requestLayout();
    }


    public void populateBotListViewNew(ArrayList<BotCustomListModel> botCustList) {
        setNewList(true);
        blaNew = new BotListCustomAdapterNew(getContext());
        blaNew.setMoreSelectionListener(botMoreOptionsClickListenerNew);
        blaNew.setOptionsList(new ArrayList<BotCustomListModel>());
        botList.setAdapter(blaNew);


        blaNew.setOptionsList(botCustList);
        blaNew.notifyDataSetChanged();
        requestLayout();
    }

    public boolean isNewList() {
        return isNewList;
    }

    public void setNewList(boolean newList) {
        isNewList = newList;
    }

    private AdapterView.OnItemClickListener botOptionsClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (getContext() instanceof BotChatActivity && !isNewList()) {
                ((BotChatActivity) getContext()).onQuickReplyItemClicked(bla.getItem(position).getPayload());
            }
        }
    };

    BotListCustomAdapter.MoreSelectionListener botMoreOptionsClickListener = new BotListCustomAdapter.MoreSelectionListener() {
        @Override
        public void onMoreSelected() {
            bla.notifyDataSetChanged();
            requestLayout();

        }
    };

    BotListCustomAdapterNew.MoreSelectionListener botMoreOptionsClickListenerNew = new BotListCustomAdapterNew.MoreSelectionListener() {
        @Override
        public void onMoreSelected() {
            blaNew.notifyDataSetChanged();
            requestLayout();

        }
    };

    private int measureRequiredHeight(BotListCustomAdapter rootView) {
        int cumulativeHeight = rootView.getCount() * (46 * dp1) + 5 * dp1;
        Log.d(LOG_TAG, cumulativeHeight + "");
        return cumulativeHeight;
    }

    private int measureRequiredHeightNew(BotListCustomAdapterNew rootView) {
        int cumulativeHeight = rootView.getCount() == 4 ? (((rootView.getCount() - 1) * (100 * dp1))) : ((rootView.getCount()) * (87 * dp1));//(rootView.getCount() * (85  * dp1)) + 5 * dp1;
        Log.d(LOG_TAG, cumulativeHeight + "");
        return cumulativeHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int _height = 0;
        if (isNewList()) {
            _height = measureRequiredHeightNew(blaNew);
        } else {
            _height = measureRequiredHeight(bla);
        }

        int heightSpec = View.MeasureSpec.makeMeasureSpec(_height, View.MeasureSpec.EXACTLY);

        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthMeasureSpec, View.MeasureSpec.EXACTLY);
        View lv = getChildAt(0);
        MeasureUtils.measure(lv, widthMeasureSpec, heightSpec);

        setMeasuredDimension(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        int itemWidth = (r - l) / getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }

    }
}
