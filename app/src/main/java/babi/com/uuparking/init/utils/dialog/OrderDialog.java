package babi.com.uuparking.init.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2018/2/3.
 */

public class OrderDialog {

    public static void showNormalDialog(final Context context) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final OkHttpClient client = new OkHttpClient();
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage("你有一个订单是否进入?");
        normalDialog.setPositiveButton("进入",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("进入订单", "onClick: ");
//                        requsetUserOrder();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.show();
    }

    public static void normalDialogResult(final Context context, String message) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", null);
//       normalDialog.setNegativeButton("取消", null);
        normalDialog.show();
    }

    private static Callback mcallback;

    public static void normalDialogPositiveNext(final Context context, String message, Callback callback) {
        mcallback = callback;
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   mcallback.click();
            }
        });
        normalDialog.setNegativeButton("取消", null);
        normalDialog.show();
    }
    /**/
    public static void normalDialogPositiveUncancle(final Context context, String message, Callback callback) {
        mcallback = callback;
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   mcallback.click();
            }
        });
        normalDialog.setNegativeButton("取消", null);
        normalDialog.show();
    }
  /**/
    public static void normalDialogPositiveUncancle(final Context context, String message, Callback callback,boolean show) {
        mcallback = callback;
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   mcallback.click();
            }
        });
        if (show){
            normalDialog.setNegativeButton("取消", null);
        }
        normalDialog.show();
    }

    public interface Callback {
        void click();
    }

    /*跳转页面*/
    public static void normalDialogResult(final Context context, String message, final Bundle bundle, final Class c) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final OkHttpClient client = new OkHttpClient();
        final AlertDialog.Builder normalDialog;
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("提示！");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("进入",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, c);
                        intent.putExtra("bundle", bundle);
                        context.startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("取消", null);
        normalDialog.show();
    }
}
