package kore.botssdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import kore.botssdk.R;
import kore.botssdk.drawables.ProfileDrawable;
import kore.botssdk.utils.StringConstants;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class CircularProfileView extends AppCompatImageView {

    private Paint borderPaint;

    private float width, height;

    private int dp1;
    private int DEFAULT_HEIGHT;
    private int DEFAULT_WIDTH;
    private int CPV_TEXT_SIZE;

    private int borderStrokeWidth = 0;
    private int borderColor = 0xffffffff;
    private boolean hasBorder;

    private String initials;
    private int profileColor;

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

        if (!isInEditMode()) {
            dp1 = (int) DimensionUtil.dp1;

            DEFAULT_HEIGHT = dp1 * 52;
            DEFAULT_WIDTH = dp1 * 52;

            borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        int profileDrawablePadding;

        if (attrs != null) {

            TypedArray attr = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.CircularProfileView,
                    0,
                    0
            );

            CPV_TEXT_SIZE = attr.getDimensionPixelSize(
                    R.styleable.CircularProfileView_text_size,
                    dp1 * 17
            );

            profileDrawablePadding = (int) attr.getDimension(
                    R.styleable.CircularProfileView_profile_drawable_padding,
                    0
            );

            hasBorder = attr.getBoolean(
                    R.styleable.CircularProfileView_has_border,
                    false
            );

            borderColor = attr.getColor(
                    R.styleable.CircularProfileView_border_color,
                    0xffffffff
            );

            borderStrokeWidth = attr.getInt(
                    R.styleable.CircularProfileView_border_width,
                    0
            );

            attr.recycle();

        } else {

            CPV_TEXT_SIZE = 17 * dp1;
            profileDrawablePadding = 0;
        }

        setPadding(
                profileDrawablePadding,
                profileDrawablePadding,
                profileDrawablePadding,
                profileDrawablePadding
        );

        int[] attrsArray = new int[]{
                android.R.attr.layout_width,
                android.R.attr.layout_height
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        width = ta.getDimensionPixelSize(0, DEFAULT_WIDTH);
        height = ta.getDimensionPixelSize(1, DEFAULT_HEIGHT);

        ta.recycle();

        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childWidthSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);

        int childHeightSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);

        int zeroSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);

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

        ProfileDrawable profileDrawable = new ProfileDrawable(
                color,
                initials,
                textSize
        );

        profileDrawable.mutate();

        setBackground(profileDrawable);
    }

    public void setProfileImageUrl(String url) {

        Object source = url.startsWith(StringConstants.HTTP_SCHEME)
                ? url
                : new File(url);

        loadCircularImage(source);
    }

    private void loadCircularImage(Object source) {

        Glide.with(getContext())
                .asBitmap()
                .load(source)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop())
                .into(viewTarget);
    }

    public void populateLayout(String nameInitials,
                               String url,
                               Drawable drawable,
                               int imageRes,
                               int color,
                               boolean ignored) {

        populateLayout(nameInitials, url, drawable, imageRes, color, ignored, -1, -1);
    }

    public void populateLayout(String nameInitials,
                               String imageUrl,
                               Drawable drawable,
                               int imageResource,
                               int color,
                               boolean ignored,
                               float width,
                               float height) {

        if (color == 0) {
            color = ContextCompat.getColor(getContext(), R.color.bgBlueSignup);
        }

        this.profileColor = color;

        Glide.with(getContext()).clear(viewTarget);

        if (nameInitials != null) {
            nameInitials = nameInitials.toUpperCase();
        }

        this.initials = nameInitials;

        setDefaultBackground(color, "");

        if (imageResource != -1) {

            loadCircularImage(imageResource);

        } else if (drawable != null) {

            loadCircularImage(drawable);

        } else if (imageUrl != null
                && !imageUrl.isEmpty()
                && !"no_avatar".equalsIgnoreCase(imageUrl)) {

            setProfileImageUrl(imageUrl);

        } else {

            setImageDrawable(null);

            setDefaultBackground(color, nameInitials);
        }

        if (width != -1 && height != -1) {

            this.width = width;
            this.height = height;

            requestLayout();
        }
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
        borderPaint.setStrokeWidth(borderStrokeWidth);
        borderPaint.setColor(borderColor);

        int centerX = getMeasuredWidth() / 2;

        int centerY = getMeasuredHeight() / 2;

        int radius = (getMeasuredWidth() / 2) - (borderStrokeWidth / 2);

        canvas.drawCircle(centerX, centerY, radius, borderPaint);
    }

    private final CustomTarget<Bitmap> viewTarget = new CustomTarget<Bitmap>() {

        @Override
        public void onResourceReady(@NonNull Bitmap resource,
                                    Transition<? super Bitmap> transition) {

            setBackgroundResource(0);

            setImageBitmap(resource);
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
        }

        @Override
        public void onLoadFailed(Drawable errorDrawable) {

            setImageDrawable(null);

            setBackgroundResource(0);

            setDefaultBackground(profileColor, initials);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {

            setImageDrawable(null);

            setBackgroundResource(0);

            setDefaultBackground(profileColor, initials);
        }
    };

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }
}