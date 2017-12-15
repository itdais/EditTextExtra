package com.example.dingjunwei.edittextextra;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * 描述:
 * 作者: dingjunwei on 2017/12/7.
 * 演出及格瓦拉平台部/智慧剧院/研发组
 */

public class FilterEditText extends AppCompatEditText {

    /**
     * 清除按钮
     */
    private Drawable mClearDraw;

    /**
     * 系统属性 drawableRight
     */
    private Drawable mRightDrawable;
    /**
     * 系统属性 drawablePadding
     */
    private int drawablePadding;

    /**
     * inner top padding
     */
    private int innerPaddingTop;

    /**
     * inner bottom padding
     */
    private int innerPaddingBottom;

    /**
     * inner left padding
     */
    private int innerPaddingLeft;

    /**
     * inner right padding
     */
    private int innerPaddingRight;

    /**
     * 密码可见图标
     */
    private Drawable mPwdVisibleDraw;
    /**
     * 密码不可见图标
     */
    private Drawable mPwdGoneDraw;
    /**
     * 密码是否可见
     */
    private boolean isPwdVisible;

    private int iconWidth;
    private int iconHeight;

    private OnPwdVisibleChangeListener mPwdVisibleChangeListener;

    public FilterEditText(Context context) {
        super(context);
        init(context, null);
    }

