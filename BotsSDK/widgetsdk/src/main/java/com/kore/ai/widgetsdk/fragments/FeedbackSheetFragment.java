package com.kore.ai.widgetsdk.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.FeedBackillustrationActivity;
import com.kore.ai.widgetsdk.adapters.FeedBackAdapter;
import com.kore.ai.widgetsdk.events.FinishActivityEvent;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.events.RatingEvent;
import com.kore.ai.widgetsdk.interfaces.FeedBackButtonState;
import com.kore.ai.widgetsdk.models.FeedbackDataModel;
import com.kore.ai.widgetsdk.models.FeedbackDataResponse;
import com.kore.ai.widgetsdk.models.FeedbakResponse;
import com.kore.ai.widgetsdk.utils.EmailSessionManager;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FeedbackSheetFragment extends BottomSheetDialogFragment implements FeedBackButtonState {


    private BottomSheetBehavior bottomSheetBehavior;
    ArrayList<FeedbackDataModel> listData = new ArrayList<>();
    RecyclerView recycler_feedback;
    LinearLayoutManager layoutManager;
    View view;
    int position;
    Button submit_button;
    ImageView icon_1, icon_2, icon_3, icon_4, icon_5;
    TextView label_view, sub_text, closeBtnPanel;
    String id;
    FeedBackAdapter adapter;
    Context context;

    public FeedbackSheetFragment(int position, String id, Context context) {
        this.position = position;
        this.id = id;
        this.context = context;
    }


    public List<FeedbackDataResponse.Feedback> getParticularWidgetFeedback(String id, FeedbackDataResponse response) {

        for (FeedbackDataResponse.Feature feature : response.getFeatures()) {
            String currentId = feature.getId();
            if (currentId.toLowerCase().trim().equalsIgnoreCase(id.toLowerCase().trim())) {
                return feature.getFeedbacks();
            }
        }
        return null;
    }


    private void resetAll() {
        icon_1.setImageResource(R.drawable.feedback_icon_1);
        icon_2.setImageResource(R.drawable.feedback_icon_2);
        icon_3.setImageResource(R.drawable.feedback_icon_3);
        icon_4.setImageResource(R.drawable.feedback_icon_4);
        icon_5.setImageResource(R.drawable.feedback_icon_5);
    }


    public void loademojis(int position) {


        switch (position) {
            case 0:

                Glide.with(getActivity()).load(R.drawable.feedbac_ic_emo_1).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_1));
                break;
            case 1:
                Glide.with(getActivity()).load(R.drawable.feedbac_ic_emo_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_2));
                break;
            case 2:
                Glide.with(getActivity()).load(R.drawable.feedbac_ic_emo_3).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_3));


                break;
            case 3:
               // sub_text.setText("Thank you..");
                Glide.with(getActivity()).load(R.drawable.feedbac_ic_emo_4).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_4));

                break;
            case 4:
                Glide.with(getActivity()).load(R.drawable.feedbacon_emo_5).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_5));

                break;

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feedback_fullview_layout, container,
                false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        recycler_feedback = view.findViewById(R.id.recycler_feedback);
        submit_button = view.findViewById(R.id.submit_button);
        closeBtnPanel = view.findViewById(R.id.closeBtnPanel);
        closeBtnPanel.setBackground(KaUtility.changeColorOfDrawable(getActivity(), R.color.color_grey_e8));
        closeBtnPanel.setTypeface(KaUtility.getTypeFaceObj(getActivity()));

        layoutManager = new LinearLayoutManager(getActivity());
        try {


            RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider));
            recycler_feedback.addItemDecoration(dividerItemDecoration);
        } catch (Exception e) {

        }
        recycler_feedback.setLayoutManager(layoutManager);
        icon_1 = view.findViewById(R.id.icon_1);
        icon_2 = view.findViewById(R.id.icon_2);
        icon_3 = view.findViewById(R.id.icon_3);
        icon_4 = view.findViewById(R.id.icon_4);
        icon_5 = view.findViewById(R.id.icon_5);
        label_view = view.findViewById(R.id.label_view);
        sub_text = view.findViewById(R.id.sub_text);

        closeBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                if (context instanceof FeedBackillustrationActivity) {
                    ((FeedBackillustrationActivity) context).finish();
                   // KoreEventCenter.post(new FinishActivityEvent());
                }

            }
        });

        icon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetAll();
                // icon_1.setImageResource(R.drawable.feedbac_ic_emo_1);
                loademojis(0);
                position = 0;
                updateData();
            }
        });

        icon_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetAll();
                // icon_2.setImageResource(R.drawable.feedbac_ic_emo_2);
                loademojis(1);
                position = 1;
                updateData();
            }
        });

        icon_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetAll();
                //    icon_3.setImageResource(R.drawable.feedbac_ic_emo_3);
                loademojis(2);
                position = 2;
                updateData();
            }
        });
        icon_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
                //  icon_4.setImageResource(R.drawable.feedbac_ic_emo_4);
                loademojis(3);
                position = 3;
                updateData();
            }
        });

        icon_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
                // icon_5.setImageResource(R.drawable.feedbacon_emo_5);
                loademojis(4);
                position = 4;
                updateData();
            }
        });

        updateData();


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter != null && adapter.getSubmitData() != null) {
                    FeedbakResponse fb = adapter.getSubmitData();
                    if (fb.getOptions() != null && fb.getOptions().size() > 0) {
                        ArrayList<String> list = new ArrayList<>();
                        String comment = "";
                        boolean commentFound = false;
                        HashMap<String, Object> hmObj = new HashMap();
                        for (FeedbackDataResponse.Option temp : fb.getOptions()) {
                            list.add(temp.getId());
                            if (temp.getAction().equalsIgnoreCase("inputText")) {
                                comment = !StringUtils.isNullOrEmptyWithTrim(temp.getUserAnswer()) ? temp.getUserAnswer().trim() : " ";
                                commentFound = !StringUtils.isNullOrEmptyWithTrim(temp.getUserAnswer()) ? true : false;
                            }

                            //}
                        }
                        if (list.size() > 0) {
                            hmObj.put("featureId", id);
                            hmObj.put("rating", "" + (position + 1));
                            hmObj.put("options", list);
                            //if (!StringUtils.isNullOrEmptyWithTrim(comment)) {
                            if (commentFound) {
                                hmObj.put("comments", comment);
                            }
                            //}

                            KoreEventCenter.post(new RatingEvent(hmObj));
                            bottomSheetBehavior.setHideable(true);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            if (context instanceof FeedBackillustrationActivity) {
                                ((FeedBackillustrationActivity) context).finish();
                                KoreEventCenter.post(new FinishActivityEvent());
                            }else
                            {
                                new EmailSessionManager(getActivity()).saveTime(System.currentTimeMillis());
                            }


                        }
                    } else {
                        // ToastUtils.showToast(getActivity(), "please select option");
                    }

                } else {
                    HashMap<String, Object> hmObj = new HashMap();
                    hmObj.put("featureId", id);
                    hmObj.put("rating", "" + (position + 1));
                    KoreEventCenter.post(new RatingEvent(hmObj));
                    bottomSheetBehavior.setHideable(true);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    if (context instanceof FeedBackillustrationActivity) {
                        ((FeedBackillustrationActivity) context).finish();
                        KoreEventCenter.post(new FinishActivityEvent());
                    }
                    else {
                        new EmailSessionManager(getActivity()).saveTime(System.currentTimeMillis());
                    }
                    // ToastUtils.showToast(getActivity(), "please select option");
                }
            }
        });

        return view;

    }


    private void updateData() {
        if (KaUtility.getFeedbackDataResponse() != null) {


            FeedbackDataResponse response = KaUtility.getFeedbackDataResponse();
            String title = response.getRatings().get(position).getTitle();
            List<FeedbackDataResponse.Feedback> data = getParticularWidgetFeedback(id, response);
            if (data != null) {
                String sub = data.get(position).getTitle();
                List<FeedbackDataResponse.Option> options = data.get(position).getOptions();
                sub_text.setText(sub);
                label_view.setText(title);
                loademojis(position);


                adapter = new FeedBackAdapter(getActivity(), options,recycler_feedback);
                adapter.setCallBack(this);
                recycler_feedback.setAdapter(adapter);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                //  d.setCanceledOnTouchOutside(false);
                // d.setCancelable(false);

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                // FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                //   bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {


                        // Check Logs to see how bottom sheets behaves
                        switch (newState) {
                            case BottomSheetBehavior.STATE_COLLAPSED:

                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:

                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());

                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
                                break;
                            case BottomSheetBehavior.STATE_HIDDEN:
                                dismiss();
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                break;
                            case BottomSheetBehavior.STATE_HALF_EXPANDED:
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });


        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }


    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {

        }
    }

    @Override
    public void notify(boolean buttonState) {

        submit_button.setClickable(buttonState);
        submit_button.setEnabled(buttonState);
//        if (buttonState)
            submit_button.setAlpha(1F);
//        else
//            submit_button.setAlpha(0.6F);

    }


    public class DividerItemDecorator extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i <= childCount - 2; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
