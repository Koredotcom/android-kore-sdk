package com.kore.findlysdk.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kore.findlysdk.R;
import com.kore.findlysdk.adapters.LiveSearchCyclerAdapter;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.CircularRevealTransition;

import java.util.ArrayList;

public class AllResultsFragment extends Fragment
{
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private LiveSearchCyclerAdapter liveSearchCyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private TextView tvCount;
    private boolean showCount;

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
//            if(liveSearchCyclerAdapter == null)
                lvLiveSearch.setAdapter( liveSearchCyclerAdapter = new LiveSearchCyclerAdapter(getActivity(), arrLiveSearchResultsModels, 1, invokeGenericWebViewInterface, null));
//            else
//                liveSearchCyclerAdapter.refresh(arrLiveSearchResultsModels);
        }

        lvLiveSearch.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(floatingActionButton != null)
                {
                    if (dy > 0 ||dy<0 && floatingActionButton.isShown())
                    {
                        floatingActionButton.hide();
                        tvCount.setVisibility(View.GONE);
//                        floatingActionButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    if(floatingActionButton != null)
                    {
                        floatingActionButton.show();

                        if(showCount)
                            tvCount.setVisibility(View.VISIBLE);
//                        floatingActionButton.setVisibility(View.VISIBLE);
                    }

                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface)
    {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setFloatingActionButton(FloatingActionButton flo)
    {
        this.floatingActionButton = flo;
    }

    public void setFilterCountView(TextView tvCount)
    {
        this.tvCount = tvCount;
    }

    public void showFilterCount(boolean showCount)
    {
        this.showCount = showCount;
    }

    

}
