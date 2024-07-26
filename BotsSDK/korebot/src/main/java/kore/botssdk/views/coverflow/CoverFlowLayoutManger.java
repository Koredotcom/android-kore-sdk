package kore.botssdk.views.coverflow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.RecyclerView;

public class CoverFlowLayoutManger extends RecyclerView.LayoutManager {
    private final static int SCROLL_TO_RIGHT = 1;
    private final static int SCROLL_TO_LEFT = 2;
    private final static int MAX_RECT_COUNT = 100;
    private int mOffsetAll = 0;
    private int mDecoratedChildWidth = 0;
    private int mDecoratedChildHeight = 0;
    private float mIntervalRatio = 0.5f;
    private int mStartX = 0;
    private int mStartY = 0;
    private SparseArray<Rect> mAllItemFrames = new SparseArray<>();
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();
    private RecyclerView.Recycler mRecycle;
    private RecyclerView.State mState;
    private ValueAnimator mAnimation;
    private int mSelectPosition = 0;
    private int mLastSelectPosition = 0;
    private OnSelected mSelectedListener;
    private boolean mIsFlatFlow = false;
    private boolean mItemGradualGrey = false;
    private boolean mItemGradualAlpha = false;
    private boolean mIsLoop = false;
    private boolean mItem3D = false;

    private CoverFlowLayoutManger(boolean isFlat, boolean isGreyItem, boolean isAlphaItem, float cstInterval, boolean isLoop, boolean is3DItem) {
        mIsFlatFlow = isFlat;
        mItemGradualGrey = isGreyItem;
        mItemGradualAlpha = isAlphaItem;
        mIsLoop = isLoop;
        mItem3D = is3DItem;
        if (cstInterval >= 0) {
            mIntervalRatio = cstInterval;
        } else {
            if (mIsFlatFlow) {
                mIntervalRatio = 1.1f;
            }
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            mOffsetAll = 0;
            return;
        }
        mAllItemFrames.clear();
        mHasAttachedItems.clear();

        View scrap = recycler.getViewForPosition(0);
        addView(scrap);
        measureChildWithMargins(scrap, 0, 0);
        mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
        mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);
        mStartX = Math.round((getHorizontalSpace() - mDecoratedChildWidth) * 1.0f / 2);
        mStartY = Math.round((getVerticalSpace() - mDecoratedChildHeight) * 1.0f / 2);

        float offset = mStartX;

