package com.example.dingjunwei.edittextextra;

import android.text.TextUtils;

/**
 * 描述:  验证工具类
 * 作者: dingjunwei on 2017/12/8.
 * 演出及格瓦拉平台部/智慧剧院/研发组
 */
public class VerifyUtils {

    /**
     * 校验银行卡卡号
     *
     * @param input
     * @return
     */
    public static boolean isBankCard(final String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        char bit = getBankCardCheckCode(input.substring(0, input.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return input.charAt(input.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