    public FilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        iconWidth = dp2px(context, 42);
        iconHeight = dp2px(context, 32);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FilterEditText);
        mClearDraw = ta.getDrawable(R.styleable.FilterEditText_clearDrawable);
        mPwdVisibleDraw = ta.getDrawable(R.styleable.FilterEditText_pwdVisibleDrawable);
        mPwdGoneDraw = ta.getDrawable(R.styleable.FilterEditText_pwdGoneDrawable);

        ta.recycle();

        int[] normalAttrs = new int[]{
                android.R.attr.padding, // 0
                android.R.attr.paddingLeft, // 1
                android.R.attr.paddingTop, // 2
                android.R.attr.paddingRight, // 3
                android.R.attr.paddingBottom, // 4
                android.R.attr.drawableLeft, // 5
                android.R.attr.drawableTop, // 6
                android.R.attr.drawableRight, // 7
                android.R.attr.drawableBottom, // 8
                android.R.attr.drawablePadding // 9
        };
        TypedArray typedArray = context.obtainStyledAttributes(attrs, normalAttrs);
        int padding = typedArray.getDimensionPixelSize(0, 0);
        innerPaddingLeft = typedArray.getDimensionPixelSize(1, padding);
        innerPaddingTop = typedArray.getDimensionPixelSize(2, padding);
        innerPaddingRight = typedArray.getDimensionPixelSize(3, padding);
        innerPaddingBottom = typedArray.getDimensionPixelSize(4, padding);
        mRightDrawable = typedArray.getDrawable(7);
        drawablePadding = typedArray.getDimensionPixelSize(9, 0);
        typedArray.recycle();

        initPadding();
    }

    private void initPadding() {

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int lineStartY = getScrollY() + height - getPaddingBottom();
        int rectLeft = width;
        if (mRightDrawable != null) {
            rectLeft -= drawablePadding + mRightDrawable.getIntrinsicWidth();
        }
        //比较大小的宽度和高度
        int compareW, compareH;
        //间距的宽度和高度
        int spaceW, spaceH;
        int top;

        //密码可见切换图标
        if (mPwdVisibleDraw != null && mPwdGoneDraw != null) {
            compareW = Math.max(mPwdVisibleDraw.getIntrinsicWidth(), mPwdGoneDraw.getIntrinsicWidth());
            spaceW = (iconWidth - Math.min(compareW, iconWidth)) / 2;
            compareH = Math.max(mPwdVisibleDraw.getIntrinsicHeight(), mPwdGoneDraw.getIntrinsicHeight());
            spaceH = (iconHeight - Math.min(compareH, iconHeight)) / 2;
            rectLeft -= iconWidth;
            top = lineStartY - iconHeight + spaceH;
            if (isPwdVisible) {
                mPwdVisibleDraw.setBounds(rectLeft + spaceW, top + spaceH, rectLeft + (iconWidth - spaceW), top + (iconHeight - spaceH));
                mPwdVisibleDraw.draw(canvas);
            } else {
                mPwdGoneDraw.setBounds(rectLeft + spaceW, top + spaceH, rectLeft + (iconWidth - spaceW), top + (iconHeight - spaceH));
                mPwdGoneDraw.draw(canvas);
            }
        }

        //删除按钮
        if (hasFocus() && !TextUtils.isEmpty(getText()) && null != mClearDraw) {
            spaceW = (iconWidth - Math.min(mClearDraw.getIntrinsicWidth(), iconWidth)) / 2;
            compareH = Math.min(mClearDraw.getIntrinsicHeight(), iconHeight);
            spaceH = (iconHeight - Math.min(compareH, iconHeight)) / 2;

            rectLeft -= iconWidth;
            top = lineStartY - iconHeight + spaceH;
            mClearDraw.setBounds(rectLeft + spaceW, top + spaceH, rectLeft + (iconWidth - spaceW), top + (iconHeight - spaceH));
            mClearDraw.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < getMeasuredWidth() / 2) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int actionRightX = getMeasuredWidth();
                int actionLeftX = actionRightX;
                if (mRightDrawable != null) {
                    actionRightX = actionLeftX -= drawablePadding + mRightDrawable.getIntrinsicWidth();
                }
                //最右边按钮 密码切换
                if (event.getX() > actionLeftX - iconWidth && event.getX() < actionRightX) {
                    if (mPwdVisibleDraw != null && mPwdGoneDraw != null) {
                        Toast.makeText(getContext(), "123", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (null != mClearDraw && !TextUtils.isEmpty(getText())) {
                        setText(null);
                        return true;
                    }
                } else if (event.getX() > actionLeftX - iconWidth * 2 && event.getX() < actionRightX - iconWidth) {
                    if (null != mClearDraw && !TextUtils.isEmpty(getText())) {
                        setText(null);
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 切换密码的显示和隐藏
     *
     * @param isVisible 是否显示
     *                  true 显示密码  false 隐藏密码
     */
    public void changePwdVisible(boolean isVisible) {
        if (isVisible) {
            isPwdVisible = true;
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            isPwdVisible = false;
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    /**
     * 设置清除按钮
     *
     * @param res
     */
    public void setClearDraw(@DrawableRes int res) {
        mClearDraw = ContextCompat.getDrawable(getContext(), res);
        invalidate();
    }

    /**
     * 设置清除按钮
     *
     * @param draw
     */
    public void setClearDraw(Drawable draw) {
        mClearDraw = draw;
        invalidate();
    }


    /**
     * 手机号匹配,涵盖范围较广
     * 13* 14* 15* 17* 18*
     *
     * @return
     */
    public boolean isMobile() {
        return RegexUtils.isMobileNormal(getText());
    }

    /**
     * 精准匹配
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、171、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     *
     * @return
     */
    public boolean isMobileExact() {
        return RegexUtils.isMobileExact(getText());
    }

    /**
     * 判断是否是邮箱
     * 如果想精准匹配163,QQ邮箱的话 请自行匹配
     *
     * @return
     */
    public boolean isEmail() {
        return RegexUtils.isEmail(getText());
    }

    /**
     * 是否是银行卡
     *
     * @return
     */
    public boolean isBankCard() {
        return VerifyUtils.isBankCard(getText().toString());
    }

    /**
     * 是否是中文
     *
     * @return
     */
    public boolean isZh() {
        return RegexUtils.isZh(getText());
    }

    /**
     * 是否是数值类型
     * 可以是正负整数_正负浮点
     *
     * @return
     */
    public boolean isNumeric() {
        return RegexUtils.isNumeric(getText());
    }

    /**
     * 是否是整数
     *
     * @return
     */
    public boolean isInteger() {
        return RegexUtils.isInteger(getText());
    }

    /**
     * 是否是整数
     *
     * @return
     */
    public boolean isFloat() {
        return RegexUtils.isFloat(getText());
    }

    /**
     * 正则匹配项
     *
     * @param regex 自定义的匹配规则
     * @return
     */
    public boolean isMatch(String regex) {
        return RegexUtils.isMatch(regex, getText());
    }


    private static int dp2px(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }

    public interface OnPwdVisibleChangeListener {
        void onVisibleChange(CharSequence text, boolean isVisible);
    }

    public void setOnPwdVisibleChangeListener(OnPwdVisibleChangeListener mListener) {
        this.mPwdVisibleChangeListener = mListener;
    }
}