        for (int i = 0; i < getItemCount() && i < MAX_RECT_COUNT; i++) {
            Rect frame = mAllItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth), mStartY + mDecoratedChildHeight);
            mAllItemFrames.put(i, frame);
            mHasAttachedItems.put(i, false);
            offset = offset + getIntervalDistance();
        }

        detachAndScrapAttachedViews(recycler);
        if ((mRecycle == null || mState == null) && mSelectPosition != 0) {
            mOffsetAll = calculateOffsetForPosition(mSelectPosition);
            onSelectedCallBack();
        }

        layoutItems(recycler, state, SCROLL_TO_LEFT);

        mRecycle = recycler;
        mState = state;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mAnimation != null && mAnimation.isRunning()) mAnimation.cancel();
        int travel = dx;
        if (!mIsLoop) {
            if (dx + mOffsetAll < 0) {
                travel = -mOffsetAll;
            } else if (dx + mOffsetAll > getMaxOffset()) {
                travel = (int) (getMaxOffset() - mOffsetAll);
            }
        }
        mOffsetAll += travel;
        layoutItems(recycler, state, dx > 0 ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT);
        return travel;
    }

    private void layoutItems(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDirection) {
        if (state == null || state.isPreLayout()) return;

        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());

        int position = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getTag() != null) {
                TAG tag = checkTag(child.getTag());
                position = tag.pos;
            } else {
                position = getPosition(child);
            }

            Rect rect = getFrame(position);

            if (!Rect.intersects(displayFrame, rect)) {
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.delete(position);
            } else {
                layoutItem(child, rect);
                mHasAttachedItems.put(position, true);
            }
        }

        if (position == 0) position = getCenterPosition();

        int min = position - 20;
        int max = position + 20;

        if (!mIsLoop) {
            if (min < 0) min = 0;
            if (max > getItemCount()) max = getItemCount();
        }

        for (int i = min; i < max; i++) {
            Rect rect = getFrame(i);
            if (Rect.intersects(displayFrame, rect) &&
                    !mHasAttachedItems.get(i)) {
                int actualPos = i % getItemCount();
                if (actualPos < 0) actualPos = getItemCount() + actualPos;

                View scrap = recycler.getViewForPosition(actualPos);
                checkTag(scrap.getTag());
                scrap.setTag(new TAG(i));

                measureChildWithMargins(scrap, 0, 0);
                if (scrollDirection == SCROLL_TO_RIGHT || mIsFlatFlow) {
                    addView(scrap, 0);
                } else {
                    addView(scrap);
                }
                layoutItem(scrap, rect);
                mHasAttachedItems.put(i, true);
            }
        }
    }

    private void layoutItem(View child, Rect frame) {
        layoutDecorated(child, frame.left - mOffsetAll, frame.top, frame.right - mOffsetAll, frame.bottom);
        if (!mIsFlatFlow) {
            child.setScaleX(computeScale(frame.left - mOffsetAll));
            child.setScaleY(computeScale(frame.left - mOffsetAll));
        }

        if (mItemGradualAlpha) {
            child.setAlpha(computeAlpha(frame.left - mOffsetAll));
        }

        if (mItemGradualGrey) {
            greyItem(child, frame);
        }

        if (mItem3D) {
            item3D(child, frame);
        }
    }

    private Rect getFrame(int index) {
        Rect frame = mAllItemFrames.get(index);
        if (frame == null) {
            frame = new Rect();
            float offset = mStartX + getIntervalDistance() * index;
            frame.set(Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth), mStartY + mDecoratedChildHeight);
        }

        return frame;
    }

    private void greyItem(View child, Rect frame) {
        float value = computeGreyScale(frame.left - mOffsetAll);
        ColorMatrix cm = new ColorMatrix(new float[]{
                value, 0, 0, 0, 120 * (1 - value),
                0, value, 0, 0, 120 * (1 - value),
                0, 0, value, 0, 120 * (1 - value),
                0, 0, 0, 1, 250 * (1 - value),
        });
        Paint greyPaint = new Paint();
        greyPaint.setColorFilter(new ColorMatrixColorFilter(cm));

        child.setLayerType(View.LAYER_TYPE_HARDWARE, greyPaint);
        if (value >= 1) {
            child.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    private void item3D(View child, Rect frame) {
        float center = (frame.left + frame.right - 2 * mOffsetAll) / 2f;
        float value = (center - (mStartX + mDecoratedChildWidth / 2f)) * 1f / (getItemCount() * getIntervalDistance());
        value = (float) Math.sqrt(Math.abs(value));
        float symbol = center > (mStartX + mDecoratedChildWidth / 2f) ? -1 : 1;
        child.setRotationY(symbol * 50 * value);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                fixOffsetWhenFinishScroll();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount() - 1) return;
        mOffsetAll = calculateOffsetForPosition(position);
        if (mRecycle == null || mState == null) {
            mSelectPosition = position;
        } else {
            layoutItems(mRecycle, mState, position > mSelectPosition ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT);
            onSelectedCallBack();
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        if (mIsLoop) return;
        int finalOffset = calculateOffsetForPosition(position);
        if (mRecycle == null || mState == null) {
            mSelectPosition = position;
        } else {
            startScroll(mOffsetAll, finalOffset);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        removeAllViews();
        mRecycle = null;
        mState = null;
        mOffsetAll = 0;
        mSelectPosition = 0;
        mLastSelectPosition = 0;
        mHasAttachedItems.clear();
        mAllItemFrames.clear();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private float getMaxOffset() {
        return (getItemCount() - 1) * getIntervalDistance();
    }

    private float computeScale(int x) {
        float scale = 1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / mIntervalRatio);
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }

    private float computeGreyScale(int x) {
        float itemMidPos = x + mDecoratedChildWidth / 2;
        float itemDx2Mid = Math.abs(itemMidPos - getHorizontalSpace() / 2f);
        float value = 1 - itemDx2Mid * 1.0f / (getHorizontalSpace() / 2);
        if (value < 0.1) value = 0.1f;
        if (value > 1) value = 1;
        value = (float) Math.pow(value, .8);
        return value;
    }

    private float computeAlpha(int x) {
        float alpha = 1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / mIntervalRatio);
        if (alpha < 0.3f) alpha = 0.3f;
        if (alpha > 1) alpha = 1.0f;
        return alpha;
    }

    private int calculateOffsetForPosition(int position) {
        return Math.round(getIntervalDistance() * position);
    }

    private void fixOffsetWhenFinishScroll() {
        if (getIntervalDistance() != 0) {
            int scrollN = (int) (mOffsetAll * 1.0f / getIntervalDistance());
            float moreDx = (mOffsetAll % getIntervalDistance());
            if (Math.abs(moreDx) > (getIntervalDistance() * 0.5)) {
                if (moreDx > 0) scrollN++;
                else scrollN--;
            }
            int finalOffset = scrollN * getIntervalDistance();
            startScroll(mOffsetAll, finalOffset);
            mSelectPosition = Math.abs(Math.round(finalOffset * 1.0f / getIntervalDistance())) % getItemCount();
        }
    }

    private void startScroll(int from, int to) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
        }
        final int direction = from < to ? SCROLL_TO_LEFT : SCROLL_TO_RIGHT;
        mAnimation = ValueAnimator.ofFloat(from, to);
        mAnimation.setDuration(500);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.addUpdateListener(animation -> {
            mOffsetAll = Math.round((float) animation.getAnimatedValue());
            layoutItems(mRecycle, mState, direction);
        });
        mAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onSelectedCallBack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimation.start();
    }

    private int getIntervalDistance() {
        return Math.round(mDecoratedChildWidth * mIntervalRatio);
    }

    private void onSelectedCallBack() {
        mSelectPosition = Math.round(mOffsetAll / getIntervalDistance());
        mSelectPosition = Math.abs(mSelectPosition % getItemCount());
        if (mSelectedListener != null && mSelectPosition != mLastSelectPosition) {
            mSelectedListener.onItemSelected(mSelectPosition);
        }
        mLastSelectPosition = mSelectPosition;
    }

    private TAG checkTag(Object tag) {
        if (tag != null) {
            if (tag instanceof TAG) {
                return ((TAG) tag);
            } else {
                throw new IllegalArgumentException("You should not use View#setTag(Object tag), use View#setTag(int key, Object tag) instead!");
            }
        } else {
            return null;
        }
    }

    public int getFirstVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur - 1; ; i--) {
            Rect rect = getFrame(i);
            if (rect.left <= displayFrame.left) {
                return Math.abs(i) % getItemCount();
            }
        }
    }

    public int getLastVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur + 1; ; i++) {
            Rect rect = getFrame(i);
            if (rect.right >= displayFrame.right) {
                return Math.abs(i) % getItemCount();
            }
        }
    }

    int getChildActualPos(int index) {
        View child = getChildAt(index);
        if (child.getTag() != null) {
            TAG tag = checkTag(child.getTag());
            return tag.pos;
        } else {
            return getPosition(child);
        }
    }

    public int getMaxVisibleCount() {
        int oneSide = (getHorizontalSpace() - mStartX) / (getIntervalDistance());
        return oneSide * 2 + 1;
    }

    int getCenterPosition() {
        int pos = mOffsetAll / getIntervalDistance();
        int more = mOffsetAll % getIntervalDistance();
        if (Math.abs(more) >= getIntervalDistance() * 0.5f) {
            if (more >= 0) pos++;
            else pos--;
        }
        return pos;
    }

    public void setOnSelectedListener(OnSelected l) {
        mSelectedListener = l;
    }

    public int getSelectedPos() {
        return mSelectPosition;
    }

    public interface OnSelected {
        void onItemSelected(int position);
    }

    private class TAG {
        int pos;

        TAG(int pos) {
            this.pos = pos;
        }
    }

    static class Builder {
        boolean isFlat = false;
        boolean isGreyItem = false;
        boolean isAlphaItem = false;
        float cstIntervalRatio = -1f;
        boolean isLoop = false;
        boolean is3DItem = false;

        Builder setFlat(boolean flat) {
            isFlat = flat;
            return this;
        }

        Builder setGreyItem(boolean greyItem) {
            isGreyItem = greyItem;
            return this;
        }

        Builder setAlphaItem(boolean alphaItem) {
            isAlphaItem = alphaItem;
            return this;
        }

        Builder setIntervalRatio(float ratio) {
            cstIntervalRatio = ratio;
            return this;
        }

        Builder loop() {
            isLoop = true;
            return this;
        }

        Builder set3DItem(boolean d3) {
            is3DItem = true;
            return this;
        }

        public CoverFlowLayoutManger build() {
            return new CoverFlowLayoutManger(isFlat, isGreyItem,
                    isAlphaItem, cstIntervalRatio, isLoop, is3DItem);
        }
    }
}