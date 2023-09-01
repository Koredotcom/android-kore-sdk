package kore.botssdk.views;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import kore.botssdk.R;

public class DotsTextView extends AppCompatTextView {

    private JumpingSpan dotOne;
    private JumpingSpan dotTwo;
    private JumpingSpan dotThree;
    private final int showSpeed = 700;
    private int jumpHeight;
    private boolean autoPlay;
    private boolean isPlaying;
    private boolean isHide;
    private int period;
    private Handler handler;
    private final AnimatorSet mAnimatorSet = new AnimatorSet();
    private float textWidth;


    public DotsTextView(Context context) {
        super(context);
        init(context, null);
    }


    public DotsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public DotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        handler = new Handler(Looper.getMainLooper());


        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            jumpHeight = typedArray.getInt(R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4));
            autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoplay, true);
            typedArray.recycle();
        }
        dotOne = new JumpingSpan();
        dotTwo = new JumpingSpan();
        dotThree = new JumpingSpan();

        SpannableString spannable = new SpannableString("...");
        spannable.setSpan(dotOne, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotTwo, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotThree, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable, BufferType.SPANNABLE);


        textWidth = getPaint().measureText(".", 0, 1);


        ObjectAnimator dotOneJumpAnimator = createDotJumpAnimator(dotOne, 0);
        dotOneJumpAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        mAnimatorSet.playTogether(dotOneJumpAnimator, createDotJumpAnimator(dotTwo,
                period / 6), createDotJumpAnimator(dotThree, period * 2L / 6));
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                isPlaying = false;
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });


        isPlaying = autoPlay;
        if (autoPlay) {
            start();
        }
    }


    public void start() {
        isPlaying = true;
        setAllAnimationsRepeatCount(100);
        mAnimatorSet.start();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }


    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0, -jumpHeight);
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {
            @Override
            public Number evaluate(float fraction, Number from, Number to) {
                return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
            }
        });
        jumpAnimator.setDuration(period);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        return jumpAnimator;
    }

    public void setAllAnimationsRepeatCount(int repeatCount) {
        for (Animator animator : mAnimatorSet.getChildAnimations()) {
            if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }

    public void show() {
        ObjectAnimator dotThreeMoveRightToLeft = createDotShowAnimator(dotThree, 2);
        dotThreeMoveRightToLeft.start();

        ObjectAnimator dotTwoMoveRightToLeft = createDotShowAnimator(dotTwo, 1);
        dotTwoMoveRightToLeft.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                invalidate();
            }
        });

        dotTwoMoveRightToLeft.start();
        isHide = false;
    }


    private ObjectAnimator createDotHideAnimator(JumpingSpan span, float widthMultiplier) {
        return createDotHorizontalAnimator(span, 0, -textWidth * widthMultiplier);
    }


    private ObjectAnimator createDotShowAnimator(JumpingSpan span, int widthMultiplier) {
        return createDotHorizontalAnimator(span, -textWidth * widthMultiplier, 0);
    }


    private ObjectAnimator createDotHorizontalAnimator(JumpingSpan span, float from, float to) {
        ObjectAnimator dotThreeMoveRightToLeft = ObjectAnimator.ofFloat(span, "translationX", from, to);
        dotThreeMoveRightToLeft.setDuration(showSpeed);
        return dotThreeMoveRightToLeft;
    }
}
