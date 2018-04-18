package babi.com.uuparking.init.utils.commentUtil;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by b on 2017/10/24.
 */

public class RegularExpressionUtil {
    /**
     * 正则表达式:验证数字
     */
    public static final String REGEX_CHINESE = "^[0,9]$";
    /**
     * 正则表达式:验证数字不限精度
     */
    public static final String IS_FLOAT ="/^([1-9]\\d*(\\.\\d+)?)|(\\d+(\\.\\d+))$/";
    /**
     * 正则表达式:验证数字小数点两位
     */
    public static final String IS_FLOAT_POINT ="^\\d+\\.?\\d{0,2}$";
    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
    /**
     * 校验身份证
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */ public static boolean isIDCard(String idCard) { return Pattern.matches(REGEX_ID_CARD, idCard); }

    /**
     * 正则表达式:验证邮箱
     */

    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */ public static boolean isEmail(String email) { return Pattern.matches(REGEX_EMAIL, email); }
    /**
     *判断是否为数字
     */ public static boolean isNumber(String number) { return number.matches(REGEX_CHINESE); }
    /**
     *判断是否为数字
     */ public static boolean isFloat(String number) { return number.matches(IS_FLOAT); }
    /**
     *判断是否为数字
     */ public static boolean isFloatTwo(String number) { return number.matches(IS_FLOAT_POINT); }
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) { /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][356789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else { //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
}
