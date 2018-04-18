package babi.com.uuparking.init.utils.commentUtil;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by b on 2018/3/14.
 */

public class ToastUtil {
    public static void ToastUtilmsg(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
