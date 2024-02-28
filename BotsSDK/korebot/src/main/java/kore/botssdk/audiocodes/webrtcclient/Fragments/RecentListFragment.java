package kore.botssdk.audiocodes.webrtcclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Adapters.RecentListAdapter;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;


public class RecentListFragment extends BaseFragment implements FragmentLifecycle {

    private final String TAG = "RecentListFragment";


    private ListView recentList;
    private RecentListAdapter recentListAdapter;

    private final int DELETE_ALL_MENU = 201;


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment_recent_list, container, false);

        initGui(rootView);



        return rootView;
    }

    private void initGui(View rootView) {

        recentList = (ListView) rootView.findViewById(R.id.recent_listview);
        Button erase = (Button) rootView.findViewById(R.id.recent_button_erase);


        refreshData();

        //Erase all table
        if (erase != null)
        {
            erase.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    BotApplication.getDataBase().deleteTable();
                    refreshData();
                }
            });
        }
    }

    private void refreshData() {

        List<CallEntry> callEntryList = BotApplication.getDataBase().getAllEntries();
//        if(callEntryList!=null) {
//            Collections.reverse(callEntryList);
//        }
        recentListAdapter = new RecentListAdapter(getActivity(), callEntryList);

        recentListAdapter.setonCreateItemClickListener(clickListener);
        recentListAdapter.setCallButtonClickListener(callBtnClickListener);
        //recentListAdapter.set
        //recentList.setOnLongClickListener(LisviewOnItemLongClickListener);

        recentList.setAdapter(recentListAdapter);

        //registerForContextMenu(recentList);


    }

    private RecentListAdapter.ListOnItemClickListener clickListener = new RecentListAdapter.ListOnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            Log.d(TAG, "list view row selected");
            CallEntry callEntry = recentListAdapter.getCallEntryList().get(position);
            Toast.makeText(RecentListFragment.this.getContext(), "audio call to: " + callEntry.getContactName() + " number: " + callEntry.getContactNumber(), Toast.LENGTH_SHORT).show();
            ACManager.getInstance().callNumber(callEntry.getContactNumber());
        }
    };


    private RecentListAdapter.ListOnItemClickListener callBtnClickListener = new RecentListAdapter.ListOnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            Log.d(TAG, "list view button selected");
            CallEntry callEntry = recentListAdapter.getCallEntryList().get(position);
            Toast.makeText(RecentListFragment.this.getContext(), "Video call to: " + callEntry.getContactName() + " number: " + callEntry.getContactNumber(), Toast.LENGTH_SHORT).show();
            ACManager.getInstance().callNumber(callEntry.getContactNumber(), true);

        }
    };

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
//        boolean isTablet = MainApp.getGlobalContext().getResources().getBoolean(R.bool.isTablet);
//        if (!isTablet) {
//            Log.d(TAG, "refreshData");
//            refreshData();
//        }
        try {
            Log.d(TAG, "refreshData");
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

}