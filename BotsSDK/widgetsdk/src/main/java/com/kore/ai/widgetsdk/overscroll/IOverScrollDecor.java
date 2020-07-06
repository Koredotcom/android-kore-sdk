package com.kore.ai.widgetsdk.overscroll;

import android.view.View;

/**
 * @author amit
 */
public interface IOverScrollDecor {
    View getView();

    void setOverScrollStateListener(IOverScrollStateListener listener);
    void setOverScrollUpdateListener(IOverScrollUpdateListener listener);

    /**
     * Get the current decorator's runtime state, i.e. one of the values specified by {@link IOverScrollState}.
     * @return The state.
     */
    int getCurrentState();


    void detach();
}
