package kore.botssdk.listener;

import java.util.ArrayList;

public interface RecyclerViewDataAccessor {
    ArrayList getData();
    void  setData(ArrayList data);
    void  setExpanded(boolean isExpanded);
    void  setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper);
}
