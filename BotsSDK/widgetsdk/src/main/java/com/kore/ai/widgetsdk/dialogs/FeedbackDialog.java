package com.kore.ai.widgetsdk.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.NonNull;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.activities.PanelMainActivity;
import com.kore.ai.widgetsdk.utils.DimensionUtil;
import com.kore.ai.widgetsdk.utils.KaUtility;


public class FeedbackDialog extends Dialog {

    private final LayoutInflater layoutInflater;
    private Context _context;
    boolean isScreenTouched;
    View close;
    View icon_1, icon_2, icon_3, icon_4, icon_5;

    PanelMainActivity activity;
    String id;
    Handler handler;
    public FeedbackDialog(Context context, String id) {
        super(context);
        _context = context;
        this.id = id;
        activity = (PanelMainActivity) context;
        layoutInflater = LayoutInflater.from(context);

        handler = new Handler();
        handler.postDelayed(runnable, 4000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isScreenTouched) {
                dismissDialogWithAnim();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View layoutView = layoutInflater.inflate(R.layout.feedback_dialog_layout, null);
        setContentView(layoutView);
        findViews(layoutView);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        isScreenTouched = false;
        return super.dispatchTouchEvent(ev);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    public void dismissDialog()
    {
        KaUtility.skipped_feedbackId=null;
        handler.removeCallbacks(runnable);
        dismiss();
    }
    public void dismissDialogWithAnim() {

        KaUtility.skipped_feedbackId=id;
         View decorView = getWindow()
                .getDecorView();
        final  View decorView1=findViewById(R.id.view_dialog);



        int[] location = new int[2];
        decorView.getLocationOnScreen(location);
        int decoreX=location[0];
        int decoreY=location[1];


        ObjectAnimator scaleDown1 = ObjectAnimator.ofPropertyValuesHolder(decorView,


             //   PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f),
                PropertyValuesHolder.ofFloat("translationX", decoreX*(20* DimensionUtil.dp1)),
                PropertyValuesHolder.ofFloat("translationY", (-decorView1.getHeight()/2.5f) ));
        scaleDown1.setDuration(400);
        scaleDown1.start();
        scaleDown1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
             /*  decorView = getWindow()
                        .getDecorView();*/
                final  View decorView=findViewById(R.id.view_dialog);
                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView,

                        PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f),
                        PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f),
                        PropertyValuesHolder.ofFloat("translationX", activity.getX()),
                        PropertyValuesHolder.ofFloat("translationY", -activity.getY()+(100*DimensionUtil.dp1) ));

                scaleDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.quitAnim();
                        dismiss();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                scaleDown.setDuration(700);
                 scaleDown.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



    }


    private void findViews(View layoutView) {

        close = layoutView.findViewById(R.id.close);
        icon_1 = layoutView.findViewById(R.id.icon_1);
        icon_2 = layoutView.findViewById(R.id.icon_2);
        icon_3 = layoutView.findViewById(R.id.icon_3);
        icon_4 = layoutView.findViewById(R.id.icon_4);
        icon_5 = layoutView.findViewById(R.id.icon_5);


        icon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.launchFeedbackSheet(0, id);

            }
        });
        icon_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.launchFeedbackSheet(1, id);

            }
        });
        icon_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.launchFeedbackSheet(2, id);

            }
        });
        icon_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.launchFeedbackSheet(3, id);

            }
        });
        icon_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.launchFeedbackSheet(4, id);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismissDialog();

            }
        });
    }
}
