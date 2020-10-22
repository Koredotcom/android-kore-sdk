package com.kore.findlysdk.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.R;
import com.kore.findlysdk.net.BotRestBuilder;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.widgetviews.CustomBottomSheetBehavior;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindlyFragment extends KaBaseFragment implements GestureDetector.OnGestureListener
{
    private View view;
    private LinearLayout perssiatentPanel,  llComposeBar;
    private ImageView img_skill;
    private TextView closeBtnPanel, editButton;
    private TextView  txtTitle;
//    private RecyclerView recyclerView_panel;
    private Button panelDrag;
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private CoordinatorLayout cordinate_layout;
    private GestureDetector gestureScanner;
    private float screenHeight;

    //Popular Search
    private PopupWindow popupWindow;
    private View popUpView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.findly_layout, container, false);
        screenHeight = (int) AppControl.getInstance(getContext()).getDimensionUtil().screenHeight;

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popUpView = inflater.inflate(R.layout.popular_search_findly_item, null);
        popupWindow = new PopupWindow(popUpView, width, height, focusable);

        findFindlyView(view);
        getPopularSearch();
        return view;
    }

    private void findFindlyView(View view) {
        perssiatentPanel = view.findViewById(R.id.persistentPanel);
        img_skill = view.findViewById(R.id.img_skill);
        txtTitle = view.findViewById(R.id.txtTitle);
        editButton = (TextView) view.findViewById(R.id.editButton);
//        editButton.setTypeface(KaUtility.getTypeFaceObj(getActivity()));

//        recyclerView_panel = view.findViewById(R.id.recyclerView_panel);
        closeBtnPanel = (TextView) view.findViewById(R.id.closeBtnPanel);
//        closeBtnPanel.setTypeface(KaUtility.getTypeFaceObj(getActivity()));
        panelDrag = view.findViewById(R.id.panel_drag);

//        recyclerView_panel.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBottomSheetBehavior = CustomBottomSheetBehavior.from(perssiatentPanel);
        cordinate_layout = view.findViewById(R.id.cordinate_layout);
        llComposeBar = view.findViewById(R.id.llComposeBar);
        gestureScanner = new GestureDetector(this);

        mBottomSheetBehavior.setBottomSheetCallback(new CustomBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setLocked(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        perssiatentPanel.setVisibility(View.VISIBLE);
//        recyclerView_panel.setVisibility(View.VISIBLE);
//        recyclerView_panel.setMinimumHeight((int) (screenHeight));
//        popupWindow.showAtLocation(llComposeBar, Gravity.TOP|Gravity.RIGHT, 80, 220);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        mBottomSheetBehavior.setLocked(false);
        try {
            if (mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.onInterceptTouchEvent(cordinate_layout, perssiatentPanel, motionEvent);
            }
        } catch (Exception ex) {

        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void getPopularSearch()
    {
//        https://app.findly.ai/api/1.1/searchAssist/sidx-f3a43e5f-74b6-5632-a488-8af83c480b88/popularSearches

        Call<ArrayList<PopularSearchModel>> getJWTTokenService = BotRestBuilder.getBotJWTRestAPI().getPopularSearch();
        getJWTTokenService.enqueue(new Callback<ArrayList<PopularSearchModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopularSearchModel>> call, Response<ArrayList<PopularSearchModel>> response) {
                if (response.isSuccessful())
                {
                    Log.e("Response", response.body()+"");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopularSearchModel>> call, Throwable t) {
                Log.e("Skill Panel Data", t.toString());
            }
        });
    }
}
