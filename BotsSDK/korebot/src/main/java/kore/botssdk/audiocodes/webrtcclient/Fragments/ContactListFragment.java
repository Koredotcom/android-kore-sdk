package kore.botssdk.audiocodes.webrtcclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.Adapters.ContactListAdapter;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.Structure.ContactListObject;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBManager;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBObject;
import kore.botssdk.audiocodes.webrtcclient.db.NativeDBPhones;


public class ContactListFragment extends BaseFragment implements FragmentLifecycle {


    private final String TAG = "ContactListFragment";

    private ListView contactListView;
    private ContactListAdapter contactListAdapter;
    private int contactListMaxSize=3000;//0 for unlimited


    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment_contact_list, container, false);

        initGui(rootView);


        return rootView;
        //return inflater.inflate(R.layout.main_fragment_dialer, container, false);
    }

    private void initGui(View rootView) {

        contactListView = (ListView) rootView.findViewById(R.id.contact_list_listview_contacts);
        //List<NativeContactObject> nativeContactObjectList = NativeContactUtils.getContactList(true);
        List<NativeDBObject> nativeDBObjectList = NativeDBManager.getContactList();

        Collections.sort(nativeDBObjectList);
        if (nativeDBObjectList != null)
        {
            Log.d(TAG, "original nativeDBObjectList: "+nativeDBObjectList.size());
            if (contactListMaxSize>0 && nativeDBObjectList.size()> contactListMaxSize)
            {
                nativeDBObjectList=nativeDBObjectList.subList(0,contactListMaxSize-1);
            }
        }


        List<ContactListObject> contactListObjectList = new ArrayList<ContactListObject>();
        // nativeContactObjectList.sort(Comparator.comparing(NativeContactObject::NativeContactObject.compare()));
        for (NativeDBObject nativeDBObject: nativeDBObjectList)
        {
            if(nativeDBObject!=null && nativeDBObject.getPhones()!=null && nativeDBObject.getPhones().size()>0)
            {
                for (NativeDBPhones nativeDBPhones:nativeDBObject.getPhones()) {
                    //.Log.d(TAG, "display name :"+nativeContactObject.getDisplayName());
                    contactListObjectList.add(new ContactListObject(nativeDBObject.getDisplayName(),nativeDBPhones.getPhoneNumber(),nativeDBObject.getPhotoURI(),nativeDBObject.getPhotoThumbnailURI()));
                }
            }
        }
        contactListAdapter = new ContactListAdapter(ContactListFragment.this.getContext(), contactListObjectList);

        contactListAdapter.setonCreateItemClickListener(clickListener);
        contactListAdapter.setCallButtonClickListener(callBtnClickListener);


        contactListView.setAdapter(contactListAdapter);



    }



    private ContactListAdapter.ListOnItemClickListener clickListener = new ContactListAdapter.ListOnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            Log.d(TAG, "list view row selected");
            ContactListObject contactListObject = contactListAdapter.getList().get(position);
            Toast.makeText(ContactListFragment.this.getContext(), "audio call to: "+contactListObject.getName()+" number: "+contactListObject.getNumber(), Toast.LENGTH_SHORT).show();
            ACManager.getInstance().callNumber(contactListObject.getNumber());
        }
    };


    private ContactListAdapter.ListOnItemClickListener callBtnClickListener = new ContactListAdapter.ListOnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            Log.d(TAG, "list view button selected");
            ContactListObject contactListObject = contactListAdapter.getList().get(position);
            Toast.makeText(ContactListFragment.this.getContext(), "Video call to: "+contactListObject.getName()+" number: "+contactListObject.getNumber(), Toast.LENGTH_SHORT).show();
            ACManager.getInstance().callNumber(contactListObject.getNumber(), true);

        }
    };

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

}