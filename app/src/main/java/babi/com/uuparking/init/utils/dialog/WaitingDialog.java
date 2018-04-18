package babi.com.uuparking.init.utils.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by b on 2017/11/1.
 */

public class WaitingDialog {
   public static ProgressDialog waitingDialog;
    public static void showWaitingDialog(Context context) {
    /* 等待Dialog具有屏蔽其他控件的交互能力
     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
     * 下载等事件完成后，主动调用函数关闭该Dialog
     */
        waitingDialog =
                new ProgressDialog(context);
        waitingDialog.setTitle("请稍等");
        waitingDialog.setMessage("等待中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }
    public static void cancelWaitingDialog(){
        waitingDialog.cancel();
    }

}
