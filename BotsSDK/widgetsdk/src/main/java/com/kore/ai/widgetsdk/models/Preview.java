package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 27-Aug-18.
 */

public class Preview {
    private int count;
    private boolean showMore;
    private List<Invitee> invitees = null;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Invitee> invitees) {
        this.invitees = invitees;
    }


}
