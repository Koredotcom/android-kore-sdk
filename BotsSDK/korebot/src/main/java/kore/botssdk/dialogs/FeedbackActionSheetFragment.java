package kore.botssdk.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class FeedbackActionSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    private View view;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private boolean isFromListMenu = false;
    private int dp1;
    private LinearLayout llCloseBottomSheet, llFeedbackComment;
    public String getSkillName() {
        return skillName;
    }
    private BottomSheetDialog bottomSheetDialog;
    private TextView tvfeedback_template_title;
    private ImageView icon_1, icon_2, icon_3, icon_4, icon_5;
    private RatingBar rbFeedback;
    private LinearLayout emojis;
    private LinearLayout multiSelectLayout;
    private PayloadInner payloadInner;
    private EditText etFeedbackComment;
    private TextView tvFeedbackSubmit, tvCommentTitle, tvGlad;
    private int position;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout rlCommentBox;

    public void setSkillName(String skillName, String trigger) {
        this.skillName = skillName;
        this.trigger = trigger;
    }

    private String skillName;
    private String trigger;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feedback_template_view, container,false);
        this.dp1 = (int) DimensionUtil.dp1;
        multiSelectLayout = view.findViewById(R.id.multiSelectLayout);
        tvfeedback_template_title = view.findViewById(R.id.tvfeedback_template_title);
        rbFeedback = view.findViewById(R.id.rbFeedback);
        emojis = view.findViewById(R.id.emojis);
        llCloseBottomSheet = view.findViewById(R.id.llCloseBottomSheet);
        llFeedbackComment = view.findViewById(R.id.llFeedbackComment);
        etFeedbackComment = view.findViewById(R.id.etFeedbackComment);
        tvFeedbackSubmit = view.findViewById(R.id.tvFeedbackSubmit);
        tvCommentTitle = view.findViewById(R.id.tvCommentTitle);
        tvGlad = view.findViewById(R.id.tvGlad);
        rlCommentBox = view.findViewById(R.id.rlCommentBox);

        icon_1 = view.findViewById(R.id.icon_1);
        icon_2 = view.findViewById(R.id.icon_2);
        icon_3 = view.findViewById(R.id.icon_3);
        icon_4 = view.findViewById(R.id.icon_4);
        icon_5 = view.findViewById(R.id.icon_5);

        icon_1.setOnClickListener(this);
        icon_2.setOnClickListener(this);
        icon_3.setOnClickListener(this);
        icon_4.setOnClickListener(this);
        icon_5.setOnClickListener(this);

        llCloseBottomSheet.setVisibility(View.VISIBLE);
        if(payloadInner != null && payloadInner.getView() != null)
        {
            if(payloadInner.getView().equalsIgnoreCase(BotResponse.VIEW_STAR))
            {
                emojis.setVisibility(View.GONE);
                rbFeedback.setVisibility(View.VISIBLE);
                rbFeedback.setRating(payloadInner.getEmojiPosition());
//                rbFeedback.setOnRatingBarChangeListener(onRatingBarChangeListener);
            }
            else
            {
                emojis.setVisibility(View.VISIBLE);
                rbFeedback.setVisibility(View.GONE);
                resetAll();
                loademojis(payloadInner.getEmojiPosition());
            }

            tvfeedback_template_title.setText(payloadInner.getText());
        }

        llCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();
            }
        });

        tvFeedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                if(!StringUtils.isNullOrEmpty(etFeedbackComment.getText().toString()))
                    composeFooterInterface.onSendClick(position+":"+etFeedbackComment.getText().toString(), position+":"+etFeedbackComment.getText().toString(), false);
                else
                    composeFooterInterface.onSendClick(position+"", position+"", false);
            }
        });

        rbFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                position = (int) rating;
                payloadInner.setEmojiPosition(position);

                if(fromUser)
                    updateData();
            }
        });

        return view;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                bottomSheet.getLayoutParams().height = (int) (AppControl.getInstance(getContext()).getDimensionUtil().screenHeight - 40 * dp1);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(200 * dp1);
            }

        });

        // Do something with your dialog like setContentView() or whatever
        return bottomSheetDialog;
    }

    public void setData(PayloadInner payloadInner) {
        this.payloadInner = payloadInner;
    }

    public void setData(PayloadInner payloadInner, boolean isFromListMenu){
        this.payloadInner = payloadInner;
        this.isFromListMenu = isFromListMenu;
    }

    private void resetAll() {
        icon_1.setImageResource(R.drawable.feedback_icon_1);
        icon_2.setImageResource(R.drawable.feedback_icon_2);
        icon_3.setImageResource(R.drawable.feedback_icon_3);
        icon_4.setImageResource(R.drawable.feedback_icon_4);
        icon_5.setImageResource(R.drawable.feedback_icon_5);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.icon_1) {
            resetAll();
            //  icon_4.setImageResource(R.drawable.feedbac_ic_emo_4);
            loademojis(0);
            position = 1;
            updateData();
        } else if (id == R.id.icon_2) {
            resetAll();
            loademojis(1);
            position = 2;
            updateData();
        } else if (id == R.id.icon_3) {
            resetAll();
            loademojis(2);
            position = 3;
            updateData();
        } else if (id == R.id.icon_4) {
            resetAll();
            loademojis(3);
            position = 4;
            updateData();
        } else if (id == R.id.icon_5) {
            resetAll();
            loademojis(4);
            position = 5;
            updateData();
        }
    }

    private void updateData()
    {
        llFeedbackComment.setVisibility(View.VISIBLE);

        if(position == 5) {
            tvGlad.setVisibility(View.VISIBLE);
            tvGlad.setText(payloadInner.getMessageTodisplay());
            rlCommentBox.setVisibility(View.GONE);
            tvCommentTitle.setVisibility(View.GONE);
            bottomSheetBehavior.setPeekHeight(300 * dp1);
        }
        else
        {
            tvGlad.setVisibility(View.GONE);
            tvCommentTitle.setVisibility(View.VISIBLE);
            rlCommentBox.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setPeekHeight(400 * dp1);
        }

    }

    public void loademojis(int position) {
        this.payloadInner.setEmojiPosition(position);
        switch (position) {
            case 0:
                Glide.with(getActivity()).load(R.drawable.feedback_icon_1).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_1));
                break;
            case 1:
                Glide.with(getActivity()).load(R.drawable.feedback_icon_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_2));
                break;
            case 2:
                Glide.with(getActivity()).load(R.drawable.feedback_icon_3).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_3));
                break;
            case 3:
                Glide.with(getActivity()).load(R.drawable.feedback_icon_4).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_4));
                break;
            case 4:
                Glide.with(getActivity()).load(R.drawable.feedback_icon_5).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_5));
                break;
        }
    }

    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this. verticalListViewActionHelper=verticalListViewActionHelper;
    }
}
