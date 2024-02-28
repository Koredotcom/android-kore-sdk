package kore.botssdk.audiocodes.webrtcclient.Structure;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kore.botssdk.R;

public class ImageViewWithText  extends RelativeLayout {
    private static final String TAG = "ImageViewWithText";

    View rootView;
    TextView valueTextView;
    ImageView valueImageView;

    public ImageViewWithText(Context context) {
        super(context);
        init(context, null);
    }

    public ImageViewWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rootView = inflate(context, R.layout.image_view_with_text, this);
        valueTextView = rootView.findViewById(R.id.imageviewwithtext_text);
        valueImageView = rootView.findViewById(R.id.imageviewwithtext_imageview);
        if(attrs!=null)
        {
//            int[] set = {
//                    android.R.attr.background, // idx 0
//                    android.R.attr.src, // idx 1
//                    android.R.attr.text,        // idx 2
//                    //android.R.attr.textColor,       // idx 3
//                    //android.R.attr.text,
//            };
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, set);

            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ImageViewWithTextStyle,
                    0, 0
            );

            Drawable attBackDrawable = typedArray.getDrawable(R.styleable.ImageViewWithTextStyle_BackgroundImage);
            Drawable attSrcDrawable = typedArray.getDrawable(R.styleable.ImageViewWithTextStyle_srcImage);
            CharSequence attText = typedArray.getText(R.styleable.ImageViewWithTextStyle_Text);
            int textColor = typedArray.getColor(R.styleable.ImageViewWithTextStyle_TextColor, -2);
//            float attTextSize = typedArray.getDimensionPixelSize(R.styleable.ImageViewWithTextStyle_TextSize,-1);


            if(attBackDrawable!=null) {
                valueImageView.setBackground(attBackDrawable);
            }
            if(attSrcDrawable!=null) {
                valueImageView.setImageDrawable(attSrcDrawable);
            }
            if(attText!=null) {
                valueTextView.setText(attText);
            }
            if(textColor!=-2) {
                valueTextView.setTextColor(textColor);
            }
                valueTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            if(attTextSize>0) {
//                valueTextView.setTextSize(attTextSize);
//            }
            typedArray.recycle();

            //valueTextView.setText(attrs.getAttributeValue(android.R.attr.text));
        }
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    public ImageView getValueImageView() {
        return valueImageView;
    }
//        minusButton = rootView.findViewById(R.id.minusButton);
//        plusButton = rootView.findViewById(R.id.plusButton);
//
//        minusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decrementValue(); //we'll define this method later
//            }
//        });
//
//        plusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                incrementValue(); //we'll define this method later        }
//            });
//        }

}