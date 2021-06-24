package taimoor.sultani.sweetalert2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

public class Sweetalert extends Dialog implements View.OnClickListener {
    private View mDialogView;
    private final AnimationSet mModalInAnim;
    private final AnimationSet mModalOutAnim;
    private final Animation mOverlayOutAnim;
    private final Animation mErrorInAnim;
    private final AnimationSet mErrorXInAnim;
    private final AnimationSet mSuccessLayoutAnimSet;
    private final Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private FrameLayout mCustomViewContainer;
    private View mCustomView;
    private String mTitleText;
    private int mTitleSize;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private String mNeutralText;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private LinearLayout mButtonsContainer;
    private Button mConfirmButton;
    private boolean mShowConfirm = false;
    private Button mCancelButton;
    private Button mNeutralButton;
    private String mConfirmButtonBackgroundColor;
    private String mConfirmButtonTextColor;
    private String mNeutralButtonBackgroundColor;
    private String mNeutralButtonTextColor;
    private String mCancelButtonBackgroundColor;
    private String mCancelButtonTextColor;
    private final ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private OnSweetClickListener mNeutralClickListener;
    private boolean mCloseFromCancel;
    private boolean mHideKeyBoardOnDismiss = true;
    private int contentTextSize = 0;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;


    public static boolean DARK_STYLE = false;

    //aliases
    public final static int BUTTON_CONFIRM = DialogInterface.BUTTON_POSITIVE;
    public final static int BUTTON_CANCEL = DialogInterface.BUTTON_NEGATIVE;

    private final float defStrokeWidth;
    private float strokeWidth = 0;

    public interface OnSweetClickListener {
        void onClick(Sweetalert sweetAlertDialog);
    }

    public Sweetalert(Context context) {
        this(context, NORMAL_TYPE);
    }

