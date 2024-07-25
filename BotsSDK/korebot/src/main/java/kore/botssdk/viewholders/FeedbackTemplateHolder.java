package kore.botssdk.viewholders;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
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
    private int position;
    private final RatingBar rbFeedback;
    private final LinearLayout emojis;

    private PayloadInner payloadInner;

    public FeedbackTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) layoutBubble.getLayoutParams();
        params.bottomMargin = (int) (10 * dp1);
        initBubbleText(layoutBubble, false);
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);
        tvFeedbackTemplateTitle = itemView.findViewById(R.id.tvfeedback_template_title);
        rbFeedback = itemView.findViewById(R.id.rbFeedback);
        emojis = itemView.findViewById(R.id.emojis);

        icon1 = itemView.findViewById(R.id.icon_1);
        icon2 = itemView.findViewById(R.id.icon_2);
        icon3 = itemView.findViewById(R.id.icon_3);
        icon4 = itemView.findViewById(R.id.icon_4);
        icon5 = itemView.findViewById(R.id.icon_5);

        icon1.setOnClickListener(this);
        icon2.setOnClickListener(this);
        icon3.setOnClickListener(this);
        icon4.setOnClickListener(this);
        icon5.setOnClickListener(this);

        dp1 = (int) DimensionUtil.dp1;
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        payloadInner = getPayloadInner(baseBotMessage);
        if(payloadInner == null) return;
        rbFeedback.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            position = (int) rating;
            payloadInner.setEmojiPosition(position);
//            if(fromUser) updateData();
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.icon_1) {
            resetAll();
            loadEmojis(0);
            position = 1;
//            updateData();
        } else if (id == R.id.icon_2) {
            resetAll();
            loadEmojis(1);
            position = 2;
//            updateData();
        } else if (id == R.id.icon_3) {
            resetAll();
            loadEmojis(2);
            position = 3;
//            updateData();
        } else if (id == R.id.icon_4) {
            resetAll();
            loadEmojis(3);
            position = 4;
//            updateData();
        } else if (id == R.id.icon_5) {
            resetAll();
            loadEmojis(4);
            position = 5;
//            updateData();
        }
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
