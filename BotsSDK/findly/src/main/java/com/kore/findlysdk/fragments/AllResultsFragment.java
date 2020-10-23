package com.kore.findlysdk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;

import java.util.ArrayList;

public class AllResultsFragment extends Fragment
{
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public static AllResultsFragment newInstance(ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels)
    {
        Bundle args = new Bundle();
        AllResultsFragment fragment = new AllResultsFragment();
        args.putSerializable("Results", arrLiveSearchResultsModels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrLiveSearchResultsModels = (ArrayList<LiveSearchResultsModel>)getArguments().getSerializable("Results");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.all_results_findly_layout, container, false);
        RecyclerView lvLiveSearch = (RecyclerView) view.findViewById(R.id.lvLiveSearch);
        lvLiveSearch.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if(arrLiveSearchResultsModels != null && arrLiveSearchResultsModels.size() > 0)
        {
            lvLiveSearch.setAdapter( new LiveSearchCyclerAdapter(getActivity(), arrLiveSearchResultsModels, 1, invokeGenericWebViewInterface));
        }
        return view;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface)
    {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
