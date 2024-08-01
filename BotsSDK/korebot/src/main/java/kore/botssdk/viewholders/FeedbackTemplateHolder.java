package kore.botssdk.viewholders;

import static kore.botssdk.models.BotResponse.VIEW_CSAT;
import static kore.botssdk.models.BotResponse.VIEW_NPS;
import static kore.botssdk.models.BotResponse.VIEW_STAR;
import static kore.botssdk.models.BotResponse.VIEW_THUMBS_UP_DOWN;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.FeedbackRatingScaleAdapter;
import kore.botssdk.dialogs.FeedbackActionSheetFragment;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackRatingModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class FeedbackTemplateHolder extends BaseViewHolderNew implements View.OnClickListener {
    private final TextView tvFeedbackTemplateTitle;
    private final ImageView icon1;
    private final ImageView icon2;
    private final ImageView icon3;
    private final ImageView icon4;
    private final ImageView icon5;
    private final RatingBar rbFeedback;
    private final LinearLayoutCompat emojis;
    private final RelativeLayout rlViewNPS;
    private final RecyclerView rvRatingScale;
    private final LinearLayoutCompat thumbsUpDown;
    private final ImageView thumbsUp;
    private final ImageView thumbsDown;
    private PayloadInner payloadInner;
    private String msgId;

    public static FeedbackTemplateHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_feedback, parent, false);
        return new FeedbackTemplateHolder(view);
    }

    private FeedbackTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        tvFeedbackTemplateTitle = itemView.findViewById(R.id.tv_feedback_template_title);
        rbFeedback = itemView.findViewById(R.id.rbFeedback);
        emojis = itemView.findViewById(R.id.emojis);
        rlViewNPS = itemView.findViewById(R.id.rlViewNPS);
        thumbsUpDown = itemView.findViewById(R.id.thumbs_up_down);
        thumbsUp = itemView.findViewById(R.id.thumbs_up);
        thumbsDown = itemView.findViewById(R.id.thumbs_down);
        rvRatingScale = itemView.findViewById(R.id.rvRatingScale);

        icon1 = itemView.findViewById(R.id.icon_1);
        icon2 = itemView.findViewById(R.id.icon_2);
        icon3 = itemView.findViewById(R.id.icon_3);
        icon4 = itemView.findViewById(R.id.icon_4);
        icon5 = itemView.findViewById(R.id.icon_5);

        dp1 = (int) DimensionUtil.dp1;
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        payloadInner = getPayloadInner(baseBotMessage);
        msgId = ((BotResponse) baseBotMessage).getMessageId();
        if (payloadInner == null) return;
        tvFeedbackTemplateTitle.setText(payloadInner.getText());
        String viewType = payloadInner.getView();
        emojis.setVisibility(viewType.equals(VIEW_CSAT) ? View.VISIBLE : View.GONE);
        rbFeedback.setVisibility(viewType.equals(VIEW_STAR) ? View.VISIBLE : View.GONE);
        rlViewNPS.setVisibility(viewType.equals(VIEW_NPS) ? View.VISIBLE : View.GONE);
        thumbsUpDown.setVisibility(viewType.equals(VIEW_THUMBS_UP_DOWN) ? View.VISIBLE : View.GONE);
        Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
        int selectedFeedback = contentState != null ? (int) contentState.get(BotResponse.SELECTED_FEEDBACK) : -1;

        switch (payloadInner.getView()) {
            case VIEW_STAR: {
                rbFeedback.setRating(selectedFeedback);
                rbFeedback.setIsIndicator(!isLastItem());
            }
            break;

            case VIEW_NPS: {
                rvRatingScale.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                List<FeedbackRatingModel> array = payloadInner.getNumbersArrays();
                String msgId = ((BotResponse) baseBotMessage).getMessageId();
                FeedbackRatingScaleAdapter adapter = new FeedbackRatingScaleAdapter(msgId, array, isLastItem(), selectedFeedback);
                adapter.setComposeFooterInterface(composeFooterInterface);
                adapter.setListener(contentStateListener);
                rvRatingScale.setAdapter(adapter);
            }

            case VIEW_CSAT: {
                resetAll();
                loadEmojis(selectedFeedback != -1 ? selectedFeedback - 1 : -1);
                icon1.setOnClickListener(this);
                icon2.setOnClickListener(this);
                icon3.setOnClickListener(this);
                icon4.setOnClickListener(this);
                icon5.setOnClickListener(this);
            }
            break;

            case VIEW_THUMBS_UP_DOWN: {
                thumbsUp.setOnClickListener(view -> {
                    if (!isLastItem()) return;
                    contentStateListener.onSelect(msgId, 1, BotResponse.SELECTED_FEEDBACK);
                    composeFooterInterface.onSendClick("1", "1", false);
                });
                thumbsDown.setOnClickListener(view -> {
                    if (!isLastItem()) return;
                    contentStateListener.onSelect(msgId, 2, BotResponse.SELECTED_FEEDBACK);
                    composeFooterInterface.onSendClick("2", "2", false);
                });
                break;
            }
        }

        if (payloadInner.getSliderView() && !payloadInner.getDialogCancel()) {
            payloadInner.setDialogCancel(true);
            FeedbackActionSheetFragment bottomSheetDialog = new FeedbackActionSheetFragment();
            bottomSheetDialog.setSkillName("skillName", "trigger");
            bottomSheetDialog.setData(payloadInner);
            bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
            bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            bottomSheetDialog.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "add_tags");
        }
        rbFeedback.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (!isLastItem()) return;
            contentStateListener.onSelect(msgId, (int) rating, BotResponse.SELECTED_FEEDBACK);
            composeFooterInterface.onSendClick(((int) rating) + "", rating + "", false);
        });
    }

    @Override
    public void onClick(View view) {
        if (!isLastItem()) return;
        int id = view.getId();
        int position = -1;
        if (id == R.id.icon_1) {
            position = 1;
        } else if (id == R.id.icon_2) {
            position = 2;
        } else if (id == R.id.icon_3) {
            position = 3;
        } else if (id == R.id.icon_4) {
            position = 4;
        } else if (id == R.id.icon_5) {
            position = 5;
        }
        contentStateListener.onSelect(msgId, position, BotResponse.SELECTED_FEEDBACK);
        composeFooterInterface.onSendClick(position + "", position + "", false);
        loadEmojis(position - 1);
    }

    private void resetAll() {
        icon1.setImageResource(R.drawable.feedback_icon_1);
        icon2.setImageResource(R.drawable.feedback_icon_2);
        icon3.setImageResource(R.drawable.feedback_icon_3);
        icon4.setImageResource(R.drawable.feedback_icon_4);
        icon5.setImageResource(R.drawable.feedback_icon_5);
    }

    public void loadEmojis(int position) {
        this.payloadInner.setEmojiPosition(position);
        switch (position) {
            case 0:
                Glide.with(itemView.getContext()).load(R.drawable.feedback_icon_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon1));
                break;
            case 1:
                Glide.with(itemView.getContext()).load(R.drawable.feedback_icon_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon2));
                break;
            case 2:
                Glide.with(itemView.getContext()).load(R.drawable.feedback_icon_3).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon3));
                break;
            case 3:
                Glide.with(itemView.getContext()).load(R.drawable.feedback_icon_4).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon4));
                break;
            case 4:
                Glide.with(itemView.getContext()).load(R.drawable.feedback_icon_5).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon5));
                break;
        }
    }
}
