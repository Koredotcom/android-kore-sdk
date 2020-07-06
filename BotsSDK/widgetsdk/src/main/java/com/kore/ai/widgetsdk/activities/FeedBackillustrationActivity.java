package com.kore.ai.widgetsdk.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.fragments.FeedbackSheetFragment;

public class FeedBackillustrationActivity extends KaAppCompatActivity {

    ImageView icon_1, icon_2, icon_3, icon_4, icon_5;
    int position;
    String id;
    String name;
    TextView text_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_backillustration);

        setupActionBar();
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        icon_1 = findViewById(R.id.icon_1);
        icon_2 = findViewById(R.id.icon_2);
        icon_3 = findViewById(R.id.icon_3);
        icon_4 = findViewById(R.id.icon_4);
        icon_5 = findViewById(R.id.icon_5);
        text_label=findViewById(R.id.text_label);
        text_label.setText("How was your experience with "+name+" ?");

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
    }

    private void updateData() {


        FeedbackSheetFragment bottomSheetDialog = new FeedbackSheetFragment(position, id, FeedBackillustrationActivity.this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show(getSupportFragmentManager(), "add_tags");
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
                Glide.with(FeedBackillustrationActivity.this).load(R.drawable.feedbac_ic_emo_1).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_1));
                break;
            case 1:
                Glide.with(FeedBackillustrationActivity.this).load(R.drawable.feedbac_ic_emo_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_2));
                break;
            case 2:
                Glide.with(FeedBackillustrationActivity.this).load(R.drawable.feedbac_ic_emo_3).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_3));
                break;
            case 3:
                // sub_text.setText("Thank you..");
                Glide.with(FeedBackillustrationActivity.this).load(R.drawable.feedbac_ic_emo_4).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_4));
                break;
            case 4:
                Glide.with(FeedBackillustrationActivity.this).load(R.drawable.feedbacon_emo_5).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon_5));
                break;

        }
    }

}
