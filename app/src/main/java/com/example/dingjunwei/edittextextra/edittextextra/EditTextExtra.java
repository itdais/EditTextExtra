package com.example.dingjunwei.edittextextra.edittextextra;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.dingjunwei.edittextextra.R;

/**
 * 描述: EditTextExtra
 * 作者: dingjunwei on 2017/12/4.
 * 演出及格瓦拉平台部/智慧剧院/研发组
 */
public class EditTextExtra extends LinearLayout {

    private EditText mEditText;
    private ImageView mClearImg;
    private ImageView mPwdSwitchImg;

    public EditTextExtra(Context context) {
        this(context, null);
    }

    public EditTextExtra(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextExtra(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_edittext_extra, this);
        mEditText = findViewById(R.id.et_extra);
        mClearImg = findViewById(R.id.iv_clear);
        mPwdSwitchImg = findViewById(R.id.iv_pwd_switch);
    }

    public void setHint(CharSequence sequence) {
        mEditText.setHint(sequence);
    }

    public void setHint(@StringRes int res) {
        mEditText.setHint(res);
        
    }

    public String getHint() {
        return mEditText.getHint().toString();
    }

    public void setHintColor(@ColorInt int colorRes) {
        mEditText.setHintTextColor(colorRes);
    }

    /**
     * 设置
     */
    public void setText(CharSequence sequence) {
        mEditText.setText(sequence);
    }

    /**
     * 获取内容
     */
    public String getText() {
        return mEditText.getText().toString();
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(float size) {
        mEditText.setTextSize(size);
    }

    /**
     * 设置字体颜色
     *
     * @param colorRes
     */
    public void setTextColor(@ColorInt int colorRes) {
        mEditText.setTextColor(colorRes);
    }

}
