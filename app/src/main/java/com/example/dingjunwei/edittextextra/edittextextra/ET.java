package com.example.dingjunwei.edittextextra.edittextextra;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.dingjunwei.edittextextra.R;

/**
 * 描述:
 * 作者: dingjunwei on 2017/12/4.
 * 演出及格瓦拉平台部/智慧剧院/研发组
 */

public class ET extends AppCompatEditText implements View.OnFocusChangeListener {

    private Drawable mClearDraw;
    private Drawable mShowPwdDraw;
    private Drawable mHidePwdDraw;

    //
    private boolean hasUnderLine;

    //底部线颜色
    private int underlineColor;

    /**
     * 是否可以切换密码显示隐藏按钮
     */
    private boolean hasChangePassword = false;
    /**
     * 密码目前的展示状态
     */
    private boolean isPasswordShowing = false;
    /**
     * 是否是密码类型
     */
    private boolean isPasswordType;

    private boolean hasFocus;

    public ET(Context context) {
        super(context);
        init(context, null);
    }

    public ET(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ET(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOnFocusChangeListener(this);

        mClearDraw = getResources().getDrawable(R.drawable.clear);
        mShowPwdDraw = getResources().getDrawable(R.drawable.ic_account_circle);
        mHidePwdDraw = getResources().getDrawable(R.drawable.ic_header);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
//        setFocusable(true);
//        setFocusableInTouchMode(true);
        setDrawPadding();
        isPasswordType();
    }

    /**
     * 判断是不是密码类型
     *
     * @return
     */
    public boolean isPasswordType() {
        int inputType = getInputType();
        if (inputType == 129 || inputType == 145 || inputType == 18 || inputType == 225) {
            isPasswordType = true;
        }
        Log.d("TTTTT", "是密码不  == " + isPasswordType);
        return isPasswordType;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int dp48 = getDip(getResources(), 48);
            if (hasChangePassword && mShowPwdDraw != null) {
                if (event.getX() > getWidth() - dp48) {
                    //切换密码的显示
                } else if (event.getX() > getWidth() - dp48 * 2) {
                    setText(null);
                    return true;
                }
            } else {
                if (event.getX() > getWidth() - dp48) {
                    setText(null);
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        drawPwdChange(canvas, width, height);
        drawClearDraw(canvas, width, height);
        drawBottomLine(canvas, width, height);
        super.onDraw(canvas);
    }

    private Paint mLinePaint;

    /**
     * 绘制底部
     */
    private void drawBottomLine(Canvas canvas, int width, int height) {
        if (!hasUnderLine) {
            return;
        }
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        int lineStartY = getScrollY() + getHeight() - getPaddingBottom();
        canvas.drawRect(0, lineStartY, width, lineStartY + 2, mLinePaint);
    }

    /**
     * 绘制切换密码按钮
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawPwdChange(Canvas canvas, int width, int height) {

    }

    /**
     * 设置清除按钮
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawClearDraw(Canvas canvas, int width, int height) {
        if (mClearDraw == null || !hasFocus || TextUtils.isEmpty(getText())) {
            return;
        }
        int left = width;
        int top = getScrollY() + getPaddingTop();
        int right = width;
        int bottom = getScrollY();

        int dp2 = getDip(getResources(), 2);
        if (mShowPwdDraw != null && hasChangePassword) {
            left -= (dp2 * 24);
            right = left;
        }

        int clearDefult = dp2 * 20;

        //宽度
        if (clearDefult > mClearDraw.getIntrinsicWidth()) {
            int space = (clearDefult - mClearDraw.getIntrinsicWidth()) / 2;
            left -= clearDefult - space;
            right -= space;
        } else {
            left -= clearDefult;
            right -= dp2 * 3;
        }
        //高度
        if (clearDefult > mClearDraw.getIntrinsicHeight()) {
            int space = (height - mClearDraw.getIntrinsicHeight()) / 2;
            top += space;
            bottom += mClearDraw.getIntrinsicHeight() + space;
        } else {
            top += dp2 * 3;
            bottom += height - dp2 * 3;
        }
        Rect rect = new Rect(left, top, right, bottom);
        mClearDraw.setBounds(rect);
        mClearDraw.draw(canvas);
    }

    /**
     * 设置padding
     */
    private void setDrawPadding() {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int width = getDip(getResources(), 40);
        if (hasUnderLine) {
            bottom += 4;
        }
        if (mClearDraw != null) {
            right += width;
        }

        if (hasChangePassword && mHidePwdDraw != null && mShowPwdDraw != null) {
            right += width;
        }

        super.setPadding(left, top, right, bottom);
    }

    public void setClearDraw(Drawable draw) {
        mClearDraw = draw;
    }

    public void setClearDraw(@DrawableRes int drawRes) {
        mClearDraw = ContextCompat.getDrawable(getContext(), drawRes);
    }

    public void setHidePwdDraw(@DrawableRes int drawRes) {
        mHidePwdDraw = ContextCompat.getDrawable(getContext(), drawRes);
    }

    public void setHidePwdDraw(Drawable draw) {
        mHidePwdDraw = draw;
    }

    public void setShowPwdDraw(@DrawableRes int drawRes) {
        mShowPwdDraw = ContextCompat.getDrawable(getContext(), drawRes);
    }

    /**
     * @param draw
     */
    public void setShowPwdDraw(Drawable draw) {
        mShowPwdDraw = draw;
    }

    /**
     * 设置是否可以切换密码
     *
     * @param changePassword
     */
    public void setChangePassword(boolean changePassword) {
        hasChangePassword = changePassword;
    }

    /**
     * 设置密码可见
     */
    public void setPasswordShow() {
        if (hasChangePassword) {
            isPasswordShowing = true;
            invalidate();
        }
    }

    /**
     * 设置密码隐藏
     */
    public void setPasswordHide() {
        if (hasChangePassword) {
            isPasswordShowing = false;
            invalidate();
        }
    }

    /**
     * 切换密码的显示和隐藏
     */
    public void changePasswordShowOrHide() {
        if (hasChangePassword) {
            if (isPasswordShowing) {
                setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    public void isPassword() {
//        getInputType() == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public int getSpSize(int textSize) {
        Context c = getContext();
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, r.getDisplayMetrics());
    }

    public static int getDip(Resources r, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
    }


}
