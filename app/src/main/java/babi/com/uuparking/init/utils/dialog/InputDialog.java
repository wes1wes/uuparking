package babi.com.uuparking.init.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by b on 2018/2/5.
 */

public class InputDialog {
    public static String inputTitleDialog(Context context) {
        final String[] nickName = new String[1];
        final EditText inputServer = new EditText(context);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入：").setIcon(
                R.drawable.bg_button_gray).setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    nickName[0] =inputServer.getText().toString();
                    }
                });
        builder.show();
        return nickName[0];
    }
}
