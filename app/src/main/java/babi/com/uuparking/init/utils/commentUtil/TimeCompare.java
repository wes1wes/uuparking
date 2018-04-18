package babi.com.uuparking.init.utils.commentUtil;

import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by b on 2017/10/9.
 */

public class TimeCompare {

    public static String getNowTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        Date cur = new Date(System.currentTimeMillis());
        String s = f.format(cur);
        return s;
    }

    public static String getNowTimeAll() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cur = new Date(System.currentTimeMillis());
        String s = f.format(cur);
        return s;
    }

    public static String getNowTimediy(String dateFormat) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        Date cur = new Date(System.currentTimeMillis());
        String s = f.format(cur);
        return s;
    }

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 把long 转换成 yyyy-MM-dd HH:mm:ss再转换成String类型
     */
    public static String transferLongToString( Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millSec);
        return sdf.format(date);
    }
    /**
     * 获取网络当前时间
     */
    public static String getInternetNowTime(String dateFormat) {
        URL url = null;//取得资源对象
        long ld = 0;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            ld = uc.getDate(); //取得网站日期时间
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(ld);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return getNowTimediy(dateFormat);
        }
    }

    public static int DateCompareAllss(String s1, String s2) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        Date d2 = sdf.parse(s2);
        //比较
        int i = (int) ((d1.getTime() - d2.getTime()) / (1000));
        return i;
    }

    public static int DateCompareAll(String s1, String s2) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        Date d2 = sdf.parse(s2);
        //比较
        int i = (int) ((d1.getTime() - d2.getTime()) / (60 * 1000));
        return i;
    }

    public static int DateCompare(String s1, String s2) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        Date d2 = sdf.parse(s2);
        //比较
        int i = (int) ((d1.getTime() - d2.getTime()) / (60 * 1000));
        return i;
    }

    public static String DateAdd15(String s1,Integer m) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        //加15分钟
        Calendar specialDate = Calendar.getInstance();
        specialDate.setTime(d1); //注意在此处将 specialDate 的值改为特定日期
        specialDate.add(Calendar.MINUTE, m);
        int hour = specialDate.get(Calendar.HOUR_OF_DAY);
        int minute = specialDate.get(Calendar.MINUTE);

        String s = (hour < 10 ? ("0" + hour) : hour) + ":" + (minute < 10 ? ("0" + minute) : minute);
        return s;
    }

    public static String DateReduce15(String s1,Integer m) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        //加15分钟
        Calendar specialDate = Calendar.getInstance();
        specialDate.setTime(d1); //注意在此处将 specialDate 的值改为特定日期
        specialDate.add(Calendar.MINUTE, -m);
        int hour = specialDate.get(Calendar.HOUR_OF_DAY);
        int minute = specialDate.get(Calendar.MINUTE);
        String s = (hour < 10 ? ("0" + hour) : hour) + ":" + (minute < 10 ? ("0" + minute) : minute);
        return s;
    }

    public static String DateReplaceNowHourAndMinute(String s1) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间
        String s = getNowTimeAll();
        String ss = s.substring(11, 16);
        String string=s.replace(ss,s1);
        Log.d("输出截取替换的结果",ss+"---"+string);
        return string;
    }
}