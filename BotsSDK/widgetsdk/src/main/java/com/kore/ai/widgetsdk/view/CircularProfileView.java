package com.kore.ai.widgetsdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.drawables.ProfileDrawable;
import com.kore.ai.widgetsdk.utils.StringConstants;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * Created by tapasya on 01/10/15.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CircularProfileView extends RoundedImageView {

    private static final int DEFAULT_BADGE_TEXT_SIZE = 12;
    public static String DEFAULT_PROFILE_COLOR = StringConstants.DEFAULT_PROFILE_COLOR;
    public static final int PRESENCE_STATUS_INVALID = -100;
    private float TAG_RADIUS;
    private Paint mPaint;
    private Paint badgeTextPaint;
    private Paint borderPaint;

    private float defaultInitialSize;
    private boolean statusAtBottom;
    private float width, height;
    int dp1, SDK;
    private int DEFAULT_HEIGHT, DEFAULT_WIDTH, PADDING;
    private int PROFILE_DRAWABLE_PADDING;
    private int CPV_TEXT_SIZE;
    int borderStrokeWidth = 0;
    int borderColor = 0xffffffff;
    boolean hasBorder;

    private Path circlePath;

    private Drawable tagBadgeDrawable;
    private String initials;
    private int profileColor;

    RectF tagBadgeRect = new RectF();

    public CircularProfileView(Context context) {
        super(context);
        init(null, context);
    }

    public CircularProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public CircularProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {

        //Essentials
        if (!isInEditMode()) {
            dp1 = (int) AppControl.getInstance(getContext()).getDimensionUtil().dp1;
            SDK = android.os.Build.VERSION.SDK_INT;
            DEFAULT_HEIGHT = dp1 * 52;
            DEFAULT_WIDTH = dp1 * 52;
            PADDING = dp1 * 4;
            borderPaint = new Paint();
        }

        if (attrs != null) {
            //init attrs
            TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProfileView, 0, 0);
            PADDING = attr.getDimensionPixelSize(R.styleable.CircularProfileView_badge_padding, dp1 * 4);
            CPV_TEXT_SIZE = attr.getDimensionPixelSize(R.styleable.CircularProfileView_text_size, dp1 * 17);
            PROFILE_DRAWABLE_PADDING = (int) attr.getDimension(R.styleable.CircularProfileView_profile_drawable_padding, 0);
            hasBorder = attr.getBoolean(R.styleable.CircularProfileView_has_border, false);
            borderColor = attr.getColor(R.styleable.CircularProfileView_border_color, 0xffffffff);
            borderStrokeWidth = attr.getInt(R.styleable.CircularProfileView_border_width, 0);
        } else {
            CPV_TEXT_SIZE = 17 * dp1;
            PROFILE_DRAWABLE_PADDING = 0;
        }

        setPadding(PROFILE_DRAWABLE_PADDING, PROFILE_DRAWABLE_PADDING, PROFILE_DRAWABLE_PADDING, PROFILE_DRAWABLE_PADDING); //TODO work on refining this later..

        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.layout_width, // 1
                android.R.attr.layout_height // 2
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        width = ta.getDimensionPixelSize(1, DEFAULT_WIDTH);
        height = ta.getDimensionPixelSize(2, DEFAULT_HEIGHT);
        ta.recycle();

        TAG_RADIUS = 20 * dp1;

        setDimens(width, height);

        //initialize kore presence background paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthSpec = View.MeasureSpec.makeMeasureSpec((int) width, View.MeasureSpec.EXACTLY);
        int childHeightSpec = View.MeasureSpec.makeMeasureSpec((int) height, View.MeasureSpec.EXACTLY);
        int zeroSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
        if (getVisibility() == View.VISIBLE || getVisibility() == View.INVISIBLE) {
            super.onMeasure(childWidthSpec, childHeightSpec);
        } else {
            super.onMeasure(zeroSpec, zeroSpec);
        }
    }

    public void setDefaultBackground(int color, String initials) {
        setDefaultBackground(color, initials, CPV_TEXT_SIZE);
    }

    public void setDefaultBackground(int color, String initials, float textSize) {
        ProfileDrawable profileDrawable = new ProfileDrawable(color, initials, textSize);
        profileDrawable.mutate();
        setBackgroundDrawable(profileDrawable);
    }

    private Transformation getRoundTransformation() {
        return new RoundedTransformationBuilder()
                .oval(true)
                .build();
    }

    public void setProfileImageUrl(String url, boolean applyRoundTransform) {
        setProfileImageUrl(url, applyRoundTransform, -1, null);
    }

    public void setProfileImageUrl(String url, boolean applyRoundTransform, int profileColor, String initials) {
        if (applyRoundTransform) {
            if (url.startsWith(StringConstants.HTTP_SCHEME)) {
                Picasso.get()
                        .load(url)
                        .transform(getRoundTransformation())
                        .into(viewTarget);
            } else {
                Picasso.get()
                        .load(new File(url))
                        .transform(getRoundTransformation())
                        .into(viewTarget);
            }

        } else {
            if (url.startsWith(StringConstants.HTTP_SCHEME)) {
                Picasso.get()
                        .load(url)
                        .into(viewTarget);
            } else {
                Picasso.get()
                        .load(new File(url))
                        .into(viewTarget);
            }
        }
    }

    public void clearProfileImage() {
        setImageBitmap(null);
    }

    public void populateLayout(int drawableId, boolean b) {
        populateLayout("", null, getResources().getDrawable(drawableId), -1, -1, b);
    }

    public void populateLayout(String nameInitials, String url, Drawable d, int imageRes,
                               int color, boolean b) {
        populateLayout(nameInitials, null, url, d, imageRes, color, b, -1, -1);
    }

    public void populateLayout(String nameInitials, String url, Drawable d, int imageRes,
                               int color, boolean b, float width, float height) {
        populateLayout(nameInitials, null, url, d, imageRes, color, b, width, height);
    }

    private int color;

    public void populateLayout(String nameInitials, String filePath, String imageUrl, Drawable d,
                               int imageResource, int color, boolean applyRoundTransform,
                               float width, float height) {

        if (color == 0) {
            color = getResources().getColor(R.color.bgBlueSignup);
        }

        this.profileColor = color;

        Picasso.get().cancelRequest(viewTarget);
        if (nameInitials != null) {
            nameInitials = nameInitials.toUpperCase();
        }

        this.initials = nameInitials;

        //Set the imageURL
        Context context = getContext();

        setDefaultBackground(color, ""); // draw initials only if there is no image drawable

        if (imageResource != -1) {
            setDefaultBackground(color, "");
            setImageResource(imageResource);
        } else if (d != null) {
            setDefaultBackground(color, "");
            setImageDrawable(d);
        } else if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equalsIgnoreCase("no_avatar")) {
            setProfileImageUrl(imageUrl, applyRoundTransform);
        } else {
            setImageDrawable(null);
            setDefaultBackground(color, nameInitials); // drawing initials.
        }

        if (width != -1 && height != -1) {
            this.width = width;
            this.height = height;
        }
    }

    public void setDimens(float width, float height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            createPath();
        }
    }

    private void createPath() {
        float xCenter = (width - (getPaddingLeft() + getPaddingRight())) / 2f;
        float yCenter = (height - (getPaddingTop() + getPaddingBottom())) / 2f;
        circlePath = new Path();
        // TODO revisit this
        circlePath.addCircle(xCenter + getPaddingLeft(), yCenter + getPaddingTop(), width / 2f, Path.Direction.CCW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hasBorder) {
            drawBorder(canvas);
        }
    }

    private void drawBorder(Canvas canvas) {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderStrokeWidth);
        borderPaint.setColor(borderColor);
        int centerX = (getMeasuredWidth() / 2);
        int centerY = (getMeasuredHeight() / 2);
        int radius = (getMeasuredWidth() / 2 - borderStrokeWidth / 2);
        canvas.drawCircle(centerX, centerY, radius, borderPaint);
    }

    private void updateWithPic(Drawable drawable) {
        if (drawable != null) {
            setImageDrawable(drawable);
        }
    }

    private void updateWithPic(Bitmap bitmap) {
        if (bitmap != null) {
            setImageBitmap(bitmap);
        }
    }

    @Override
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    Target viewTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            setImageDrawable(null);
            updateWithPic(bitmap);
            setBackgroundDrawable(null);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            setImageDrawable(null);
            setBackgroundDrawable(null);
            setDefaultBackground(profileColor, initials);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
            setImageDrawable(null);
            setBackgroundDrawable(null);
            setDefaultBackground(profileColor, initials);
        }
    };

}