    public Sweetalert(Context context, int alertType) {
        super(context, DARK_STYLE ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
        setCancelable(true);
        setCanceledOnTouchOutside(true); //TODO was false

        defStrokeWidth = getContext().getResources().getDimension(R.dimen.buttons_stroke_width);
        strokeWidth = defStrokeWidth;

        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                if (mHideKeyBoardOnDismiss) {
                    hideSoftKeyboard();
                }
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            Sweetalert.super.cancel();
                        } else {
                            Sweetalert.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = findViewById(R.id.title_text);
        mContentTextView = findViewById(R.id.content_text);
        mCustomViewContainer = findViewById(R.id.custom_view_container);
        mErrorFrame = findViewById(R.id.error_frame);
        mErrorX = mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = findViewById(R.id.success_frame);
        mProgressFrame = findViewById(R.id.progress_dialog);
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImage = findViewById(R.id.custom_image);
        mWarningFrame = findViewById(R.id.warning_frame);
        mButtonsContainer = findViewById(R.id.buttons_container);
        mConfirmButton = findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);
        mConfirmButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(this);
        mCancelButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mNeutralButton = findViewById(R.id.neutral_button);
        mNeutralButton.setOnClickListener(this);
        mNeutralButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
        mProgressHelper.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));

        setTitleText(mTitleText);
        setTitleTextSize(mTitleSize);
        setContentText(mContentText);
        setCustomView(mCustomView);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setNeutralText(mNeutralText);
        applyStroke();
        setConfirmButtonBackgroundColor(mConfirmButtonBackgroundColor);
        setConfirmButtonTextColor(mConfirmButtonTextColor);
        setCancelButtonBackgroundColor(mCancelButtonBackgroundColor);
        setCancelButtonTextColor(mCancelButtonTextColor);
        setNeutralButtonBackgroundColor(mNeutralButtonBackgroundColor);
        setNeutralButtonTextColor(mNeutralButtonTextColor);
        changeAlertType(mAlertType, true);
    }

    private void restore() {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);

        mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : View.GONE);

        adjustButtonContainerVisibility();

        mConfirmButton.setBackgroundResource(R.drawable.green_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    /**
     * Hides buttons container if all buttons are invisible or gone.
     * This deletes useless margins
     */
    private void adjustButtonContainerVisibility() {
        boolean showButtonsContainer = false;
        for (int i = 0; i < mButtonsContainer.getChildCount(); i++) {
            View view = mButtonsContainer.getChildAt(i);
            if (view instanceof Button && view.getVisibility() == View.VISIBLE) {
                showButtonsContainer = true;
                break;
            }
        }
        mButtonsContainer.setVisibility(showButtonsContainer ? View.VISIBLE : View.GONE);
    }

    private void playAnimation() {
        if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : View.GONE);
            switch (mAlertType) {
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case WARNING_TYPE:
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
            }
            adjustButtonContainerVisibility();
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public int getAlertType() {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }


    public String getTitleText() {
        return mTitleText;
    }

    public Sweetalert setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            if (text.isEmpty()) {
                mTitleTextView.setVisibility(View.GONE);
            } else {
                mTitleTextView.setVisibility(View.VISIBLE);
                mTitleTextView.setText(Html.fromHtml(mTitleText));
            }
        }
        return this;
    }

    public Sweetalert setTitleTextSize(int sps) {
        mTitleSize = sps;
        if (mTitleTextView != null && mTitleSize != 0) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sps);
        }
        return this;
    }

    public Sweetalert setTitleText(int resId) {
        return setTitleText(getContext().getResources().getString(resId));
    }

    public Sweetalert setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public Sweetalert setCustomImage(int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText() {
        return mContentText;
    }

    /**
     * @param text text which can contain html tags.
     */
    public Sweetalert setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            if (contentTextSize != 0) {
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(contentTextSize, getContext()));
            }
            mContentTextView.setText(Html.fromHtml(mContentText));
            mContentTextView.setVisibility(View.VISIBLE);
            mCustomViewContainer.setVisibility(View.GONE);
        }
        return this;
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * @param width in SP
     */
    public Sweetalert setStrokeWidth(float width) {
        this.strokeWidth = spToPx(width, getContext());
        return this;
    }

    private void applyStroke() {
        if (Float.compare(defStrokeWidth, strokeWidth) != 0) {
            Resources r = getContext().getResources();
            setButtonBackgroundColor(mConfirmButton, "#6bd505");
            setButtonBackgroundColor(mNeutralButton, "#838383");
            setButtonBackgroundColor(mCancelButton, "#DD6B55");
        }
    }

    public boolean isShowCancelButton() {
        return mShowCancel;
    }

    public Sweetalert showCancelButton(boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public Sweetalert showConfirmButton(boolean isShow) {
        mShowConfirm = isShow;
        if (mConfirmButton != null) {
            mConfirmButton.setVisibility(mShowConfirm ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText() {
        return mShowContent;
    }

    public Sweetalert showContentText(boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public Sweetalert setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    public Sweetalert setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public Sweetalert setConfirmButtonBackgroundColor(String color) {
        mConfirmButtonBackgroundColor = color;
        setButtonBackgroundColor(mConfirmButton, color);
        return this;
    }

    public String getConfirmButtonBackgroundColor() {
        return mConfirmButtonBackgroundColor;
    }

    public Sweetalert setNeutralButtonBackgroundColor(String color) {
        mNeutralButtonBackgroundColor = color;
        setButtonBackgroundColor(mNeutralButton, color);
        return this;
    }

    public String getNeutralButtonBackgroundColor() {
        return mNeutralButtonBackgroundColor;
    }

    public Sweetalert setCancelButtonBackgroundColor(String color) {
        mCancelButtonBackgroundColor = color;
        setButtonBackgroundColor(mCancelButton, color);
        return this;
    }

    public String getCancelButtonBackgroundColor() {
        return mCancelButtonBackgroundColor;
    }

    private void setButtonBackgroundColor(Button btn, String color) {
        if (btn != null && color != null) {
            Drawable[] drawableItems = ViewUtils.getDrawable(btn);
            if (drawableItems != null) {
                GradientDrawable gradientDrawableUnChecked = (GradientDrawable) drawableItems[1];
                //solid color
                gradientDrawableUnChecked.setColor(Color.parseColor(color));
                //stroke
                gradientDrawableUnChecked.setStroke((int) strokeWidth, genStrokeColor(Color.parseColor(color)));
            }
        }
    }

    private int genStrokeColor(int color) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.7f; // decrease value component
        return Color.HSVToColor(hsv);
    }

    public Sweetalert setConfirmButtonTextColor(String color) {
        mConfirmButtonTextColor = color;
        if (mConfirmButton != null && color != null) {
            mConfirmButton.setTextColor(Color.parseColor(mConfirmButtonTextColor));
        }
        return this;
    }

    public String getConfirmButtonTextColor() {
        return mConfirmButtonTextColor;
    }

    public Sweetalert setNeutralButtonTextColor(String color) {
        mNeutralButtonTextColor = color;
        if (mNeutralButton != null && color != null) {
            mNeutralButton.setTextColor(Color.parseColor(mNeutralButtonTextColor));
        }
        return this;
    }

    public String getNeutralButtonTextColor() {
        return mNeutralButtonTextColor;
    }

    public Sweetalert setCancelButtonTextColor(String color) {
        mCancelButtonTextColor = color;
        if (mCancelButton != null && color != null) {
            mCancelButton.setTextColor(Color.parseColor(mCancelButtonTextColor));
        }
        return this;
    }

    public String getCancelButtonTextColor() {
        return mCancelButtonTextColor;
    }

    public Sweetalert setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public Sweetalert setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public Sweetalert setNeutralText(String text) {
        mNeutralText = text;
        if (mNeutralButton != null && mNeutralText != null && !text.isEmpty()) {
            mNeutralButton.setVisibility(View.VISIBLE);
            mNeutralButton.setText(mNeutralText);
        }
        return this;
    }

    public Sweetalert setNeutralClickListener(OnSweetClickListener listener) {
        mNeutralClickListener = listener;
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.setTitleText(title.toString());
    }

    @Override
    public void setTitle(int titleId) {
        this.setTitleText(getContext().getResources().getString(titleId));
    }

    public Button getButton(int buttonType) {
        switch (buttonType) {
            default:
            case BUTTON_CONFIRM:
                return mConfirmButton;
            case BUTTON_CANCEL:
                return mCancelButton;
            case BUTTON_NEUTRAL:
                return mNeutralButton;
        }
    }

    public Sweetalert setConfirmButton(String text, OnSweetClickListener listener) {
        this.setConfirmText(text);
        this.setConfirmClickListener(listener);
        return this;
    }

    public Sweetalert setConfirmButton(int resId, OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setConfirmButton(text, listener);
        return this;
    }


    public Sweetalert setCancelButton(String text, OnSweetClickListener listener) {
        this.setCancelText(text);
        this.setCancelClickListener(listener);
        return this;
    }

    public Sweetalert setCancelButton(int resId, OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setCancelButton(text, listener);
        return this;
    }

    public Sweetalert setNeutralButton(String text, OnSweetClickListener listener) {
        this.setNeutralText(text);
        this.setNeutralClickListener(listener);
        return this;
    }

    public Sweetalert setNeutralButton(int resId, OnSweetClickListener listener) {
        String text = getContext().getResources().getString(resId);
        setNeutralButton(text, listener);
        return this;
    }

    /**
     * Set content text size
     *
     * @param value text size in sp
     */
    public Sweetalert setContentTextSize(int value) {
        this.contentTextSize = value;
        return this;
    }

    public int getContentTextSize() {
        return contentTextSize;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * set custom view instead of message
     *
     * @param view
     */
    public Sweetalert setCustomView(View view) {
        mCustomView = view;
        if (mCustomView != null && mCustomViewContainer != null) {
            mCustomViewContainer.addView(view);
            mCustomViewContainer.setVisibility(View.VISIBLE);
            mContentTextView.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        //several view animations can't be launched at one view, that's why apply alpha animation on child
        ((ViewGroup) mDialogView).getChildAt(0).startAnimation(mOverlayOutAnim); //alpha animation
        mDialogView.startAnimation(mModalOutAnim); //scale animation
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(Sweetalert.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(Sweetalert.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.neutral_button) {
            if (mNeutralClickListener != null) {
                mNeutralClickListener.onClick(Sweetalert.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    public Sweetalert setHideKeyBoardOnDismiss(boolean hide) {
        this.mHideKeyBoardOnDismiss = hide;
        return this;
    }

    public boolean isHideKeyBoardOnDismiss() {
        return this.mHideKeyBoardOnDismiss;
    }

    private void hideSoftKeyboard() {
        Activity activity = getOwnerActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
